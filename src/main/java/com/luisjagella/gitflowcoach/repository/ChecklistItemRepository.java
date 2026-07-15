package com.luisjagella.gitflowcoach.repository;

import com.luisjagella.gitflowcoach.entity.ChecklistItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChecklistItemRepository extends JpaRepository<ChecklistItem, Long> {
}