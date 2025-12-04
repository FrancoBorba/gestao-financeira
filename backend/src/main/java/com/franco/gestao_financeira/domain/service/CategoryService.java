package com.franco.gestao_financeira.domain.service;

import com.franco.gestao_financeira.application.dto.CategoryDTO;
import com.franco.gestao_financeira.domain.exception.BusinessRuleException;
import com.franco.gestao_financeira.domain.exception.ResourceNotFoundException;
import com.franco.gestao_financeira.domain.model.BaseCategory;
import com.franco.gestao_financeira.domain.model.User;
import com.franco.gestao_financeira.infrastructure.repository.BaseCategoryRepository;
import com.franco.gestao_financeira.infrastructure.repository.UserRepository;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryService {

    private final BaseCategoryRepository baseRepository;
    private final UserRepository userRepository;


    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public CategoryService(BaseCategoryRepository baseRepository, UserRepository userRepository) {
        this.baseRepository = baseRepository;
        this.userRepository = userRepository;
    }


    @Transactional
    public BaseCategory createCategory(Long userId, CategoryDTO dto) {
        validateDTO(dto);
        User user = findUserById(userId);
        
        validateDuplicateName(dto.name(), user);

        BaseCategory newCategory = mapToEntity(dto, user);

        return baseRepository.save(newCategory);
    }

    @Transactional
    public BaseCategory updateCategory(Long userId, Long categoryId, CategoryDTO dto) {
        validateDTO(dto);
        User user = findUserById(userId);
        BaseCategory category = findCategoryById(categoryId);

        validateOwnership(category, user);

  
        if (!category.getName().equalsIgnoreCase(dto.name())) {
            validateDuplicateName(dto.name(), user);
        }

        updateCategoryFields(category, dto);

        return baseRepository.save(category);
    }

    @Transactional
    public void deleteCategory(Long userId, Long categoryId) {
        BaseCategory category = findCategoryById(categoryId);
        
        User userRef = new User(); 
        userRef.setId(userId);
        
        validateOwnership(category, userRef);

        category.setIsActive(false); 
        baseRepository.save(category);
    }


    private User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado. ID: " + id));
    }

    private BaseCategory findCategoryById(Long id) {
        return baseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada. ID: " + id));
    }

    private void validateDuplicateName(String name, User user) {
        if (baseRepository.existsByNameAndUser(name, user)) {
            throw new BusinessRuleException("Categoria já existe: " + name);
        }
    }

    private void validateOwnership(BaseCategory category, User user) {
        if (!category.getUser().getId().equals(user.getId())) {
            throw new BusinessRuleException("Esta categoria não pertence a você");
        }
    }

    private BaseCategory mapToEntity(CategoryDTO dto, User user) {
        BaseCategory category = new BaseCategory();
        category.setName(dto.name());
        category.setColorHex(dto.colorHex());
        category.setUser(user);
        category.setIsActive(true);
        return category;
    }

    private void updateCategoryFields(BaseCategory category, CategoryDTO dto) {
        category.setName(dto.name());
        category.setColorHex(dto.colorHex());
    }

    private void validateDTO(CategoryDTO dto) {
        Set<ConstraintViolation<CategoryDTO>> violations = validator.validate(dto);
        if (!violations.isEmpty()) {
            // Pega a primeira mensagem de erro (ex: "Formato de cor inválido") e lança
            String msg = violations.iterator().next().getMessage();
            throw new BusinessRuleException(msg);
        }
    }
}