package com.luisjagella.gitflowcoach.controller;

import com.luisjagella.gitflowcoach.dto.commit.CommitValidationRequest;
import com.luisjagella.gitflowcoach.dto.commit.CommitValidationResponse;
import com.luisjagella.gitflowcoach.service.CommitMessageValidator;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/commits")
public class CommitValidationController {

    private final CommitMessageValidator commitMessageValidator;

    public CommitValidationController(CommitMessageValidator commitMessageValidator) {
        this.commitMessageValidator = commitMessageValidator;
    }

    @PostMapping("/validar")
    public ResponseEntity<CommitValidationResponse> validar(
            @Valid @RequestBody CommitValidationRequest request
    ) {
        return ResponseEntity.ok(commitMessageValidator.validar(request.mensagem()));
    }
}
