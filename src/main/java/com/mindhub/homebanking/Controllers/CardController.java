package com.mindhub.homebanking.Controllers;

import com.mindhub.homebanking.Repositories.AccountRepository;
import com.mindhub.homebanking.Repositories.CardRepository;
import com.mindhub.homebanking.Repositories.ClientRepository;
import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.dtos.CardTransactionDTO;
import com.mindhub.homebanking.model.*;
import com.mindhub.homebanking.utils.CardUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
public class CardController {

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private CardRepository cardRepository;


    @GetMapping("/api/clients/current/cards")
    public List<CardDTO> getCards(Authentication authentication){
        Client client = this.clientRepository.findByEmail(authentication.getName());
        return cardRepository.findByClient(client).stream().map(cards -> new CardDTO(cards)).collect(toList());
    }

    @PostMapping("/api/clients/current/cards")
    public ResponseEntity<Object> createCards( @RequestParam CardColor cardColor, @RequestParam CardType cardType, Authentication authentication){

        Client client = this.clientRepository.findByEmail(authentication.getName());

        if(cardColor == null || cardType == null){
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        if(cardRepository.findByTypeAndClient(cardType, client).size() >= 3){
            return new ResponseEntity<>("Se supero el numero maximo de Tarjetas", HttpStatus.FORBIDDEN);
        }

        cardRepository.save(new Card(client, cardType, cardColor, stringBuilder(cardRepository), getRandomNumber(100, 999), LocalDateTime.now(),LocalDateTime.now().plusYears(5)));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @DeleteMapping("/api/clients/current/cards")
    public ResponseEntity<Object> deleteCards( @RequestParam String number, Authentication authentication){

        Client client = this.clientRepository.findByEmail(authentication.getName());

        if(cardRepository.findByNumber(number).orElse(null) == null){
            return new ResponseEntity<>("La tarjeta no existe.", HttpStatus.FORBIDDEN);
        }

        if(cardRepository.findByNumberAndClient(number, client) == null){
            return new ResponseEntity<>("La tarjeta no le pertenece.", HttpStatus.FORBIDDEN);
        }

        Card card = this.cardRepository.findByNumberAndClient(number, client);
        cardRepository.delete(card);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public int getRandomNumber(int min, int max) {
        return CardUtils.extracted(min, max);
    }
   
    public String stringBuilder(CardRepository cardRepository) {
        return CardUtils.getString(cardRepository);
    }

}
