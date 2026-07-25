package com.luisjagella.gitflowcoach.dto.commit;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record CommitValidationRequest(
        @Schema(
                description = "Mensagem no formato tipo: descrição",
                example = "feat: adiciona validação de commits"
        )
        @NotBlank(message = "A mensagem de commit é obrigatória")
        String mensagem
) {
}
