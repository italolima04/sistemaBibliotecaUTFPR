package br.edu.utfpr.biblioteca.salas.model.dao;

import br.edu.utfpr.biblioteca.salas.model.entity.UsuarioPO;
import br.edu.utfpr.biblioteca.salas.model.entity.ReservaPO;
import br.edu.utfpr.biblioteca.salas.model.entity.StatusPO;
import br.edu.utfpr.biblioteca.salas.tools.CalendarioHelper;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

public class ReservaDAO<R> implements Serializable {

	 private static EntityManager entityManager = Persistence.createEntityManagerFactory("UP").createEntityManager();
	 private Query queryInterna;
	 private Class clazz;
	 private ReservaPO reserva;
     public ReservaDAO() {
        //super(ReservaPO.class);
    }

    /**
     * Insere uma reserva no BD, retorna true se a reserva foi inserida e false
     * se houver algum erro.
     *
     * @param reserva
     * @return Boolean
     */
    public boolean insert(ReservaPO reserva) {
        if (reserva.canReservar()) {
            ReservaDAO.getEntityManager().getTransaction().begin();
            if (isReservado(reserva)) {
                ReservaDAO.getEntityManager().getTransaction().rollback();
                return false;
            }
            ReservaDAO.getEntityManager().persist(reserva);
            ReservaDAO.getEntityManager().getTransaction().commit();
            return true;
        }

        return false;
    }


    public void delete(ReservaPO reserva) {
        throw new UnsupportedOperationException();
    }

    /**
     * Dado uma data-hora, este método retorna um lista de reservas
     * correspondente a data.
     *
     * @param date
     * @return List<ReservaPO>
     */
    public List<ReservaPO> listByDateTime(Date date) {
        this.queryInterna = getEntityManager().createQuery("SELECT e FROM Reserva e "
                + "WHERE e.dataInicial=:dataInicial AND e.status != :inativa");
        this.queryInterna.setParameter("dataInicial", date);
        this.queryInterna.setParameter("inativa", new StatusPO("inativa"));
        return this.queryInterna.getResultList();
    }


    public boolean update(R entity) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().merge(entity);
            getEntityManager().getTransaction().commit();
        } catch (Exception ex) {
            return false;
        }
        return true;
    }

    public R obter(R entity) {
        getEntityManager().clear();
        return (R) ReservaDAO.getEntityManager().find(clazz, entity);
    }


    /**
     * Dado uma data-hora, este método retorna um lista de reservas
     * correspondente a data e o id da sala.
     *
     * @param date
     * @param idSala
     * @return List<ReservaPO>
     */
    public List<ReservaPO> listByDateTimeAndSala(Date date, int idSala) {
    	this.queryInterna = getEntityManager().createQuery("SELECT e FROM Reserva e WHERE e.dataIniciaxdataInicial AND e.id =:idSala");
    	this.queryInterna.setParameter("dataInicial", date);
    	this.queryInterna.setParameter("idSala", idSala);
        return this.queryInterna.getResultList();
    }

    /**
     * Dado uma data-hora e uma sala, este método retorna se existe uma reserva
     * correspondente a data e em outro id da sala.
     *
     * @param date
     * @param idSala
     * @param usuario
     * @return
     */
    public boolean haveReservaByDateIdsalaUsuario(Date date, int idSala, UsuarioPO usuario) {
    	this.queryInterna = ReservaDAO.getEntityManager().createQuery("SELECT COUNT(e) FROM Reserva e WHERE e.dataInicial =:dataInicial AND "
                + "e.id !=:idSala AND e.status != :status AND e.usuario = :usuario");
    	this.queryInterna.setParameter("dataInicial", date);
    	this.queryInterna.setParameter("idSala", idSala);
    	this.queryInterna.setParameter("status", new StatusPO("inativa"));
    	this.queryInterna.setParameter("usuario", usuario);
        Long qtdReservas = (Long) this.queryInterna.getSingleResult();

        return qtdReservas != 0;
    }

    /**
     * SELECT busca a quantidade de reservas exitentes dado dia, mes, ano, e
     * hora inicial
     *
     * @param date
     * @return int
     */
    public int getQuantidadeReservas(Date date) {
        String strData = CalendarioHelper.getDataToDataBase(date);
        this.queryInterna = getEntityManager().createNativeQuery("SELECT count(*) FROM Reservas r WHERE r.data_inicial = '" + strData + "'");
        long qtdeReservas = 0;
        try {
            qtdeReservas = (long) this.queryInterna.getSingleResult();
        } catch (Exception ex) {
            return 0;
        }
        return (int) qtdeReservas;
    }

    /**
     * Busca por uma reserva ativa na data-hora e sala contidas no objeto dado.
     * Retorna true se já existe uma reserva neste horario ou false se não foi
     * encontrada nenhuma.
     *
     * @param reserva
     * @return
     */
    public boolean isReservado(ReservaPO reserva) {
        try {
        	this.queryInterna = getEntityManager().createQuery("SELECT e FROM Reserva e WHERE e.dataInicial = :dataInicial AND e.sala = :sala AND e.status != :status");
        	this.queryInterna.setParameter("dataInicial", reserva.getDataInicial());
        	this.queryInterna.setParameter("sala", reserva.getSala());
        	this.queryInterna.setParameter("status", new StatusPO("inativa"));
        	this.queryInterna.getSingleResult();
            return true;
        } catch (NoResultException ex) {
            return false;
        }
    }


    public void metodoNovo (boolean variavel, UsuarioPO usuario) throws Exception {
   	 if (this.isReservado(this.reserva)) {
            throw new Exception("Sala já reservada");
        } else {
       	 if (variavel) {
       		this.insert(this.reserva);
       	 } else {
                if (this.haveReservaByDateIdsalaUsuario(this.reserva.getDataInicial(), reserva.getSala().getId(), this.reserva.getUsuario())) {
                    throw new Exception("Você já possui uma reserva nesse horário em outra sala!");
                }

                if (!(reserva.canReservar())) {
                    throw new Exception("Você já efetuou o limite máximo de reservas diárias!");
                }
            }
        }
   	this.insert(reserva);
    }


    /**
     * SELECT busca as reservas entre duas datas, data inicial e data final.
     * A data inicial deve possui hora igual a 8
     * A data final deve possui hora igual a 22
     * @param dataInicial
     * @param dataFinal
     * @return lista de reservas
     */
    public List<ReservaPO> getReservasPeriodo(Date dataInicial, Date dataFinal) {
    	this.queryInterna = getEntityManager().createQuery("SELECT e FROM Reserva e WHERE e.dataInicial >= :dataInicial AND e.dataInicial <= :dataFinal");
    	this.queryInterna.setParameter("dataInicial", dataInicial);
    	this.queryInterna.setParameter("dataFinal", dataFinal);
        return this.queryInterna.getResultList();
    }

	public static EntityManager getEntityManager() {
		return entityManager;
	}

	public static void setEntityManager(EntityManager entityManager) {
		ReservaDAO.entityManager = entityManager;
	}

	public List<ReservaPO> list() {
		// TODO Auto-generated method stub
		return null;
	}
}
