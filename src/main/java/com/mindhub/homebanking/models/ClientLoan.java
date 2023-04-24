package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class ClientLoan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private double amount;

    private int payments;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="clientLoan")
    private Client client;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="loan")
    private Loan loan;


    public ClientLoan() {
    }

    public ClientLoan( double amount, int payments) {
        this.amount = amount;
        this.payments = payments;
    }

//    GETTERS

    public long getId() {return id;}

    public double getAmount() {return amount;}

    public int getPayments() {return payments;}

    public Client getClient() {return client;}

    public Loan getLoan() {return loan;}

//    SETTERS

    public void setAmount(double amount) {this.amount = amount;}


    public void setPayments(int payments) {
        this.payments = payments;
    }

    public void setClient(Client client) {this.client = client;}

    public void setLoan(Loan loan) {this.loan = loan;}

}
