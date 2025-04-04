package com.prueba.ibk.listener;

import com.prueba.ibk.config.TransaccionAcumulador;
import com.prueba.ibk.entity.Transaccion;
import com.prueba.ibk.service.TransaccionService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ReporteFinalListener implements JobExecutionListener {

    private final TransaccionAcumulador acumulador;
    private final TransaccionService transaccionService;

    @Override
    public void beforeJob(JobExecution jobExecution) {

    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        List<Transaccion> todas = acumulador.obtenerTransacciones();
        transaccionService.generarReporte(todas);
    }
}