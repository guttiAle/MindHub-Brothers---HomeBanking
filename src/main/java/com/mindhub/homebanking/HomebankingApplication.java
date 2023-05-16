package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Bean
	public CommandLineRunner initData(ClientRepository repository, AccountRepository account, TransactionRepository transactionRepo, LoanRepository loanRepo, ClientLoanRepository clientLoanRepo, CardRepository cardRepo) {
		return (args) -> {

//			CLIENTS
			Client client1 = new Client("Melba", "Morel", "melba@mindhub.com", passwordEncoder.encode("melba123"));
			Client client2 = new Client("Santiago", "Hermosilla", "carlos@mindhub.com", passwordEncoder.encode("santiago987"));
			Client admin = new Client("admin", "admin", "admin@admin.com", passwordEncoder.encode("admin123"));
			repository.save(client1);
			repository.save(client2);
			repository.save(admin);

//			ACCOUNTS
			Account account1 = new Account("VIN-001", LocalDateTime.now(), 5000);
			Account account2 = new Account("VIN-002", LocalDateTime.now().plusDays(1), 7500);
			Account account3 = new Account("VIN-003", LocalDateTime.now(), 15000);
			client1.addAccount(account1);
			client1.addAccount(account2);
			client2.addAccount(account3);
			account.save(account1);
			account.save(account2);
			account.save(account3);

//			TRANSACTIONS
			Transaction transaction1 = new Transaction(-1500, "Compra calculadora", LocalDateTime.now(), TransactionType.DEBIT, 3959.62);
			account1.addTransaction(transaction1);
			Transaction transaction2 = new Transaction(445.58, "Tranferencia recibida", LocalDateTime.now(), TransactionType.CREDIT, 4405.2);
			account1.addTransaction(transaction2);
			Transaction transaction3 = new Transaction(4000.5, "Prestamo de familiar", LocalDateTime.now(), TransactionType.CREDIT, 15000);
			account3.addTransaction(transaction3);
			transactionRepo.save(transaction1);
			transactionRepo.save(transaction2);
			transactionRepo.save(transaction3);


			Transaction transaction4 = new Transaction(-700, "Compra en supermercado", LocalDateTime.now(), TransactionType.DEBIT, 3705.2);
			account1.addTransaction(transaction4);
			Transaction transaction5 = new Transaction(1540.50, "Venta de celular", LocalDateTime.now(), TransactionType.CREDIT, 5245.7);
			account1.addTransaction(transaction5);
			Transaction transaction6 = new Transaction(-245.7, "Compra en panadería", LocalDateTime.now(), TransactionType.DEBIT, 5000);
			account1.addTransaction(transaction6);
			transactionRepo.save(transaction4);
			transactionRepo.save(transaction5);
			transactionRepo.save(transaction6);

//			LOANS
			List<Integer> mortgage = List.of(12,24,36,48,60);
			List<Integer> personal = List.of(6,12,24);
			List<Integer> automotive = List.of(6,12,24,36);

			Loan loan1 = new Loan("MORTGAGE", 500000, 0.2, mortgage);
			Loan loan2 = new Loan("PERSONAL", 100000, 0.1, personal);
			Loan loan3 = new Loan("AUTOMOTIVE", 300000, 0.15, automotive);

			loanRepo.save(loan1);
			loanRepo.save(loan2);
			loanRepo.save(loan3);

//			CLIENT LOANS
			ClientLoan clientLoan1 = new ClientLoan( 400000, 60);
			client1.addClientLoan(clientLoan1);
			loan1.addClientLoan(clientLoan1);
			clientLoanRepo.save(clientLoan1);
			ClientLoan clientLoan2 = new ClientLoan(50000, 12);
			client1.addClientLoan(clientLoan2);
			loan2.addClientLoan(clientLoan2);
			clientLoanRepo.save(clientLoan2);

			ClientLoan clientLoan3 = new ClientLoan(100000, 24);
			client2.addClientLoan(clientLoan3);
			loan1.addClientLoan(clientLoan3);
			clientLoanRepo.save(clientLoan3);
			ClientLoan clientLoan4 = new ClientLoan(200000, 36);
			client2.addClientLoan(clientLoan4);
			loan3.addClientLoan(clientLoan4);
			clientLoanRepo.save(clientLoan4);

//			CARDS
			Card card1 = new Card("Melba Morel", CardType.DEBIT, CardColor.GOLD, "1234-5678-9112-3456", 378, LocalDateTime.now(), LocalDateTime.now().plusYears(5));
			client1.addCardHolder(card1);
			cardRepo.save(card1);
			Card card2 = new Card("Melba Morel", CardType.CREDIT, CardColor.TITANIUM,"1123-4561-2345-6789", 951, LocalDateTime.now(), LocalDateTime.now().plusYears(5));
			client1.addCardHolder(card2);
			cardRepo.save(card2);

			Card card3 = new Card("Santiago Hermosilla", CardType.CREDIT, CardColor.SILVER, "6789-1121-2345-6534", 753, LocalDateTime.now(), LocalDateTime.now().plusYears(5));
			client2.addCardHolder(card3);
			cardRepo.save(card3);
		};
	}
}
