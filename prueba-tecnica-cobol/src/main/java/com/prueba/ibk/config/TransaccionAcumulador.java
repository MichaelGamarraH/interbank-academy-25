package com.prueba.ibk.config;

import com.prueba.ibk.entity.Transaccion;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TransaccionAcumulador {
    private final List<Transaccion> transacciones = new ArrayList<>();

    public void agregarTransacciones(List<? extends Transaccion> nuevas) {
        transacciones.addAll(nuevas);
    }

    public List<Transaccion> obtenerTransacciones() {
        return transacciones;
    }
}
