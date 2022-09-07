package com.mindhub.homebanking;
import com.mindhub.homebanking.Repositories.*;
import com.mindhub.homebanking.model.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Arrays;

@SpringBootApplication
public class HomebankingApplication {

	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;


	public static void main(String[] args) {

		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository, CardRepository cardRepository) {
		return (args) -> {


			Client cliente1 = new Client("Mateo", "Amodio", "telefonomateo@gmail.com", passwordEncoder.encode("12345") );
			Client cliente2 = new Client("Melba", "Morel", "MelbaMorel@Mindhub.com", passwordEncoder.encode("12345") );

			Account cuenta1 = new Account( "VIN-1", LocalDateTime.now() , 5000, AccountType.AHORRO );
			Account cuenta2 = new Account( "VIN-2", LocalDateTime.now().plusDays(1), 7500, AccountType.CORRIENTE );
			Account cuenta3 = new Account( "VIN-3", LocalDateTime.now().plusDays(2) , 12000, AccountType.CORRIENTE );

			Transaction transaction1 = new Transaction(TransactionType.CREDIT, 2000, "transferencia recibida" ,LocalDateTime.now(), cuenta1);
			Transaction transaction2 = new Transaction(TransactionType.DEBIT, -4000, "Compra tienda xx" ,LocalDateTime.now(), cuenta1);
			Transaction transaction3 = new Transaction(TransactionType.CREDIT, 1000, "transferencia recibida" ,LocalDateTime.now(), cuenta2);
			Transaction transaction4 = new Transaction(TransactionType.DEBIT, -200, "Compra tienda xy" ,LocalDateTime.now(), cuenta2);
			Transaction transaction5 = new Transaction(TransactionType.CREDIT, 6000, "transferencia recibida" ,LocalDateTime.now(), cuenta3);
			Transaction transaction6 = new Transaction(TransactionType.DEBIT, -100, "Compra tienda xy" ,LocalDateTime.now(), cuenta3);

			Loan loan1 = new Loan("Hipotecario", 500000, Arrays.asList(12,24,36,48,60), 12);
			Loan loan2 = new Loan("Personal", 100000, Arrays.asList(6,12,24), 5);
			Loan loan3 = new Loan("Automotriz", 300000, Arrays.asList(6,12,24,36), 7);

			ClientLoan prestamo1 = new ClientLoan( 400000, 60, cliente1, loan1);
			ClientLoan prestamo2 = new ClientLoan( 50000, 12, cliente1, loan2);
			ClientLoan prestamo3 = new ClientLoan( 100000, 24, cliente2, loan2);
			ClientLoan prestamo4 = new ClientLoan( 200000, 36, cliente2, loan3);



			Card card1 = new Card(cliente2, CardType.DEBIT, CardColor.GOLD, "6011-5564-4857-8945", 311,LocalDateTime.now(), LocalDateTime.now().plusYears(5));
			Card card2 = new Card(cliente2, CardType.CREDIT, CardColor.TITANIUM, "6011-0009-9130-0009", 451,LocalDateTime.now(), LocalDateTime.now().plusYears(5));
			Card card3 = new Card(cliente1, CardType.DEBIT, CardColor.SILVER, "3566-0000-2000-0410", 871,LocalDateTime.now(), LocalDateTime.now().plusYears(5));


			cliente1.addAccount(cuenta1);
			cliente1.addAccount(cuenta2);
			cliente2.addAccount(cuenta3);

			clientRepository.save(cliente1);
			clientRepository.save(cliente2);
			accountRepository.save(cuenta1);
			accountRepository.save(cuenta2);
			accountRepository.save(cuenta3);
			transactionRepository.save(transaction1);
			transactionRepository.save(transaction2);
			transactionRepository.save(transaction3);
			transactionRepository.save(transaction4);
			transactionRepository.save(transaction5);
			transactionRepository.save(transaction6);
			loanRepository.save(loan1);
			loanRepository.save(loan2);
			loanRepository.save(loan3);
			clientLoanRepository.save(prestamo1);
			clientLoanRepository.save(prestamo2);
			clientLoanRepository.save(prestamo3);
			clientLoanRepository.save(prestamo4);
			cardRepository.save(card1);
			cardRepository.save(card2);
			cardRepository.save(card3);

		};
	}

}
