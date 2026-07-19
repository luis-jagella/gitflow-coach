package com.luisjagella.gitflowcoach.controller;

import com.luisjagella.gitflowcoach.dto.checklist.ChecklistItemResponse;
import com.luisjagella.gitflowcoach.dto.checklist.ChecklistItemStatusRequest;
import com.luisjagella.gitflowcoach.service.ChecklistService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/checklist-itens")
public class ChecklistController {

    private final ChecklistService checklistService;

    public ChecklistController(ChecklistService checklistService) {
        this.checklistService = checklistService;
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ChecklistItemResponse> atualizarStatus(
            @PathVariable Long id,
            @Valid @RequestBody ChecklistItemStatusRequest request
    ) {
        return ResponseEntity.ok(checklistService.atualizarStatus(id, request));
    }
}
