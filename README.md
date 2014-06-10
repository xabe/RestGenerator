Rest Generator
======

El proyecto consiste en la generación de servicios rest desde la base de datos. Actualmente el proyecto soporta la siguientes base datos:

* Mysql
* Oracle
* SqlServer

El proyecto esta basado completamente en Maven:

* rest-archetype-webapp 
* maven-rest-plugin


Instalación
--------------

Lo primero que tenemos que hacer es instalar nuestro arquetipo en nuestra máquina local o un repositorio de maven


```sh 
git clone [git-repo-url] RestGenerator
cd rest-archetype-webapp
mvn package install
```


Con esto ya tenemos el arquetipo instalado en nuestra máquina, hacemos lo mismo con el plugin

```sh 
cd maven-rest-puglin
mvn package install
```

Ejemplo
--------------

Lo primero es crear una tabla en la base de datos por ejemplo

```sql
CREATE TABLE t_profesor (
 id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
 nombre VARCHAR(100)
);

insert into t_profesor (nombre) values ("Juan");

```

Creamos nuestro proyecto web con el arquetipo que hemos instalado anteriormente

```sh 
mvn archetype:generate -DarchetypeCatalog=local
```

Seleccionamos nuestro arquetipo y rellenamos los datos que nos pide

```sh 
cd nuestro-proyecto
```

Abrimos el pom.xml y modificamos la configuración de la base de datos y las tablas (separadas por ;) que deseamos generar sus servicios web.

```sh
<config.database.driver>com.mysql.jdbc.Driver</config.database.driver>
<config.database.username>root</config.database.username>
<config.database.password>root</config.database.password>
<config.database.catalog>KE</config.database.catalog>
<config.database.url>jdbc:mysql://localhost:3306/KE</config.database.url>
<config.database.tables>t_profesor</config.database.tables>
```

Procedemos a invocar al plugin para generar la clases:

```sh 
mvn clean -PGenerate package
```

Por último para probar nuestro servicios rest ejecutamos:

```sh 
mvn clean package -PJetty
```

Abrimos un navegador y ponemos la siguiente url:

```sh 
http://localhost:9080/test/profesor/all
```

Con esto tenemos un proyecto base para hacer Servicios web

Si te interesa saber mas de java y programación no dudes en seguir mi blog:

* [Mi blog]

Licencia
----

MIT

**Free Software, Hell Yeah!**

[git-repo-url]:https://github.com/xabe/RestGenerator.git
[Mi blog]:http://tirandolineasdecodigo.blogspot.com.es/