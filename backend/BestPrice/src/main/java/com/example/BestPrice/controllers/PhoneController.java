package com.example.BestPrice.controllers;

import com.example.BestPrice.data.dto.Phone;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.*;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

@RestController
@CrossOrigin(origins = "http://192.168.0.147:5500/")
@RequestMapping("/devices")
public class PhoneController {


    @GetMapping("/{devicetype}/{brand}/{model}")
    public List<Phone> getPhones(@PathVariable String devicetype, @PathVariable String brand, @PathVariable String model) {

        try {
            URL url = null;

            if(Objects.equals(devicetype, "phone"))
                url = new URL(String.format("http://localhost:9081/crawl.json?spider_name=phonespider&start_requests=true&crawl_args={\"brand\":\"%s\",\"model\":\"%s\"}", brand, model));
            else if (Objects.equals(devicetype, "tablet")) {
                url = new URL(String.format("http://localhost:9081/crawl.json?spider_name=tabletspider&start_requests=true&crawl_args={\"brand\":\"%s\",\"model\":\"%s\"}", brand, model));
            }

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

                for(Phone p: listPhones)
                    System.out.println(p.toString());

                listPhones.sort((o1, o2) ->
                        Float.valueOf(o1.getPrice().replace(".", ""))
                        .compareTo(Float.valueOf(o2.getPrice().replace(".", ""))));

                return listPhones;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
