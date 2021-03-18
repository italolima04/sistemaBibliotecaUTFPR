/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.biblioteca.salas.controller;

import br.edu.utfpr.biblioteca.salas.model.bo.ReservaBO;
import br.edu.utfpr.biblioteca.salas.model.entity.UsuarioPO;
import br.edu.utfpr.biblioteca.salas.model.entity.ReservaPO;
import br.edu.utfpr.biblioteca.salas.model.entity.SalaPO;
import br.edu.utfpr.biblioteca.salas.tools.CalendarioHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

/**
 *
 * @author marco
 */
@Named(value = "exibirReservaMB")
@ViewScoped
@ManagedBean
public class ExibirReservaMB {

    private ReservaPO reserva;
    private String hora;
    private CalendarioMB calendario;
    private int idReserva;
    private LoginMB sessionLogin;
    private UsuarioPO usuario;

    public ExibirReservaMB() {
//        this.sessionLogin = new LoginMB();
        this.usuario = SessionContext.getInstance().getUsuarioLogado();
        this.reserva = new ReservaPO(new UsuarioPO(null, null, null, null), new SalaPO(0, true), new Date(), 0);
    }

    public ReservaPO getReserva() {
        return reserva;
    }

    /*
    public String getDiaMesReserva() {
    	  DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
          String reportDate = df.format(reserva.getDataInicial());
          return reportDate;
    }
    */

    public CalendarioMB getCalendario() {
        return calendario;

    }

    public String canCancelReserva() {
        if (reserva.getUsuario().getRa().equals(usuario.getRa()) || usuario.getRa().equals("admin")) {
            return "true";
        }
        return "false";
    }

    public void cancelarReserva() {
        FacesMessage msg;
        if(canCancelReserva().equals("false")){
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Essa reserva não é sua!", getReserva().getStrDataInicial());
        }else if (ReservaBO.setStatus(reserva, "inativa")) {
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Reserva Cancelada", getReserva().getStrDataInicial());
        } else {
            msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Reserva não pôde ser cancelada!", getReserva().getStrDataInicial());
        }
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public int getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(int idReserva) {
        this.idReserva = idReserva;
        if (idReserva != -1) {
            reserva = ReservaBO.getReservaPorId(idReserva);
            setHora(CalendarioHelper.getHora(reserva.getDataInicial()));
        }
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

}
