package com.mindhub.homebanking;

import com.mindhub.homebanking.Repositories.*;
import com.mindhub.homebanking.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.List;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RepositoriesTests {
    @Autowired
    ClientRepository clientRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    LoanRepository loanRepository;

    @Autowired
    ClientLoanRepository clientLoanRepository;

    @Autowired
    CardRepository cardRepository;

    /************* Client Tests *************/

    @Test
    public void existClients() {
        List<Client> clients = clientRepository.findAll();
        assertThat(clients, is(not(empty())));
    }

    @Test
    public void checkEmails() {
        List<Client> clients = clientRepository.findAll();
        assertThat( clients, hasItem(hasProperty("email", endsWith(".com"))));;
    }

    @Test
    public void checkPasswords() {
        List<Client> clients = clientRepository.findAll();
        assertThat( clients, hasItem(hasProperty("password", is(not(emptyOrNullString())))));
    }

    /************* Account Tests *************/

    @Test
    public void existAccounts() {
        List<Account> accounts = accountRepository.findAll();
        assertThat(accounts, is(not(empty())));
    }

    @Test
    public void checkAccountNumbers() {
        List<Account> accounts = accountRepository.findAll();
        assertThat(accounts, hasItem(hasProperty("number", is(not(emptyOrNullString())))));
    }

    @Test
    public void checkAccountNumbers2() {
        List<Account> accounts = accountRepository.findAll();
        assertThat(accounts, hasItem(hasProperty("number", startsWith("VIN-"))));
    }


    /************* Transaction Tests *************/

    @Test
    public void existTransactions() {
        List<Transaction> transactions = transactionRepository.findAll();
        assertThat(transactions, is(not(empty())));
    }

    @Test
    public void checkTransactionType() {
        List<Transaction> transactions = transactionRepository.findAll();
        assertThat(transactions, hasItem(hasProperty("type", is(not(empty())))));
    }

    /************* Loan Tests *************/

    @Test
    public void existLoans() {
        List<Loan> loans = loanRepository.findAll();
        assertThat(loans, is(not(empty())));
    }

    @Test
    public void checkNames() {
        List<Loan> loans = loanRepository.findAll();
        assertThat(loans, hasItem(hasProperty("name", is(not(emptyOrNullString())))));
    }

    /************* ClientLoan Tests *************/

    @Test
    public void existClientLoans() {
        List<ClientLoan> clientLoans = clientLoanRepository.findAll();
        assertThat(clientLoans, is(not(empty())));
    }

    /************* Card Tests *************/

    @Test
    public void existCards() {
        List<Card> cards = cardRepository.findAll();
        assertThat(cards, is(not(empty())));
    }

    @Test
    public void checkCardNumbers() {
        List<Card> cards = cardRepository.findAll();
        assertThat(cards, hasItem(hasProperty("number", is(not(emptyOrNullString())))));
    }

}
