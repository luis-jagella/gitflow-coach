package com.luisjagella.gitflowcoach.dto.error;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.Map;

@Schema(description = "Resposta padronizada retornada quando uma requisição não pode ser processada")
public record ApiErrorResponse(
        @Schema(description = "Data e hora em que o erro foi produzido", example = "2026-07-24T23:45:00")
        LocalDateTime timestamp,
        @Schema(description = "Código de status HTTP", example = "400")
        int status,
        @Schema(description = "Categoria resumida do erro", example = "Erro de validação")
        String erro,
        @Schema(description = "Mensagem pública com detalhes do problema", example = "Um ou mais campos estão inválidos")
        String mensagem,
        @Schema(description = "Caminho da requisição que produziu o erro", example = "/projetos")
        String caminho,
        @Schema(
                description = "Mensagens de validação organizadas por campo; nulo quando o erro não é de campo",
                nullable = true,
                example = "{\"nome\":\"O nome é obrigatório\"}"
        )
        Map<String, String> campos
) {
}
