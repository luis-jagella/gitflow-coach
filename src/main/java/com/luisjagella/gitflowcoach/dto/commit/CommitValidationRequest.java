package com.luisjagella.gitflowcoach.dto.commit;

import jakarta.validation.constraints.NotBlank;

public record CommitValidationRequest(
        @NotBlank(message = "A mensagem de commit é obrigatória")
        String mensagem
) {
}
