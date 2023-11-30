package com.example.BestPrice.controllers;

import com.example.BestPrice.data.dto.Phone;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@RestController
@RequestMapping("/phone")
public class PhoneController {


    @GetMapping("/{brand}/{model}")
    public List<Phone> getPhones(@PathVariable String brand, @PathVariable String model) {

        try {
            URL url = new URL(String.format("http://localhost:9081/crawl.json?spider_name=phonespider&start_requests=true&crawl_args={\"brand\":\"%s\",\"model\":\"%s\"}", brand, model));

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            int responseCode = connection.getResponseCode();

            if( responseCode != 200){
                throw new RuntimeException("HttpResponseCode: " + responseCode);
            }
            else {
                String inline = "";
                Scanner scanner = new Scanner(url.openStream());

                //Write all the JSON data into a string using a scanner
                while (scanner.hasNext()) {
                    inline += scanner.nextLine();
                }

                //Close the scanner
                scanner.close();

                //Using the JSON simple library parse the string into a json object
                JSONParser parse = new JSONParser();
                JSONObject data_obj = (JSONObject) parse.parse(inline);

                JSONArray arr = (JSONArray) data_obj.get("items");

                List<Phone> listPhones = new ArrayList<>();

                for (int i = 0; i < arr.size(); i++) {

                    JSONObject phone = (JSONObject) arr.get(i);
                    ObjectMapper mapper = new ObjectMapper();
                    listPhones.add(mapper.readValue(phone.toJSONString(), new TypeReference<Phone>() {
                    }));
                }
                return listPhones;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
