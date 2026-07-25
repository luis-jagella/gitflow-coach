package com.luisjagella.gitflowcoach.dto.commit;

import io.swagger.v3.oas.annotations.media.Schema;

public record CommitValidationResponse(
        @Schema(description = "Indica se a mensagem atende ao padrão aceito", example = "true")
        boolean valida,
        @Schema(description = "Tipo de commit identificado", example = "feat", nullable = true)
        String tipo,
        @Schema(description = "Descrição identificada na mensagem", example = "adiciona validação de commits", nullable = true)
        String descricao,
        @Schema(description = "Motivo da rejeição quando a mensagem é inválida", nullable = true)
        String motivo,
        @Schema(description = "Correção sugerida quando pode ser inferida", nullable = true)
        String sugestao
) {
}
