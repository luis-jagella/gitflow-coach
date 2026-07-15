package com.luisjagella.gitflowcoach.repository;

import com.luisjagella.gitflowcoach.entity.Projeto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjetoRepository extends JpaRepository<Projeto, Long> {
}