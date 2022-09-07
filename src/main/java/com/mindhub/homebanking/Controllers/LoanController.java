package com.mindhub.homebanking.Controllers;

import com.mindhub.homebanking.Repositories.*;
import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class LoanController {

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    ClientLoanRepository clientLoanRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    LoanRepository loanRepository;



    @GetMapping("/api/loans")
    public List<LoanDTO> getLoans() {
        return loanRepository.findAll().stream().map(LoanDTO::new).collect(Collectors.toList());
    }

    @Transactional
    @PostMapping("/api/loans")
    public ResponseEntity<Object> createCards(@RequestBody LoanApplicationDTO loanApplicationDTO, Authentication authentication){

        Client client = this.clientRepository.findByEmail(authentication.getName());

        if(loanRepository.findById(loanApplicationDTO.getLoanId()) == null){
            return new ResponseEntity<>("Ese prestamo no existe.", HttpStatus.FORBIDDEN);
        }

        if(accountRepository.findByNumber(loanApplicationDTO.getToAccountNumber()) == null){
            return new ResponseEntity<>("La cuenta no existe.", HttpStatus.FORBIDDEN);
        }

        Account account = accountRepository.findByNumber(loanApplicationDTO.getToAccountNumber());

        Loan loan = loanRepository.findById(loanApplicationDTO.getLoanId()).orElse(null);

        if(loanApplicationDTO.getAmount() > loan.getMaxAmount()){
            return new ResponseEntity<>("El importe solicitado supera el maximo estipulado por el prestamo.", HttpStatus.FORBIDDEN);
        }

        if(loanApplicationDTO.getAmount() <= 0){
            return new ResponseEntity<>("El importe solicitado debe ser mayor a 0.", HttpStatus.FORBIDDEN);
        }

        if(loan.getPayments().contains(loanApplicationDTO.getPayments()) == false){
            return new ResponseEntity<>("El numero de cuotas no se encuentra entre los estipulados por el prestamo.", HttpStatus.FORBIDDEN);
        }

        if(accountRepository.findByNumberAndClient(account.getNumber(), client) == null){
            return new ResponseEntity<>("La cuenta no le pertenece.", HttpStatus.FORBIDDEN);
        }



        account.setBalance(account.getBalance() + loanApplicationDTO.getAmount());
        ClientLoan newLoan = new ClientLoan(loanApplicationDTO.getAmount() + (0.01 * loan.getPercentage() * loanApplicationDTO.getAmount()),loanApplicationDTO.getPayments(), client, loan );
        Transaction transactionCredit = new Transaction(TransactionType.CREDIT, loanApplicationDTO.getAmount(), loan.getName(), LocalDateTime.now(), account);

        accountRepository.save(account);
        clientLoanRepository.save(newLoan);
        transactionRepository.save(transactionCredit);

        return new ResponseEntity<>(HttpStatus.CREATED);

    }
}
