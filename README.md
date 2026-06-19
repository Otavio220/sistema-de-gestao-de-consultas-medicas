# Sistema de Gestão de Consultas Médicas

## 📌 Sobre o Projeto

O **Sistema de Gestão de Consultas Médicas** é uma aplicação backend desenvolvida em **Java** com **Spring Boot**, utilizando os princípios da Programação Orientada a Objetos (POO) e persistência de dados com **MySQL**.

O objetivo do sistema é simular um ambiente de gerenciamento de uma clínica ambulatorial, permitindo o cadastro de pacientes, agendamento, reagendamento e cancelamento de consultas médicas em diferentes especialidades, de forma organizada e segura.

O projeto foi desenvolvido como atividade da disciplina de Engenharia de Software, aplicando conceitos de modelagem, organização em camadas, reutilização de código e controle de versões com Git.

---

## ✅ Funcionalidades

O sistema oferece as seguintes funcionalidades:

- Cadastro e gerenciamento de pacientes;
- Cadastro e gerenciamento de médicos por especialidade;
- Agendamento, reagendamento e cancelamento de consultas;
- Atribuição de médico e consultório conforme especialidade e disponibilidade;
- Gerenciamento da agenda de consultórios;
- Registro de histórico clínico dos pacientes;
- Notificação de requisitos prévios à consulta;
- Controle de acesso por perfis de usuários.

---

## 👥 Perfis de Usuários

O sistema possui diferentes tipos de usuários, cada um representando um papel dentro do processo de gestão clínica:

- **Administrador** — Gerencia usuários, médicos, consultores e especialidades.
- **Médico** — Visualiza agenda, consultas e registra evoluções dos pacientes.
- **Recepcionista** — Agenda, reagenda e cancela consultas, além de cadastrar pacientes.
- **Paciente** — Tem seus dados e histórico clínico gerenciados pelo sistema.

---

## 🏥 Tipos de Consulta

O sistema gerencia consultas em diferentes especialidades, como:

- Clínica Geral;
- Pediatria;
- Odontologia;
- Laboratório.

Cada tipo de consulta possui duração própria, recursos associados (consultórios e equipamentos) e requisitos prévios distintos.

---

## 🗄️ Persistência de Dados

Os registros são armazenados em um banco de dados **MySQL**, permitindo que as informações permaneçam salvas mesmo após o encerramento da aplicação.

A camada de persistência utiliza **Spring Data JPA** e realiza automaticamente operações como:

- Inserção de registros;
- Busca por ID, status e outros filtros;
- Atualização de dados;
- Exclusão de registros.

---

## 🗂️ Estrutura do Projeto

```
clinica-api/
│
├── controller/
│   ├── AdministradorController.java
│   ├── AgendaController.java
│   ├── ConsultaController.java
│   ├── ConsultorioController.java
│   ├── EspecialidadeController.java
│   ├── HistoricoClinicoController.java
│   ├── MedicoController.java
│   ├── MedicoUsuarioController.java
│   ├── PacienteController.java
│   ├── RecepcionistaController.java
│   └── UsuarioController.java
│
├── dto/
│   ├── AgendaDTO.java
│   ├── ConsultaDTO.java
│   ├── ConsultorioDTO.java
│   ├── EspecialidadeDTO.java
│   ├── HistoricoClinicoDTO.java
│   ├── MedicoDTO.java
│   ├── PacienteDTO.java
│   └── UsuarioDTO.java
│
├── entity/
│   ├── Agenda.java
│   ├── Consulta.java
│   ├── Consultorio.java
│   ├── Especialidade.java
│   ├── Equipamento.java
│   ├── HistoricoClinico.java
│   ├── Medico.java
│   ├── Paciente.java
│   ├── StatusConsulta.java
│   └── Usuario.java
│
├── repository/
│   ├── AgendaRepository.java
│   ├── ConsultaRepository.java
│   ├── ConsultorioRepository.java
│   ├── EspecialidadeRepository.java
│   ├── EquipamentoRepository.java
│   ├── HistoricoClinicoRepository.java
│   ├── MedicoRepository.java
│   ├── PacienteRepository.java
│   └── UsuarioRepository.java
│
├── service/
│   ├── AdministradorService.java
│   ├── AgendaService.java
│   ├── ConsultaService.java
│   ├── ConsultorioService.java
│   ├── EspecialidadeService.java
│   ├── HistoricoClinicoService.java
│   ├── MedicoService.java
│   ├── MedicoUsuarioService.java
│   ├── PacienteService.java
│   ├── RecepcionistaService.java
│   └── UsuarioService.java
│
└── ClinicaApiApplication.java
```

---

## 🛠️ Tecnologias Utilizadas

- Java 17
- Spring Boot
- Spring Data JPA
- MySQL
- Programação Orientada a Objetos (POO)
- Git e GitHub
- Postman (testes de API)

---

## ▶️ Como Executar

Clone o repositório:

```bash
git clone https://github.com/Otavio220/sistema-de-gestao-de-consultas-medicas.git
```

Entre na pasta do projeto:

```bash
cd sistema-de-gestao-de-consultas-medicas/clinica-api/clinica-api
```

Configure o banco de dados no arquivo `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/clinica_db
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.jpa.hibernate.ddl-auto=update
```

Execute a aplicação:

```bash
./mvnw spring-boot:run
```

---

## 💡 Conceitos Aplicados

Durante o desenvolvimento foram utilizados conceitos importantes de Engenharia de Software e Programação Orientada a Objetos, como:

- Encapsulamento;
- Herança;
- Polimorfismo;
- Abstração;
- Modularização em camadas (Controller, Service, Repository, Entity, DTO);
- Separação de responsabilidades;
- Persistência de dados com MySQL via JPA;
- API RESTful;
- Controle de versão utilizando Git e GitHub.

---

## 👨‍💻 Autores

**Júlio César de Lucena** — Estudante de Engenharia de Software.

**Reilson Batista da Fonseca** — Estudante de Engenharia de Software.

**Otávio Fernandes Santos e Silva** — Estudante de Engenharia de Software.

Projeto desenvolvido para a disciplina de Engenharia de Software, com o objetivo de aplicar conceitos de orientação a objetos, persistência de dados e boas práticas de desenvolvimento de software.
