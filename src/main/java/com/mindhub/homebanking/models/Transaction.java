package com.mindhub.homebanking.models;

import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private double amount;
    private String description;
    private LocalDateTime date;
    private TransactionType type;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="transactionOwner")
    private Account account;
    private Transaction(){ };

    public Transaction(double amount, String description, LocalDateTime date, Account account, TransactionType type) {
        this.amount = amount;
        this.description = description;
        this.date = date;
        this.account = account;
        this.type = type;
    }

//    Setters

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }
    //    Getters

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getDate() {
        return date;
    }

//    @JsonIgnore
    public Account getAccount() {
        return account;
    }

    public long getId() {
        return id;
    }

    public TransactionType getType() {
        return type;
    }
}
