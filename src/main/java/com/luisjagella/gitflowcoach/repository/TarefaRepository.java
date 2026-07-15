package com.luisjagella.gitflowcoach.repository;

import com.luisjagella.gitflowcoach.entity.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TarefaRepository extends JpaRepository<Tarefa, Long> {
}