package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.ClientLoan;

import java.util.Set;
import java.util.stream.Collectors;

public class ClientDTO {
    private long id;
    private String firstName;
    private String lastName;
    private String email;

    private Set<AccountDTO> accounts;
    private Set<ClientLoanDTO> loans;
    private Set<CardDTO> cards;

    public ClientDTO(Client client) {

        this.id = client.getId();

        this.firstName = client.getFirstName();

        this.lastName = client.getLastName();

        this.email = client.getEMail();

//        this.accounts = client.getAccount().stream().map(account -> new AccountDTO(account)).collect(Collectors.toSet());
        this.accounts = client.getAccount().stream()
                .filter(account -> account.isStatus())
                .map(account -> new AccountDTO(account))
                .collect(Collectors.toSet());

        this.loans = client.getClientLoans().stream().map(loan -> new ClientLoanDTO(loan)).collect(Collectors.toSet());

        this.cards = client.getCards().stream().map(card -> new CardDTO(card)).collect(Collectors.toSet());
    }

//    GETTERS
    public long getId() {return id;}

    public String getFirstName() {return firstName;}

    public String getLastName() {return lastName;}

    public String getEmail() {return email;}

    public Set<AccountDTO> getAccounts() {return accounts;}

    public Set<ClientLoanDTO> getLoans() {return loans;}

    public Set<CardDTO> getCards() {return cards;}
}
