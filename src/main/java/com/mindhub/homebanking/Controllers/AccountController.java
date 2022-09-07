package com.mindhub.homebanking.Controllers;
import com.mindhub.homebanking.Repositories.AccountRepository;
import com.mindhub.homebanking.Repositories.ClientRepository;
import com.mindhub.homebanking.Repositories.TransactionRepository;
import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.model.*;
import com.mindhub.homebanking.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import static java.util.stream.Collectors.toList;

@RestController
public class AccountController {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @RequestMapping("/api/accounts")
    public List<AccountDTO> getAccounts() {
        return accountRepository.findAll().stream().map(account -> new AccountDTO(account)).collect(toList());
    }


    @RequestMapping("/api/accounts/{id}")
    public AccountDTO getAccounts(@PathVariable Long id){
        if(accountRepository.findById(id).isPresent()){
            Account account = accountRepository.findById(id).get();
            return new AccountDTO(account);
        }
        return null;
    }

    @GetMapping("/api/clients/current/accounts")
    public List<AccountDTO> getClientAccounts(Authentication authentication){
        Client client = this.clientRepository.findByEmail(authentication.getName());
        return accountRepository.findByClient(client).stream().map(account -> new AccountDTO(account)).collect(toList());
    }


    @PostMapping("/api/clients/current/accounts")
    public ResponseEntity<Object> createAccounts(@RequestParam AccountType accountType, Authentication authentication){
        Client client = this.clientRepository.findByEmail(authentication.getName());

        if(client.getAccounts().size() >= 3){
         return new ResponseEntity<>("Se supero el numero maximo de cuentas", HttpStatus.FORBIDDEN);
        }

        Account account = new Account( generateRandomNumber(1, 99999999, accountRepository), LocalDateTime.now(), 0, accountType);

        client.addAccount(account);
        accountRepository.save(account);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/api/clients/current/accounts")
    public ResponseEntity<Object> deleteAccount( @RequestParam String number, Authentication authentication){

        Client client = this.clientRepository.findByEmail(authentication.getName());

        if(accountRepository.findByNumber(number) == null){
            return new ResponseEntity<>("La cuenta no existe.", HttpStatus.FORBIDDEN);
        }

        if(accountRepository.findByNumberAndClient(number, client) == null){
            return new ResponseEntity<>("La cuenta no le pertenece.", HttpStatus.FORBIDDEN);
        }

        Account account = this.accountRepository.findByNumberAndClient(number, client);

        for(int i = 0; i< this.transactionRepository.findByAccount(account).size(); i++){
            transactionRepository.delete(this.transactionRepository.findByAccount(account).get(i));
        }

        accountRepository.delete(account);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public String generateRandomNumber(int min, int max, AccountRepository accountRepository) {
        return AccountUtils.getString(min, max, accountRepository);
    }


}
