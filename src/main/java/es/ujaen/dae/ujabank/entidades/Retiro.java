/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ujaen.dae.ujabank.entidades;

import es.ujaen.dae.ujabank.DTO.DTOTransaccion;
import es.ujaen.dae.ujabank.interfaces.Transaccion;

/**
 *
 * @author axpos
 */
public class Retiro extends Transaccion {

    private int _idOrigen;
    private int _idDestino;

    public Retiro() {
    }

    public int getIDOrigen() {
        return _idOrigen;
    }

    public void setIDOrigen(int idCuentaOrigen) {
        this._idOrigen = idCuentaOrigen;
    }

    public int getIDDestino() {
        return _idDestino;
    }

    public void setDestino(int _idDestino) {
        this._idDestino = _idDestino;
    }

    @Override
    public String toString() {
        return "Retirada: \n"
                + "Fecha: " + this.getFecha().toString() + "\n"
                + "ID cuenta origen: " + this.getIDOrigen() + "\n"
                + "Número cuenta destino: " + this.getIDDestino() + "\n"
                + "Cantidad (UJAC): " + this.getCantidad();
    }

    @Override
    public DTOTransaccion.TIPO getTipo() {
        return DTOTransaccion.TIPO.retiro;
    }
}
