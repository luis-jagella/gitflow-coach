package com.luisjagella.gitflowcoach.service;

import com.luisjagella.gitflowcoach.dto.commit.CommitValidationResponse;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Set;

@Component
public class CommitMessageValidator {

    private static final Set<String> TIPOS_PERMITIDOS = Set.of(
            "feat",
            "fix",
            "refactor",
            "test",
            "docs",
            "chore",
            "style",
            "perf",
            "build",
            "ci",
            "revert"
    );

    private static final String MOTIVO_FORMATO =
            "A mensagem deve seguir o formato tipo: descrição";
    private static final String MOTIVO_TIPO_NAO_PERMITIDO =
            "O tipo de commit não é permitido";
    private static final String MOTIVO_TIPO_MAIUSCULO =
            "O tipo deve ser escrito em letras minúsculas";
    private static final String MOTIVO_DESCRICAO_OBRIGATORIA =
            "A descrição do commit é obrigatória";

    public CommitValidationResponse validar(String mensagem) {
        if (mensagem == null || mensagem.isBlank()) {
            return invalida(null, null, MOTIVO_FORMATO, null);
        }

        String mensagemNormalizada = mensagem.strip();
        int indiceDoisPontos = mensagemNormalizada.indexOf(':');

        if (indiceDoisPontos >= 0) {
            return validarComDoisPontos(mensagemNormalizada, indiceDoisPontos);
        }

        return validarSemDoisPontos(mensagemNormalizada);
    }

    private CommitValidationResponse validarComDoisPontos(
            String mensagem,
            int indiceDoisPontos
    ) {
        String tipo = mensagem.substring(0, indiceDoisPontos).strip();
        String descricao = mensagem.substring(indiceDoisPontos + 1).strip();
        String tipoMinusculo = tipo.toLowerCase(Locale.ROOT);

        if (TIPOS_PERMITIDOS.contains(tipoMinusculo) && !tipo.equals(tipoMinusculo)) {
            return invalida(
                    tipo,
                    descricao,
                    MOTIVO_TIPO_MAIUSCULO,
                    criarSugestao(tipoMinusculo, descricao)
            );
        }

        if (!TIPOS_PERMITIDOS.contains(tipo)) {
            return invalida(tipo, descricao, MOTIVO_TIPO_NAO_PERMITIDO, null);
        }

        if (descricao.isEmpty()) {
            return invalida(tipo, descricao, MOTIVO_DESCRICAO_OBRIGATORIA, null);
        }

        if (!possuiSeparadorExato(mensagem, tipo, indiceDoisPontos)) {
            return invalida(
                    tipo,
                    descricao,
                    MOTIVO_FORMATO,
                    criarSugestao(tipo, descricao)
            );
        }

        return new CommitValidationResponse(true, tipo, descricao, null, null);
    }

    private CommitValidationResponse validarSemDoisPontos(String mensagem) {
        String[] partes = mensagem.split("\\s+", 2);
        if (partes.length < 2 || partes[1].isBlank()) {
            return invalida(null, null, MOTIVO_FORMATO, null);
        }

        String tipo = partes[0];
        String descricao = partes[1].strip();
        String tipoMinusculo = tipo.toLowerCase(Locale.ROOT);

        if (!TIPOS_PERMITIDOS.contains(tipoMinusculo)) {
            return invalida(null, null, MOTIVO_FORMATO, null);
        }

        if (!tipo.equals(tipoMinusculo)) {
            return invalida(
                    tipo,
                    descricao,
                    MOTIVO_TIPO_MAIUSCULO,
                    criarSugestao(tipoMinusculo, descricao)
            );
        }

        return invalida(
                tipo,
                descricao,
                MOTIVO_FORMATO,
                criarSugestao(tipo, descricao)
        );
    }

    private boolean possuiSeparadorExato(
            String mensagem,
            String tipo,
            int indiceDoisPontos
    ) {
        int indiceDescricao = indiceDoisPontos + 2;
        return indiceDoisPontos == tipo.length()
                && indiceDescricao < mensagem.length()
                && mensagem.charAt(indiceDoisPontos + 1) == ' '
                && !Character.isWhitespace(mensagem.charAt(indiceDescricao));
    }

    private String criarSugestao(String tipo, String descricao) {
        if (descricao == null || descricao.isBlank()) {
            return null;
        }
        return tipo + ": " + descricao.strip();
    }

    private CommitValidationResponse invalida(
            String tipo,
            String descricao,
            String motivo,
            String sugestao
    ) {
        return new CommitValidationResponse(false, tipo, descricao, motivo, sugestao);
    }
}
