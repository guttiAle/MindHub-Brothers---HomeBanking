package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.service.AccountService;
import com.mindhub.homebanking.service.ClientService;

import com.mindhub.homebanking.service.PDFGeneratorService;
import com.mindhub.homebanking.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@RestController
public class TransactionController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private PDFGeneratorService pdfGeneratorService;

    /*POST TO MAKE TRANSACTIONS*/
    @Transactional
    @PostMapping("/api/transactions")
    public ResponseEntity<Object> createTransaction(@RequestParam double amount, @RequestParam String description, @RequestParam String sourceNumber, @RequestParam String destinationNumber, Authentication authentication) {
        Client client = clientService.findByEmail(authentication.getName());
        Account sourceAccount = accountService.findByNumber(sourceNumber);
        Account destinationAccount = accountService.findByNumber(destinationNumber);

        if (!destinationAccount.isStatus() || !sourceAccount.isStatus()) {
            return new ResponseEntity<>("The account is no longer active", HttpStatus.FORBIDDEN);
        }
        if (client == null) {
            return new ResponseEntity<>("Client doesn't exist", HttpStatus.FORBIDDEN);
        }
        if (description.isBlank() || sourceNumber.isBlank() || destinationNumber.isBlank() || amount == 0.0) {
            return new ResponseEntity<>("Missing Data", HttpStatus.FORBIDDEN);
        }
        if (sourceNumber.equals(destinationNumber)) {
            return new ResponseEntity<>("Same Account", HttpStatus.FORBIDDEN);
        }
        if (sourceAccount == null) {
            return new ResponseEntity<>("Source account doesn't exist", HttpStatus.FORBIDDEN);
        }
        if (!client.getAccount().contains(sourceAccount)) {
            return new ResponseEntity<>("The account doesn't belong to this client ", HttpStatus.FORBIDDEN);
        }
        if (destinationAccount == null) {
            return new ResponseEntity<>("Destination account doesn't exist", HttpStatus.FORBIDDEN);
        }
        if (sourceAccount.getBalance() < amount) {
            return new ResponseEntity<>("The account does not have enough money", HttpStatus.FORBIDDEN);
        }
        if (amount < 0) {
            return new ResponseEntity<>("You cannot transfer negative amounts", HttpStatus.FORBIDDEN);
        }

        Transaction transaction1 = new Transaction(amount * -1, destinationAccount.getNumber() + ": " + description, LocalDateTime.now(), TransactionType.DEBIT, sourceAccount.getBalance() - amount);
        Transaction transaction2 = new Transaction(amount, sourceAccount.getNumber() + ": " + description, LocalDateTime.now(), TransactionType.CREDIT, destinationAccount.getBalance() + amount);
        sourceAccount.addTransaction(transaction1);
        destinationAccount.addTransaction(transaction2);
        transactionService.saveTransaction(transaction1);
        transactionService.saveTransaction(transaction2);
        sourceAccount.setBalance(sourceAccount.getBalance() - amount);
        destinationAccount.setBalance(destinationAccount.getBalance() + amount);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    /*GET TRANSACTIONS BY DATE BETWEEN*/
    @GetMapping("/api/transactions")
    public ResponseEntity<Object> getTransactionsByDate(HttpServletResponse response , Authentication authentication,@RequestParam String accountNumber,String start, String end) throws IOException {
        Client client = clientService.findByEmail(authentication.getName());
        Account account = accountService.findByNumber(accountNumber);
        List<Transaction> transactions;

        if (client == null) {
            return new ResponseEntity<>("Client doesn't exist", HttpStatus.FORBIDDEN);
        }
        if (account == null) {
            return new ResponseEntity<>("Account doesn't exist", HttpStatus.FORBIDDEN);
        }
        if (!client.getAccount().contains(account)) {
            return new ResponseEntity<>("Account doesn't belong to this client", HttpStatus.FORBIDDEN);
        }
        if(start.equals("all") || end.equals("all") || start.isEmpty() || end.isEmpty()){
            transactions = transactionService.getTransactionsByAccount(account);
            this.pdfGeneratorService.export(response, transactions, account, "all", "all");
        } else {
            LocalDateTime startDate = LocalDateTime.parse(start);
            LocalDateTime endDate = LocalDateTime.parse(end);
            transactions = transactionService.getTransactionsByAccountAndDate(account, startDate, endDate);

            response.setContentType("application/pdf");
            String headerKey = "Content-Disposition";
            String headerValue = "attachment; filename=MB-" + account.getNumber() + "-Transactions.pdf";
            response.setHeader(headerKey, headerValue);
            this.pdfGeneratorService.export(response, transactions, account, start, end);
        }

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}


