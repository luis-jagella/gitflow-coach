package com.luisjagella.gitflowcoach.service;

import org.springframework.stereotype.Component;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

@Component
public class BranchNameGenerator {

    private static final Pattern MARCAS_DIACRITICAS = Pattern.compile("\\p{M}+");
    private static final Pattern CARACTERES_INVALIDOS = Pattern.compile("[^\\p{L}\\p{N}]+");
    private static final Pattern HIFENS_NAS_EXTREMIDADES = Pattern.compile("^-+|-+$");

    public String gerar(String codigo, String titulo) {
        String tituloSemAcentos = MARCAS_DIACRITICAS.matcher(
                Normalizer.normalize(titulo, Normalizer.Form.NFD)
        ).replaceAll("");

        String tituloNormalizado = CARACTERES_INVALIDOS.matcher(
                tituloSemAcentos.toLowerCase(Locale.ROOT)
        ).replaceAll("-");
        tituloNormalizado = HIFENS_NAS_EXTREMIDADES.matcher(tituloNormalizado).replaceAll("");

        return "tarefa/" + codigo.strip() + "-" + tituloNormalizado;
    }
}
