# Farmacia-Firebase

Este projeto é um **sistema de farmácia** que utiliza o **Firebase Firestore** como banco de dados. Ele permite **cadastrar, listar, editar e excluir** remédios, clientes e funcionários, simulando o funcionamento de um software de farmácia.

---

## 1. Funcionalidades

- **CRUD de Remédios**: incluir, listar, editar e excluir medicamentos.
- **CRUD de Clientes**: gerenciar dados de clientes.
- **CRUD de Funcionários**: gerenciar dados de funcionários.
- **Integração com o Firebase** para armazenar e recuperar dados em tempo real.

---

## 2. Pré-Requisitos

1. **Java 8+** (versão utilizada neste projeto).
2. **Maven** para gerenciar dependências.

Caso não tenha o Maven instalado, veja abaixo um pequeno tutorial de como instalar no **Windows** via terminal.

---

## 3. Como Instalar o Maven (Windows via Terminal)

1. **Abra o Prompt de Comando (cmd) ou PowerShell como Administrador.**  
2. **Instale o Chocolatey** (gerenciador de pacotes para Windows), se ainda não tiver:
    ```powershell
    Set-ExecutionPolicy Bypass -Scope Process -Force
    [System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072
    iex ((New-Object System.Net.WebClient).DownloadString('https://community.chocolatey.org/install.ps1'))
   
3. **Instale o Maven:**
    ```powershell
    choco install maven -y
  
4. **Verifique se o Maven foi instalado corretamente:**
     ```powershell
     mvn -version
     ```
     Se aparecer algo como Apache Maven 3.x.x, o Maven está pronto!

---

## 4. Clonando o Projeto e Instalando Dependências
 1. **Clone o repositório no seu computador:**
      ```bash
      git clone https://github.com/MOR4Xx/Farmacia-Firebase.git
      cd Farmacia-Firebase
 
 2. **Instale as dependências via Maven:**
      ```bash
      mvn clean install
      ```
      Isso vai baixar todas as bibliotecas necessárias para rodar o projeto (incluindo o Firebase Admin SDK).

---

## 5. Estrutura do Projeto
```terminal
Farmacia-Firebase
├── src
│   ├── main
│   │   └── java
│   │       └── com.FarmaciaFirebase
│   │           ├── DAO
│   │           │   ├── ClienteDAO.java
│   │           │   ├── FuncionarioDAO.java
│   │           │   ├── RemedioDAO.java
│   │           │   └── ...
│   │           ├── Entidades
│   │           │   ├── Cliente.java
│   │           │   ├── Funcionario.java
│   │           │   ├── Pessoa.java
│   │           │   └── Remedio.java
│   │           ├── Conexao
│   │           │   └── FirebaseConfig.java
│   │           └── Main.java
├── pom.xml
└── ...
```

### Principais Classes

- **`FirebaseConfig.java`**  
  Responsável por **configurar** a conexão com o Firebase (carrega a `serviceAccountKey.json` e inicializa o Firestore).

- **`RemedioDAO.java`, `FuncionarioDAO.java`, `ClienteDAO.java`**  
  **DAOs** (Data Access Objects) que gerenciam a persistência dos dados no Firestore, contendo métodos de **criar, listar, editar, excluir**.

- **`Remedio.java`, `Cliente.java`, `Funcionario.java`, `Pessoa.java`**  
  **Entidades** que representam o modelo de dados da farmácia. Ex:  
  - `Remedio` tem atributos como **nome, laboratório, preço, validade**, etc.  
  - `Cliente` e `Funcionario` herdam de `Pessoa`.

- **`Main.java`**  
  Classe principal com um **menu** que permite gerenciar remédios, clientes e funcionários, chamando métodos dos DAOs para executar operações de CRUD no Firestore.

---

## 6. Como Executar

1. **Configure o arquivo `serviceAccountKey.json`**  
   - Faça o download do arquivo JSON de credenciais no console do Firebase e coloque-o na raiz do projeto (ou onde o `FirebaseConfig.java` espera).  
   - Verifique se o **caminho** está correto no `FirebaseConfig`.

2. **Execute a aplicação** via Maven:
   ```bash
   mvn clean compile exec:java -Dexec.mainClass="com.FarmaciaFirebase.Main"
   ```
   Ou, se preferir, abra o projeto em uma IDE (IntelliJ, Eclipse, VS Code etc.) e rode a classe Main.java.

   1. **Interaja com o Menu**

   2. **Escolha Remédios, Clientes ou Funcionários.**

   3. **Siga as instruções para adicionar, listar, editar ou excluir itens no banco.**
  
---

## 7. Contato

Caso tenha dúvidas, sugestões ou queira contribuir para o projeto, entre em contato com os desenvolvedores através dos seguintes meios:

Jorge Afonso
- **GitHub**: [@MOR4Xx](https://github.com/MOR4Xx)
- **E-mail**: `jarabelo68@gmail.com`
- **LinkedIn**: [Jorge Afonso]([https://www.linkedin.com/in/seu-perfil/](https://www.linkedin.com/in/jorge-afonso-rabelo-de-araujo-598088239/))
      
Leandro Rosa
- **GitHub**: [@LDRRosa](https://github.com/LDRRosa)
- **E-mail**: `leandro.rosa.prof@gmail.com`
- **LinkedIn**: [Leandro Rosa]([https://www.linkedin.com/in/seu-perfil/](https://www.linkedin.com/in/leandro-rosa-da-silva-684809276/))
