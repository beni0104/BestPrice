import scrapy
import re


class PhoneSpider(scrapy.Spider):
    name = 'laptopspider'

    # custom_settings = {
    #     'DOWNLOAD_DELAY': 5
    # }

    def start_requests(self):
        urls = [
            f'https://www.flanco.ro/laptop-it-tablete/laptop/filtre/brand/{self.brand}.html',
            f'https://altex.ro/laptopuri/cpl/filtru/in-stoc-5182/in-stoc/brand-3334/{self.brand}/',
            f'https://mediagalaxy.ro/laptopuri/cpl/filtru/in-stoc-5182/in-stoc/brand-3334/{self.brand}/',
            f'https://www.evomag.ro/portabile-laptopuri-notebook/{self.brand}/'
        ]

        for url in urls:
            if "altex.ro" in url:
                yield scrapy.Request(url=url, callback=self.parse_altex)
            elif "flanco.ro" in url:
                yield scrapy.Request(url=url, callback=self.parse_flanco)
            elif "mediagalaxy.ro" in url:
                yield scrapy.Request(url=url, callback=self.parse_mediagalaxy)
            elif "evomag.ro" in url:
                yield scrapy.Request(url=url, callback=self.parse_evomag)

    def parse_altex(self, response):
        model = self.model.replace("-", " ").lower()

        for products in response.css("li.Products-item"):
            try:
                name = products.css("span.Product-name::text").get()

                if model in name.lower():

                    yield {
                        "name": name,
                        "price": re.search("[0-9]*[.,]?[0-9]+",
                                           products.css(".Price-int::text")[1].get())[0],
                        "link": "https://altex.ro" + products.css("a::attr(href)").get(),
                        "photo": products.css("div.Product-photoWrapper img::attr(src)").get()
                    }
            except:
                name = products.css("span.Product-name::text").get()

                if model in name.lower():
                    yield {
                        "name": name,
                        "price": re.search("[0-9]*[.,]?[0-9]+",
                                           products.css(".Price-int::text")[0].get())[0],
                        "link": "https://altex.ro" + products.css("a::attr(href)").get(),
                        "photo": products.css("div.Product-photoWrapper img::attr(src)").get()
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
            yield response.follow(next_page, callback=self.parse_altex)

    def parse_flanco(self, response):
        model = self.model.replace("-", " ").lower()

        for products in response.css("li.item.product"):
            name = products.css("a.product-item-link h2::text").get()

            if model in name.lower():
                photo = re.sub('.*http', 'http', products.css("a.product.photo.product-item-photo img::attr(src)").get())
                try:
                    yield {
                        "name": name,
                        "price": re.search("[0-9]*[.,]?[0-9]+", products.css("span.special-price span.price::text").get())[0],
                        "link": products.css("a.product-item-link::attr(href)").get(),
                        "photo": photo
                    }
                except:
                    yield {
                        "name": name,
                        "price":
                            re.search("[0-9]*[.,]?[0-9]+", products.css("span.singlePrice span.price::text").get())[
                                0],
                        "link": products.css("a.product-item-link::attr(href)").get(),
                        "photo": photo
                    }

    # Flanco forbids to acces other pages =(
        # next_page = response.css("li.item.pages-item-next a.action::attr(href)").get()
        # if next_page is not None:
        #     yield response.follow(next_page, callback=self.parse_flanco)

    def parse_evomag(self, response):
        model = self.model.replace("-", " ").lower()

        for product in response.css("div.nice_product_container"):
            title = product.css("div.npi_image a::attr(title)").get()
            photo = product.css(f"img[alt='{title}']::attr(src)").get()

            if model in title.lower():
                yield {
                    "title": title.replace(" (", ", ").replace(")", ""),
                    "link": "https://www.evomag.ro" + product.css("div.npi_image a::attr(href)").get(),
                    "price": re.search("[0-9]*[.,]?[0-9]+",
                                       product.css("span.real_price::text").get())[0],
                    "photo": photo
                }

    def parse_mediagalaxy(self, response):
        model = self.model.replace("-", " ").lower()

        for products in response.css("li.Products-item"):
            try:
                name = products.css("span.Product-name::text").get()
                if model in name.lower():
                    yield {
                        "name": name,
                        "price": re.search("[0-9]*[.,]?[0-9]+",
                                           products.css(".Price-int::text")[1].get())[0],
                        "link": "https://mediagalaxy.ro" + products.css("a::attr(href)").get(),
                        "photo": products.css("div.Product-photoWrapper img::attr(src)").get()
                    }
            except:
                name = products.css("span.Product-name::text").get()
                if model in name.lower():
                    yield {
                        "name": name,
                        "price": re.search("[0-9]*[.,]?[0-9]+",
                                           products.css(".Price-int::text").get())[0],
                        "link": "https://mediagalaxy.ro" + products.css("a::attr(href)").get(),
                        "photo": products.css("div.Product-photoWrapper img::attr(src)").get()
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
