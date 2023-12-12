import scrapy


class GSMArenaSpider(scrapy.Spider):
    name = "gsmarenaspider"
    # allowed_domains = ['gsmarena.com']

    custom_settings = {
        'DOWNLOAD_DELAY': 10
    }

    def start_requests(self):
        brands_links = ['samsung-phones-9.php', 'apple-phones-48.php', 'huawei-phones-58.php', 'nokia-phones-1.php',
                        'sony-phones-7.php', 'lg-phones-20.php', 'htc-phones-45.php', 'motorola-phones-4.php',
                        'lenovo-phones-73.php', 'xiaomi-phones-80.php', 'google-phones-107.php',
                        'honor-phones-121.php', 'oppo-phones-82.php', 'realme-phones-118.php', 'oneplus-phones-95.php',
                        'vivo-phones-98.php', 'meizu-phones-74.php', 'blackberry-phones-36.php', 'asus-phones-46.php',
                        'alcatel-phones-5.php', 'zte-phones-62.php', 'microsoft-phones-64.php',
                        'vodafone-phones-53.php', 'energizer-phones-106.php', 'cat-phones-89.php',
                        'sharp-phones-23.php', 'micromax-phones-66.php', 'infinix-phones-119.php',
                        'ulefone_-phones-124.php', 'tecno-phones-120.php', 'doogee-phones-129.php',
                        'blackview-phones-116.php', 'cubot-phones-130.php', 'oukitel-phones-132.php',
                        'itel-phones-131.php', 'tcl-phones-123.php', 'makers.php3', 'rumored.php3']

        for link in brands_links:
            if self.brand in link:
                yield scrapy.Request(url="https://www.gsmarena.com/" + link, callback=self.parse_find_model)
                break

    # def parse_find_brand(self, response):
    #     brands = response.css("div[class='brandmenu-v2 light l-box clearfix']").get()
    #     for brand in brands.css("li").get():
    #         link = brand.css("a::attr(href)").get()
    #         yield {
    #             "link": link
    #         }

    def parse_find_model(self, response):
        model = self.model.replace("-", "_").lower()
        found = False

        for product in response.css("div.makers li"):
            link = product.css("a::attr(href)").get()
            name = product.css(f"a[href='{link}'] span::text").get().lower()
            if model == name:
                found = True
                yield response.follow(link, callback=self.parse_extract_data)
                break

        if not found:
            next_page = response.css("a[class='pages-next']::attr(href)").get()
            if next_page is not None:
                yield response.follow(next_page, callback=self.parse_find_model)

    def parse_extract_data(self, response):
        yield {
            "deviceName": response.css("h1[class='specs-phone-name-title']::text").get(),
            "released": response.css("td[data-spec='status']::text").get(),
            "displayType": response.css("td[data-spec='displaytype']::text").get(),
            "displaySize": response.css("td[data-spec='displaysize']::text").get(),
            "displayResolution": response.css("td[data-spec='displayresolution']::text").get(),
            "operatingSystem": response.css("td[data-spec='os']::text").get(),
            "chipset": response.css("td[data-spec='chipset']::text").get(),
            "cpu": response.css("td[data-spec='cpu']::text").get(),
            "gpu": response.css("td[data-spec='gpu']::text").get(),
            "frontCamera": ' '.join(response.css("td[data-spec='cam1modules']::text").extract()),
            "backCamera": response.css("td[data-spec='cam2modules']::text").get(),
            "wlan": response.css("td[data-spec='wlan']::text").get(),
            "bluetooth": response.css("td[data-spec='bluetooth']::text").get(),
            "sensors": response.css("td[data-spec='sensors']::text").get(),
            "battery": response.css("td[data-spec='batdescription1']::text").get()
        }
