package com.mindhub.homebanking.dtos;

public class LoanApplicationDTO {

    /****************** ATRIBUTOS ******************/

    private long loanId;
    private double amount;
    private int payments;
    private String toAccountNumber;

    /*************** CONSTRUCTORES ***************/
    public LoanApplicationDTO() {
    }

    public LoanApplicationDTO(long loanId, double amount, int payments, String toAccountNumber) {
        this.loanId = loanId;
        this.amount = amount;
        this.payments = payments;
        this.toAccountNumber = toAccountNumber;
    }

    /****************** MÉTODOS ******************/

    public long getLoanId() {return loanId;}
    public double getAmount() {return amount;}
    public int getPayments() {return payments;}
    public String getToAccountNumber() {return toAccountNumber;}
}
