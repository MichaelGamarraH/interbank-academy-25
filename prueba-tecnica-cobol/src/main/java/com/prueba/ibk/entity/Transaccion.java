package com.prueba.ibk.entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Transaccion {
    private int id;
    private String tipo;
    private BigDecimal monto;

}
