package com.luisjagella.gitflowcoach.exception;

import com.luisjagella.gitflowcoach.controller.ChecklistController;
import com.luisjagella.gitflowcoach.controller.CommitValidationController;
import com.luisjagella.gitflowcoach.controller.ProjetoController;
import com.luisjagella.gitflowcoach.controller.TarefaController;
import com.luisjagella.gitflowcoach.dto.checklist.ChecklistItemStatusRequest;
import com.luisjagella.gitflowcoach.service.ChecklistService;
import com.luisjagella.gitflowcoach.service.CommitMessageValidator;
import com.luisjagella.gitflowcoach.service.ProjetoService;
import com.luisjagella.gitflowcoach.service.TarefaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {
        ProjetoController.class,
        TarefaController.class,
        ChecklistController.class,
        CommitValidationController.class
})
@Import(GlobalExceptionHandler.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProjetoService projetoService;

    @MockitoBean
    private TarefaService tarefaService;

    @MockitoBean
    private ChecklistService checklistService;

    @MockitoBean
    private CommitMessageValidator commitMessageValidator;

    @Test
    void devePadronizarProjetoNaoEncontrado() throws Exception {
        when(projetoService.buscarPorId(1L))
                .thenThrow(new ProjetoNaoEncontradoException(1L));

        mockMvc.perform(get("/projetos/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp").isString())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.erro").value("Recurso não encontrado"))
                .andExpect(jsonPath("$.mensagem").value("Projeto não encontrado com o id: 1"))
                .andExpect(jsonPath("$.caminho").value("/projetos/1"))
                .andExpect(jsonPath("$.campos").value(nullValue()));
    }

    @Test
    void devePadronizarTarefaNaoEncontrada() throws Exception {
        when(tarefaService.buscarPorId(2L))
                .thenThrow(new TarefaNaoEncontradaException(2L));

        mockMvc.perform(get("/tarefas/2"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.erro").value("Recurso não encontrado"))
                .andExpect(jsonPath("$.mensagem").value("Tarefa não encontrada com o id: 2"))
                .andExpect(jsonPath("$.caminho").value("/tarefas/2"));
    }

    @Test
    void devePadronizarItemDeChecklistNaoEncontrado() throws Exception {
        when(checklistService.atualizarStatus(
                eq(3L),
                any(ChecklistItemStatusRequest.class)
        )).thenThrow(new ChecklistItemNaoEncontradoException(3L));

        mockMvc.perform(patch("/checklist-itens/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "concluido": true
                                }
                                """))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.erro").value("Recurso não encontrado"))
                .andExpect(jsonPath("$.mensagem").value(
                        "Item de checklist não encontrado com o id: 3"
                ))
                .andExpect(jsonPath("$.caminho").value("/checklist-itens/3"));
    }

    @Test
    void deveRetornarCamposInvalidosPorNome() throws Exception {
        mockMvc.perform(post("/projetos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nome": "",
                                  "repositorio": "",
                                  "branchBase": ""
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.erro").value("Erro de validação"))
                .andExpect(jsonPath("$.mensagem").value("Um ou mais campos estão inválidos"))
                .andExpect(jsonPath("$.caminho").value("/projetos"))
                .andExpect(jsonPath("$.campos.nome").value("O nome é obrigatório"))
                .andExpect(jsonPath("$.campos.repositorio").value("O repositório é obrigatório"))
                .andExpect(jsonPath("$.campos.branchBase").value("A branch base é obrigatória"));
    }

    @Test
    void deveTratarJsonMalformado() throws Exception {
        mockMvc.perform(post("/projetos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.erro").value("Requisição inválida"))
                .andExpect(jsonPath("$.mensagem").value(
                        "O corpo da requisição está ausente ou possui formato inválido"
                ))
                .andExpect(jsonPath("$.caminho").value("/projetos"))
                .andExpect(jsonPath("$.campos").value(nullValue()));
    }

    @Test
    void deveTratarArgumentoInvalido() throws Exception {
        String mensagem = "feat: adiciona tratamento global";
        when(commitMessageValidator.validar(mensagem))
                .thenThrow(new IllegalArgumentException("Argumento informado é inválido"));

        mockMvc.perform(post("/commits/validar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "mensagem": "feat: adiciona tratamento global"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.erro").value("Argumento inválido"))
                .andExpect(jsonPath("$.mensagem").value("Argumento informado é inválido"))
                .andExpect(jsonPath("$.caminho").value("/commits/validar"))
                .andExpect(jsonPath("$.campos").value(nullValue()));
    }

    @Test
    void deveTratarErroInesperadoSemExporDetalhesInternos() throws Exception {
        when(projetoService.listar())
                .thenThrow(new RuntimeException("Falha sensível de banco de dados"));

        mockMvc.perform(get("/projetos"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.erro").value("Erro interno"))
                .andExpect(jsonPath("$.mensagem").value("Ocorreu um erro interno inesperado"))
                .andExpect(jsonPath("$.caminho").value("/projetos"))
                .andExpect(jsonPath("$.campos").value(nullValue()))
                .andExpect(jsonPath("$.exception").doesNotExist())
                .andExpect(jsonPath("$.cause").doesNotExist())
                .andExpect(jsonPath("$.stackTrace").doesNotExist())
                .andExpect(content().string(not(containsString("Falha sensível"))));
    }

    @Test
    void deveRetornarEstruturaCompletaNoErroDeValidacao() throws Exception {
        mockMvc.perform(post("/commits/validar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp").isString())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.erro").isString())
                .andExpect(jsonPath("$.mensagem").isString())
                .andExpect(jsonPath("$.caminho").value("/commits/validar"))
                .andExpect(jsonPath("$.campos.mensagem").value(
                        "A mensagem de commit é obrigatória"
                ));
    }
}
