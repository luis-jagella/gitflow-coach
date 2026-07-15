package com.luisjagella.gitflowcoach.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "checklist_items")
public class ChecklistItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descricao;

    private boolean concluido;

    @ManyToOne
    @JoinColumn(name = "tarefa_id")
    private Tarefa tarefa;

    public Long getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public boolean isConcluido() {
        return concluido;
    }

    public Tarefa getTarefa() {
        return tarefa;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setConcluido(boolean concluido) {
        this.concluido = concluido;
    }

    public void setTarefa(Tarefa tarefa) {
        this.tarefa = tarefa;
    }
}