package com.luisjagella.gitflowcoach.dto.projeto;

public record ProjetoResponse(
        Long id,
        String nome,
        String repositorio,
        String branchBase
) {
}
