package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import com.mindhub.homebanking.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RestController
public class LoanController {
    @Autowired
    private ClientService clientService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private LoanService loanService;
    @Autowired
    private ClientLoanService clientLoanService;
    @Autowired
    private TransactionService transactionService;


    @GetMapping("/api/loans")
    public List<LoanDTO> getLoans() {
        return loanService.getLoans();
    }

    @Transactional
    @PostMapping("/api/loans")
    public ResponseEntity<Object> applicationForLoan (Authentication authentication, @RequestBody LoanApplicationDTO loanApplication){
        Client client = clientService.findByEmail(authentication.getName());
        Loan loan = loanService.findById(loanApplication.getId());
        Account destinationAccount = accountService.findByNumber(loanApplication.getDestinationAccount());

        if (loanApplication.getAmount() <= 0 || loanApplication.getPayments() <= 0){
            return new ResponseEntity<>("The amount or payments cannot be 0", HttpStatus.FORBIDDEN);
        }
        if (loanApplication.getDestinationAccount().isBlank()){
            return new ResponseEntity<>("Missing destination account", HttpStatus.FORBIDDEN);
        }
        if (destinationAccount == null){
            return new ResponseEntity<>("The destination account does not exist", HttpStatus.FORBIDDEN);
        }
        if (!client.getAccount().contains(destinationAccount)){
            return new ResponseEntity<>("The account does not belong to this client", HttpStatus.FORBIDDEN);
        }
        if (loan == null) {
            return new ResponseEntity<>("Loan doesn't exist", HttpStatus.FORBIDDEN);
        }
        if (loanApplication.getAmount() > loan.getMaxAmount()) {
            return new ResponseEntity<>("Excess amount", HttpStatus.FORBIDDEN);
        }
        if (!loan.getPayments().contains(loanApplication.getPayments())) {
            return new ResponseEntity<>("Payment not available", HttpStatus.FORBIDDEN);
        }
        List<String> loansName = client.getClientLoans().stream().map(clientLoan1 -> clientLoan1.getLoan().getName()).collect(Collectors.toList());
        if (loansName.contains(loan.getName())){
            return new ResponseEntity<>("You already have an active loan", HttpStatus.FORBIDDEN);
        }
        ClientLoan clientLoan = new ClientLoan((loanApplication.getAmount() * (1 + (loan.getInterest() + calcularInteres(loan.getPayments(), loanApplication.getPayments())))), loanApplication.getPayments());
        client.addClientLoan(clientLoan);
        loan.addClientLoan(clientLoan);
        clientLoanService.saveClientLoan(clientLoan);
        Transaction transaction1 = new Transaction(loanApplication.getAmount(), loan.getName() + " loan approved", LocalDateTime.now() , TransactionType.CREDIT);
        destinationAccount.addTransaction(transaction1);
        transactionService.saveTransaction(transaction1);
        destinationAccount.setBalance(destinationAccount.getBalance() + loanApplication.getAmount());

        return new ResponseEntity<>("Approved loan", HttpStatus.CREATED);
    }
    public static double calcularInteres(List<Integer> lista, int valor) {
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i) == valor) {
                return (i + 1) * 0.05;
            }
        }
        return 0.0;
    }

}
