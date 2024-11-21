package org.gvs.axis.util;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

/**
 *
 * @author vitor
 */
@Component
@NoArgsConstructor
public class NomeValidator {

    private static final int MINIMUM_WORDS = 2;

    public static void validar(String nome) {
        if (nome == null) {
            throw new IllegalArgumentException("O nome não pode ser nulo");
        }

        String trimmedName = nome.trim();

        if (trimmedName.isEmpty()) {
            throw new IllegalArgumentException("O nome não pode estar vazio");
        }

        String normalizedName = trimmedName.replaceAll("\\s+", " ");

        String[] palavras = normalizedName.split(" ");

        if (palavras.length < MINIMUM_WORDS) {
            throw new IllegalArgumentException("O campo nome completo deve conter nome e sobrenome");
        }
    }
}
