# Annotation Magic
Este es el módulo de anotaciones de la herramienta Annotation Magic.<br>
Este módulo se encarga de leer las anotaciones de los archivos de entrada y generar los archivos de salida con las anotaciones aplicadas.

## Anotaciones
Annotation Magic soporta las siguientes anotaciones:
- `@Data` para generar
  - VO interface
    - Todos los getters de la clase anotada
  - POJO class que implementa el VO y a Tabulable 
    - Todos los campos son privados
    - Constructor con todos los campos
    - Constructor vacío
    - Todos los setters de la clase anotada
    - Todos los getters de la clase anotada y su respectivo `@Override` al método del VO
    - Su respectivo `toString()` que retorna un `String` con todos los valores de los campos
    - Un metodo `toTable()` que retorna un `Object[][]` para ser usado en una tabla
- `@FK` que representa una llave foranea (Es decorativo, no genera nada)
- `@PK` que representa una llave primaria (Es necesario para generar las queries que usan a la PK) y tiene de campo el
  nombre de la secuencia que lo genera
- `@Query` & `@Queries` para almacenar una tupla llave valor con el nombre de la query y su respectiva query SQL

## Ejemplo
Teniendo la siguiente clase:
```java
@Data("persona") @Query(k="test", v="SELECT * FROM test")
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
Se generará el siguiente VO:
```java
public interface VOPersona extends Tabulable {
  int getId();
  String getNombre();
  String getApellido();
  int getEdad();
  int getIdCiudad();
  int getIdPais();
  @Override String toString();
  @Override Object[][] toTable();
}
```
Y el siguiente POJO:
```java
@Query(k = "INSERT", v = "INSERT INTO persona (id,nombre,apellido,edad,id_ciudad,id_pais) VALUES (?,?,?,?,?,?)")
@Query(k = "SELECT_ONE", v = "SELECT * FROM persona WHERE id=?")
@Query(k = "SELECT_ALL", v = "SELECT * FROM persona")
@Query(k = "UPDATE", v = "UPDATE persona SET nombre=?,apellido=?,edad=?,id_ciudad=?,id_pais=? WHERE id=?")
@Query(k = "DELETE", v = "DELETE FROM persona WHERE id=?")
@Query(k = "test", v = "SELECT * FROM test")
public class Persona implements VOPersona {
  private int id;
  private String nombre;
  private String apellido;
  private int edad;
  private int idCiudad;
  private int idPais;

  public Persona() {
    this.id = 0;
    this.nombre = "";
    this.apellido = "";
    this.edad = 0;
    this.idCiudad = 0;
    this.idPais = 0;
  }

  public Persona(int id, String nombre, String apellido, int edad, int idCiudad, int idPais) {
    this.id = id;
    this.nombre = nombre;
    this.apellido = apellido;
    this.edad = edad;
    this.idCiudad = idCiudad;
    this.idPais = idPais;
  }

  public static String[] getHeaders() {
    return new String[]{"id", "nombre", "apellido", "edad", "idCiudad", "idPais"};
  }

  @Override public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  @Override public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  @Override public String getApellido() {
    return apellido;
  }

  public void setApellido(String apellido) {
    this.apellido = apellido;
  }

  @Override public int getEdad() {
    return edad;
  }

  public void setEdad(int edad) {
    this.edad = edad;
  }

  @Override public int getIdCiudad() {
    return idCiudad;
  }

  public void setIdCiudad(int idCiudad) {
    this.idCiudad = idCiudad;
  }

  @Override public int getIdPais() {
    return idPais;
  }

  public void setIdPais(int idPais) {
    this.idPais = idPais;
  }

  @Override public String toString() {
    return "Persona" + '[' + "id=" + this.id + "nombre=" + this.nombre + "apellido=" + this.apellido + "edad=" + this.edad + "idCiudad=" + this.idCiudad + "idPais=" + this.idPais + ']';
  }

  @Override public Object[][] toTable() {
    return new Object[][]{new Object[]{"id", "nombre", "apellido", "edad", "idCiudad", "idPais"}, new Object[]{id, nombre, apellido, edad, idCiudad, idPais}};
  }
}
```

