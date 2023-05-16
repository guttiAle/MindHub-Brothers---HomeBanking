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
import java.util.stream.Collectors;

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

    @PostMapping("/api/clients/current/accounts/delete")
    public ResponseEntity<Object> deleteAccount(Authentication authentication, @RequestParam String number) {
        Client clientOwner = clientService.findByEmail(authentication.getName());
        Account selectAccount = accountService.findByNumber(number);
        if (!(clientOwner == selectAccount.getClient())) {
            return new ResponseEntity<>("The selected account does not belong to you", HttpStatus.FORBIDDEN);
        }
        if (selectAccount == null) {
            return new ResponseEntity<>("The selected account does not exist", HttpStatus.FORBIDDEN);
        }
        if (selectAccount.getBalance() < 0.0){
            return new ResponseEntity<>("You cannot delete accounts with debt", HttpStatus.FORBIDDEN);
        }
        if (selectAccount.getBalance() > 1.0){
            return new ResponseEntity<>("You have money in the selected account", HttpStatus.FORBIDDEN);
        }
        if (!selectAccount.isStatus()){
            return new ResponseEntity<>("The selected account has already been deleted", HttpStatus.FORBIDDEN);
        }
        else {
            accountService.deleteAccount(number);
        }
        return new ResponseEntity<>( HttpStatus.CREATED);
    }

    @PostMapping("/api/clients/current/accounts")
    public ResponseEntity<Object> createAccount(Authentication authentication) {
        Client clientOwner = clientService.findByEmail(authentication.getName());

        List<Account> filteredAccounts = clientOwner.getAccount().stream()
                .filter(account -> account.isStatus())
                .collect(Collectors.toList());

        if (filteredAccounts.size() < 3) {
            Account newAccount = new Account(randomNumber(), LocalDateTime.now(),0, true);
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
