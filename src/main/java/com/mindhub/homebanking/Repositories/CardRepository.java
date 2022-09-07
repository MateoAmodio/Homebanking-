package com.mindhub.homebanking.Repositories;
import com.mindhub.homebanking.model.Card;
import com.mindhub.homebanking.model.CardType;
import com.mindhub.homebanking.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource
public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> findByTypeAndClient (CardType type, Client client);
    Optional<Card> findByNumber (String num);

    List<Card> findByClient(Client client);
    Card findByNumberAndClient (String number, Client client);

}