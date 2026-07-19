package com.luisjagella.gitflowcoach.dto.checklist;

import jakarta.validation.constraints.NotNull;

public record ChecklistItemStatusRequest(
        @NotNull(message = "O status de conclusão é obrigatório")
        Boolean concluido
) {
}
