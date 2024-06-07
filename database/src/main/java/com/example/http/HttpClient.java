package com.example.http;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class HttpClient {
    private final RestTemplate restTemplate;

    @Autowired
    HttpClient(RestTemplateBuilder restTemplateBuilder){
        restTemplate = restTemplateBuilder.build();
    }

    public String request(String id){
        String url = "https://proxy2-search.proxy.amap.com/opendi_online/bin/search?auction?&outfmt=json&group_module=groupmeta_poi_detail&show_fields=all&ids=B0FFFAB6J2";
        return restTemplate.getForObject(url, String.class);
    }
}
