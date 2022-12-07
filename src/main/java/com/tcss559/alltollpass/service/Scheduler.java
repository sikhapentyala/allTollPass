package com.tcss559.alltollpass.service;

import com.tcss559.alltollpass.entity.toll.TollTransaction;
import com.tcss559.alltollpass.entity.toll.TransactionStatus;
import com.tcss559.alltollpass.entity.traveler.TravelerAccount;
import com.tcss559.alltollpass.entity.traveler.TravelerRfid;
import com.tcss559.alltollpass.model.request.traveler.DebitRequest;
import com.tcss559.alltollpass.model.request.traveler.TravelerBalance;
import com.tcss559.alltollpass.repository.toll.TollTransactionRepository;
import com.tcss559.alltollpass.repository.traveler.TravelerAccountRepository;
import com.tcss559.alltollpass.repository.traveler.TravelerRfidRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author sikha
 * Schedular to update inprocess transaction automatically
 */

@Service
public class Scheduler {

    @Autowired
    TollTransactionRepository tollTransactionRepository;

    @Autowired
    TravelerAccountRepository travelerAccountRepository;

    @Autowired
    TravelerRfidRepository travelerRfidRepository;

    @Autowired
    TravelerService travelerService;

    //TODO: In production, this would be a service endpoint that would be called at a schedule
    // instead of this being a cron method itself. This is for POC only.

    // TODO: A few assumptions are made here. When the schedular is run,
    //  if there is no sufficient balance, we will make it failed
    //  This in future should consider a time lapse or dfiiference betwwen time created and the status change.



    @Scheduled(cron = "0 * 0 ? * *")
    public void test(){
        System.out.println("Runnign cron");
    }

    public void updateTransactionStatus(){

        try {
            // Get all transactions that are inprocess
            List<TollTransaction> transactions = tollTransactionRepository.findByStatus(TransactionStatus.IN_PROCESS);

            //Generate a map with <RFID, associated transactions in process
            Map<String, List<TollTransaction>> transactionsMapForRFID = transactions.stream()
                    .collect(Collectors.groupingBy(TollTransaction::getRfid));


            // for each rfid in map check the user balance, deduct balance in sequence of created timestamp
            for (Map.Entry<String, List<TollTransaction>> entry : transactionsMapForRFID.entrySet()) {
                // Get user id
                Optional<TravelerRfid> optionRfid = travelerRfidRepository.findByRfid(entry.getKey());
                if(optionRfid.isEmpty()){
                    //TODO: check if "continue" will continue with other records
                    continue;
                }

                // Get balance
                Optional<TravelerAccount> travelerAccount = travelerAccountRepository.findById(optionRfid.get().getUserId());
                if(travelerAccount.isEmpty()){
                    //TODO: check if "continue" will continue with other records
                    continue;
                }

                //Get balance for that rfid user account
                double balanceToUpdate = travelerAccount.get().getBalance();
                TransactionStatus statusToSet = TransactionStatus.FALLBACK;

                // sort transactions by timestamp
                List<TollTransaction> sortedList = entry.getValue().stream()
                        .sorted(Comparator.comparing(TollTransaction::getCreateTimestamp))
                        .collect(Collectors.toList());

                for(TollTransaction t : sortedList)
                {
                    // for each transaction
                    if(balanceToUpdate >= t.getAmount()) {

                        // update the transaction table with appropriate status
                        try {
                            TravelerBalance newBalance = travelerService.debitTransaction(DebitRequest.builder()
                                    .rfid(t.getRfid())
                                    .userId(optionRfid.get().getUserId())
                                    .amount(t.getAmount())
                                    .tollLocation("SCHEDULER")
                                    .build());
                            balanceToUpdate = newBalance.getAmount();
                            statusToSet = TransactionStatus.SUCCESS;
                        }catch (Exception e){
                            System.out.println("Scheduler threw exception while debiting user balance: "+ e.getMessage());
                            continue;
                        }
                    }

                    t.setStatus(statusToSet);
                    tollTransactionRepository.save(t);
                }

            }

        }
        catch(Exception e)
        {
            System.out.println("Scheduler threw exception: " + e.getMessage());
        }

    }
}
