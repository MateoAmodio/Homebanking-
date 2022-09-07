package com.mindhub.homebanking.Controllers;

import com.mindhub.homebanking.Repositories.AccountRepository;
import com.mindhub.homebanking.Repositories.CardRepository;
import com.mindhub.homebanking.Repositories.ClientRepository;
import com.mindhub.homebanking.Repositories.TransactionRepository;
import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.CardTransactionDTO;
import com.mindhub.homebanking.dtos.TransactionDTO;
import com.mindhub.homebanking.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
public class TransactionController {

        @Autowired
        private TransactionRepository transactionRepository;
        @Autowired
        private AccountRepository accountRepository;
        @Autowired
        private ClientRepository clientRepository;
        @Autowired
        private CardRepository cardRepository;

        @Transactional
        @PostMapping("/api/transactions")
        public ResponseEntity<Object> createCards(@RequestParam  double amount , @RequestParam String description, @RequestParam String fromAccountNumber,  @RequestParam String toAccountNumber, Authentication authentication){

            Client client = this.clientRepository.findByEmail(authentication.getName());

            if( description == null || fromAccountNumber == null || toAccountNumber == null){
                return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
            }

            if(accountRepository.findByNumberAndClient(fromAccountNumber, client) == null){
                return new ResponseEntity<>("No posee una cuenta con ese identificador.", HttpStatus.FORBIDDEN);
            }

            if(fromAccountNumber == toAccountNumber){
                return new ResponseEntity<>("Esta intentando realizar una transaccion a la misma cuenta.", HttpStatus.FORBIDDEN);
            }

            if(amount <= 0){
                return new ResponseEntity<>("El monto transferido debe ser mayor a $0.", HttpStatus.FORBIDDEN);
            }

            if(accountRepository.findByNumber(fromAccountNumber).getBalance() < amount){
                return new ResponseEntity<>("No tiene los fondos suficientes para realizar la transaccion.", HttpStatus.FORBIDDEN);
            }

            if(accountRepository.findByNumber(toAccountNumber) == null){
                return new ResponseEntity<>("La cuenta destino ingresada no existe.", HttpStatus.FORBIDDEN);
            }



            Account transactionOrigin = accountRepository.findByNumber(fromAccountNumber);
            Account transactionDestiny = accountRepository.findByNumber(toAccountNumber);

            transactionOrigin.setBalance(transactionOrigin.getBalance() - amount);
            Transaction transactionDebit = new Transaction(TransactionType.DEBIT, -amount, toAccountNumber + " " + description ,LocalDateTime.now(), transactionOrigin);

            transactionDestiny.setBalance(transactionDestiny.getBalance() + amount);
            Transaction transactionCredit = new Transaction(TransactionType.CREDIT, amount, fromAccountNumber + " " + description ,LocalDateTime.now(), transactionDestiny);

            accountRepository.save(transactionOrigin);
            accountRepository.save(transactionDestiny);
            transactionRepository.save(transactionCredit);
            transactionRepository.save(transactionDebit);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }

        @Transactional
        @PostMapping("/api/cardTransactions")
        public ResponseEntity<Object> createCards(@RequestBody CardTransactionDTO cardTransactionDTO, Authentication authentication){

        Client client = this.clientRepository.findByEmail(authentication.getName());
        int i = 0;

        if(cardTransactionDTO.getAmount() <= 0 || cardTransactionDTO.getCvv() < 100 || cardTransactionDTO.getCvv() > 999 || cardTransactionDTO.getDescription() == null || cardTransactionDTO.getNumber() == null) {
            return new ResponseEntity<>("Alguno de los datos ingresados es incorrecto", HttpStatus.FORBIDDEN);
        }

        if(cardRepository.findByNumberAndClient(cardTransactionDTO.getNumber(), client).getThruDate().isAfter(LocalDateTime.now())== true) {
            return new ResponseEntity<>("La tarjeta se encuentra vencida", HttpStatus.FORBIDDEN);
        }

        while(i>=0){
            if(accountRepository.findByClient(client).get(i).getBalance() >= cardTransactionDTO.getAmount()){
                break;
            }
            i++;
            if (accountRepository.findByClient(client).size() == i){
                return new ResponseEntity<>("No posee el saldo suficiente en ninguna de sus cuentas para realizar la transaccion", HttpStatus.FORBIDDEN);
            }

        }

        Account account = accountRepository.findByClient(client).get(i);
        account.setBalance(account.getBalance() - cardTransactionDTO.getAmount());
        Transaction transaction = new Transaction(TransactionType.DEBIT, -cardTransactionDTO.getAmount(), cardTransactionDTO.getDescription() ,LocalDateTime.now(), account);
        accountRepository.save(account);
        transactionRepository.save(transaction);

        return new ResponseEntity<>(HttpStatus.CREATED);

    }

    @GetMapping("/api/transactionFilter")
    public List<TransactionDTO> filterTransactions(@RequestParam String fromDate, @RequestParam String thruDate, @RequestParam String number, Authentication authentication){

        Account account = accountRepository.findByNumber(number);

        LocalDate from = LocalDate.parse(fromDate);
        LocalDate thru = LocalDate.parse(thruDate);


        LocalTime localTime = LocalTime.MIDNIGHT;
        LocalDateTime fromDateTime = LocalDateTime.of(from, localTime);
        LocalDateTime thruDateTime = LocalDateTime.of(thru, localTime);

        List<Transaction> transactions= transactionRepository.findByAccount(account);
        List<Transaction> transactionsFiltered = new ArrayList<>();

        for(int i = 0; i < transactions.size(); i++){

        if(transactions.get(i).getDate().isAfter(fromDateTime) && transactions.get(i).getDate().isBefore(thruDateTime)){
            transactionsFiltered.add(transactions.get(i));
        }
        }

        return transactionsFiltered.stream().map(transaction -> new TransactionDTO(transaction)).collect(toList());
    }

}
