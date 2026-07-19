package com.luisjagella.gitflowcoach.service;

import com.luisjagella.gitflowcoach.dto.git.GitCommandResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GitCommandGenerator {

    public List<GitCommandResponse> gerar(String branchBase, String branchSugerida) {
        validarBranch(branchBase, "A branch base não pode ser nula ou vazia");
        validarBranch(branchSugerida, "A branch sugerida não pode ser nula ou vazia");

        String branchBaseNormalizada = branchBase.strip();
        String branchSugeridaNormalizada = branchSugerida.strip();

        return List.of(
                new GitCommandResponse(
                        1,
                        "Acessar a branch base",
                        "git switch " + branchBaseNormalizada
                ),
                new GitCommandResponse(
                        2,
                        "Atualizar a branch base",
                        "git pull origin " + branchBaseNormalizada
                ),
                new GitCommandResponse(
                        3,
                        "Criar a branch da tarefa",
                        "git switch -c " + branchSugeridaNormalizada
                ),
                new GitCommandResponse(
                        4,
                        "Publicar a branch da tarefa",
                        "git push -u origin " + branchSugeridaNormalizada
                )
        );
    }

    private void validarBranch(String branch, String mensagem) {
        if (branch == null || branch.isBlank()) {
            throw new IllegalArgumentException(mensagem);
        }
    }
}
