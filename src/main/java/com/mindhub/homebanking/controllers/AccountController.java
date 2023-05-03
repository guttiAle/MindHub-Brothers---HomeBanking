package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
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
    private AccountRepository repoAccountController;

    @Autowired
    private ClientRepository repository;

    @RequestMapping("/api/accounts")
    public List<AccountDTO> getAccount() {
        return repoAccountController.findAll().stream().map(account -> new AccountDTO(account)).collect(toList());
    }
    @RequestMapping("/api/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id){
        Optional<Account> optionalAccount = repoAccountController.findById(id);
        return optionalAccount.map(account -> new AccountDTO(account)).orElse(null);
    }

    @RequestMapping(path = "/api/clients/current/accounts", method = RequestMethod.POST)
    public ResponseEntity<Object> createAccount(Authentication authentication) {
        Client clientOwner = repository.findByEmail(authentication.getName());
        if (clientOwner.getAccount().size() < 3) {
            Account newAccount = new Account(randomNumber(), LocalDateTime.now(),0);
            clientOwner.addAccount(newAccount);
            repoAccountController.save(newAccount);
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
        } while (repoAccountController.findByNumber(accountNumber) != null);
        return accountNumber;
    }


}




//            String accountNumber;
//            do {
//                int randomNumber = (int) (Math.random() * 100000000);
//                accountNumber = "VIN-" + String.format("%08d", randomNumber);
//            } while (repoAccountController.findByNumber(accountNumber) != null);