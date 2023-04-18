package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository repository, AccountRepository account, TransactionRepository transactionRepo, LoanRepository loanRepo, ClientLoanRepository clientLoanRepo, CardRepository cardRepo) {
		return (args) -> {
//			CLIENTS
			Client client1 = new Client("Melba", "Morel", "melba@mindhub.com");
			Client client2 = new Client("Santiago", "Hermosilla", "carlos@mindhub.com");
			repository.save(client1);
			repository.save(client2);

//			ACCOUNTS
			Account account1 = new Account("VIN001", LocalDateTime.now(), 5000);
			Account account2 = new Account("VIN002", LocalDateTime.now().plusDays(1), 7500);
			Account account3 = new Account("VIN003", LocalDateTime.now(), 15000);
			client1.addAccount(account1);
			client1.addAccount(account2);
			client2.addAccount(account3);
			account.save(account1);
			account.save(account2);
			account.save(account3);

//			TRANSACTIONS
			Transaction transaction1 = new Transaction(-1500, "Compra calculadora", LocalDateTime.now(), TransactionType.DEBIT);
			account1.addTransaction(transaction1);
			Transaction transaction2 = new Transaction(4456500.58, "Tranferencia recibida", LocalDateTime.now(), TransactionType.CREDIT);
			account1.addTransaction(transaction2);
			Transaction transaction3 = new Transaction(4000.5, "Prestamo de familiar", LocalDateTime.now(), TransactionType.CREDIT);
			account3.addTransaction(transaction3);
			transactionRepo.save(transaction1);
			transactionRepo.save(transaction2);
			transactionRepo.save(transaction3);


			Transaction transaction4 = new Transaction(-700, "Compra en supermercado", LocalDateTime.now(), TransactionType.DEBIT);
			account1.addTransaction(transaction4);
			Transaction transaction5 = new Transaction(15400.50, "Venta de celular", LocalDateTime.now(), TransactionType.CREDIT);
			account1.addTransaction(transaction5);
			Transaction transaction6 = new Transaction(-245.7, "Compra en panadería", LocalDateTime.now(), TransactionType.DEBIT);
			account1.addTransaction(transaction6);
			transactionRepo.save(transaction4);
			transactionRepo.save(transaction5);
			transactionRepo.save(transaction6);

//			LOANS
			List<Integer> mortgage = List.of(12,24,36,48,60);
			List<Integer> personal = List.of(6,12,24);
			List<Integer> automotive = List.of(6,12,24,36);

			Loan loan1 = new Loan("MORTGAGE", 500000, mortgage);
			Loan loan2 = new Loan("PERSONAL", 100000, personal);
			Loan loan3 = new Loan("AUTOMOTIVE", 300000, automotive);

			loanRepo.save(loan1);
			loanRepo.save(loan2);
			loanRepo.save(loan3);

//			CLIENT LOANS
			ClientLoan clientLoan1 = new ClientLoan("Mortgage", 400000, 60);
			client1.addClientLoan(clientLoan1);
			loan1.addClientLoan(clientLoan1);
			clientLoanRepo.save(clientLoan1);
			ClientLoan clientLoan2 = new ClientLoan("Personal", 50000, 12);
			client1.addClientLoan(clientLoan2);
			loan2.addClientLoan(clientLoan2);
			clientLoanRepo.save(clientLoan2);

			ClientLoan clientLoan3 = new ClientLoan("Personal", 100000, 24);
			client2.addClientLoan(clientLoan3);
			loan1.addClientLoan(clientLoan3);
			clientLoanRepo.save(clientLoan3);
			ClientLoan clientLoan4 = new ClientLoan("Automotive", 200000, 36);
			client2.addClientLoan(clientLoan4);
			loan3.addClientLoan(clientLoan4);
			clientLoanRepo.save(clientLoan4);

//			CARDS
			Card card1 = new Card("Melba Morel", CardType.DEBIT, CardColor.GOLD, Long.parseLong("1234567891123456"), 378, LocalDateTime.now(), LocalDateTime.now().plusYears(5));
			client1.addCardHolder(card1);
			cardRepo.save(card1);
			Card card2 = new Card("Melba Morel", CardType.CREDIT, CardColor.TITANIUM, Long.parseLong("1123456123456789"), 951, LocalDateTime.now(), LocalDateTime.now().plusYears(5));
			client1.addCardHolder(card2);
			cardRepo.save(card2);

			Card card3 = new Card("Santiago Hermosilla", CardType.CREDIT, CardColor.SILVER, Long.parseLong("6789112123456534"), 753, LocalDateTime.now(), LocalDateTime.now().plusYears(5));
			client2.addCardHolder(card3);
			cardRepo.save(card3);
		};
	}
}
