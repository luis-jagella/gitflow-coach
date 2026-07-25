package com.luisjagella.gitflowcoach.dto.tarefa;

import com.luisjagella.gitflowcoach.dto.checklist.ChecklistItemResponse;
import com.luisjagella.gitflowcoach.dto.git.GitCommandResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record TarefaResponse(
        Long id,
        String codigo,
        String titulo,
        String descricao,
        @Schema(
                description = "Nome de branch gerado para iniciar o trabalho na tarefa",
                example = "tarefa/216026-ajuste-calculo-apuracao"
        )
        String branchSugerida,
        Long projetoId,
        String projetoNome,
        @Schema(description = "Itens do checklist padrão da tarefa, ordenados pelo campo ordem")
        List<ChecklistItemResponse> checklist,
        @Schema(description = "Sugestões ordenadas de comandos Git geradas a partir das branches atualizadas")
        List<GitCommandResponse> comandosGit
) {
}
