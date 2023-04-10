package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository repository, AccountRepository account, TransactionRepository transactionRepo) {
		return (args) -> {
			Client cliente1 = new Client("Melba", "Morel", "melba@mindhub.com");
			repository.save(cliente1);
			Account cuenta1 = new Account("VIN001", LocalDateTime.now(), 5000, cliente1);
			account.save(cuenta1);
			Account cuenta2 = new Account("VIN002", LocalDateTime.now().plusDays(1), 7500, cliente1);
			account.save(cuenta2);
			Client cliente2 = new Client("Santiago", "Hermosilla", "carlos@mindhub.com");
			repository.save(cliente2);
			Account cuenta3 = new Account("VIN003", LocalDateTime.now(), 15000, cliente2);
			account.save(cuenta3);
			Transaction transaction1 = new Transaction(-1500, "Compra calculadora", LocalDateTime.now(), cuenta1, TransactionType.DEBIT);
			transactionRepo.save(transaction1);
			Transaction transaction2 = new Transaction(4456500.58, "Tranferencia recibida", LocalDateTime.now(), cuenta1, TransactionType.CREDIT);
			transactionRepo.save(transaction2);
			Transaction transaction3 = new Transaction(4000.5, "Prestamo de familiar", LocalDateTime.now(), cuenta3, TransactionType.CREDIT);
			transactionRepo.save(transaction3);


			Transaction transaction4 = new Transaction(-700, "Compra en supermercado", LocalDateTime.now(), cuenta1, TransactionType.DEBIT);
			transactionRepo.save(transaction4);
			Transaction transaction5 = new Transaction(15400.50, "Venta de celular", LocalDateTime.now(), cuenta1, TransactionType.CREDIT);
			transactionRepo.save(transaction5);
			Transaction transaction6 = new Transaction(-245.7, "Compra en panadería", LocalDateTime.now(), cuenta1, TransactionType.DEBIT);
			transactionRepo.save(transaction6);
		};
	}
}
