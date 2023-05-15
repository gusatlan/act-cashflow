## 2023-05-16

* Criação da camada de segurança usando JWT;


## 2023-05-15

* Ajustes nos scripts para uso no Windows;
* Criado infra (docker-compose-infra.yml) para usar em conjunto no debug com a IDE;
* Ajustes no application.yml para comportar a infra;
* Adicionado nginx como proxy reverso/ssl;
* Documentação do serviço;

## 2023-05-14

* Criação do Dockerfile e docker-compose.yml
  * Pacotes para renderizar o relatório em PDF;
  * Criação de certificado auto-assinado;
* Criação automática do keyspacename do Apache Cassandra
* Adicionado Keycloak como autenticador;

## 2023-05-13

* Implementado Rest retornando CashFlow


## 2023-05-12

* Implementado classes de persistência para Apache Cassandra e JPA PostgreSQL (Usado para montar o relatório);
* Implementado testes unitários;
* Implementado as interfaces Repository e as classes Service;
* Implementado Kafka Stream para persistir no Apache Cassandra, usando o spring-cloud-stream;

## 2023-05-11

Início do desenvolvimento do micro serviço

