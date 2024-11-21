package org.gvs.axis.util;

import org.gvs.axis.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author vitor
 */
@Component
public class CPFValidator {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public void validar(String cpf) {

        String cpfLimpo = limpar(cpf);

        if (cpfLimpo.length() != 11) {
            throw new IllegalArgumentException("CPF deve conter exatamente 11 dígitos");
        }
        if (!cpfLimpo.matches("\\d{11}")) {
            throw new IllegalArgumentException("CPF deve conter apenas números");
        }

        if (usuarioRepository.findByCpf(cpfLimpo).isPresent()) {
            throw new IllegalArgumentException("CPF já cadastrado");
        }
    }

    public static String limpar(String cpf) {
        return cpf.replaceAll("[^0-9]", "");
    }
}
