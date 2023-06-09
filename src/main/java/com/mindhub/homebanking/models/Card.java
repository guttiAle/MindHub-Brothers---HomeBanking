package com.mindhub.homebanking.models;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.time.LocalDateTime;
@Entity
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private CardType type;
    private String cardOwner;
    private CardColor color;
    private String number;
    private int cvv;
    private LocalDateTime fromDate;
    private LocalDateTime thruDate;
    private boolean status;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="cardHolder_id")
    private Client cardHolder;

    public Card() {}

    public Card(String cardOwner, CardType type, CardColor color, String number, int cvv, LocalDateTime fromDate, LocalDateTime thruDate) {
        this.cardOwner = cardOwner;
        this.type = type;
        this.color = color;
        this.number = number;
        this.cvv = cvv;
        this.fromDate = fromDate;
        this.thruDate = thruDate;
        this.status = true;
    }

//    GETTERS

    public long getId() {return id;}

    public CardType getType() {return type;}

    public CardColor getColor() {return color;}

    public String getNumber() {return number;}

    public int getCvv() {return cvv;}

    public LocalDateTime getFromDate() {return fromDate;}

    public LocalDateTime getThruDate() {return thruDate;}

    public Client getCardHolder() {return cardHolder;}

    public String getCardOwner() {return cardOwner;}

    public boolean isStatus() {return status;}


    //    SETTERS

    public void setType(CardType type) {this.type = type;}

    public void setColor(CardColor color) {this.color = color;}

    public void setNumber(String number) {this.number = number;}

    public void setCvv(int cvv) {this.cvv = cvv;}

    public void setFromDate(LocalDateTime fromDate) {this.fromDate = fromDate;}

    public void setThruDate(LocalDateTime thruDate) {this.thruDate = thruDate;}

    public void setCardHolder(Client cardHolder) {this.cardHolder = cardHolder;}

    public void setCardOwner(String cardOwner) {this.cardOwner = cardOwner;}

    public void setStatus(boolean status) {this.status = status;}
}
