## Sonar

```shell
mvn clean verify sonar:sonar  
```

### + Cobertura

```shell
mvn clean verify  sonar:sonar -Dsonar.token=sqa_ec859168ecfffdda087407ae4720d4d951c56c90   -Pcoverage
```