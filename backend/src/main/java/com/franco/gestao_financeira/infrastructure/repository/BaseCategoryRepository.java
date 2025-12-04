package com.franco.gestao_financeira.infrastructure.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.franco.gestao_financeira.domain.model.BaseCategory;
import com.franco.gestao_financeira.domain.model.User;

@Repository
public interface BaseCategoryRepository extends JpaRepository<BaseCategory, Long> {
    // Buscar todas as categorias base de um usuário
    List<BaseCategory> findByUser(User user);

    Boolean existsByNameAndUser(String string, User franco);
}