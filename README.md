# Ejemplo Spring Boot + Reactor

Ejemplo de un proyecto Spring Boot reactivo

## Características principales

1. No usa `@Controller`. En su lugar se utiliza _Router_ y _Handlers_
2. No usa JDBC ni JPA. En su lugar se utiliza _R2DBC_
3. EL código está organizado con Arquitectura Hexagonal
4. La cobertura de las pruebas llega al 100%
5. Las pruebas unitarias utiliza la base de datos H2
6. En la prueba integral se utiliza MySQL + TestContainers