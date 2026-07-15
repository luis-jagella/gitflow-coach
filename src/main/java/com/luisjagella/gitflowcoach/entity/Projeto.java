package com.luisjagella.gitflowcoach.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "projetos")
public class Projeto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String repositorio;

    private String branchBase;

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getRepositorio() {
        return repositorio;
    }

    public String getBranchBase() {
        return branchBase;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setRepositorio(String repositorio) {
        this.repositorio = repositorio;
    }

    public void setBranchBase(String branchBase) {
        this.branchBase = branchBase;
    }
}