package com.franco.gestao_financeira.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.franco.gestao_financeira.domain.model.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}