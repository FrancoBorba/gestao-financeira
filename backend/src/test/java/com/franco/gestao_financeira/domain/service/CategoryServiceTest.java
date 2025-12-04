package com.franco.gestao_financeira.domain.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.franco.gestao_financeira.application.dto.CategoryDTO;
import com.franco.gestao_financeira.domain.exception.BusinessRuleException;
import com.franco.gestao_financeira.domain.model.BaseCategory;
import com.franco.gestao_financeira.domain.model.User;
import com.franco.gestao_financeira.infrastructure.repository.BaseCategoryRepository;
import com.franco.gestao_financeira.infrastructure.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    BaseCategoryRepository baseRepository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    CategoryService categoryService;

    User franco;

    @BeforeEach
    void setUp() {
        // Usuário padrão para todos os testes
        franco = new User(1L, "Franco", "email@test.com", "franco.dev", 1);
    }

    // =================================================================
    // TESTES: CRIAÇÃO (CREATE)
    // =================================================================

   @Test
    void shouldCreateBaseCategorySuccessfully() {
        CategoryDTO dto = new CategoryDTO("Alimentação", "#FF0000");
        
        when(userRepository.findById(1L)).thenReturn(Optional.of(franco));
        when(baseRepository.existsByNameAndUser("Alimentação", franco)).thenReturn(false);
        
        when(baseRepository.save(any(BaseCategory.class))).thenAnswer(i -> i.getArgument(0));

        BaseCategory result = categoryService.createCategory(1L, dto);

        assertNotNull(result);
        assertEquals("Alimentação", result.getName());
        assertEquals("#FF0000", result.getColorHex());
        assertTrue(result.getIsActive());
    }

    @Test
    void shouldThrowErrorDuplicateCategory() {
        CategoryDTO dto = new CategoryDTO("Lazer", "#00FF00");
        
        when(userRepository.findById(1L)).thenReturn(Optional.of(franco));
        when(baseRepository.existsByNameAndUser("Lazer", franco)).thenReturn(true);

        BusinessRuleException ex = assertThrows(BusinessRuleException.class, () -> {
            categoryService.createCategory(1L, dto);
        });
        
        assertTrue(ex.getMessage().contains("Categoria já existe"));
        verify(baseRepository, never()).save(any());
    }

    @Test
    void shouldThrowErrorWhenUserNotFound() {
        Long userIdInexistente = 99L;
        CategoryDTO dto = new CategoryDTO("Teste", "#000");
        
        when(userRepository.findById(userIdInexistente)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            categoryService.createCategory(userIdInexistente, dto);
        });
        
        verify(baseRepository, never()).save(any());
    }

    // =================================================================
    // TESTES: ATUALIZAÇÃO (UPDATE) - CORRIGIDO
    // =================================================================

    @Test
    void shouldUpdateCategorySuccessfully() {
        Long categoryId = 10L;
        CategoryDTO updateDto = new CategoryDTO("Lazer Editado", "#0000FF");
        
        // --- CORREÇÃO AQUI: Criando objeto com SETTERS para evitar erro no construtor ---
        BaseCategory existing = new BaseCategory();
        existing.setId(categoryId);
        existing.setName("Lazer");
        existing.setColorHex("#FF0000");
        existing.setIsActive(true);
        existing.setUser(franco); // <--- IMPORTANTE: Setando o dono explicitamente!
        
        when(userRepository.findById(1L)).thenReturn(Optional.of(franco));
        when(baseRepository.findById(categoryId)).thenReturn(Optional.of(existing));
        when(baseRepository.existsByNameAndUser("Lazer Editado", franco)).thenReturn(false);
        
        when(baseRepository.save(any(BaseCategory.class))).thenAnswer(i -> i.getArgument(0));

        BaseCategory updated = categoryService.updateCategory(1L, categoryId, updateDto);

        assertEquals("Lazer Editado", updated.getName());
        assertEquals("#0000FF", updated.getColorHex());
    }

    @Test
    void shouldThrowErrorWhenUpdatingToExistingName() {
        Long categoryId = 10L;
        CategoryDTO updateDto = new CategoryDTO("Alimentação", "#000");
        
     
        BaseCategory existing = new BaseCategory();
        existing.setId(categoryId);
        existing.setName("Lazer");
        existing.setUser(franco); 
        
        when(userRepository.findById(1L)).thenReturn(Optional.of(franco));
        when(baseRepository.findById(categoryId)).thenReturn(Optional.of(existing));
        when(baseRepository.existsByNameAndUser("Alimentação", franco)).thenReturn(true);

        BusinessRuleException ex = assertThrows(BusinessRuleException.class, () -> {
            categoryService.updateCategory(1L, categoryId, updateDto);
        });
        
        assertTrue(ex.getMessage().contains("Categoria já existe"));
        verify(baseRepository, never()).save(any());
    }

    // =================================================================
    // TESTES: EXCLUSÃO (DELETE) - CORRIGIDO
    // =================================================================

    @Test
    void shouldDeactivateCategoryInsteadOfDeleting() {
        Long categoryId = 10L;
    
        BaseCategory existing = new BaseCategory();
        existing.setId(categoryId);
        existing.setName("Lazer");
        existing.setIsActive(true);
        existing.setUser(franco); // <--- Setando dono (Obrigatório para validação de ownership)

       

        when(baseRepository.findById(categoryId)).thenReturn(Optional.of(existing));
        when(baseRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        categoryService.deleteCategory(1L, categoryId);

        assertFalse(existing.getIsActive());
        verify(baseRepository).save(existing);
    }
    
    @Test
    void shouldThrowErrorWhenDeletingCategoryThatDoesNotBelongToUser() {
        Long categoryId = 10L;
        
        // Outro usuário
        User outroUsuario = new User();
        outroUsuario.setId(99L);
        
        // Categoria pertence ao Outro
        BaseCategory existing = new BaseCategory();
        existing.setId(categoryId);
        existing.setName("Lazer");
        existing.setUser(outroUsuario); 

      
        when(baseRepository.findById(categoryId)).thenReturn(Optional.of(existing));

        BusinessRuleException ex = assertThrows(BusinessRuleException.class, () -> {
            categoryService.deleteCategory(1L, categoryId); 
        });
        
        assertEquals("Esta categoria não pertence a você", ex.getMessage());
        verify(baseRepository, never()).save(any());
    }
}