# Persistence
En este módulo se encuentran las clases que se encargan de la persistencia de los datos y de las operaciones transaccionales en la base de datos.

## Clases
- `ParranderosP`<br>
La clase de donde parte la ejecucion de la seccion transaccional y de consulta de la aplicacion.<br>
Como contiene dos grandes secciones se permite la libertad de separar esta clase en dos o mas, a gusto del desarrollador.
    - `TxManager`<br>
      La clase que se encarga de la ejecucion de las operaciones transaccionales otorgandole el esqueleto transaccional
      a lel codigo escrito.<br>
      Se usa a manera de lambdas:
      ```java
      () -> {codigo_que_buscas_ejecutar_transaccionalmente();}
      ```
- `SQLHandler`<br>
EL objetivo de esta clase es el de facilitar la ejecución de las operaciones CRUD en la base de datos.
- `SQLReq`<br>
El objetivo de esta clase es la de facilitar la ejecucion de las operaciones de consulta en la base de datos.
- `SQLUtil`<br>
El objetivo de esta clase es la de facilitar la ejecución de, literalmente, conseguir el siguiente valor de la secuencia involucrada.
Se puede expandir a gusto para añadir otras operaciones que sean utilidades pero no pertenezcan conceptualmente a las otras.