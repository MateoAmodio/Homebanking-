package com.mindhub.homebanking.Repositories;
import com.mindhub.homebanking.model.Account;
import com.mindhub.homebanking.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource
public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByNumber(String number);
    Account findByNumberAndClient (String number, Client client);
    List<Account> findByClient (Client client);
}
