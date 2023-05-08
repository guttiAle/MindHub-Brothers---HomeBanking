package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Loan;

import java.util.ArrayList;
import java.util.List;

public class LoanDTO {
    private long id;
    private String name;
    private double maxAmount;
    private double interest;
    private List<Integer> payments = new ArrayList<>();

    public LoanDTO(Loan loan) {
        this.id = loan.getId();
        this.name = loan.getName();
        this.maxAmount = loan.getMaxAmount();
        this.interest = loan.getInterest();
        this.payments = loan.getPayments();
    }

    public long getId() {return id;}

    public String getName() {return name;}

    public double getMaxAmount() {return maxAmount;}

    public double getInterest() {return interest;}

    public List<Integer> getPayments() {return payments;}
}
