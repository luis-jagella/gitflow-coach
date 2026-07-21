package com.luisjagella.gitflowcoach.controller;

import com.luisjagella.gitflowcoach.dto.commit.CommitValidationResponse;
import com.luisjagella.gitflowcoach.service.CommitMessageValidator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommitValidationController.class)
class CommitValidationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CommitMessageValidator commitMessageValidator;

    @Test
    void deveRetornarOkParaMensagemValida() throws Exception {
        String mensagem = "feat: adiciona validação de commits";
        when(commitMessageValidator.validar(mensagem)).thenReturn(
                new CommitValidationResponse(
                        true,
                        "feat",
                        "adiciona validação de commits",
                        null,
                        null
                )
        );

        mockMvc.perform(post("/commits/validar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "mensagem": "feat: adiciona validação de commits"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valida").value(true))
                .andExpect(jsonPath("$.tipo").value("feat"))
                .andExpect(jsonPath("$.descricao").value("adiciona validação de commits"));
    }

    @Test
    void deveRetornarOkComValidaFalsoParaMensagemInvalida() throws Exception {
        String mensagem = "feat adiciona validação de commits";
        when(commitMessageValidator.validar(mensagem)).thenReturn(
                new CommitValidationResponse(
                        false,
                        "feat",
                        "adiciona validação de commits",
                        "A mensagem deve seguir o formato tipo: descrição",
                        "feat: adiciona validação de commits"
                )
        );

        mockMvc.perform(post("/commits/validar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "mensagem": "feat adiciona validação de commits"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valida").value(false))
                .andExpect(jsonPath("$.motivo").value(
                        "A mensagem deve seguir o formato tipo: descrição"
                ))
                .andExpect(jsonPath("$.sugestao").value(
                        "feat: adiciona validação de commits"
                ));
    }

    @Test
    void deveRetornarBadRequestParaMensagemVazia() throws Exception {
        mockMvc.perform(post("/commits/validar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "mensagem": "   "
                                }
                                """))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(commitMessageValidator);
    }

    @Test
    void deveRetornarBadRequestParaCampoAusente() throws Exception {
        mockMvc.perform(post("/commits/validar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(commitMessageValidator);
    }
}
