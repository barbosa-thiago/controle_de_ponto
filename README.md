### Controle de ponto

Sistema simples de controle de ponto

#### Rodando o sistema

A seguintes variáveis de ambiente são necessárias para subir a aplicação:

    DB_PASSWORD=<password-postgres>
    DB_USER=<postgres-user>
    DB_HOST=<postgres-host>
    DB_PORT=<postgres-port>
    DB_NAME=<nomedo-banco>
    JWT_SECRET=<assinatura-token>

A documentação do swagger estará acessível no endpoint:

http://localhost:8080/docs

#### Tecnologias

- Maven
- Spring boot
- Hibernate
- PostgreSQL
- Flyway
- Spring Security

#### Subindo aplicação com docker

Da raiz do projeto, digite o comando:

    docker-compose up

#### Testando a aplicação

Para fins de teste, foram adicionados 2 usuários no banco de dados:

    username: teste, password: teste

    username: teste2, password: teste2

Para adquirir o token de autenticação, o endpoint **/authenticate** aceita <br /> um método POST com o seguinte payload:

```json
{
  "username": "teste",
  "password": "teste"
}

```