package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Loan;

public class LoanDTO {
    private long id;
    private String name;
    private double maxAmount;
    private double interest;

    public LoanDTO(Loan loan) {
        this.id = loan.getId();
        this.name = loan.getName();
        this.maxAmount = loan.getMaxAmount();
        this.interest = loan.getInterest();
    }

    public long getId() {return id;}

    public String getName() {return name;}

    public double getMaxAmount() {return maxAmount;}

    public double getInterest() {return interest;}
}
