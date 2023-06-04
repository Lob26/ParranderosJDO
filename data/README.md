# Data
Este es el módulo con la capa de datos. Este módulo contiene las clases asociadas con cada tabla de la base de datos.<br>
Al construir el proyecto se vale de las anotaciones definidas en el módulo `annotation-magic` para construir las clases que se van a usar en el proyecto

## Lo que hay que hacer
- Crear las clases que se van a usar en el proyecto
  - Ejemplo:
    ```java
    @Data("persona")
    @Query(k="test", v="SELECT * FROM test")
    class Persona {
    @PK
    int id;
    String nombre;
    String apellido;
    int edad;
    @FK("Ciudad")
    int idCiudad;
    @FK("Pais")
    int idPais;
    }
    ```
- Construir el proyecto
  - Correr el comando `./mvnw clean install` en la terminal (en el directorio del proyecto)
  - Se construye el proyecto y se generan las clases que se van a usar en el proyecto
  - Se puede ver el resultado en el directorio `target/generated-sources/annotations`
  - Corre el proyecto de la forma que prefieras