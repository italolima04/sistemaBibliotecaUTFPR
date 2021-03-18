package br.edu.utfpr.biblioteca.salas.model.bo;

import br.edu.utfpr.biblioteca.salas.model.Dia;
import br.edu.utfpr.biblioteca.salas.model.Hora;
import br.edu.utfpr.biblioteca.salas.model.dao.ReservaDAO;
import br.edu.utfpr.biblioteca.salas.model.dao.SalaDAO;
import br.edu.utfpr.biblioteca.salas.model.entity.UsuarioPO;
import br.edu.utfpr.biblioteca.salas.model.entity.ReservaPO;
import br.edu.utfpr.biblioteca.salas.model.entity.SalaPO;
import br.edu.utfpr.biblioteca.salas.model.entity.StatusPO;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import br.edu.utfpr.biblioteca.salas.tools.CalendarioHelper;
import br.edu.utfpr.biblioteca.salas.tools.HashMapHelper;
import java.util.Collections;

public class ReservaBO {
	private static CalendarioHelper calendario;
    public static ReservaDAO reservaDAO = new ReservaDAO();

    /**
     * Método que invoca o DAO para obter as reservas de um determinado dia-hora
     * e sala.
     *
     * @param data Date
     * @param idSala int
     * @return lista de reservas
     */
    public static List<ReservaPO> getReservaAtiva(Date data, int idSala) {
        return reservaDAO.listByDateTimeAndSala(data, idSala);
    }

    /**
     * Sobrecarga de método.
     *
     * @param data Date
     * @param sala
     * @return lista de reservas
     */
    public static List<ReservaPO> getReservaAtiva(Date data, SalaPO sala) {
        return getReservaAtiva(data, sala.getId());
    }

    /**
     * Método descreve um dia. Obtém um relatório de todas as reservas feitas e
     * não feitas em todas as salas durante um dia.
     *
     * @see descreverDia(Date date)
     * @param date uma data válida
     * @return um hashmap que associa data ao conjunto de salas, cada qual com
     * sua reserva
     */


    public static List<Date> retornaHorarios(Date date) {
        List<Date> horarios;

        horarios = CalendarioHelper.getHorarios(date);
        return horarios;
    }

    public static  List<SalaPO> retornaSalas() {
        List<SalaPO> salas;
        SalaDAO salaDAO;
        salaDAO = new SalaDAO();
        salas = salaDAO.list();
        return salas;
    }


    public static HashMap<Date, HashMap<SalaPO, ReservaPO>> descreverDiaHash(Date date) {
        HashMap<SalaPO, ReservaPO> salaTemReservas;
        HashMap<Date, HashMap<SalaPO, ReservaPO>> dataTemReservas;
        List<ReservaPO> reservas;

        salaTemReservas = new HashMap();
        dataTemReservas = new HashMap();

        for (SalaPO sala : retornaSalas()) {
            salaTemReservas.put(sala, null);
        }

        for (Date horario : retornaHorarios(date)) {
            reservas = reservaDAO.listByDateTime(horario);
            dataTemReservas.put(horario, HashMapHelper.clone(salaTemReservas));

            for (ReservaPO reserva : reservas) {
                dataTemReservas.get(reserva.getDataInicial()).put(reserva.getSala(), reserva);
            }
        }
        return dataTemReservas;
    }

    /**
     * Método que descreve um dia. Obtém um relatório de todas as reservas
     * feitas e não feitas em todas as salas durante um dia.
     *
     * @see descreverDiaHash(Date date)
     * @param date
     * @return Um objeto dia composto de uma lista de horário. Cada horário
     * contém uma lista de Reservas cujo tamanho máximo limita-se ao número de
     * salas.
     */


    public static void descreverHorario(Dia dia, Date date) {
        Hora horario;
        HashMap<Date, HashMap<SalaPO, ReservaPO>> dataTemReservas = descreverDiaHash(date);
        for (Map.Entry<Date, HashMap<SalaPO, ReservaPO>> horaTemReserva : dataTemReservas.entrySet()) {
            Date hora = horaTemReserva.getKey();
            HashMap<SalaPO, ReservaPO> salaTemReservas = horaTemReserva.getValue();
            horario = new Hora(hora);
            for (Map.Entry<SalaPO, ReservaPO> salaTemReserva : salaTemReservas.entrySet()) {
                ReservaPO reserva = salaTemReserva.getValue();
                horario.addReserva(reserva);
            }
            dia.addHora(horario);
        }
    }
    public static Dia descreverDia(Date date) {
        Dia dia = new Dia();
        dia.setData(date);
        descreverHorario(dia, date);
        Collections.sort(dia.getHorario());
        return dia;
    }

    /**
     * Obtém um relatório mensal de todos os dias. Contém todas as reservas
     * feitas e não feitas em todos os horários e salas.
     *
     * @param date
     * @return
     */
    public static List<Dia> descreverMes(Date date) {
        List<Date> dias = calendario.getCalendario(date);
        List<Dia> mes = new ArrayList<>();
        for (Date dia : dias) {
            mes.add(descreverDia(dia));
        }
        return mes;
    }

    /**
     * Método atualiza a reserva, se possível, senão propaga exceção.
     *
     * @param reservaAnterior
     * @param atualizarPara
     */
    public static void update(ReservaPO reservaAnterior, ReservaPO atualizarPara) throws Exception {
        try {
            SalaBO.reservarSala(atualizarPara);
            SalaBO.cancelarSala(reservaAnterior);
        } catch (Exception ex) {
            throw new Exception("Impossível atualizar reserva!");
        }
    }

    /**
     * Altera o status de uma reserva e faz o update no banco.
     *
     * @param reserva
     * @param newStatus
     * @return boolean
     */
    public static boolean setStatus(ReservaPO reserva, String newStatus) {
        reserva.setStatus(new StatusPO(newStatus));
        return reservaDAO.update(reserva);
    }


    /**
     * Obtém uma reserva pelo id
     *
     * @param index
     * @return ReservaPO
     */
    public static ReservaPO getReservaPorId(int index) {
        return (ReservaPO) reservaDAO.obter(index);
    }

    /**
     * Dado um usuario, verifica suas reservas e faz o checkin se possível.
     *
     * @param usuario
     * @throws java.lang.Exception
     */
    public static void fazerCheckin(UsuarioPO usuario) throws Exception {
        UsuarioPO usuarioPopulado = UsuarioBO.isAutentico(usuario);
        if (usuarioPopulado == null) {
            throw new Exception("Login e senha inválidos");
        }
        if (!UsuarioBO.canDoChekin(usuarioPopulado)) {
            throw new Exception("Não há reservas disponíveis para chekin no momentos");
        }
        ReservaPO reserva = UsuarioBO.getMyReservaNow(usuarioPopulado);
        if (reserva.getStatus().equals(new StatusPO("emCurso"))) {
            throw new Exception("Você já fez checkin, pode usar a sala!");
        }
        ReservaBO.setStatus(reserva, "emCurso");
    }

    public static void fazerCheckout(UsuarioPO usuario) throws Exception {
        UsuarioPO usuarioLogado = UsuarioBO.isAutentico(usuario);
        if (usuarioLogado == null) {
            throw new Exception("Usuário não autenticado!");
        }
        if (!UsuarioBO.canDoCheckout(usuario)) {
            throw new Exception("Impossível realizar checkout nesta hora");
        }

        ReservaPO reserva = UsuarioBO.getReservaEmCursoHoje(usuarioLogado);
        if (reserva.getStatus().equals(new StatusPO("concluida"))) {
            throw new Exception("Você já realizou o checkout!");
        }
        ReservaBO.setStatus(reserva, "concluida");
    }
}
