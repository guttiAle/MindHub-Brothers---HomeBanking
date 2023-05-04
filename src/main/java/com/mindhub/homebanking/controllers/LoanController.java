package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Loan;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;

@RestController
public class LoanController {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    private AccountRepository accountRepository;

    @Transactional
    @PostMapping("/api/loans")
    public ResponseEntity<Object> applicationForLoan (Authentication authentication, @RequestBody LoanApplicationDTO loanApplication){
        Client client = clientRepository.findByEmail(authentication.getName());
        Loan loan = loanRepository.findById(loanApplication.getId());
        Account destinationAccount = accountRepository.findByNumber(loanApplication.getDestinationAccount());
//        if (loanApplication.getDestinationAccount().isBlank() || loanApplication.getAmount() == 0 || loanApplication.getId() == 0)


        if (!client.getAccount().contains(destinationAccount)){
            return new ResponseEntity<>("The account does not belong to this client", HttpStatus.FORBIDDEN);
        }
    }
}
