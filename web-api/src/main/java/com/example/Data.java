package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/data")
public class Data {
    public final DataService dataService;

    @Autowired
    public Data(DataService dataService) {
        this.dataService = dataService;
    }

    @RequestMapping("/get")
    String get(@RequestParam String key){
        return dataService.get(key);
    }

    @RequestMapping("/poi")
    String poi(@RequestParam String id){
        return dataService.requestPoi(id);
    }
}
