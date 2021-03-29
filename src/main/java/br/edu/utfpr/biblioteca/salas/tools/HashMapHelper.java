/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.biblioteca.salas.tools;

import br.edu.utfpr.biblioteca.salas.model.entity.ReservaPO;
import br.edu.utfpr.biblioteca.salas.model.entity.SalaPO;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author romulo
 */
public class HashMapHelper {
	public HashMap<SalaPO, ReservaPO> copy;

	public HashMapHelper() {
        HashMap<SalaPO, ReservaPO> copy = new HashMap<SalaPO, ReservaPO>();
	}
    /**
     * Clona um HashMap<SalaPO, ReservaPO> clonando devidamente os objetos
     * relacionados.
     *
     * @param map
     * @return
     */

    public HashMap<SalaPO, ReservaPO> clone(HashMap<SalaPO, ReservaPO> map) {

        for (Map.Entry<SalaPO, ReservaPO> entry : map.entrySet()) {
            try {
                this.copy.put((SalaPO) entry.getKey().clone(), (ReservaPO) entry.getValue().clone());
            } catch (NullPointerException ex) {
                this.copy.put((SalaPO) entry.getKey().clone(), null);
            }
        }
        return this.copy;
    }
}
