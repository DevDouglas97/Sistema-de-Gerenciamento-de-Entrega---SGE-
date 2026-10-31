/*
 * Hash e verificação de senhas.
 *
 * Algoritmo: PBKDF2WithHmacSHA256 (disponível no JDK, sem dependências externas).
 * Formato armazenado: pbkdf2$sha256$<iteracoes>$<salBase64>$<hashBase64>
 *
 * Também aceita senhas antigas em texto puro para permitir migração transparente
 * no primeiro login (ver precisaMigrar).
 */
package Util;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public final class SenhaUtil {

    private static final String PREFIXO = "pbkdf2$sha256$";
    private static final String ALGORITMO = "PBKDF2WithHmacSHA256";
    private static final int ITERACOES_PADRAO = 120_000;
    private static final int TAMANHO_SAL_BYTES = 16;
    private static final int TAMANHO_CHAVE_BITS = 256;

    private static final SecureRandom RANDOM = new SecureRandom();

    private SenhaUtil() {
    }

    /**
     * Gera um hash novo (com sal aleatório) para a senha informada.
     */
    public static String gerarHash(String senha) {
        return gerarHash(senha, ITERACOES_PADRAO);
    }

    static String gerarHash(String senha, int iteracoes) {
        if (senha == null) {
            throw new IllegalArgumentException("A senha não pode ser nula.");
        }
        byte[] sal = new byte[TAMANHO_SAL_BYTES];
        RANDOM.nextBytes(sal);

        byte[] hash = derivar(senha.toCharArray(), sal, iteracoes, TAMANHO_CHAVE_BITS);

        return PREFIXO
                + iteracoes + "$"
                + Base64.getEncoder().withoutPadding().encodeToString(sal) + "$"
                + Base64.getEncoder().withoutPadding().encodeToString(hash);
    }

    /**
     * Verifica se a senha informada corresponde ao valor armazenado.
     * Valores legados (texto puro) também são aceitos.
     */
    public static boolean verificar(String senha, String armazenado) {
        if (senha == null || armazenado == null) {
            return false;
        }

        if (!armazenado.startsWith(PREFIXO)) {
            return compararSeguro(senha, armazenado);
        }

        try {
            String[] partes = armazenado.split("\\$");
            // partes = [pbkdf2, sha256, iteracoes, sal, hash]
            if (partes.length != 5) {
                return false;
            }
            int iteracoes = Integer.parseInt(partes[2]);
            byte[] sal = Base64.getDecoder().decode(partes[3]);
            byte[] esperado = Base64.getDecoder().decode(partes[4]);

            byte[] calculado = derivar(senha.toCharArray(), sal, iteracoes, esperado.length * 8);
            return MessageDigest.isEqual(esperado, calculado);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Indica se o valor armazenado ainda é texto puro e deveria ser re-hasheado.
     */
    public static boolean precisaMigrar(String armazenado) {
        return armazenado == null || !armazenado.startsWith(PREFIXO);
    }

    /**
     * Indica se o valor já está no formato de hash gerado por esta classe.
     */
    public static boolean estaHasheada(String valor) {
        return valor != null && valor.startsWith(PREFIXO);
    }

    private static byte[] derivar(char[] senha, byte[] sal, int iteracoes, int tamanhoBits) {
        PBEKeySpec especificacao = new PBEKeySpec(senha, sal, iteracoes, tamanhoBits);
        try {
            SecretKeyFactory fabrica = SecretKeyFactory.getInstance(ALGORITMO);
            return fabrica.generateSecret(especificacao).getEncoded();
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("Falha ao derivar o hash da senha.", e);
        } finally {
            especificacao.clearPassword();
        }
    }

    private static boolean compararSeguro(String a, String b) {
        return MessageDigest.isEqual(
                a.getBytes(StandardCharsets.UTF_8),
                b.getBytes(StandardCharsets.UTF_8));
    }
}
