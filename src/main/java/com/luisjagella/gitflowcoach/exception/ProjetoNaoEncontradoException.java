package com.luisjagella.gitflowcoach.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ProjetoNaoEncontradoException extends RuntimeException {

    public ProjetoNaoEncontradoException(Long id) {
        super("Projeto não encontrado com o id: " + id);
    }
}
