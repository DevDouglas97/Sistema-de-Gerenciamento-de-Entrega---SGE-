/*
 * Regras de acesso por perfil de usuário.
 *
 * A lógica fica isolada da interface para poder ser testada sem abrir janelas.
 */
package Util;

import java.util.Locale;

public final class PermissaoUtil {

    public enum Modulo {
        DASHBOARD,
        USUARIOS,
        CLIENTES,
        PRODUTOS,
        ENDERECOS,
        EXPEDICAO,
        ROTAS
    }

    private PermissaoUtil() {
    }

    /**
     * Retorna true quando o perfil pode acessar o módulo informado.
     */
    public static boolean podeAcessar(String perfil, Modulo modulo) {
        if (modulo == null) {
            return false;
        }
        if (perfil == null) {
            return modulo == Modulo.DASHBOARD;
        }

        String normalizado = perfil.trim().toLowerCase(Locale.ROOT);

        switch (normalizado) {
            case "gerente":
            case "administrador":
            case "admin":
                return true;

            case "auxiliar logistico":
            case "auxiliar logístico":
            case "expedidor":
                // Não podem administrar usuários; o restante é operacional.
                return modulo != Modulo.USUARIOS;

            case "motorista":
                // Motorista enxerga apenas o que é da operação dele.
                return modulo == Modulo.DASHBOARD
                        || modulo == Modulo.ROTAS
                        || modulo == Modulo.EXPEDICAO;

            default:
                return modulo == Modulo.DASHBOARD;
        }
    }

    public static boolean podeGerenciarUsuarios(String perfil) {
        return podeAcessar(perfil, Modulo.USUARIOS);
    }
}
