package com.luisjagella.gitflowcoach.service;

import com.luisjagella.gitflowcoach.dto.checklist.ChecklistItemResponse;
import com.luisjagella.gitflowcoach.dto.checklist.ChecklistItemStatusRequest;
import com.luisjagella.gitflowcoach.entity.ChecklistItem;
import com.luisjagella.gitflowcoach.exception.ChecklistItemNaoEncontradoException;
import com.luisjagella.gitflowcoach.repository.ChecklistItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChecklistServiceTest {

    private static final Long ITEM_ID = 1L;

    @Mock
    private ChecklistItemRepository checklistItemRepository;

    @InjectMocks
    private ChecklistService checklistService;

    @Test
    void deveAtualizarItemParaConcluido() {
        ChecklistItem item = criarItem();
        when(checklistItemRepository.findById(ITEM_ID)).thenReturn(Optional.of(item));
        when(checklistItemRepository.save(item)).thenReturn(item);

        ChecklistItemResponse response = checklistService.atualizarStatus(
                ITEM_ID,
                new ChecklistItemStatusRequest(true)
        );

        assertAll(
                () -> assertEquals(ITEM_ID, response.id()),
                () -> assertEquals("Atualizar a branch base", response.descricao()),
                () -> assertTrue(response.concluido()),
                () -> assertEquals(1, response.ordem()),
                () -> assertTrue(item.isConcluido())
        );
        verify(checklistItemRepository).save(item);
    }

    @Test
    void deveLancarExcecaoQuandoItemNaoExistir() {
        when(checklistItemRepository.findById(ITEM_ID)).thenReturn(Optional.empty());

        assertThrows(
                ChecklistItemNaoEncontradoException.class,
                () -> checklistService.atualizarStatus(
                        ITEM_ID,
                        new ChecklistItemStatusRequest(true)
                )
        );

        verify(checklistItemRepository, never()).save(any(ChecklistItem.class));
    }

    private ChecklistItem criarItem() {
        ChecklistItem item = new ChecklistItem();
        item.setId(ITEM_ID);
        item.setDescricao("Atualizar a branch base");
        item.setConcluido(false);
        item.setOrdem(1);
        return item;
    }
}
