/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.biblioteca.salas.tools;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author romulo
 */
public class CalendarioHelper{

    Calendar calendarioClasse = Calendar.getInstance();



    /**
     * Obtém as datas do mês todo
     *
     * @param ano
     * @param mes
     * @return lista das datas do mês, incluido mês anterior, mês atual e mês
     * posterior
     * @deprecated Utilizar getCalendario(Date)
     */
	/*
    @Deprecated
    public static List<Date> getCalendario(Integer ano, Integer mes) {
        int primeiroDia, ultimoDia, diaPrimeiraSemana, i;
        List<Date> calendario = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        calendar.clear(Calendar.MINUTE);
        calendar.clear(Calendar.SECOND);
        calendar.clear(Calendar.MILLISECOND);
        calendar.set(Calendar.YEAR, ano);

        calendar.set(Calendar.MONTH, mes - 1);
        ultimoDia = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        calendar.set(Calendar.MONTH, mes);
        primeiroDia = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, primeiroDia);
        diaPrimeiraSemana = calendar.get(Calendar.DAY_OF_WEEK);
        if (diaPrimeiraSemana == 1) {
            diaPrimeiraSemana = 8;
        }

        calendar.set(Calendar.MONTH, mes - 1);
        for (i = ultimoDia; i > (ultimoDia - diaPrimeiraSemana) + 1; i--) {
            calendar.set(Calendar.DAY_OF_MONTH, i);
            calendario.add(0, calendar.getTime());
        }

        calendar.set(Calendar.MONTH, mes);
        ultimoDia = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (i = primeiroDia; i <= ultimoDia; i++) {
            calendar.set(Calendar.DAY_OF_MONTH, i);
            calendario.add(calendar.getTime());
        }

        calendar.set(Calendar.MONTH, mes + 1);
        for (i = primeiroDia; calendario.size() < 42; i++) {
            calendar.set(Calendar.DAY_OF_MONTH, i);
            calendario.add(calendar.getTime());
        }

        return calendario;
    }

    */

    /**
     * Obtém as datas do mês todo. Observa apenas mês e ano
     *
     * @param date
     * @return return lista das datas do mês, incluido mês anterior, mês atual e
     * mês posterior
     */
    public List<Date> getCalendario(Date date) {
        int primeiroDia, ultimoDia, diaPrimeiraSemana, i, ano, mes;
        List<Date> calendario;
        Calendar calendar;

        calendario = new ArrayList<>();
        //calendar = Calendar.getInstance();

        this.calendarioClasse.setTime(date);
        ano = this.calendarioClasse.get(Calendar.YEAR);
        mes = this.calendarioClasse.get(Calendar.MONTH);

        this.calendarioClasse.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        this.calendarioClasse.clear(Calendar.MINUTE);
        this.calendarioClasse.clear(Calendar.SECOND);
        this.calendarioClasse.clear(Calendar.MILLISECOND);
        this.calendarioClasse.set(Calendar.YEAR, ano);

        this.calendarioClasse.set(Calendar.MONTH, mes - 1);
        ultimoDia = this.calendarioClasse.getActualMaximum(Calendar.DAY_OF_MONTH);

        this.calendarioClasse.set(Calendar.MONTH, mes);
        primeiroDia = this.calendarioClasse.getActualMinimum(Calendar.DAY_OF_MONTH);
        this.calendarioClasse.set(Calendar.DAY_OF_MONTH, primeiroDia);
        diaPrimeiraSemana = this.calendarioClasse.get(Calendar.DAY_OF_WEEK);
        if (diaPrimeiraSemana == 1) {
            diaPrimeiraSemana = 8;
        }

        this.calendarioClasse.set(Calendar.MONTH, mes - 1);
        for (i = ultimoDia; i > (ultimoDia - diaPrimeiraSemana) + 1; i--) {
        	this.calendarioClasse.set(Calendar.DAY_OF_MONTH, i);
            calendario.add(0, this.calendarioClasse.getTime());
        }

        this.calendarioClasse.set(Calendar.MONTH, mes);
        ultimoDia = this.calendarioClasse.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (i = primeiroDia; i <= ultimoDia; i++) {
        	this.calendarioClasse.set(Calendar.DAY_OF_MONTH, i);
            calendario.add(this.calendarioClasse.getTime());
        }

        this.calendarioClasse.set(Calendar.MONTH, mes + 1);
        for (i = primeiroDia; calendario.size() < 42; i++) {
        	this.calendarioClasse.set(Calendar.DAY_OF_MONTH, i);
            calendario.add(this.calendarioClasse.getTime());
        }

        return calendario;
    }

    /**
     * Obtém uma lista de horários (Date) ordenada com os horários em que a
     * biblioteca está em funcionamento.
     *
     * @param date
     * @return
     */
    public List<Date> getHorarios(Date date) {
        List<Date> horarios = new ArrayList();
        this.calendarioClasse.setTime(date);
        this.calendarioClasse.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        this.calendarioClasse.clear(Calendar.MINUTE);
        this.calendarioClasse.clear(Calendar.SECOND);
        this.calendarioClasse.clear(Calendar.MILLISECOND);

        for (int i = 8; i <= 21; i++) {
        	this.calendarioClasse.set(Calendar.HOUR_OF_DAY, i);
            horarios.add((Date) this.calendarioClasse.getTime().clone());
        }

        return horarios;
    }

    /**
     * Converte uma String em Date. Formato da String: dd-mm-yyyy
     *
     * @param date
     * @return
     */

    public Date parseDateDate(String date) {
        String dateParsed[] = date.split("-");
        //Calendar calendar = Calendar.getInstance();
        this.calendarioClasse.set(Calendar.HOUR_OF_DAY, 0);
        this.calendarioClasse.clear(Calendar.MINUTE);
        this.calendarioClasse.clear(Calendar.SECOND);
        this.calendarioClasse.clear(Calendar.MILLISECOND);

        this.calendarioClasse.set(Calendar.DAY_OF_MONTH, Integer.valueOf(dateParsed[0]));
        this.calendarioClasse.set(Calendar.MONTH, Integer.valueOf(dateParsed[1]) - 1);
        this.calendarioClasse.set(Calendar.YEAR, Integer.valueOf(dateParsed[2]));
        return this.calendarioClasse.getTime();
    }

    /**
     * Este método recebe data, hora, minutos, segundos e converte para Date
     *
     * @param data
     * @param hora
     * @param minutos
     * @param segundos
     * @return Date
     */
    public static Date parseDate(String data, String hora, String minutos, String segundos) {
        if (data == null || data.equals("")) {
            return null;
        }
        Date date = null;
        try {
            DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String d = data + " " + hora + ":" + minutos + ":" + segundos;
            date = (java.util.Date) formatter.parse(d);
        } catch (ParseException e) {
            System.err.println("Erro: " + e.getMessage());
        }
        return date;
    }

    /*public static Date parseDate(String data, int hora, int minutos, int segundos) {
        if (data == null || data.equals("")) {
            return null;
        }
        Date date = null;
        try {
            DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String d = data + " " + hora + ":" + minutos + ":" + segundos;
            date = (java.util.Date) formatter.parse(d);
        } catch (ParseException e) {
            System.err.println("Erro: " + e.getMessage());
        }
        return date;
    }

    /**
     * Este método recebe uma data tipo Date e converte para string (dd/MM/yyyy
     * HH:mm:ss)
     *
     * @param date
     * @return String(dd/MM/yyyy HH:mm:ss)
     */
    public String getData(Date date) {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        String reportDate = df.format(date);

        return reportDate;
    }

    /**
     * Este método recebe uma data tipo Date e converte para string (dd/MM)
     *
     * @param date
     * @return String(dd/MM/yyyy HH:mm:ss)
     */
    public static String getDiaMes(Date date) {
        DateFormat df = new SimpleDateFormat("dd/MM");

        String reportDate = df.format(date);

        return reportDate;
    }

    /**
     * Este método recebe uma data tipo Date e converte para string (dd/MM/yyyy)
     *
     * @param date
     * @return String(dd/MM/yyyy)
     */

    public static String getDiaMesAno(Date date) {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

        String reportDate = df.format(date);

        return reportDate;
    }

    /**
     * Este método recebe uma data tipo Date e converte para string (HH:mm)
     *
     * @param date
     * @return
     */
    public static String getHora(Date date) {
        DateFormat df = new SimpleDateFormat("HH:mm");
        String reportDate = df.format(date);
        return reportDate;
    }

    /**
     * Este método recebe uma data tipo Date e converte para string (yyyy-MM-dd
     * HH:mm:ss)
     *
     * @param date
     * @return String(yyyy-MM-dd HH:mm:ss)
     */
    public static String getDataToDataBase(Date date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String reportDate = df.format(date);

        return reportDate;
    }

    /**
     * Incrementa a data em uma hora.
     *
     * @param date
     * @return uma data com hora acrescida em uma unidade
     */
    public Date addHora(Date date) {
    	this.calendarioClasse.setTime(date);
        this.calendarioClasse.add(Calendar.HOUR_OF_DAY, 1);
        return this.calendarioClasse.getTime();
    }

    public Date lessHora(Date date) {
        this.calendarioClasse.setTime(date);
        this.calendarioClasse.add(Calendar.HOUR_OF_DAY, -1);
        return this.calendarioClasse.getTime();
    }

    /**
     * Atribui o valor da hora na hora do dia(do tipo Date)
     *
     * @param dia
     * @param hora
     * @return
     */
    public Date mergeDiaHora(Date dia, String hora) {
        //Calendar calendar = Calendar.getInstance();
    	this.calendarioClasse.setTime(dia);
    	this.calendarioClasse.set(Calendar.HOUR_OF_DAY, Integer.valueOf(hora));
        return this.calendarioClasse.getTime();
    }

    /**
     * a partir de uma data retorna o mes anterior
     *
     * @param data
     * @return
     */
    public Date mesAnterior(Date data) {
        //Calendar calendar = Calendar.getInstance();
    	this.calendarioClasse.setTime(data);
    	this.calendarioClasse.set(Calendar.MONTH, this.calendarioClasse.get(Calendar.MONTH) - 1);
        return this.calendarioClasse.getTime();
    }

    /**
     * a partir de uma data retorna o mes posterior
     *
     * @param data
     * @return
     */
    public Date mesPosterior(Date data) {
        //Calendar calendar = Calendar.getInstance();
    	this.calendarioClasse.setTime(data);
    	this.calendarioClasse.set(Calendar.MONTH, this.calendarioClasse.get(Calendar.MONTH) + 1);
        return this.calendarioClasse.getTime();
    }

    public static String getNomeMes(Date date) {
        SimpleDateFormat formatoEmDia = new SimpleDateFormat("MMMM");
        return formatoEmDia.format(date);
    }

    public static String getMesAno(Date date) {
        SimpleDateFormat formatoEmDia = new SimpleDateFormat("MMMM 'de' yyyy", new Locale("pt", "BR"));
        return formatoEmDia.format(date);
    }

    public Date getDateComHoraSete(Date date) {
        int hora = Integer.parseInt(CalendarioHelper.getHora(date).substring(0, 2));
        for (int i = hora; i >= 7; i--) {
            date = this.lessHora(date);
        }
        return date;
    }

    public Date getDateComHoraVinteUma(Date date) {
        int hora = Integer.parseInt(CalendarioHelper.getHora(date).substring(0, 2));
        for (int i = hora; i <= 21; i++) {
            date = this.addHora(date);
        }
        return date;
    }

    public int getMinutes(Date date) {
        //Calendar calendar = Calendar.getInstance();
    	this.calendarioClasse.setTime(date);
        return this.calendarioClasse.get(Calendar.MINUTE);
    }

    public Date getHoraCheia(Date date) {

    	this.calendarioClasse.setTime(date);
    	this.calendarioClasse.clear(Calendar.MINUTE);
    	this.calendarioClasse.clear(Calendar.SECOND);
    	this.calendarioClasse.clear(Calendar.MILLISECOND);
        return this.calendarioClasse.getTime();
    }

    public Date setMinute(Date date, int minute) {
        //Calendar calendar = Calendar.getInstance();
    	this.calendarioClasse.setTime(date);
    	this.calendarioClasse.clear(Calendar.MINUTE);
    	this.calendarioClasse.clear(Calendar.SECOND);
    	this.calendarioClasse.clear(Calendar.MILLISECOND);
    	this.calendarioClasse.set(Calendar.MINUTE, minute);
        return this.calendarioClasse.getTime();
    }

    public Date setHoraMinutosSegundos(Date date, int hora, int minutos, int segundos) {
        //Calendar calendar = Calendar.getInstance();
        calendarioClasse.setTime(date);
        this.calendarioClasse.set(Calendar.HOUR, hora);
        this.calendarioClasse.set(Calendar.MINUTE, minutos);
        this.calendarioClasse.set(Calendar.SECOND, segundos);
        return this.calendarioClasse.getTime();
    }

}
