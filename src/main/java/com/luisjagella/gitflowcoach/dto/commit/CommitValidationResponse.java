package com.luisjagella.gitflowcoach.dto.commit;

public record CommitValidationResponse(
        boolean valida,
        String tipo,
        String descricao,
        String motivo,
        String sugestao
) {
}
