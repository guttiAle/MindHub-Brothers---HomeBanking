package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.ClientLoan;

public class ClientLoanDTO{
    private long id;
    private String name;
    private double amount;
    private long loanId;
    private int payments;

    public ClientLoanDTO(ClientLoan clientLoan) {
        this.id = clientLoan.getId();
        this.name = clientLoan.getName();
        this.amount = clientLoan.getAmount();
        this.payments = clientLoan.getPayments();
        this.loanId = clientLoan.getLoan().getId();
    }
//    GETTERS
    public long getId() {return id;}

    public String getName() {return name;}

    public double getAmount() {return amount;}

    public int getPayments() {return payments;}

    public long getLoanId() {return loanId;}

    //    SETTERS

    public void setName(String name) {this.name = name;}

    public void setAmount(double amount) {this.amount = amount;}

    public void setLoanId(long loanId) {this.loanId = loanId;}

    public void setPayments(int payments) {this.payments = payments;}
}
