package com.mindhub.homebanking.dtos;

public class LoanApplicationDTO {
    private long id;
    private Double amount;
    private int payments;
    private String destinationAccount;

    public LoanApplicationDTO(long id, Double amount, int payments, String destinationAccount) {
        this.id = id;
        this.amount = amount;
        this.payments = payments;
        this.destinationAccount = destinationAccount;
    }

//    GETTERS
    public long getId() {return id;}
    public Double getAmount() {return amount;}
    public int getPayments() {return payments;}

    public String getDestinationAccount() {return destinationAccount;}
}
