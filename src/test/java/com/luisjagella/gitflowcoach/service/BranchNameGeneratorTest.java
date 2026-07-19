package com.luisjagella.gitflowcoach.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BranchNameGeneratorTest {

    private final BranchNameGenerator branchNameGenerator = new BranchNameGenerator();

    @Test
    void deveGerarBranchParaTituloSimples() {
        String branch = branchNameGenerator.gerar("123", "Corrigir erro");

        assertEquals("tarefa/123-corrigir-erro", branch);
    }

    @Test
    void deveRemoverAcentosDoTitulo() {
        String branch = branchNameGenerator.gerar("123", "Cálculo de apuração");

        assertEquals("tarefa/123-calculo-de-apuracao", branch);
    }

    @Test
    void deveSubstituirMultiplosEspacosPorUmHifen() {
        String branch = branchNameGenerator.gerar("123", "Corrigir   erro   no Git");

        assertEquals("tarefa/123-corrigir-erro-no-git", branch);
    }

    @Test
    void deveRemoverCaracteresEspeciais() {
        String branch = branchNameGenerator.gerar("123", "Corrigir erro !!! no Git");

        assertEquals("tarefa/123-corrigir-erro-no-git", branch);
    }

    @Test
    void deveRemoverHifensDuplicados() {
        String branch = branchNameGenerator.gerar("123", "Corrigir---erro");

        assertEquals("tarefa/123-corrigir-erro", branch);
    }

    @Test
    void deveRemoverHifensGeradosNasExtremidades() {
        String branch = branchNameGenerator.gerar("123", "  Corrigir erro  ");

        assertEquals("tarefa/123-corrigir-erro", branch);
    }

    @Test
    void devePreservarCodigoNumerico() {
        String branch = branchNameGenerator.gerar("216026", "Ajuste");

        assertEquals("tarefa/216026-ajuste", branch);
    }

    @Test
    void deveConverterLetrasMaiusculasParaMinusculas() {
        String branch = branchNameGenerator.gerar("123", "CORRIGIR ERRO");

        assertEquals("tarefa/123-corrigir-erro", branch);
    }

    @Test
    void deveNormalizarCombinacaoGitSvn() {
        String branch = branchNameGenerator.gerar("123", "Integração Git/SVN");

        assertEquals("tarefa/123-integracao-git-svn", branch);
    }

    @Test
    void deveGerarResultadoCompletoComPrefixoTarefa() {
        String branch = branchNameGenerator.gerar(
                "216026",
                "Ajuste no cálculo de apuração de custo"
        );

        assertEquals("tarefa/216026-ajuste-no-calculo-de-apuracao-de-custo", branch);
    }
}
