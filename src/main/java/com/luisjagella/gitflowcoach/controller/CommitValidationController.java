package com.luisjagella.gitflowcoach.controller;

import com.luisjagella.gitflowcoach.dto.commit.CommitValidationRequest;
import com.luisjagella.gitflowcoach.dto.commit.CommitValidationResponse;
import com.luisjagella.gitflowcoach.dto.error.ApiErrorResponse;
import com.luisjagella.gitflowcoach.service.CommitMessageValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/commits")
@Tag(name = "Commits", description = "Validação de mensagens no padrão Conventional Commits")
@ApiResponse(
        responseCode = "500",
        description = "Erro interno inesperado",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))
)
public class CommitValidationController {

    private final CommitMessageValidator commitMessageValidator;

    public CommitValidationController(CommitMessageValidator commitMessageValidator) {
        this.commitMessageValidator = commitMessageValidator;
    }

    @PostMapping("/validar")
    @Operation(
            summary = "Validar mensagem de commit",
            description = "Valida o formato tipo: descrição. Mensagens semanticamente válidas e inválidas retornam HTTP 200; o campo valida informa o resultado. Um request estruturalmente inválido retorna HTTP 400."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Resultado semântico da validação",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CommitValidationResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Mensagem válida",
                                            value = "{\"valida\":true,\"tipo\":\"feat\",\"descricao\":\"adiciona validação de commits\",\"motivo\":null,\"sugestao\":null}"
                                    ),
                                    @ExampleObject(
                                            name = "Mensagem inválida",
                                            value = "{\"valida\":false,\"tipo\":\"feat\",\"descricao\":\"adiciona validação de commits\",\"motivo\":\"A mensagem deve seguir o formato tipo: descrição\",\"sugestao\":\"feat: adiciona validação de commits\"}"
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Request estruturalmente inválido",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    public ResponseEntity<CommitValidationResponse> validar(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Mensagem no formato tipo: descrição",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = CommitValidationRequest.class),
                            examples = @ExampleObject(
                                    name = "Commit de funcionalidade",
                                    value = "{\"mensagem\":\"feat: adiciona validação de commits\"}"
                            )
                    )
            )
            @Valid @RequestBody CommitValidationRequest request
    ) {
        return ResponseEntity.ok(commitMessageValidator.validar(request.mensagem()));
    }
}
