import scrapy
import re


class PhoneSpider(scrapy.Spider):
    name = 'phonespider'
    allowed_domains = ['altex.ro', 'flanco.ro']

    custom_settings = {
        'DOWNLOAD_DELAY': 5
    }

    def start_requests(self):
        # urls = [
        #     f'https://www.flanco.ro/telefoane-tablete/smartphone/filtre/brand/{self.brand}.html',
        #     f'https://altex.ro/telefoane/cpl/filtru/in-stoc-5182/in-stoc/brand-3334/{self.brand}/',
        #     f'https://flip.ro/magazin/{self.brand}/?modelType=Telefoane',
        #     f'https://mediagalaxy.ro/telefoane/cpl/filtru/in-stoc-5182/in-stoc/brand-3334/{self.brand}/model-8071/{self.model}/',
        #     f'https://www.evomag.ro/telefoane-tablete-accesorii-telefoane/{self.brand}/'
        # ]
        #
        # for url in urls:
        #     if "altex.ro" in url:
        #         yield scrapy.Request(url=url, callback=self.parse_altex)
        #     elif "flanco.ro" in url:
        #         yield scrapy.Request(url=url, callback=self.parse_flanco)
        #     elif "flip.ro" in url:
        #         yield scrapy.Request(url=url, callback=self.parse_flip)
        #     elif "mediagalaxy.ro" in url:
        #         yield scrapy.Request(url=url, callback=self.parse_mediagalaxy)
        yield scrapy.Request(url=f'https://www.evomag.ro/telefoane-tablete-accesorii-telefoane/{self.brand}/', callback=self.parse_evomag)

    def parse_altex(self, response):
        found = False
        for products in response.css("li.Products-item"):
            try:
                name = products.css("span.Product-name::text").get()
                price = products.css(".Price-int::text")[1].get()
                link = products.css("a::attr(href)").get()
                if "s23" in name.lower():
                    found = True
                yield {
                    "name": name,
                    "price": price,
                    "link": "https://altex.ro" + link
                }
            except:
                name = products.css("span.Product-name::text").get()
                price = products.css(".Price-int::text").get()
                link = products.css("a::attr(href)").get()
                yield {
                    "name": name,
                    "price": price,
                    "link": "https://altex.ro" + link
                }

        if not found:
            try:
                next_page = response.css(
                    'a[class^="inline-block py-1 px-2 mx-0.5 sm:mx-1 text-sm border border-gray-1100 rounded-md '
                    'items-center text-center bg-white"] ::attr(href)')[
                    1].get()
            except:
                next_page = response.css(
                    'a[class^="inline-block py-1 px-2 mx-0.5 sm:mx-1 text-sm border border-gray-1100 rounded-md '
                    'items-center text-center bg-white"] ::attr(href)').get()
            if next_page is not None:
                yield response.follow(next_page, callback=self.parse_altex)

    def parse_flanco(self, response):
        model = self.model.replace("-", " ")

        for products in response.css("li.item.product"):
            name = products.css("a.product-item-link h2::text").get()
            price = products.css("span.price::text").get()
            if model in name.lower() and price is not None:
                yield {
                    "name": name,
                    "price": price,
                    "link": products.css("a.product-item-link::attr(href)").get()
                }

        next_page = response.css("li.item.pages-item-next a.action::attr(href)").get()
        if next_page is not None:
            yield response.follow(next_page, callback=self.parse_flanco)

    def parse_flip(self, response):
        model = self.model.replace("-", " ")
        brand = self.brand.capitalize()

        for products in response.css("div.card-phone-new.position-relative.d-flex.flex-md-column"):
            name = re.sub('\s{2,}', ' ', products.css("div.phone-title a::text").get())
            if model in name.lower():
                yield {
                    "name": brand + name,
                    "price": response.css("div.real-price.font-bold span::text").get(),
                    "link": "https://flip.ro" + response.css("div.phone-cont a::attr(href)").get()
                }

    def parse_evomag(self, response):

        for product in response.css("div.nice_product_container"):
            link = product.css("div.npi_image a::attr(href)").get()
            title = product.css("div.npi_image a::attr(title)").get()
            if self.model in link:
                yield {
                    "title": title,
                    "link": link
                }


    def parse_mediagalaxy(self, response):
        for products in response.css("li.Products-item"):
            try:
                yield {
                    "name": products.css("span.Product-name::text").get(),
                    "price": products.css(".Price-int::text")[1].get(),
                    "link": "https://mediagalaxy.ro" + products.css("a::attr(href)").get()
                }
            except:
                yield {
                    "name": products.css("span.Product-name::text").get(),
                    "price": products.css(".Price-int::text").get(),
                    "link": "https://mediagalaxy.ro" + products.css("a.title::attr(href)").get()
                }

        try:
            next_page = response.css(
                'a[class^="inline-block py-1 px-2 mx-0.5 sm:mx-1 text-sm border border-gray-1100 rounded-md '
                'items-center text-center bg-white"] ::attr(href)')[
                1].get()
        except:
            next_page = response.css(
                'a[class^="inline-block py-1 px-2 mx-0.5 sm:mx-1 text-sm border border-gray-1100 rounded-md '
                'items-center text-center bg-white"] ::attr(href)').get()
        if next_page is not None:
            yield response.follow(next_page, callback=self.parse_mediagalaxy)
