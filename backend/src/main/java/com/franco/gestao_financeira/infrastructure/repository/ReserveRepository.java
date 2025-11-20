package com.franco.gestao_financeira.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.franco.gestao_financeira.domain.model.Reserve;

@Repository
public interface ReserveRepository extends JpaRepository<Reserve, Long> {
}