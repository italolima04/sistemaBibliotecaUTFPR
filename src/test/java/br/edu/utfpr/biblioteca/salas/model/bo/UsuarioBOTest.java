package br.edu.utfpr.biblioteca.salas.model.bo;

import br.edu.utfpr.biblioteca.salas.tools.CalendarioHelper;
import java.util.Date;
import br.edu.utfpr.biblioteca.salas.model.dao.UsuarioDAO;
import br.edu.utfpr.biblioteca.salas.model.entity.UsuarioPO;
import static org.junit.Assert.*;


public class UsuarioBOTest {

    UsuarioDAO daoUsuario = new UsuarioDAO();
    CalendarioHelper ch = new CalendarioHelper();
    public UsuarioBOTest() {

    }

//    @Test
    public void test_CanDoCheckout(){
        UsuarioPO u = daoUsuario.obter("1602063");
        assertTrue(UsuarioBO.canDoCheckout(u));
    }

//    @Test
    public void test_Data(){
        Date data = this.ch.getHoraCheia(new Date());
        System.out.println("Data" +  this.ch.getData(data));
    }

}
