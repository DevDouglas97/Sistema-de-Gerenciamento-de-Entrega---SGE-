package DAO;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import Model.Usuario;
import Util.SenhaUtil;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class UsuarioDAOTest {

    private UsuarioDAO dao;

    @BeforeClass
    public static void setUpClass() {
        ConexaoTesteUtil.configurar();
    }

    @Before
    public void setUp() {
        ConexaoTesteUtil.recriarSchema();
        dao = new UsuarioDAO();
    }

    @Test(expected = IllegalArgumentException.class)
    public void rejeitaNovoUsuarioSemSenha() {
        dao.salvar(novoUsuario("Sem Senha", "semsenha", "", "Motorista", true));
    }

    @Test
    public void salvaSenhaComoHash() {
        dao.salvar(novoUsuario("João", "joao", "senha123", "Motorista", true));

        String armazenada = ConexaoTesteUtil.texto(
                "SELECT senha FROM usuarios WHERE login = ?", "joao");

        assertNotNull(armazenada);
        assertNotEquals("senha123", armazenada);
        assertTrue(SenhaUtil.estaHasheada(armazenada));
        assertTrue(SenhaUtil.verificar("senha123", armazenada));
    }

    @Test
    public void autenticaComSenhaCorreta() {
        dao.salvar(novoUsuario("Maria", "maria", "segredo", "Gerente", true));

        Usuario autenticado = dao.autenticar("maria", "segredo");

        assertNotNull(autenticado);
        assertEquals("maria", autenticado.getLogin());
        assertEquals("Gerente", autenticado.getPerfil());
    }

    @Test
    public void rejeitaSenhaIncorreta() {
        dao.salvar(novoUsuario("Pedro", "pedro", "certa", "Expedidor", true));

        assertNull(dao.autenticar("pedro", "errada"));
        assertNull(dao.autenticar("inexistente", "certa"));
    }

    @Test
    public void naoAutenticaUsuarioInativo() {
        dao.salvar(novoUsuario("Inativo", "inativo", "senha", "Motorista", false));

        assertNull(dao.autenticar("inativo", "senha"));
    }

    @Test
    public void migraSenhaLegadaEmTextoPuroNoLogin() {
        ConexaoTesteUtil.executar(
                "INSERT INTO usuarios (nome, cpf, login, senha, perfil, ativo) VALUES (?, ?, ?, ?, ?, ?)",
                "Legado", "000", "legado", "antiga", "Gerente", true);

        Usuario autenticado = dao.autenticar("legado", "antiga");
        assertNotNull(autenticado);

        String hash = ConexaoTesteUtil.texto(
                "SELECT senha FROM usuarios WHERE login = ?", "legado");
        assertTrue(SenhaUtil.estaHasheada(hash));
        assertTrue(SenhaUtil.verificar("antiga", hash));
    }

    @Test
    public void atualizarSemSenhaMantemHashAtual() {
        dao.salvar(novoUsuario("Ana", "ana", "original", "Gerente", true));
        String hashAntes = ConexaoTesteUtil.texto(
                "SELECT senha FROM usuarios WHERE login = ?", "ana");

        Usuario usuario = dao.listar().get(0);
        usuario.setNome("Ana Atualizada");
        usuario.setSenha(null);
        dao.atualizar(usuario);

        String hashDepois = ConexaoTesteUtil.texto(
                "SELECT senha FROM usuarios WHERE login = ?", "ana");

        assertEquals(hashAntes, hashDepois);
        assertEquals("Ana Atualizada", dao.autenticar("ana", "original").getNome());
    }

    @Test
    public void atualizarComNovaSenhaGeraNovoHash() {
        dao.salvar(novoUsuario("Carlos", "carlos", "antiga", "Motorista", true));
        String hashAntes = ConexaoTesteUtil.texto(
                "SELECT senha FROM usuarios WHERE login = ?", "carlos");

        Usuario usuario = dao.listar().get(0);
        usuario.setSenha("novaSenha");
        dao.atualizar(usuario);

        String hashDepois = ConexaoTesteUtil.texto(
                "SELECT senha FROM usuarios WHERE login = ?", "carlos");

        assertNotEquals(hashAntes, hashDepois);
        assertTrue(SenhaUtil.estaHasheada(hashDepois));
        assertNull(dao.autenticar("carlos", "antiga"));
        assertNotNull(dao.autenticar("carlos", "novaSenha"));
    }

    @Test
    public void listarNaoExpoeHashDeSenha() {
        dao.salvar(novoUsuario("Bia", "bia", "senha", "Expedidor", true));

        ArrayList<Usuario> usuarios = dao.listar();
        assertEquals(1, usuarios.size());
        assertNull(usuarios.get(0).getSenha());
    }

    @Test
    public void excluiUsuario() {
        dao.salvar(novoUsuario("Excluir", "excluir", "senha", "Motorista", true));
        int id = dao.listar().get(0).getId();

        dao.excluir(id);

        assertTrue(dao.listar().isEmpty());
        assertFalse(dao.autenticar("excluir", "senha") != null);
    }

    private Usuario novoUsuario(String nome, String login, String senha,
            String perfil, boolean ativo) {
        Usuario usuario = new Usuario();
        usuario.setNome(nome);
        usuario.setCpf("00000000000");
        usuario.setLogin(login);
        usuario.setSenha(senha);
        usuario.setPerfil(perfil);
        usuario.setAtivo(ativo);
        return usuario;
    }
}
