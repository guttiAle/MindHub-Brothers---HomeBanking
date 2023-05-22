package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientLoanDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController

public class ClientLoanController {
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

    @GetMapping("/api/current-loans")
    public List<ClientLoanDTO> getLoans(Authentication authentication) {
        Client client = clientService.findByEmail(authentication.getName());
        List<ClientLoanDTO> list = client.getClientLoans().stream()
                .filter(ClientLoan::isStatus)
                .map(ClientLoanDTO::new)
                .collect(Collectors.toList());

        return list;
    }
    @Transactional
    @PostMapping("/api/loans/pay")
    public ResponseEntity<Object> payALoan (Authentication authentication, @RequestParam String loanName, @RequestParam int amountOfPayments, @RequestParam String accountNumber){
        Client client = clientService.findByEmail(authentication.getName());
        Loan currentLoan = loanService.getLoanByName(loanName);
        Account currentAccount = accountService.findByNumber(accountNumber);
    //        I get the clientLoan to modify
        Optional<ClientLoan> currentClientLoan = client.getClientLoans().stream()
                .filter(clientLoan -> clientLoan.getLoan().getName().equals(loanName))
                .findFirst();

        if (currentAccount == null){
            return new ResponseEntity<>("The selected account does not exist", HttpStatus.FORBIDDEN);
        }
        if (currentAccount.getClient() != client){
            return new ResponseEntity<>("The account does not belong to you", HttpStatus.FORBIDDEN);
        }
        if (currentLoan == null){
            return new ResponseEntity<>("There is no such type of loan", HttpStatus.FORBIDDEN);
        }
        if (loanName.isEmpty()){
            return new ResponseEntity<>("You have to choose a type of loan", HttpStatus.FORBIDDEN);
        }
        if(amountOfPayments < 1){
            return new ResponseEntity<>("Payments cannot be < 1", HttpStatus.FORBIDDEN);
        }
        if (currentClientLoan.isPresent()) {
            ClientLoan clientLoan = currentClientLoan.get();

            double remainingAmount = clientLoan.getAmount();
            int remainingPayments = clientLoan.getPayments();

    //            I check that the client has that amount of pending payments left
            if (remainingPayments < amountOfPayments){
                return new ResponseEntity<>("You don't have so many pending payments", HttpStatus.FORBIDDEN);
            }

            double eachPaymentAmount = remainingAmount / remainingPayments;

            if (currentAccount.getBalance() < (eachPaymentAmount * amountOfPayments)){
                return new ResponseEntity<>("The account does not have enough money", HttpStatus.FORBIDDEN);
            }

            if (remainingPayments - amountOfPayments == 0){
                clientLoan.setStatus(false);
            }
            clientLoan.setPayments(remainingPayments - amountOfPayments);
            clientLoan.setAmount(remainingAmount - (eachPaymentAmount * amountOfPayments));
            clientLoanService.saveClientLoan(clientLoan);

            Transaction transaction1 = new Transaction((eachPaymentAmount * amountOfPayments) * -1, loanName +" - Loan Payment", LocalDateTime.now(), TransactionType.DEBIT, currentAccount.getBalance() - (eachPaymentAmount * amountOfPayments));
            currentAccount.addTransaction(transaction1);
            transactionService.saveTransaction(transaction1);

            currentAccount.setBalance(currentAccount.getBalance() - (eachPaymentAmount * amountOfPayments));

        } else {
            return new ResponseEntity<>("You don't have a loan of that type", HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>("Payment made", HttpStatus.CREATED);

    }


}
