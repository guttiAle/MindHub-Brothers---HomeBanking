package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.service.AccountService;
import com.mindhub.homebanking.service.ClientService;
import com.mindhub.homebanking.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@RestController
public class TransactionController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private TransactionService transactionService;

    /*POST TO MAKE TRANSACTIONS*/
    @Transactional
    @PostMapping("/api/transactions")
    public ResponseEntity<Object> createTransaction(@RequestParam double amount, @RequestParam String description, @RequestParam String sourceNumber, @RequestParam String destinationNumber, Authentication authentication) {
        Client client = clientService.findByEmail(authentication.getName());
        Account sourceAccount = accountService.findByNumber(sourceNumber);
        Account destinationAccount = accountService.findByNumber(destinationNumber);

        if (client == null){
            return new ResponseEntity<>("Client doesn't exist", HttpStatus.FORBIDDEN);
        }
        if (description.isBlank() || sourceNumber.isBlank() || destinationNumber.isBlank() || amount == 0.0) {
            return new ResponseEntity<>("Missing Data", HttpStatus.FORBIDDEN);
        }
        if (sourceNumber.equals(destinationNumber)){
            return new ResponseEntity<>("Same Account", HttpStatus.FORBIDDEN);
        }
        if (sourceAccount == null){
            return new ResponseEntity<>("Source account doesn't exist", HttpStatus.FORBIDDEN);
        }
        if (!client.getAccount().contains(sourceAccount)){
            return new ResponseEntity<>("The account doesn't belong to this client ", HttpStatus.FORBIDDEN);
        }
        if (destinationAccount == null){
            return new ResponseEntity<>("Destination account doesn't exist", HttpStatus.FORBIDDEN);
        }
        if (sourceAccount.getBalance() < amount){
            return new ResponseEntity<>("The account does not have enough money", HttpStatus.FORBIDDEN);
        }
        if (amount < 0){
            return new ResponseEntity<>("You cannot transfer negative amounts", HttpStatus.FORBIDDEN);
        }

        Transaction transaction1 = new Transaction(amount * -1,destinationAccount.getNumber() + " " + description, LocalDateTime.now() , TransactionType.DEBIT);
        Transaction transaction2 = new Transaction(amount, sourceAccount.getNumber() + " " + description, LocalDateTime.now(), TransactionType.CREDIT);
        sourceAccount.addTransaction(transaction1);
        destinationAccount.addTransaction(transaction2);
        transactionService.saveTransaction(transaction1);
        transactionService.saveTransaction(transaction2);
        sourceAccount.setBalance(sourceAccount.getBalance() - amount);
        destinationAccount.setBalance(destinationAccount.getBalance() + amount);
        return new ResponseEntity<>( HttpStatus.CREATED);
    }}
