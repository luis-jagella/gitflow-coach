package com.luisjagella.gitflowcoach.controller;

import com.luisjagella.gitflowcoach.dto.checklist.ChecklistItemResponse;
import com.luisjagella.gitflowcoach.dto.checklist.ChecklistItemStatusRequest;
import com.luisjagella.gitflowcoach.dto.error.ApiErrorResponse;
import com.luisjagella.gitflowcoach.service.ChecklistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/checklist-itens")
@Tag(name = "Checklist", description = "Atualização dos itens do checklist de tarefas")
@ApiResponse(
        responseCode = "500",
        description = "Erro interno inesperado",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))
)
public class ChecklistController {

    private final ChecklistService checklistService;

    public ChecklistController(ChecklistService checklistService) {
        this.checklistService = checklistService;
    }

    @PatchMapping("/{id}")
    @Operation(
            summary = "Atualizar status do item",
            description = "Marca um item do checklist como concluído ou não concluído"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Status do item atualizado"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Status informado inválido",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Item de checklist não encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    public ResponseEntity<ChecklistItemResponse> atualizarStatus(
            @Parameter(description = "Identificador do item de checklist", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody ChecklistItemStatusRequest request
    ) {
        return ResponseEntity.ok(checklistService.atualizarStatus(id, request));
    }
}
