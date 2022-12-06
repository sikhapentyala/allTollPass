package com.tcss559.alltollpass.controller;

import com.tcss559.alltollpass.repository.CustomTraceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    CustomTraceRepository customTraceRepository;

    @GetMapping
    public ResponseEntity<?> getHealth(){
        return ResponseEntity.ok("App is up");
    }

    @RequestMapping(value="/trace", method = RequestMethod.TRACE)
    public ResponseEntity<?> adminOptions()
    {
        return ResponseEntity
                .ok()
                .body(customTraceRepository.findAll());
    }
}
