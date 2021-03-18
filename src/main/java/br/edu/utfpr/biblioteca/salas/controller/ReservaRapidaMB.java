package br.edu.utfpr.biblioteca.salas.controller;

import br.edu.utfpr.biblioteca.salas.model.bo.ReservaBO;
import br.edu.utfpr.biblioteca.salas.view.BotaoHorario;
import br.edu.utfpr.biblioteca.salas.model.bo.SalaBO;
import br.edu.utfpr.biblioteca.salas.model.entity.UsuarioPO;
import br.edu.utfpr.biblioteca.salas.model.entity.ReservaPO;
import br.edu.utfpr.biblioteca.salas.model.entity.SalaPO;
import br.edu.utfpr.biblioteca.salas.tools.CalendarioHelper;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FlowEvent;
import org.primefaces.event.SelectEvent;

import java.io.Serializable;
import java.util.Map;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Iterator;
import javax.inject.Named;

@Named(value = "reservaRapidaMB")
@ManagedBean
@ViewScoped
public class ReservaRapidaMB implements Serializable {

    private ReservaPO reserva;
    private String strHora = "0";
    private List<BotaoHorario> botoesHorario;
    List<String> list = new ArrayList();
    private String reservado;

    //Formatadores de data
    private final SimpleDateFormat formatoEmHoras;
    private final SimpleDateFormat formatoEmDia;

    public ReservaRapidaMB() {
        reservado = "false";
        formatoEmHoras = new SimpleDateFormat("HH");
        formatoEmDia = new SimpleDateFormat("dd/MM/yyyy");
        this.reserva = new ReservaPO(new UsuarioPO(null, null, null, null), new SalaPO(0, true), new Date(), 0);
        this.botoesHorario = new ArrayList<>();
        updateBotoesAtivosPorDia(new Date());
    }

    public ReservaPO getReserva() {
        return this.reserva;
    }

    public ReservaPO setReserva(ReservaPO reserva) {
        return this.reserva = reserva;
    }

    public String getStrHora() {
        return strHora;
    }

    public void setStrHora(String strHora) {
        for (Iterator<BotaoHorario> iterator = botoesHorario.iterator(); iterator.hasNext();) {
            BotaoHorario next = iterator.next();
            String classe = next.getClasse();
            if (next.getHoraStr().equals(strHora)) {
                next.setClasse("azul");
            } else if(classe.equals("btn btn-primary")){
                next.setClasse("verde");
            }
        }
        this.strHora = strHora;
        reservado = "false";
    }

    public Date getDataAtual() {
        return new Date();
    }

    public List<BotaoHorario> getBotoesHorario() {
        return botoesHorario;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public List<String> getList() {
        return list;
    }

    public List getSelectQtd() {
        List qtdA = new ArrayList();
        for (int i = 1; i <= 5; i++) {
            qtdA.add(i);
        }
        return qtdA;
    }


    /**
     * Método executado ao ser escolhida uma data no calendário
     *
     * @param event
     */
    public void onDateSelect(SelectEvent event) {
        Date dataSelecionada = (Date) event.getObject();
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Data Selecionada", formatoEmDia.format(event.getObject())));
        updateBotoesAtivosPorDia(dataSelecionada);
    }


    public void click() {
        RequestContext requestContext = RequestContext.getCurrentInstance();

        requestContext.update("form:display");
        requestContext.execute("PF('dlg').show()");

    }

    /**
     * Este método solicita para a classe SalaBO uma lista de salas disponíveis
     * dado um dia-hora. Exibe em um Select<html> as salas
     *
     * @return
     */
    public HashMap<String, String> getSalasDisponiveis() {
        HashMap<String, String> salasHash = new HashMap<>();
        List<SalaPO> salas = SalaBO.getSalasDisponiveis(this.reserva.getDataInicial());
        list = new ArrayList<>();
        for (SalaPO s : salas) {
            salasHash.put(String.valueOf(s.getId()), "Sala " + s.getId());
            list.add(String.valueOf(s.getId()));
        }
        return salasHash;
    }

    /**
     * Valida a entrada do usuário e faz a reserva invocando as camadas
     * inferiores.
     */
    public void reservarSala() {
        FacesMessage msg;

        try {
            SalaBO.reservarSala(reserva);
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Reservado", getReserva().getStrDataInicial());
            reservado = "true";
        } catch (Exception ex) {
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fail", ex.getMessage());
        }
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void reservarSalaLogado() {
        FacesMessage msg;


         UsuarioPO usuarioLogado = SessionContext.getInstance().getUsuarioLogado();
//        try {
//            if (usuarioLogado == null) {
//                if (usuario.getRa() == null || usuario.getSenha() == null) {
//                    throw new Exception("Campos login e senha não podem ser nulos!");
//                }
//                throw new RuntimeException("must be logged in");
//            } else if (usuario.getRa().isEmpty() || usuario.getSenha().isEmpty()) {
//                throw new Exception("Informe o login e a senha!");
//            }
//
//            this.usuario = usuarioLogado;
//            ReservaBO.fazerCheckout(this.usuario);
//            message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Checkout efetuado!", null);
//        } catch (Exception ex) {
//            message = new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), null);
//        } finally {
//            FacesContext.getCurrentInstance().addMessage(null, message);
//        }
        reserva.setUsuario(usuarioLogado);
        try {
            SalaBO.reservarSala(reserva);
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Reservado", getReserva().getStrDataInicial());
            reservado = "true";
        } catch (Exception ex) {
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fail", ex.getMessage());
        }
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    /**
     * Este método deve solicitar para a classe SalaBO um hash contendo as salas
     * que possuem horários disponíveis dado um dia. Ele é quem deve setar o css
     * dos botões.
     *
     * @param data
     */
    public void updateBotoesAtivosPorDia(Date data) {
        //hash com key inteiro (strHora -> 8, 9, 10...) e boolean se existe alguma sala disponível ou todas estão reservas.
        HashMap<Date, Boolean> salasDisponiveis = SalaBO.getHorariosDisponiveis(data);
        if (salasDisponiveis == null) {
            return;
        }
        botoesHorario.clear();
        for (Map.Entry<Date, Boolean> entry : salasDisponiveis.entrySet()) {
            if ((entry.getValue()) && ((new Date()).before(entry.getKey()))) {
                botoesHorario.add(new BotaoHorario(entry.getKey().getHours(), "verde", false));
            }
            else {
                botoesHorario.add(new BotaoHorario(entry.getKey().getHours(), "vermelho", true));
            }
        }
        botoesHorario.add(new BotaoHorario(0, "branco", true));
        Collections.sort(botoesHorario);
    }

    public void alterarEstilo() {
        String parametroUmAtivo = "";
        if (parametroUmAtivo.equals("btn btn-success")) {
            parametroUmAtivo = "ui-priority-primary";
        } else {
            parametroUmAtivo = "btn btn-success";
        }
    }

    public void ativarReserva() {
        throw new UnsupportedOperationException();
    }

    public List<ReservaPO> listarReservas(UsuarioPO usuario) {
        throw new UnsupportedOperationException();
    }

    public String getReservado() {
        return reservado;
    }

}
