package com.tcss559.alltollpass;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author sikha
 * Starting point - main for Java based web service
 */
@SpringBootApplication
@EnableScheduling
public class AllTollPassApplication {

    public static void main(String[] args) {
        SpringApplication.run(AllTollPassApplication.class, args);
    }

}
