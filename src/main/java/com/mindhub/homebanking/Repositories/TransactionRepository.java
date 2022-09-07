package com.mindhub.homebanking.Repositories;
import com.mindhub.homebanking.model.Account;
import com.mindhub.homebanking.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.LocalDateTime;
import java.util.List;

@RepositoryRestResource
public interface TransactionRepository extends JpaRepository<Transaction, Long>{

    List<Transaction> findByAccount(Account account);
}