package com.luisjagella.gitflowcoach.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tarefas")
public class Tarefa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String codigo;

    private String titulo;

    private String descricao;

    private String branchSugerida;

    @ManyToOne
    @JoinColumn(name = "projeto_id")
    private Projeto projeto;

    @OneToMany(mappedBy = "tarefa", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("ordem ASC")
    private List<ChecklistItem> checklist = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getBranchSugerida() {
        return branchSugerida;
    }

    public Projeto getProjeto() {
        return projeto;
    }

    public List<ChecklistItem> getChecklist() {
        return checklist;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setBranchSugerida(String branchSugerida) {
        this.branchSugerida = branchSugerida;
    }

    public void setProjeto(Projeto projeto) {
        this.projeto = projeto;
    }

    public void adicionarChecklistItem(ChecklistItem checklistItem) {
        checklist.add(checklistItem);
        checklistItem.setTarefa(this);
    }
}
