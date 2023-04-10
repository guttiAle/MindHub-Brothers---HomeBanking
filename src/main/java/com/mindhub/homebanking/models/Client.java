package com.mindhub.homebanking.models;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    private String firstName;
    private String lastName;
    private String email;

    @OneToMany(mappedBy="client", fetch=FetchType.EAGER)
    Set<Account> accounts = new HashSet<>();
    public Client() { }

    public Client(String first, String last, String mail) {
        firstName = first;
        lastName = last;
        email = mail;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getEMail() {
        return email;
    }

    public void setEMail(String email) {
        this.email = email;
    }

//    public String toString() {
//        return firstName + " " + lastName;
//    }
    public Set<Account> getAccount() {
        return accounts;
    }
    public void addAccount(Account account) {
        account.setClient(this);
        accounts.add(account);
    }

    public long getId() {
        return id;
    }
}
