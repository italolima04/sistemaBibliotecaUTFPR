/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.biblioteca.salas.controller;

import br.edu.utfpr.biblioteca.salas.model.entity.EstudantePO;
import br.edu.utfpr.biblioteca.salas.model.entity.ReservaPO;
import br.edu.utfpr.biblioteca.salas.model.entity.SalaPO;
import br.edu.utfpr.biblioteca.salas.tools.CalendarioHelper;
import java.util.Date;
import javax.inject.Named;
import javax.faces.bean.ManagedBean;
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
    private CalendarioEstudanteMB calendario;
    private int idReserva;

    /**
     * Creates a new instance of SalaMB
     */
    public ExibirReservaMB() {
        this.reserva = new ReservaPO(new EstudantePO(null, null, null, null), new SalaPO(0, true), new Date(), 0);
    }

    public ReservaPO getReserva() {
        return reserva;
    }

    public CalendarioEstudanteMB getCalendario() {
        return calendario;
    }

    public void setCalendario(CalendarioEstudanteMB calendario) {
        this.calendario = calendario;
    }

    public String getDiaMesReserva(){
        return CalendarioHelper.getDiaMesAno(reserva.getDataInicial());
    }
    
    public String getHora(){
        return CalendarioHelper.getHora(reserva.getDataInicial());
    }

    public int getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(int idReserva) {
        this.idReserva = idReserva;
    }

}
