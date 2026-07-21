package com.luisjagella.gitflowcoach.service;

import com.luisjagella.gitflowcoach.dto.commit.CommitValidationResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CommitMessageValidatorTest {

    private static final String MOTIVO_FORMATO =
            "A mensagem deve seguir o formato tipo: descrição";

    private final CommitMessageValidator commitMessageValidator = new CommitMessageValidator();

    @ParameterizedTest
    @ValueSource(strings = {
            "feat",
            "fix",
            "refactor",
            "test",
            "docs",
            "chore",
            "style",
            "perf",
            "build",
            "ci",
            "revert"
    })
    void deveAceitarCadaTipoPermitido(String tipo) {
        CommitValidationResponse response = commitMessageValidator.validar(
                tipo + ": adiciona validação de commits"
        );

        assertAll(
                () -> assertTrue(response.valida()),
                () -> assertEquals(tipo, response.tipo()),
                () -> assertEquals("adiciona validação de commits", response.descricao()),
                () -> assertNull(response.motivo()),
                () -> assertNull(response.sugestao())
        );
    }

    @Test
    void deveRejeitarMensagemSemDoisPontosESugerirCorrecao() {
        CommitValidationResponse response = commitMessageValidator.validar(
                "feat adiciona cadastro"
        );

        assertAll(
                () -> assertFalse(response.valida()),
                () -> assertEquals("feat", response.tipo()),
                () -> assertEquals("adiciona cadastro", response.descricao()),
                () -> assertEquals(MOTIVO_FORMATO, response.motivo()),
                () -> assertEquals("feat: adiciona cadastro", response.sugestao())
        );
    }

    @Test
    void deveRejeitarMensagemSemEspacoAposDoisPontosESugerirCorrecao() {
        CommitValidationResponse response = commitMessageValidator.validar(
                "feat:adiciona cadastro"
        );

        assertAll(
                () -> assertFalse(response.valida()),
                () -> assertEquals("feat", response.tipo()),
                () -> assertEquals("adiciona cadastro", response.descricao()),
                () -> assertEquals(MOTIVO_FORMATO, response.motivo()),
                () -> assertEquals("feat: adiciona cadastro", response.sugestao())
        );
    }

    @Test
    void deveRejeitarTipoNaoPermitido() {
        CommitValidationResponse response = commitMessageValidator.validar(
                "outro: adiciona cadastro"
        );

        assertAll(
                () -> assertFalse(response.valida()),
                () -> assertEquals("outro", response.tipo()),
                () -> assertEquals("adiciona cadastro", response.descricao()),
                () -> assertEquals("O tipo de commit não é permitido", response.motivo()),
                () -> assertNull(response.sugestao())
        );
    }

    @Test
    void deveRejeitarTipoEmMaiusculasESugerirMinusculas() {
        CommitValidationResponse response = commitMessageValidator.validar(
                "FEAT: adiciona cadastro"
        );

        assertAll(
                () -> assertFalse(response.valida()),
                () -> assertEquals("FEAT", response.tipo()),
                () -> assertEquals("adiciona cadastro", response.descricao()),
                () -> assertEquals(
                        "O tipo deve ser escrito em letras minúsculas",
                        response.motivo()
                ),
                () -> assertEquals("feat: adiciona cadastro", response.sugestao())
        );
    }

    @Test
    void deveRejeitarDescricaoVazia() {
        CommitValidationResponse response = commitMessageValidator.validar("feat:");

        assertAll(
                () -> assertFalse(response.valida()),
                () -> assertEquals("feat", response.tipo()),
                () -> assertEquals("", response.descricao()),
                () -> assertEquals("A descrição do commit é obrigatória", response.motivo()),
                () -> assertNull(response.sugestao())
        );
    }

    @Test
    void deveRemoverEspacosExternosAntesDeValidar() {
        CommitValidationResponse response = commitMessageValidator.validar(
                "  fix: corrige cálculo da branch  "
        );

        assertAll(
                () -> assertTrue(response.valida()),
                () -> assertEquals("fix", response.tipo()),
                () -> assertEquals("corrige cálculo da branch", response.descricao())
        );
    }

    @Test
    void deveRejeitarMaisDeUmEspacoAposDoisPontos() {
        CommitValidationResponse response = commitMessageValidator.validar(
                "feat:  adiciona cadastro"
        );

        assertAll(
                () -> assertFalse(response.valida()),
                () -> assertEquals(MOTIVO_FORMATO, response.motivo()),
                () -> assertEquals("feat: adiciona cadastro", response.sugestao())
        );
    }

    @Test
    void naoDeveSugerirQuandoNaoForPossivelInferirOTipo() {
        CommitValidationResponse response = commitMessageValidator.validar(
                "Adiciona cadastro"
        );

        assertAll(
                () -> assertFalse(response.valida()),
                () -> assertNull(response.tipo()),
                () -> assertNull(response.descricao()),
                () -> assertEquals(MOTIVO_FORMATO, response.motivo()),
                () -> assertNull(response.sugestao())
        );
    }
}
