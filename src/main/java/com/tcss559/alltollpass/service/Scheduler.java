package com.tcss559.alltollpass.service;

import com.tcss559.alltollpass.entity.toll.TollTransaction;
import com.tcss559.alltollpass.entity.toll.TransactionStatus;
import com.tcss559.alltollpass.entity.traveler.TravelerAccount;
import com.tcss559.alltollpass.entity.traveler.TravelerRfid;
import com.tcss559.alltollpass.exception.DatabaseException;
import com.tcss559.alltollpass.exception.RfidNotFoundException;
import com.tcss559.alltollpass.exception.UserNotFoundException;
import com.tcss559.alltollpass.repository.UserRepository;
import com.tcss559.alltollpass.repository.toll.TollTransactionRepository;
import com.tcss559.alltollpass.repository.traveler.TravelerAccountRepository;
import com.tcss559.alltollpass.repository.traveler.TravelerRfidRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
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



        //TODO: In production, this would be a service endpoint that would be called at a schedule
    // instead of this being a cron method itself. This is for POC only.

    @Scheduled(cron = "0 * 0 ? * *")
    public void updateTransactionStatus() throws RfidNotFoundException,UserNotFoundException,DatabaseException{

        try {
            // Get all transactions that are inprocess
            List<TollTransaction> transactions = tollTransactionRepository.findByStatus(TransactionStatus.IN_PROCESS);

            //Generate a map with <RFID, associated transactions in process> sort in created timestamp
            //TODO : sort by createdtimestmap
            Map<String, List<TollTransaction>> transactionsMapForRFID = transactions.stream()
                    .collect(Collectors.groupingBy(TollTransaction::getRfid));

            // for each rfid in map check the user balance, deduct balance in sequence and change the status to suceess
            for (Map.Entry<String, List<TollTransaction>> entry : transactionsMapForRFID.entrySet()) {
                // Get user id
                TravelerRfid thisRfid = travelerRfidRepository.findByRfid(entry.getKey()).
                        orElseThrow(() -> new RfidNotFoundException("RFID not found"));

                TravelerAccount travelerAccount = travelerAccountRepository.findById(thisRfid.getUserId()).
                        orElseThrow(() -> new UserNotFoundException("User not found"));

                //Get balance for that rfid user account
                double balanceToUpdate = travelerAccount.getBalance();
                List<TollTransaction> toUpdateTransactions = entry.getValue();

                for(TollTransaction t : toUpdateTransactions)
                {
                    // we dnt have the amount in the toll transaction table. Need to update it. --> done
                    if(balanceToUpdate >= t.getAmount()) {
                        balanceToUpdate -= t.getAmount();

                    }

                }

            }

            System.out.println("Running cron job");
        }
        catch(Exception e)
        {
            throw new DatabaseException(e);
        }

    }
}
