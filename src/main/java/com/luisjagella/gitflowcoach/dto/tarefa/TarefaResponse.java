package com.luisjagella.gitflowcoach.dto.tarefa;

import com.luisjagella.gitflowcoach.dto.checklist.ChecklistItemResponse;

import java.util.List;

public record TarefaResponse(
        Long id,
        String codigo,
        String titulo,
        String descricao,
        String branchSugerida,
        Long projetoId,
        String projetoNome,
        List<ChecklistItemResponse> checklist
) {
}
