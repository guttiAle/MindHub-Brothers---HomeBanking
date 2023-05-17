package com.mindhub.homebanking.dtos;

import java.util.List;

public class CreateLoanDTO {
    private String name;
    private double amount;
    private double interest;
    private List<Integer> payments;

    public CreateLoanDTO(String name, double amount, double interest, List<Integer> payments) {
        this.name = name;
        this.amount = amount;
        this.interest = interest;
        this.payments = payments;
    }

//    GETTERS
    public String getName() {return name;}
    public double getAmount() {return amount;}
    public double getInterest() {return interest;}
    public List<Integer> getPayments() {return payments;}
}
