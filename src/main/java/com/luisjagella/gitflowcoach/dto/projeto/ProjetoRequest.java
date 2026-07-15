package com.luisjagella.gitflowcoach.dto.projeto;

import jakarta.validation.constraints.NotBlank;

public record ProjetoRequest(
        @NotBlank(message = "O nome é obrigatório")
        String nome,

        @NotBlank(message = "O repositório é obrigatório")
        String repositorio,

        @NotBlank(message = "A branch base é obrigatória")
        String branchBase
) {
}
