package com.luisjagella.gitflowcoach.service;

import com.luisjagella.gitflowcoach.entity.ChecklistItem;
import com.luisjagella.gitflowcoach.entity.Tarefa;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ChecklistFactoryTest {

    private static final List<String> DESCRICOES_ESPERADAS = List.of(
            "Atualizar a branch base",
            "Criar a branch da tarefa",
            "Implementar a alteração",
            "Executar os testes",
            "Realizar commits pequenos e descritivos",
            "Enviar a branch para o repositório remoto",
            "Abrir o pull request",
            "Solicitar revisão"
    );

    private final ChecklistFactory checklistFactory = new ChecklistFactory();

    @Test
    void deveCriarOsOitoItensPadrao() {
        List<ChecklistItem> itens = checklistFactory.criarPara(new Tarefa());

        assertEquals(8, itens.size());
        assertIterableEquals(
                DESCRICOES_ESPERADAS,
                itens.stream().map(ChecklistItem::getDescricao).toList()
        );
    }

    @Test
    void deveAtribuirOrdemSequencialAosItens() {
        List<ChecklistItem> itens = checklistFactory.criarPara(new Tarefa());

        assertIterableEquals(
                List.of(1, 2, 3, 4, 5, 6, 7, 8),
                itens.stream().map(ChecklistItem::getOrdem).toList()
        );
    }

    @Test
    void deveCriarTodosOsItensComoNaoConcluidos() {
        List<ChecklistItem> itens = checklistFactory.criarPara(new Tarefa());

        assertTrue(itens.stream().noneMatch(ChecklistItem::isConcluido));
    }

    @Test
    void deveAssociarTodosOsItensATarefa() {
        Tarefa tarefa = new Tarefa();

        List<ChecklistItem> itens = checklistFactory.criarPara(tarefa);

        assertEquals(itens, tarefa.getChecklist());
        assertTrue(itens.stream().allMatch(item -> item.getTarefa() == tarefa));
        itens.forEach(item -> assertSame(tarefa, item.getTarefa()));
    }
}
