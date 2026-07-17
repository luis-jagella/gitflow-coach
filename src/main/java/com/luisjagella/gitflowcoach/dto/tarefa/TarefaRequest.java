package com.luisjagella.gitflowcoach.dto.tarefa;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TarefaRequest(
        @NotBlank(message = "O código é obrigatório")
        String codigo,

        @NotBlank(message = "O título é obrigatório")
        String titulo,

        @NotBlank(message = "A descrição é obrigatória")
        String descricao,

        @NotBlank(message = "A branch sugerida é obrigatória")
        String branchSugerida,

        @NotNull(message = "O projeto é obrigatório")
        Long projetoId
) {
}
