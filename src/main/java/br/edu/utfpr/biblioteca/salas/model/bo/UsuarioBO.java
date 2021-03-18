package br.edu.utfpr.biblioteca.salas.model.bo;

import br.edu.utfpr.biblioteca.salas.model.dao.UsuarioDAO;
import br.edu.utfpr.biblioteca.salas.model.entity.UsuarioPO;
import br.edu.utfpr.biblioteca.salas.model.entity.ReservaPO;
import br.edu.utfpr.biblioteca.salas.tools.CalendarioHelper;
import java.util.Date;

public class UsuarioBO {
	private static CalendarioHelper calendario;

    static UsuarioDAO usuarioDAO = new UsuarioDAO();

    /**
     * Este método chama o método isAutentico do dao
     *
     * @param ra
     * @param senha
     * @return boolean
     */
    public static UsuarioPO isAutentico(String ra, String senha) {
        return usuarioDAO.isAutentico(ra, senha);
    }

    /**
     * Sobrecarga de método para permitir abstrações. Invoca isAutentico(ra,
     * senha)
     *
     * @param usuario
     * @return boolean
     */
    public static UsuarioPO isAutentico(UsuarioPO usuario) {
        return isAutentico(usuario.getRa(), usuario.getSenha());
    }

    /**
     * Contata o dao para obter um usuario dado um ra.
     *
     * @param ra
     * @return
     */
    public static UsuarioPO obterUsuario(String ra) {
        return usuarioDAO.obter(ra);
    }

    /**
     * verifica se o usuario está cadastrado
     *
     * @param usuario
     * @return boolean
     */
    public static boolean alreadyCadastrado(UsuarioPO usuario) {
        return usuarioDAO.obter(usuario.getRa()) != null;
    }

    /**
     * verifica se o usuario já está cadastrado e se a senha é vazia, caso ele
     * nao seja cadastrado e sua senha exista, o usuario é inserido.
     *
     * @param usuario
     */
    public static void cadastrarUsuario(UsuarioPO usuario) {
        if (alreadyCadastrado(usuario)) {
            return;
        }
        if (usuario.getSenha().isEmpty()) {
            return;
        }
        usuarioDAO.insert(usuario);
    }

    public static boolean canDoChekin(UsuarioPO usuario) {
        Date fifteenBefore;
        Date fifteenAfter;
        Date horaAtual = new Date();
        int minutos = getCalendario().getMinutes(horaAtual);
        if (minutos <= 15) {
            horaAtual = CalendarioHelper.getHoraCheia(horaAtual);
        } else if (minutos >= 45) {
            horaAtual = CalendarioHelper.getHoraCheia(CalendarioHelper.addHora(horaAtual));
        } else {
            return false;
        }
        fifteenBefore = calendario.lessHora(horaAtual);
        calendario.setMinute(fifteenBefore, 45);
        fifteenAfter = (Date) horaAtual.clone();
        calendario.setMinute(fifteenAfter, 15);
        return usuarioDAO.getReservaInTime(usuario, horaAtual) != null;
    }

    public static ReservaPO getMyReservaNow(UsuarioPO usuario) {
    	CalendarioHelper calendario = new CalendarioHelper();
        Date horaAtual = new Date();
        int minutos = calendario.getMinutes(horaAtual);
        if (minutos <= 15) {
            horaAtual = calendario.getHoraCheia(horaAtual);
        } else if (minutos >= 45) {
            horaAtual = calendario.getHoraCheia(calendario.addHora(horaAtual));
        }
        return usuarioDAO.getReservaInTime(usuario, horaAtual);
    }

    public static ReservaPO getReservaEmCursoHoje(UsuarioPO usuario){
        return usuarioDAO.getReservaEmCurso(usuario, new Date());
    }

    public static boolean canDoCheckout(UsuarioPO usuario) {
    	CalendarioHelper calendario = new CalendarioHelper();
        Date data = calendario.getHoraCheia(new Date());

        return usuarioDAO.getReservaEmCurso(usuario, data) != null;
    }

	public static CalendarioHelper getCalendario() {
		return calendario;
	}

	public void setCalendario(CalendarioHelper calendario) {
		this.calendario = calendario;
	}

}
