package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@RestController
public class AccountController {
    @Autowired
    private AccountRepository repoAccountController;

    @RequestMapping("/api/accounts")
    public List<AccountDTO> getAccount() {
        return repoAccountController.findAll().stream().map(account -> new AccountDTO(account)).collect(toList());
    }
    @RequestMapping("/api/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id){
        Optional<Account> optionalAccount = repoAccountController.findById(id);
        return optionalAccount.map(account -> new AccountDTO(account)).orElse(null);
    }
}
