# API
Este es el modulo con la capa del negocio.<br>
En este módulo se encuentra la clase `ParranderosB` que se encargan de realizar las operaciones de negocio, en este caso Parranderos
## Crear
### Crear un objeto que solo ocupa una tabla en la base de datos
Cuando para crear completamente una entidad solo se necesita una tabla en la base de datos, se puede seguir una estructura similar a la siguiente para crear el objeto
```java
public String createOne(Object[] input) {
    LOG.info("START ⟼ Creando un unico {}", input[0]);
    Object[][] response = persistence.createOne(input);
    LOG.info("END ⟼ Unico {} creado", input[0]);
    return TextTable.builder().append(response).toString();
}
```

### Crear un objeto que ocupa varias tablas en la base de datos
Cuando para crear completamente una entidad se necesitan varias tablas en la base de datos, se puede seguir una estructura similar a la siguiente para crear el objeto
```java
public String createManyInheritor(OrderedMap<Object, Object> input1, OrderedMap<Object, Object> inpu2...) {
    LOG.info("START ⟼ Creando muchos {}", input1.get(0));
    Object[][][] response = persistence.createMany(input1, inpu2);
    LOG.info("END ⟼ Muchos {} creados", input1.get(0));
    return "Input1::\n" + TextTable.builder().append(response[0]).toString() +
           "Input2::\n" + TextTable.builder().append(response[1]).toString();
}
```

## Leer
### Leer un unico objeto por su(s) PK
Se vale de reflexión para obtener el metodo de que ejecuta su consulta<br>
Se vale de la clase `TextTable` para mostrar los resultados de la consulta en una tabla
```java
public <R extends Tabulable> String retrieveOne(Class<R> arg, Object... pks)
        throws ReflectiveOperationException {
    LOG.info("START ⟼ Recuperando {} con id(s) {}", arg.getSimpleName(), Arrays.toString(pks));
    List<Object[]> vo = persistence.retrieveOne(arg, pks);
    LOG.info("END ⟼ {} con id(s) {} recuperado [{}]", arg, Arrays.toString(pks), vo);

    TextTable builder = TextTable.builder();
    vo.forEach(builder::append);

    return arg.getSimpleName() + " PK:" + Arrays.toString(pks) + "::\n" + builder;
}
```

### Leer todos los objetos de una tabla
Se vale de reflexión para obtener el metodo de que ejecuta su consulta<br>
Se vale de la clase `TextTable` para mostrar los resultados de la consulta en una tabla
```java
public <R extends Tabulable> String retrieveAll(Class<R> arg)
            throws ReflectiveOperationException {
    LOG.info("START ⟼ Recuperando todos los {}", arg.getSimpleName());
    List<Object[]> vos = persistence.retrieveAll(arg);
    LOG.info("END ⟼ {} de {} recuperados", vos.size(), arg.getSimpleName());

    TextTable builder = TextTable.builder();
    vos.forEach(builder::append);

    return arg.getSimpleName() + "::\n" + builder;
}
```

## Actualizar
No hay metodo que ya implementa esta funcionalidad, sin embargo, valiendose de las `@Query` generadas se puede hacer por su PK
```java
public <R extends Tabulable> String update(Class<R> arg, Object[] input, Object... pks) {
    LOG.info("START ⟼ Actualizando un unico {}", pks);
    List<Object[]> l = persistence.delete(arg, input, pks);
    LOG.info("END ⟼ {} {} actualizado(s)", l.get(0), arg);
    TextTable builder = TextTable.builder();
    l.forEach(builder::append);
    
    return arg.getSimpleName() + " PK:" + Arrays.toString(pks) + "::\n" + builder;
}
```

## Borrar
### Borrar un unico objeto por su(s) PK
Se vale de reflexión para obtener el metodo de que ejecuta su consulta<br>
Se vale de la clase `TextTable` para mostrar los resultados de la consulta en una tabla
```java
public <R extends Tabulable> String delete(Class<R> arg, Object... pks)
        throws ReflectiveOperationException {
    LOG.info("START ⟼ Borrando {} con id(s) {}", arg.getSimpleName(), pks);
    List<Object[]> l = persistence.delete(arg, pks);
    LOG.info("END ⟼ {} {} borrado(s)", l.get(0), arg);
    TextTable builder = TextTable.builder();
    l.forEach(builder::append);

    return arg.getSimpleName() + " PK:" + Arrays.toString(pks) + "::\n" + builder;
}
```

## Consultas
Para operaciones que no son CRUD se puede seguir una estructura similar a la siguiente.<br>
La operacion sigue en el módulo de persistencia, especificamente en la clase `ParranderosP`:
```java
public String consulta(Object... args) {
    LOG.info("START ⟼ Consulta");
    List<Object[]> list = persistence.metodoConsulta();
    LOG.info("END ⟼ Consulta");
    TextTable builder = TextTable.builder();
    list.forEach(builder::append);
    return builder.toString();
}
```