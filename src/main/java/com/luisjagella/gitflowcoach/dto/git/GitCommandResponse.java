package com.luisjagella.gitflowcoach.dto.git;

public record GitCommandResponse(
        int ordem,
        String descricao,
        String comando
) {
}
