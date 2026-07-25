package com.luisjagella.gitflowcoach.controller;

import com.luisjagella.gitflowcoach.dto.projeto.ProjetoRequest;
import com.luisjagella.gitflowcoach.dto.projeto.ProjetoResponse;
import com.luisjagella.gitflowcoach.service.ProjetoService;
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
@RequestMapping("/projetos")
@Tag(name = "Projetos", description = "Cadastro e gerenciamento dos projetos acompanhados")
@ApiResponse(responseCode = "500", description = "Erro interno inesperado")
public class ProjetoController {

    private final ProjetoService projetoService;

    public ProjetoController(ProjetoService projetoService) {
        this.projetoService = projetoService;
    }

    @PostMapping
    @Operation(summary = "Cadastrar projeto", description = "Cria um projeto com seu repositório e branch base")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Projeto cadastrado"),
            @ApiResponse(responseCode = "400", description = "Dados do projeto inválidos")
    })
    public ResponseEntity<ProjetoResponse> cadastrar(@Valid @RequestBody ProjetoRequest request) {
        ProjetoResponse response = projetoService.cadastrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "Listar projetos", description = "Retorna todos os projetos cadastrados")
    @ApiResponse(responseCode = "200", description = "Projetos encontrados")
    public ResponseEntity<List<ProjetoResponse>> listar() {
        return ResponseEntity.ok(projetoService.listar());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar projeto por ID", description = "Retorna os dados de um projeto específico")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Projeto encontrado"),
            @ApiResponse(responseCode = "404", description = "Projeto não encontrado")
    })
    public ResponseEntity<ProjetoResponse> buscarPorId(
            @Parameter(description = "Identificador do projeto", example = "1")
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(projetoService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar projeto", description = "Atualiza os dados de um projeto existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Projeto atualizado"),
            @ApiResponse(responseCode = "400", description = "Dados do projeto inválidos"),
            @ApiResponse(responseCode = "404", description = "Projeto não encontrado")
    })
    public ResponseEntity<ProjetoResponse> atualizar(
            @Parameter(description = "Identificador do projeto", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody ProjetoRequest request
    ) {
        return ResponseEntity.ok(projetoService.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir projeto", description = "Remove um projeto existente")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Projeto excluído"),
            @ApiResponse(responseCode = "404", description = "Projeto não encontrado")
    })
    public ResponseEntity<Void> excluir(
            @Parameter(description = "Identificador do projeto", example = "1")
            @PathVariable Long id
    ) {
        projetoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
