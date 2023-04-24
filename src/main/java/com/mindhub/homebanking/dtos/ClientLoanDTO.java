package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.ClientLoan;

public class ClientLoanDTO{
    private long id;
    private String loanName;
    private double amount;
    private long loanId;
    private int payments;

    public ClientLoanDTO(ClientLoan clientLoan) {
        this.id = clientLoan.getId();
        this.loanId = clientLoan.getLoan().getId();
        this.loanName = clientLoan.getLoan().getName();
        this.amount = clientLoan.getAmount();
        this.payments = clientLoan.getPayments();
    }
//    GETTERS
    public long getId() {return id;}

    public String getLoanName() {return loanName;}

    public double getAmount() {return amount;}

    public int getPayments() {return payments;}

    public long getLoanId() {return loanId;}

}
