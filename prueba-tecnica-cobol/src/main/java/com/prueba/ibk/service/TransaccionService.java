package com.prueba.ibk.service;

import com.prueba.ibk.entity.Transaccion;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransaccionService {
    public void generarReporte(List<Transaccion> transacciones) {
        BigDecimal balanceFinal = BigDecimal.ZERO;
        int creditoCount = 0, debitoCount = 0;
        Transaccion mayorTransaccion = null;

        //Se recorre las transacciones para poder generar el reporte
        for (Transaccion transaccion : transacciones) {
            if (transaccion.getTipo().equalsIgnoreCase("Crédito")) {
                balanceFinal = balanceFinal.add(transaccion.getMonto());
                creditoCount++;
            } else if (transaccion.getTipo().equalsIgnoreCase("Débito")) {
                balanceFinal  = balanceFinal.subtract(transaccion.getMonto());
                debitoCount++;
            }
            if (mayorTransaccion == null || transaccion.getMonto().compareTo(mayorTransaccion.getMonto()) > 0) {
                mayorTransaccion = transaccion;
            }
        }

        // Imprimir el reporte
        System.out.println("REPORTE DE TRANSACCIONES");
        System.out.println("--------------------------------------------------------------");
        System.out.printf("Balance final: %.2f%n", balanceFinal);
        if (mayorTransaccion != null) {
            System.out.printf("Transacción de mayor Monto: ID %d - %.2f%n", mayorTransaccion.getId(), mayorTransaccion.getMonto());
        }
        System.out.printf("Conteo de transacciones: Crédito: %d  Débito: %d%n", creditoCount, debitoCount);
    }
}
