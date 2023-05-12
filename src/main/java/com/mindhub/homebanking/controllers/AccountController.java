package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.service.AccountService;
import com.mindhub.homebanking.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@RestController
public class AccountController {
    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;

    @GetMapping("/api/accounts")
    public List<AccountDTO> getAccounts() {
        return accountService.getAccounts();
    }

    @GetMapping("/api/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id){
        return accountService.getAccount(id);
    }

    @PostMapping("/api/clients/current/accounts")
    public ResponseEntity<Object> createAccount(Authentication authentication) {
        Client clientOwner = clientService.findByEmail(authentication.getName());
        if (clientOwner.getAccount().size() < 3) {
            Account newAccount = new Account(randomNumber(), LocalDateTime.now(),0);
            clientOwner.addAccount(newAccount);
            accountService.saveAccount(newAccount);
        } else {
            return new ResponseEntity<>("Already have 3 accounts", HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>( HttpStatus.CREATED);
    }

    private String randomNumber() {
        String accountNumber;
        do {
            int randomNumber = (int) (Math.random() * 100000000);
            accountNumber = "VIN-" + String.format("%08d", randomNumber);
        } while (accountService.findByNumber(accountNumber) != null);
        return accountNumber;
    }


}




//            String accountNumber;
//            do {
//                int randomNumber = (int) (Math.random() * 100000000);
//                accountNumber = "VIN-" + String.format("%08d", randomNumber);
//            } while (repoAccountController.findByNumber(accountNumber) != null);