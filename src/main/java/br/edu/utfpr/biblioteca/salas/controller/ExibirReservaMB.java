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

    public int getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(int idReserva) {
        this.idReserva = idReserva;
        if (idReserva != -1) {
        	ReservaBO rb = new ReservaBO();
            reserva = rb.getReservaPorId(idReserva);
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
