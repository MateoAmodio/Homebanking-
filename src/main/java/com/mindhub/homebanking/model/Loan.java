package com.mindhub.homebanking.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Entity
public class Loan {

    /****************** ATRIBUTOS ******************/

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
    public String name;
    public long maxAmount;
    @ElementCollection
    @Column(name = "payments")
    private List<Integer> payments = new ArrayList<>();
    @OneToMany(mappedBy = "loan", fetch = FetchType.EAGER)
    private Set<ClientLoan> clientLoans = new HashSet<>();

    private int percentage;

    /*************** CONSTRUCTORES ***************/

    public Loan() {}
    public Loan(String name, long maxAmount, List<Integer> payments, int percentage) {
        this.name = name;
        this.maxAmount = maxAmount;
        this.payments = payments;
        this.percentage = percentage;
    }

    /****************** MÉTODOS ******************/

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public long getMaxAmount() {return maxAmount;}
    public void setMaxAmount(long maxAmount) {this.maxAmount = maxAmount;}
    public List<Integer> getPayments() {return payments;}
    public void setPayments(List<Integer> payments) {this.payments = payments;}
    public long getId() {return id;}
    public Set<ClientLoan> getClientLoans() {return clientLoans;}
    public void setClientLoans(Set<ClientLoan> clientLoans) {this.clientLoans = clientLoans;}
    public void setId(long id) {this.id = id;}
    public int getPercentage() {return percentage;}
    public void setPercentage(int percentage) {this.percentage = percentage;}

    @JsonIgnore
    public Set<Client> getClients(){return clientLoans.stream().map(clientLoan -> clientLoan.getClient()).collect(Collectors.toSet());}
    public void addClient(ClientLoan client) {
        client.setLoan(this);
        clientLoans.add(client);
    }
}
