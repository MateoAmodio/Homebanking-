package com.mindhub.homebanking.dtos;
import com.mindhub.homebanking.model.ClientLoan;

public class ClientLoanDTO {

    /****************** ATRIBUTOS ******************/

    private long id;
    private long loanid;
    public String name;
    public double amount;
    public int payment;

    /*************** CONSTRUCTORES ***************/

    public ClientLoanDTO(ClientLoan clientLoan) {
        this.id = clientLoan.getId();
        this.loanid = clientLoan.getLoan().getId();
        this.name = clientLoan.getName();
        this.amount = clientLoan.getAmount();
        this.payment = clientLoan.getPayments();
    }

    /****************** MÉTODOS ******************/

    public long getId() {return id;}
    public long getLoanid() {return loanid;}
    public String getName() {return name;}
    public double getAmount() {return amount;}
    public int getPayment() {return payment;}
}
