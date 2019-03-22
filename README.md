# opticks-test

repositorio para desarrollo de prueba tecnica para Opticks

El ejercicio consiste en desarrollar un pequeño programa de tipo web service en Java que
se encienda y se quede escuchando peticiones http en un puerto de la máquina.
El servicio debe quedarse escuchando en el puerto 7000 y tener un endpoint bajo el path
“time” que acepte peticiones GET y debe contestar con el timestamp actual de la máquina.
Debe contestar con un JSON que contenga el resultado en el miembro “result”. Por ejemplo,
a la petición:

http://localhost:7000/time

Debe contestar:
{
“result” : 1546533260
}

Este endpoint debe tener un rate limit por “'Authorization” header, de modo que un mismo
usuario (identificado por su “Authorization” header) no pueda consultar el timestamp de la
máquina más de tres veces por minuto. Si un mismo “Authorization” consulta más de tres
veces en un mismo minuto el endpoint, debe retornar un mensaje de error 429 Too Many
Requests. Para este programa, se puede considerar que en el valor del “Authorization”
header vendrán strings cualesquiera.

# Guidelines:
Usar cualquier versión de Jersey 2+ para hacer el web service

Usar IntelliJ Idea como IDE

Usar la serialización built-in de Jersey para contestar en JSON y asegurarse que la
respuesta va con los headers correctos (Content-Type application/json)

Usar los Request Filters de Jersey 2 para hacer el rate limit.

Hacer el proyecto en formato maven (usando un archivo pom.xml para la gestión del
proyecto, el build y las dependencias)

# Correr la prueba

mvn clean package

mvn exec:java

curl -X GET http://localrization: opticks_auth_example1' -H 'Content-Type: application/json'
