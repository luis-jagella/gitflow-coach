package com.luisjagella.gitflowcoach.service;

import com.luisjagella.gitflowcoach.dto.git.GitCommandResponse;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GitCommandGeneratorTest {

    private static final String BRANCH_BASE = "versao/7.5";
    private static final String BRANCH_SUGERIDA = "tarefa/216026-ajuste-calculo-apuracao";

    private final GitCommandGenerator gitCommandGenerator = new GitCommandGenerator();

    @Test
    void deveGerarQuatroComandosNaOrdemCorreta() {
        List<GitCommandResponse> comandos = gitCommandGenerator.gerar(
                BRANCH_BASE,
                BRANCH_SUGERIDA
        );

        assertEquals(4, comandos.size());
        assertIterableEquals(
                List.of(1, 2, 3, 4),
                comandos.stream().map(GitCommandResponse::ordem).toList()
        );
    }

    @Test
    void deveUsarBranchBaseEBranchSugeridaNosComandos() {
        List<GitCommandResponse> comandos = gitCommandGenerator.gerar(
                BRANCH_BASE,
                BRANCH_SUGERIDA
        );

        assertIterableEquals(
                List.of(
                        "git switch versao/7.5",
                        "git pull origin versao/7.5",
                        "git switch -c tarefa/216026-ajuste-calculo-apuracao",
                        "git push -u origin tarefa/216026-ajuste-calculo-apuracao"
                ),
                comandos.stream().map(GitCommandResponse::comando).toList()
        );
    }

    @Test
    void deveRetornarDescricoesCorretas() {
        List<GitCommandResponse> comandos = gitCommandGenerator.gerar(
                BRANCH_BASE,
                BRANCH_SUGERIDA
        );

        assertIterableEquals(
                List.of(
                        "Acessar a branch base",
                        "Atualizar a branch base",
                        "Criar a branch da tarefa",
                        "Publicar a branch da tarefa"
                ),
                comandos.stream().map(GitCommandResponse::descricao).toList()
        );
    }

    @Test
    void deveRejeitarBranchBaseNula() {
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> gitCommandGenerator.gerar(null, BRANCH_SUGERIDA)
        );

        assertEquals("A branch base não pode ser nula ou vazia", excecao.getMessage());
    }

    @Test
    void deveRejeitarBranchBaseVazia() {
        assertThrows(
                IllegalArgumentException.class,
                () -> gitCommandGenerator.gerar("  ", BRANCH_SUGERIDA)
        );
    }

    @Test
    void deveRejeitarBranchSugeridaNula() {
        IllegalArgumentException excecao = assertThrows(
                IllegalArgumentException.class,
                () -> gitCommandGenerator.gerar(BRANCH_BASE, null)
        );

        assertEquals("A branch sugerida não pode ser nula ou vazia", excecao.getMessage());
    }

    @Test
    void deveRejeitarBranchSugeridaVazia() {
        assertThrows(
                IllegalArgumentException.class,
                () -> gitCommandGenerator.gerar(BRANCH_BASE, "  ")
        );
    }

    @Test
    void deveRetornarListaImutavel() {
        List<GitCommandResponse> comandos = gitCommandGenerator.gerar(
                BRANCH_BASE,
                BRANCH_SUGERIDA
        );

        assertThrows(
                UnsupportedOperationException.class,
                () -> comandos.add(new GitCommandResponse(5, "Outro", "git status"))
        );
    }
}
