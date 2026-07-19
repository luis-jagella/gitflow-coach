package com.luisjagella.gitflowcoach.service;

import com.luisjagella.gitflowcoach.dto.checklist.ChecklistItemResponse;
import com.luisjagella.gitflowcoach.dto.checklist.ChecklistItemStatusRequest;
import com.luisjagella.gitflowcoach.entity.ChecklistItem;
import com.luisjagella.gitflowcoach.exception.ChecklistItemNaoEncontradoException;
import com.luisjagella.gitflowcoach.repository.ChecklistItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChecklistService {

    private final ChecklistItemRepository checklistItemRepository;

    public ChecklistService(ChecklistItemRepository checklistItemRepository) {
        this.checklistItemRepository = checklistItemRepository;
    }

    @Transactional
    public ChecklistItemResponse atualizarStatus(Long id, ChecklistItemStatusRequest request) {
        ChecklistItem item = checklistItemRepository.findById(id)
                .orElseThrow(() -> new ChecklistItemNaoEncontradoException(id));
        item.setConcluido(request.concluido());

        return toResponse(checklistItemRepository.save(item));
    }

    private ChecklistItemResponse toResponse(ChecklistItem item) {
        return new ChecklistItemResponse(
                item.getId(),
                item.getDescricao(),
                item.isConcluido(),
                item.getOrdem()
        );
    }
}
