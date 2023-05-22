package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.TransactionType;

import java.time.LocalDateTime;
import java.util.Set;

public class CardDTO {
    private long id;
    private String number;
    private int cvv;
    private LocalDateTime fromDate;
    private LocalDateTime thruDate;
    private CardType type;
    private CardColor color;
    private String cardOwner;
    private boolean status;

    public CardDTO(Card card) {
        this.id = card.getId();
        this.cardOwner = card.getCardOwner();
        this.number = card.getNumber();
        this.cvv = card.getCvv();
        this.fromDate = card.getFromDate();
        this.thruDate = card.getThruDate();
        this.type = card.getType();
        this.color = card.getColor();
        this.status = card.isStatus();
    }

//    GETTERS

    public long getId() {return id;}

    public String getNumber() {return number;}

    public int getCvv() {return cvv;}

    public LocalDateTime getFromDate() {return fromDate;}

    public LocalDateTime getThruDate() {return thruDate;}

    public CardType getType() {return type;}

    public CardColor getColor() {return color;}

    public String getCardOwner() {return cardOwner;}

    public boolean isStatus() {return status;}
}
