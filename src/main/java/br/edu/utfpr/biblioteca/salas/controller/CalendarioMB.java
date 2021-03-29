/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.biblioteca.salas.controller;

import br.edu.utfpr.biblioteca.salas.model.Dia;
import br.edu.utfpr.biblioteca.salas.model.bo.UsuarioBO;
import br.edu.utfpr.biblioteca.salas.model.bo.ReservaBO;
import br.edu.utfpr.biblioteca.salas.model.entity.UsuarioPO;
import br.edu.utfpr.biblioteca.salas.model.entity.ReservaPO;
import br.edu.utfpr.biblioteca.salas.tools.CalendarioHelper;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

/**
 *
 * @author marco
 */
@Named(value = "calendarioMB")
@SessionScoped
@ManagedBean
public class CalendarioMB {

    private UsuarioPO usuario;
    private Dia diaSelecionado;
    private Date mesEscolhido;
    private ReservaPO reserva;
	private static CalendarioHelper calendario;

    public ReservaPO getReserva() {
        return reserva;
    }

    public CalendarioMB() {
        this.usuario = new UsuarioPO(null, null, null, null);
        mesEscolhido = new Date();
        diaSelecionado = new Dia();
    }

    public UsuarioPO getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioPO usuarioPO) {
        this.usuario = usuarioPO;
    }

    public Dia getDiaSelecionado() {
        return diaSelecionado;
    }

    public void setDiaSelecionado(Dia diaSelecionado) {
        this.diaSelecionado = diaSelecionado;
    }

    /**
     * Valida a entrada do usuário e cadastra um usuario.
     *
     * @param usuario
     */
    private void cadastrarUsuario() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        try {
            Long.parseLong(usuario.getRa());
            if (usuario.getNome().isEmpty()) {
                throw new Exception("Preencha todos os campos");
            }
            if (usuario.getEmail().isEmpty()) {
                throw new Exception("Preencha todos os campos");
            }
            if (usuario.getSenha().isEmpty()) {
                throw new Exception("Preencha todos os campos");
            }
            String[] split = usuario.getEmail().split("@");
            if (split[0].isEmpty() || split[1].isEmpty()) {
                throw new Exception("E-mail inválido");
            }
        } catch (NumberFormatException ex) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "RA inválido", "Inválido"));
        } catch (IndexOutOfBoundsException ex) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Preencher nome", "Inválido"));
        } catch (Exception ex) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ex.getMessage(), "Inválido"));
        }

        UsuarioBO.cadastrarUsuario(usuario);
    }

    /**
     * Retorna usuario logado
     *
     * @return EstuantePO
     */
    public UsuarioPO getUsuarioLogado() {
        return (UsuarioPO) SessionContext.getInstance().getUsuarioLogado();
    }

    /**
     * Obtém uma lista com todos os dias do mês dado. Cada dia é uma relação de
     * 14 horas cada qual com no máximo 6 reservas
     *
     * @param date
     * @return
     */
    public List<Dia> getMes(Date date) {
    	ReservaBO rb = new ReservaBO();
        return rb.descreverMes(date);
    }

    /**
     * Obtém lista com todos os dias do mês atual. Cada dia é uma relação de 14
     * horas cada qual com no máximo 6 reservas
     *
     * @return
     */
    public List<Dia> getMes() {
        return getMes(this.mesEscolhido);
    }

    public void renderMesAnterior() {
        this.mesEscolhido = this.calendario.mesAnterior(mesEscolhido);
    }

    public void renderMesPosterior() {
        this.mesEscolhido = this.calendario.mesPosterior(mesEscolhido);
    }

    public void renderMesAtual() {
        this.mesEscolhido = new Date();
    }

    public Date getMesAnterior() {
        return this.calendario.mesAnterior(mesEscolhido);
    }

    public Date getMesPosterior() {
        return this.calendario.mesPosterior(mesEscolhido);
    }

    public String getNomeAtual() {
        return CalendarioHelper.getMesAno(mesEscolhido);
    }

    public String getNomeAnterior() {
        return CalendarioHelper.getMesAno(getMesAnterior());
    }

    public String getNomePosterior() {
        return CalendarioHelper.getMesAno(getMesPosterior());
    }
}
