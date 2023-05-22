package com.mindhub.homebanking.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private String number;
    private LocalDateTime creationDate;
    private double balance;
    private boolean status;
    private AccountType accountType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="owner_id")
    private Client client;

    @OneToMany(mappedBy="account", fetch=FetchType.EAGER)
    private Set<Transaction> transactions = new HashSet<>();
    public Account() { }

    public Account(String number, LocalDateTime creationDate, double balance, boolean status, AccountType accountType) {
        this.number = number;
        this.creationDate = creationDate;
        this.balance = balance;
        this.status = status;
        this.accountType = accountType;
    }

//    GETTERS

    public long getId() {
        return id;
    }
    public String getNumber(){
        return number;
    }
    public Client getClient() {
        return client;
    }
    public LocalDateTime getCreationDate() {
        return creationDate;
    }
    public double getBalance(){
        return balance;
    }
    public Set<Transaction> getTransactions() {
        return transactions;
    }
    public boolean isStatus() {return status;}

    public AccountType getAccountType() {return accountType;}
    //    SETTERS

    public void setNumber(String number) {
        this.number = number;
    }
    //    @JsonIgnore
    public void setClient(Client client) {this.client = client;}
    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }
    public void setBalance(double balance) {
        this.balance = balance;
    }
    public void setStatus(boolean status) {this.status = status;}

    public void setAccountType(AccountType accountType) {this.accountType = accountType;}
    //    ADDERS

    public void addTransaction(Transaction transaction) {
        transaction.setAccount(this);
        transactions.add(transaction);
    }
}