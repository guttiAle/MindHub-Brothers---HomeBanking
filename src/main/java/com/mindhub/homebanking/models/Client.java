package com.mindhub.homebanking.models;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

@Entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    @OneToMany(mappedBy="client", fetch=FetchType.EAGER)
    private Set<Account> accounts = new HashSet<>();
    @OneToMany(mappedBy="client", fetch=FetchType.EAGER)
    private Set<ClientLoan> clientLoans = new HashSet<>();

    @OneToMany(mappedBy="cardHolder", fetch=FetchType.EAGER)
    private Set<Card> cards = new HashSet<>();

    public Client() { }

    public Client(String first, String last, String mail, String password) {
        this.firstName = first;
        this.lastName = last;
        this.email = mail;
        this.password = password;
    }

//    GETTERS

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEMail() {
        return email;
    }

    public Set<Account> getAccount() {return accounts;}

    public Set<ClientLoan> getClientLoans() {
        return clientLoans;
    }

    public List<Loan> getLoans() {
        return clientLoans.stream().map(clientLoan -> clientLoan.getLoan()).collect(toList());
    }

    public Set<Card> getCards() {return cards;}

    public String getPassword() {return password;}

//    public String gettypeCard(){
//        for (int i = 0; this.cards > i; i++){
//
//        }
//    }


    //    SETTERS

    public void setFirstName(String firstName) {this.firstName = firstName;}

    public void setLastName(String lastName) {this.lastName = lastName;}

    public void setEMail(String email) {this.email = email;}

    public void setClientLoan(Set<ClientLoan> clientLoan) {this.clientLoans = clientLoan;}

    public void setAccounts(Set<Account> accounts) {this.accounts = accounts;}

    public void setCards(Set<Card> cards) {this.cards = cards;}

    public void setPassword(String password) {this.password = password;}

    //    ADDERS

    public void addAccount(Account account) {
        account.setClient(this);
        accounts.add(account);
    }

    public void addClientLoan(ClientLoan clientLoan) {
        clientLoan.setClient(this);
        clientLoans.add(clientLoan);
    }
    public void addCardHolder(Card cardHolder){
        cardHolder.setCardHolder(this);
        cards.add(cardHolder);
    }
}
