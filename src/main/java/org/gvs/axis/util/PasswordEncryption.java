package org.gvs.axis.util;

import org.springframework.security.crypto.bcrypt.BCrypt;


/**
 *
 * @author vitor
 */
public class PasswordEncryption {

    private PasswordEncryption() {
    }

    public static String criptografarSenha(String senha) {
        return BCrypt.hashpw(senha, BCrypt.gensalt());
    }

    public static boolean verificarSenha(String senha, String senhaCriptografada) {
        return BCrypt.checkpw(senha, senhaCriptografada);
    }
}
