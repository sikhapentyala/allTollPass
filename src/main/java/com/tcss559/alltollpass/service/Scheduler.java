package com.tcss559.alltollpass.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class Scheduler {

    //TODO: Write a cron job to change transaction is in_process ,

    @Scheduled(cron = "0 * 0 ? * *")
    public void updateTransactionStatus(){

        System.out.println("Running cron job");

    }
}
