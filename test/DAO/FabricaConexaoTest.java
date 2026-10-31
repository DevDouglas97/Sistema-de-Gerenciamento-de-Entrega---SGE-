package DAO;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import Util.FabricaConexao;
import java.sql.Connection;
import org.junit.BeforeClass;
import org.junit.Test;

public class FabricaConexaoTest {

    @BeforeClass
    public static void setUp() {
        ConexaoTesteUtil.configurar();
    }

    @Test
    public void abreConexaoUsandoConfiguracaoExterna() throws Exception {
        try (Connection conn = FabricaConexao.getConexao()) {
            assertNotNull(conn);
            assertTrue(conn.isValid(2));
        }
    }

    @Test
    public void abreConexoesIndependentes() throws Exception {
        try (Connection primeira = FabricaConexao.getConexao();
             Connection segunda = FabricaConexao.getConexao()) {

            assertNotNull(primeira);
            assertNotNull(segunda);
            assertTrue(primeira != segunda);
        }
    }
}
