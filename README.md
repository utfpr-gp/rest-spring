# Demo - Rest Spring

É um projeto para demonstração de uma API REST com Spring Boot. 

## Principais Funcionalidades

- CRUD de uma entidade aluno no domínio de uma universidade.
  - Uso de DTOs
  - Uso de um objeto encapsulador da resposta para padronização - Response
  - Uso de diferentes formas de requisição e resposta em paginação
  - Validação de dados
  - Customização de Querys em Spring Data
  - Diferentes modos de invocação dos endpoints
  - Testes de repositório 
  - Testes de validação de DTOs
  - Tratamento genérico de exceptions

## Tecnologias

- Spring Boot (2.0.6)
- Spring Data
- Spring MVC
- Lombok
- Swagger

## Ferramentas

- STS
- Tomcat (Embutido)
- MySQL
- H2
- Maven

## Manual de Execução

- Clonar o repositório com `git clone`
- Entrar na raiz do projeto via prompt de comando e instalar as dependência definidas no arquivo `pom.xml` do Maven
  - Digite o comando `mvn clean install`
- Para usar o banco de dados MySQL
  - Criar um novo banco de dados com o nome `universidade_db`
  - Alterar o arquivo `application.properties` para uso do perfil `dev`
- Para usar o banco de dados H2
  - Alterar o arquivo `application.properties` para uso do perfil `test` (default)
- Executar o projeto como Spring Boot.

## Versão Corrente

0.0.1 - Release de 21/11/2018
