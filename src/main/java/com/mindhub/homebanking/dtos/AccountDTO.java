package com.mindhub.homebanking.dtos;
import com.mindhub.homebanking.model.Account;
import com.mindhub.homebanking.model.AccountType;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public class AccountDTO {

    /****************** ATRIBUTOS ******************/

    private long id;
    private String number;
    private LocalDateTime creationDate;
    private double balance;
    private Set<TransactionDTO> transactions;

    private AccountType accountType;

    /*************** CONSTRUCTORES ***************/

    public AccountDTO(Account account) {
        this.id = account.getId();
        this.number = account.getNumber();
        this.creationDate = account.getCreationDate();
        this.balance = account.getBalance();
        this.transactions = account.getTransactions().stream().map(transaction -> new TransactionDTO(transaction)).collect(Collectors.toSet());
        this.accountType = account.getAccountType();
    }

    /****************** MÉTODOS ******************/

    public long getId() {return id;}
    public void setId(long id) {this.id = id;}
    public String getNumber() {return number;}
    public void setNumber(String number) {this.number = number;}
    public LocalDateTime getCreationDate() {return creationDate;}
    public void setCreationDate(LocalDateTime creationDate) {this.creationDate = creationDate;}
    public double getBalance() {return balance;}
    public void setBalance(double balance) {this.balance = balance;}
    public Set<TransactionDTO> getTransactions() {return transactions;}
    public AccountType getAccountType() {return accountType;}
}
