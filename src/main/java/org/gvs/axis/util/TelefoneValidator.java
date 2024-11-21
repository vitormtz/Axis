package org.gvs.axis.util;

import java.util.regex.Pattern;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

/**
 *
 * @author vitor
 */
@Component
@NoArgsConstructor
public class TelefoneValidator {

    private static final String TELEPHONE_PATTERN
            = "^(?:\\(?([1-9][0-9])\\)?\\s*)?9[0-9]{8}$";

    private static final Pattern pattern = Pattern.compile(TELEPHONE_PATTERN);

    public static void validar(String telefone) {

        String telefoneLimpo = limpar(telefone);

        if (telefoneLimpo == null) {
            throw new IllegalArgumentException("O número de celular não pode ser nulo");
        }

        if (telefoneLimpo.length() < 10 || telefoneLimpo.length() > 11) {
            throw new IllegalArgumentException("O número deve ter 10 ou 11 dígitos (incluindo DDD)");
        }

        if (telefoneLimpo.length() == 10) {
            telefoneLimpo = telefoneLimpo.substring(0, 2) + "9" + telefoneLimpo.substring(2);
        }

        if (!pattern.matcher(telefoneLimpo).matches()) {
            throw new IllegalArgumentException("Formato de celular inválido");
        }

        String ddd = telefoneLimpo.substring(0, 2);
        if (!isValidDDD(ddd)) {
            throw new IllegalArgumentException("DDD inválido");
        }
    }

    private static boolean isValidDDD(String ddd) {
        int dddNum = Integer.parseInt(ddd);
        
        return dddNum >= 11 && dddNum <= 99
                && dddNum != 20 && dddNum != 23 && dddNum != 25 && dddNum != 26
                && dddNum != 29 && dddNum != 30 && dddNum != 36 && dddNum != 39
                && dddNum != 40 && dddNum != 50 && dddNum != 52 && dddNum != 56
                && dddNum != 57 && dddNum != 58 && dddNum != 59 && dddNum != 60
                && dddNum != 70 && dddNum != 72 && dddNum != 76 && dddNum != 78
                && dddNum != 80 && dddNum != 90;
    }

    public static String limpar(String telefone) {
        return telefone.replaceAll("[^0-9]", "");
    }
}
