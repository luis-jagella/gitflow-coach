package com.luisjagella.gitflowcoach.dto.tarefa;

public record TarefaResponse(
        Long id,
        String codigo,
        String titulo,
        String descricao,
        String branchSugerida,
        Long projetoId,
        String projetoNome
) {
}
