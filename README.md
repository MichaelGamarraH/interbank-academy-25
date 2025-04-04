# Prueba Técnica

## INTRODUCCIÓN
La prueba técnica en mención se basa en el desarrollo de una aplicación que procese un archivo CSV con transacciones bancarias y genere un reporte en el cual se pueda apreciar el balance final, la transacción de mayor monto y el conteo de transacciones de acuerdo al tipo “Crédito” o “Débito”. 

## INSTRUCCIONES DE EJECUCIÓN
La aplicación está realizada en Java (versión 17) con Spring Boot 2.6.7, el cual es compatible con Spring Batch (herramienta para procesar grandes volúmenes de datos). 
Se han utilizado las siguientes dependencias, las cuales están en el archivo `pom.xml`:
- Spring Batch
- Spring Boot
- Lombok (Para getter, setter, inyección de dependencias)

Para ejecutar el proyecto solo se necesita dar clic en **Run Application**.

## ENFOQUE Y SOLUCIÓN
Para desarrollar correctamente la solución, se utilizó **Spring Batch**, que permitirá leer y procesar de manera eficiente los datos. Esta herramienta es útil para sectores de banca donde cuentan con millones de datos y necesitan ser procesados eficientemente.
La solución partió de definir la entidad **"Transacción"**, sobre la cual permitirá leer datos del archivo CSV. Se necesitaron colocar anotaciones de Lombok como `@Getter`, `@Setter`, `@AllArgsConstructor`, `@NoArgsConstructor` para facilitar las configuraciones manuales. A partir de ello, se crearon carpetas de acuerdo a su funcionalidad para separar las responsabilidades.

## ESTRUCTURA DEL PROYECTO

### Carpetas:
**com.prueba.ibk**

#### `config/SpringBatchConfig.java`:
- **Función**: Configuración de Spring Batch, define el job, los steps, el lector y el escritor de datos, y configura la ejecución asincrónica para mejorar el rendimiento.
- **Responsabilidad**: Configura el proceso batch para leer y procesar transacciones de un archivo CSV.

#### `config/TransaccionAcumulador.java`:
- **Función**: Acumula las transacciones leídas durante el proceso batch.
- **Responsabilidad**: Mantiene una lista de transacciones que se van acumulando a medida que se procesan, para su posterior uso, como generar un reporte.

#### `entity/Transaccion.java`:
- **Función**: Representa la entidad **Transacción**, que sirve para mapear las transacciones.
- **Responsabilidad**: Define la estructura de los datos que se procesan en el batch, como id, tipo y monto.

#### `listener/ReporteFinalListener.java`:
- **Función**: Escucha la finalización del job de Spring Batch y genera un reporte al finalizar.
- **Responsabilidad**: Una vez que el job ha terminado, este listener se asegura de que se genere un reporte con las transacciones procesadas.

#### `service/TransaccionService.java`:
- **Función**: Servicio encargado de manejar la lógica relacionada con las transacciones.
- **Responsabilidad**: Contiene el método que genera el reporte de las transacciones, y realiza operaciones de negocio relacionadas con las transacciones.

### Archivos:
- **`resources/data.csv`**: Contiene las transacciones que serán procesadas por la aplicación.


