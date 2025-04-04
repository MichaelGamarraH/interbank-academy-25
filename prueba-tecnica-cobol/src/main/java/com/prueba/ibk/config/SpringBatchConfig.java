package com.prueba.ibk.config;

import com.prueba.ibk.entity.Transaccion;
import com.prueba.ibk.listener.ReporteFinalListener;
import com.prueba.ibk.service.TransaccionService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import java.util.List;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class SpringBatchConfig {

    private final StepBuilderFactory stepBuilderFactory;
    private final TransaccionService transaccionService;
    private final TransaccionAcumulador acumulador;

    @Bean
    public FlatFileItemReader<Transaccion> reader() {
        FlatFileItemReader<Transaccion> itemReader = new FlatFileItemReader<>();
        itemReader.setResource(new FileSystemResource("src/main/resources/data.csv"));
        itemReader.setName("csvReader");
        itemReader.setLinesToSkip(1);
        itemReader.setLineMapper(lineMapper());
        return itemReader;
    }

    private LineMapper<Transaccion> lineMapper() {
        DefaultLineMapper<Transaccion> lineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("id", "tipo", "monto");

        BeanWrapperFieldSetMapper<Transaccion> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Transaccion.class);

        fieldSetMapper.setConversionService(new DefaultConversionService());

        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        return lineMapper;

    }


    //Por cada nuevo item se va llenando en el acumulador
    @Bean
    public ItemWriter<Transaccion> writer() {
        return items -> acumulador.agregarTransacciones(items);
    }


    @Bean
    public Step step1() {
        return stepBuilderFactory.get("csv-step").<Transaccion, Transaccion>chunk(10)
                .reader(reader())
                .writer(writer())
                .build();
    }

    @Bean
    public Job job(JobBuilderFactory jobBuilderFactory,
                   Step step,
                   ReporteFinalListener listener) {
        return jobBuilderFactory.get("jobReporteTransacciones")
                .listener(listener) //
                .start(step)
                .build();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        SimpleAsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor();
        asyncTaskExecutor.setConcurrencyLimit(10);
        return asyncTaskExecutor;
    }

    @Bean
    public JobExecutionListener listener() {
        return new JobExecutionListener() {
            @Override
            public void beforeJob(JobExecution jobExecution) {

            }

            @Override
            public void afterJob(JobExecution jobExecution) {
                // Generar reporte después de que todas las transacciones sean leídas
                if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
                    System.out.println("Generando reporte de transacciones...");

                    // Obtener las transacciones procesadas
                    List<Transaccion> transacciones = (List<Transaccion>) jobExecution.getExecutionContext().get("transacciones");
                    if (transacciones != null) {
                        transaccionService.generarReporte(transacciones);
                    }
                }
            }
        };
    }

}
