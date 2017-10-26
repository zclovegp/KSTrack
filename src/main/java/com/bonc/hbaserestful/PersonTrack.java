package com.bonc.hbaserestful;

import com.bonc.hbaserestful.trackv2.GetTodayTrack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@CrossOrigin(origins = "*")
@SpringBootApplication
public class PersonTrack {

    @Autowired
    public static GetTodayTrack gtt;

    public static void main(String[] args) throws IOException {
        //获取基站码表
        gtt.getBaseStationCodeMap();
        SpringApplication.run(PersonTrack.class, args);
    }
}
