package com.mindhub.homebanking.dtos;
import com.mindhub.homebanking.model.Client;
import java.util.Set;
import java.util.stream.Collectors;

public class ClientDTO {

    /****************** ATRIBUTOS ******************/

    private long id;
    public String firstName;
    public String lastName;
    public String email;
    private Set<AccountDTO> accounts;
    private Set <ClientLoanDTO> loans;
    private Set<CardDTO> cards;

    /*************** CONSTRUCTORES ***************/

    public ClientDTO(Client client) {
        this.id = client.getId();
        this.firstName = client.getFirstName();
        this.lastName = client.getLastName();
        this.email = client.getEmail();
        this.accounts = client.getAccounts().stream().map(account -> new AccountDTO(account)).collect(Collectors.toSet());
        this.loans = client.getClientLoans().stream().map(clientLoan -> new ClientLoanDTO(clientLoan)).collect(Collectors.toSet());
        this.cards = client.getCards().stream().map(card -> new CardDTO(card)).collect(Collectors.toSet());
    }

    /****************** MÉTODOS ******************/

    public long getId() {return id;}
    public void setId(long id) {this.id = id;}
    public String getFirstName() {return firstName;}
    public void setFirstName(String firstName) {this.firstName = firstName;}
    public String getLastName() {return lastName;}
    public void setLastName(String lastName) {this.lastName = lastName;}
    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}
    public Set<AccountDTO> getAccounts() {return accounts;}
    public void setAccounts(Set<AccountDTO> accounts) {this.accounts = accounts;}
    public Set<ClientLoanDTO> getLoans() {return loans;}
    public void setLoans(Set<ClientLoanDTO> loans) {this.loans = loans;}
    public Set<CardDTO> getCards() {return cards;}
    public void setCards(Set<CardDTO> cards) {this.cards = cards;}
}
