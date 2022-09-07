package com.mindhub.homebanking.dtos;
import com.mindhub.homebanking.model.Transaction;
import com.mindhub.homebanking.model.TransactionType;
import java.time.LocalDateTime;

public class TransactionDTO {

    /****************** ATRIBUTOS ******************/

    private long id;
    private TransactionType type;
    private double amount;
    private String description;
    private LocalDateTime date;

    /*************** CONSTRUCTORES ***************/

    public TransactionDTO(Transaction transaction) {
        this.id = transaction.getId();
        this.type = transaction.getType();
        this.amount = transaction.getAmount();
        this.description = transaction.getDescription();
        this.date = transaction.getDate();
    }

    /****************** MÉTODOS ******************/

    public long getId() {return id;}
    public TransactionType getType() {return type;}
    public double getAmount() {return amount;}
    public String getDescription() {return description;}
    public LocalDateTime getDate() {return date;}
}
