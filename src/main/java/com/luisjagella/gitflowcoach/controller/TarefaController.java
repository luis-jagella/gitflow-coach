package com.luisjagella.gitflowcoach.controller;

import com.luisjagella.gitflowcoach.dto.git.GitCommandResponse;
import com.luisjagella.gitflowcoach.dto.tarefa.TarefaRequest;
import com.luisjagella.gitflowcoach.dto.tarefa.TarefaResponse;
import com.luisjagella.gitflowcoach.service.TarefaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tarefas")
@Tag(name = "Tarefas", description = "Gerenciamento de tarefas e orientações Git associadas")
@ApiResponse(responseCode = "500", description = "Erro interno inesperado")
public class TarefaController {

    private final TarefaService tarefaService;

    public TarefaController(TarefaService tarefaService) {
        this.tarefaService = tarefaService;
    }

    @PostMapping
    @Operation(
            summary = "Cadastrar tarefa",
            description = "Cria uma tarefa vinculada a um projeto e retorna a branch sugerida, o checklist padrão e os comandos Git"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tarefa cadastrada"),
            @ApiResponse(responseCode = "400", description = "Dados da tarefa inválidos"),
            @ApiResponse(responseCode = "404", description = "Projeto associado não encontrado")
    })
    public ResponseEntity<TarefaResponse> cadastrar(@Valid @RequestBody TarefaRequest request) {
        TarefaResponse response = tarefaService.cadastrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(
            summary = "Listar tarefas",
            description = "Retorna todas as tarefas com branch sugerida, checklist e comandos Git"
    )
    @ApiResponse(responseCode = "200", description = "Tarefas encontradas")
    public ResponseEntity<List<TarefaResponse>> listar() {
        return ResponseEntity.ok(tarefaService.listar());
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar tarefa por ID",
            description = "Retorna uma tarefa com os dados básicos do projeto, branch sugerida, checklist e comandos Git"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tarefa encontrada"),
            @ApiResponse(responseCode = "404", description = "Tarefa não encontrada")
    })
    public ResponseEntity<TarefaResponse> buscarPorId(
            @Parameter(description = "Identificador da tarefa", example = "1")
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(tarefaService.buscarPorId(id));
    }

    @GetMapping("/{id}/comandos-git")
    @Operation(
            summary = "Obter comandos Git da tarefa",
            description = "Gera uma lista ordenada de sugestões textuais de comandos Git usando as branches base e sugerida"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Comandos Git gerados"),
            @ApiResponse(responseCode = "404", description = "Tarefa não encontrada")
    })
    public ResponseEntity<List<GitCommandResponse>> buscarComandosGit(
            @Parameter(description = "Identificador da tarefa", example = "1")
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(tarefaService.buscarComandosGit(id));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Atualizar tarefa",
            description = "Atualiza a tarefa e sua associação com o projeto, recalculando a branch sugerida e os comandos Git"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tarefa atualizada"),
            @ApiResponse(responseCode = "400", description = "Dados da tarefa inválidos"),
            @ApiResponse(responseCode = "404", description = "Tarefa ou projeto associado não encontrado")
    })
    public ResponseEntity<TarefaResponse> atualizar(
            @Parameter(description = "Identificador da tarefa", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody TarefaRequest request
    ) {
        return ResponseEntity.ok(tarefaService.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir tarefa", description = "Remove uma tarefa existente")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Tarefa excluída"),
            @ApiResponse(responseCode = "404", description = "Tarefa não encontrada")
    })
    public ResponseEntity<Void> excluir(
            @Parameter(description = "Identificador da tarefa", example = "1")
            @PathVariable Long id
    ) {
        tarefaService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
