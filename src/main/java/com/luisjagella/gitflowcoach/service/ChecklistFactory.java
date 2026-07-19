package com.luisjagella.gitflowcoach.service;

import com.luisjagella.gitflowcoach.entity.ChecklistItem;
import com.luisjagella.gitflowcoach.entity.Tarefa;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ChecklistFactory {

    private static final List<String> DESCRICOES_PADRAO = List.of(
            "Atualizar a branch base",
            "Criar a branch da tarefa",
            "Implementar a alteração",
            "Executar os testes",
            "Realizar commits pequenos e descritivos",
            "Enviar a branch para o repositório remoto",
            "Abrir o pull request",
            "Solicitar revisão"
    );

    public List<ChecklistItem> criarPara(Tarefa tarefa) {
        List<ChecklistItem> itens = new ArrayList<>(DESCRICOES_PADRAO.size());

        for (int indice = 0; indice < DESCRICOES_PADRAO.size(); indice++) {
            ChecklistItem item = new ChecklistItem();
            item.setDescricao(DESCRICOES_PADRAO.get(indice));
            item.setConcluido(false);
            item.setOrdem(indice + 1);
            tarefa.adicionarChecklistItem(item);
            itens.add(item);
        }

        return itens;
    }
}
