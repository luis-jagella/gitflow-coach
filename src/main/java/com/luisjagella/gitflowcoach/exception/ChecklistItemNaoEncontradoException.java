package com.luisjagella.gitflowcoach.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ChecklistItemNaoEncontradoException extends RuntimeException {

    public ChecklistItemNaoEncontradoException(Long id) {
        super("Item de checklist não encontrado com o id: " + id);
    }
}
