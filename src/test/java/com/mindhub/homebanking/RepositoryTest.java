package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.hamcrest.Matchers;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;


//@DataJpaTest // Este es para PostgreSQL
@SpringBootTest // Este es para h2-console
@AutoConfigureTestDatabase(replace = NONE)
public class RepositoryTest {
    @Autowired
    LoanRepository loanRepository;
    @Autowired
    CardRepository cardRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    TransactionRepository transactionRepository;
    @Test
    public void existLoans(){
        List<Loan> loans = loanRepository.findAll();
        assertThat(loans,is(not(empty())));
    }
    @Test
    public void existPersonalLoan(){
        List<Loan> loans = loanRepository.findAll();
        assertThat(loans, hasItem(hasProperty("name", is("PERSONAL"))));
    }



//    Card Repository tests
    @Test
    public void existCardsStartNumber(){
        List<Card> cards = cardRepository.findAll();
        assertThat(cards, hasItem(hasProperty("number", startsWith("1"))));
    }
    @Test
    public void checkIfMelbaHasCards(){
        List<Card> cards = cardRepository.findAll();
        assertThat(cards, hasItem(hasProperty("cardOwner", equalTo("Melba Morel"))));
    }

//    Account Repository tests
    @Test
    public void findAccountByNumber(){
        Account account = accountRepository.findByNumber("VIN-001");
        assertThat(account, is(notNullValue()));
    }
    @Test
    public void existAccountBalance(){
        List<Account> accounts = accountRepository.findAll();
        assertThat(accounts, hasItem(hasProperty("balance", equalTo(5000.0))));
    }

//    Client Repository tests

    @Test
    public void checkIfEmailIsInUse(){
        Client client = clientRepository.findByEmail("melba@mindhub.com");
        assertThat(client, is(notNullValue()));
    }

    @Test
    public void checkIfIdsAreUnique() {
        List<Client> clients = clientRepository.findAll();
        assertThat(clients, hasSize(clients.stream().map(Client::getId).collect(Collectors.toSet()).size()));
    }

//    Loan Repository tests
    @Test
    public void checkTheMaxAmountOfTheLoans(){
        List<Loan> loans = loanRepository.findAll();
        List<Double> payments = loans.stream().map(loan -> loan.getMaxAmount()).collect(Collectors.toList());
        assertThat(payments, hasItem(500000.0));
    }

    @Test
    public void checkInterestValues() {
        List<Loan> loans = loanRepository.findAll();
        List<Double> interest = loans.stream().map(loan -> loan.getInterest()).collect(Collectors.toList());
        assertThat(interest, everyItem(lessThanOrEqualTo(0.4)));
    }

//    Transaction Repository tests

    @Test
    public void checkTransactionValues() {
        List<Transaction> transactions = transactionRepository.findAll();
        List<Double> amount = transactions.stream().map(transaction -> transaction.getAmount()).collect(Collectors.toList());
        assertThat(amount, hasItem(greaterThanOrEqualTo(5000.0)));
    }

    @Test
    public void existTransactionsDescription(){
        List<Transaction> transactions = transactionRepository.findAll();
        assertThat(transactions, hasItem(hasProperty("description", equalToIgnoringCase("Compra calculadora"))));
    }

}
