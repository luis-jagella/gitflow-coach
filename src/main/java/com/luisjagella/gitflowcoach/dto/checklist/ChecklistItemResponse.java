package com.luisjagella.gitflowcoach.dto.checklist;

public record ChecklistItemResponse(
        Long id,
        String descricao,
        boolean concluido,
        int ordem
) {
}
