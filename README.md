# Cashflow Microservice

Desenvolvido para prover um fluxo de caixa demandado pela empresa.

O sistema deve receber através de requisições POST Rest os lançamentos de entradas de débito e crédito.
Armazenar em banco de dados essas mesmas entradas e posteriormente retornar esses dados via API Rest.

Além desses requisitos, também deverá gerar um relatório acumulado diário desse fluxo de caixa.

É requisito não funcional um código limpo de fácil manutenção, além de cumprir com os requisitos de segurança (permissões, controle de acesso e conexão segura).

## Como rodar a aplicação

#### Pré-requisito
* Deverá ter instalado o docker e docker-compose

## Processo para levantar a aplicação

Faça o download dos dois projetos:
* act-platform https://github.com/gusatlan/act-cashflow/archive/refs/heads/main.zip;
* act-cashflow https://github.com/gusatlan/act-cashflow/archive/refs/heads/main.zip;

Crie uma pasta (act, por exemplo) e descompacte os dois projetos dentro da mesma.

### Para Linux:

    $ cd act-platform
    $ ./compile
    $ cd ../act-cashflow
    $ ./run

Dessa forma irá compilar a biblioteca act-platform utilizando o docker.
E em seguida, irá compilar o projeto do micro-serviço, criará a imagem do mesmo, criará a imagem do nginx e subirá a aplicação com seus serviços de dependência (Apache Cassandra, PostgreSQL, Keycloak, Kafka, Nginx)

### Para Windows:

    cd act-cashflow
    .\run.bat

Irá fazer o mesmo processo do Linux

## Testando a aplicação

A mesma tem suporte ao swagger: http://localhost:8080/swagger-ui.html

Existe no projeto scripts para teste utilizando o curl e fazendo as chamadas pelo nginx

* request_token - Pede a autenticação para o Keycloak;
* request_report - Pede o relatório em PDF do fluxo de caixa;

## Testando na IDE

Abra o projeto do act-cashflow na IDE de preferência.

No terminal, execute ./run_infra (Linux) ou .\run_infra.bat (Windows).


## Endpoints

* item-cashflow - https://localhost/item-cash-flow (SSL)
    * GET com filtros:
        * date (dia dos lançamentos yyyy-MM-dd);
        * begin(data/hora de início dos lançamentos yyyy-MM-dd HH:mm:ss);
        * end(data/hora de término dos lançamentos yyyy-MM-dd HH:mm:ss);
    * POST/PUT/DELETE - Realiza a inclusão/alteração/exclusão dos lançamentos, o payload:

~~~
        {
            "action": "UPSERT" /* UPSERT ou DELETE */,
            "entity": {
            "date": "2023-05-15T17:43:52.928Z",
            "type": "DEBIT" /* DEBIT ou CREDIT */,
            "value": 0.55 /*Valor decimal sempre positivo ou zero*/,
            "description": "string" /* Descrição do lançamento */
        }
~~~
    
* cashflow - https://localhost/cashflow (SSL)
    * GET com filtros:
        * begin (data/hora de início yyyy-MM-dd HH:mm:ss);
        * end (data/hora de término yyyy-MM-dd HH:mm:ss);

Esse endpoint retorna uma lista de fluxo de caixas:

~~~
[
  {
    "date": "2023-05-15",
    "total": 0,
    "items": [
      {
        "date": "2023-05-15T18:55:22.917Z",
        "type": "DEBIT",
        "value": 0,
        "description": "string"
      }
    ]
  }
]
~~~

* report - https://localhost/report (SSL)
    * POST com payload:

~~~
{
  "requestId": "string" /* Opcional */,
  "beginDate": "2023-05-15" /* Data inicial yyyy-MM-dd */,
  "endDate": "2023-05-15" /* Data final yyyy-MM-dd */
}
~~~

Payload de resposta:

~~~
{
  "request": {
    "requestId": "string",
    "beginDate": "2023-05-15",
    "endDate": "2023-05-15"
  },
  "content": "string" /* Relatório PDF encodado em Base64*/,
  "linkContent": "string" /*Link para o relatório*/
}
~~~

Esse endpoint gera o relatório de fluxo de caixa em formato PDF, é do período e não diário.


# Desenho da solução

Baseado em arquitetura de micro serviços.

* act-platform

Biblioteca de plataforma contendo classes model e utilitárias utilizadas pelo micro-serviço e podendo ser utilizada para novos projetos. A idéia é simular uma biblioteca de plataforma real, em que o código seja reaproveitado por diversos projetos.

* cashflow

O micro serviço em si, construído para atender as necessidades da empresa, realizará o lançamento das entradas de débito e crédito, registrando em banco de dados e criando relatório de fluxo de caixa consolidado.

## Padrões utilizados


### Banco de dados

Banco de dados por micro serviço, somente o micro serviço utilizará o banco de dados, isolando e evitando acesso de programas de terceiros na base de dados. Como banco de dados principal foi utilizado o *Apache Cassandra* por ter alta disponibilidade sem um ponto de falha único, além da escalabilidade do mesmo.

PostgreSQL é utilizado para montar o relatório de fluxo de caixa, também é um banco isolado somente acessado pela aplicação. O mesmo é populado somente em tempo de geração do relatório, sendo excluído os dados logo após o término do processo.

### Streaming - Mensageria

Sendo utilizado o *Apache Kafka* como serviço de streaming de dados. Ao chegar lançamentos de débito/crédito é adicionado a mensagem para o tópico e o consumer do mesmo se encarrega de salvar os dados no *Apache Cassandra*. Tratando assim do *back pressure* que a aplicação poderia sofrer com o aumento de uso.

### Conexão segura

Utilizando o *NGINX* como proxy reverso, além disso o mesmo cuida da criptografia dos dados (TLS, https).

### Relatório

Utilizado o *Jasper Studio* como ferramenta de desenho de relatório.

Através do endpoint */report*, é consultado a base de dados, filtrando os dados pelo período parametrizado, os objetos retornados então são mapeados para objetos entidade relacionamento (JPA) e gravados na base do PostgreSQL, é então gerado o relatório consumindo a base relacional, esses registros são excluídos após o término do processamento do relatório e finalmente é devolvido o relatório via REST.

### REST

Utilizado o modelo reativo, ganhando-se uma performance e escalabilidade melhores.

### Segurança e controle de acesso

É utilizado o *Keycloak* como serviço de permissão e controle de acesso, pode ser acessado em http://localhost:8089, o mesmo se encarrega do gerenciamento de usuários, roles, geração de token JWT

### Monitoramento e Métricas

Utilizando a biblioteca do *Spring actuators*, sendo possível verificar a saúde e métricas no endpoint http://localhost:8080/actuator

### Conteiners

Utilizando docker como serviço de conteiners, todos os serviços citados à cima são levantados com ele.

## Serviços utilizados

* Apache Kafka;
* Apache Cassandra;
* PostgreSQL;
* Keycloak;
* NGinx;

## Ferramentas utilizadas

* Jasper Studio;

## Bibliotecas utilizadas

* Java;
* Spring Boot;
* Spring Cloud Stream;
* Spring Data;
* Spring Security;
* Spring Reactive;
* Spring Doc (Swagger);
* Actuators;

## Design Patterns utilizados

* Builder;
