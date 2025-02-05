package com.FarmaciaFirebase;

import com.FarmaciaFirebase.Conexao.FirebaseConfig;
import com.FarmaciaFirebase.DAO.ClienteDAO;
import com.FarmaciaFirebase.DAO.FuncionarioDAO;
import com.FarmaciaFirebase.DAO.RemedioDAO;
import com.FarmaciaFirebase.Etidades.Cliente;
import com.FarmaciaFirebase.Etidades.Funcionario;
import com.FarmaciaFirebase.Etidades.Remedio;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class Main {
    static Scanner scanner = new Scanner(System.in);
    static ArrayList<Remedio> remedios = new ArrayList<>();
    static ArrayList<Cliente> clientes = new ArrayList<>();
    static ArrayList<Funcionario> funcionarios = new ArrayList<>();
    static RemedioDAO remedioDAO = new RemedioDAO();
    static ClienteDAO clienteDAO = new ClienteDAO();
    static FuncionarioDAO funcionarioDAO = new FuncionarioDAO();

    public static void main(String[] args) {
        int opcao;
        FirebaseConfig firebaseConfig = new FirebaseConfig();
        firebaseConfig.initializeFirebase();

        do {
            System.out.println("\n==== MENU PRINCIPAL ====");
            System.out.println("1 - Gerenciar Remédios");
            System.out.println("2 - Gerenciar Clientes");
            System.out.println("3 - Gerenciar Funcionários");
            System.out.println("0 - Sair");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine(); // Limpar buffer

            switch (opcao) {
                case 1:
                    menuRemedios();
                    break;
                case 2:
                    menuClientes();
                    break;
                case 3:
                    menuFuncionarios();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        } while (opcao != 0);
    }

    // ================== MENU REMÉDIOS ==================
    public static void menuRemedios() {
        int opcao;
        do {
            System.out.println("\n==== MENU REMÉDIOS ====");
            System.out.println("1 - Adicionar Remédio");
            System.out.println("2 - Editar Remédio");
            System.out.println("3 - Listar Remédios");
            System.out.println("4 - Excluir Remédio");
            System.out.println("0 - Voltar");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine(); // Limpar buffer

            switch (opcao) {
                case 1:
                    adicionarRemedio();
                    break;
                case 2:
                    editarRemedio();
                    break;
                case 3:
                    listarRemedios();
                    break;
                case 4:
                    excluirRemedio();
                    break;
                case 0:
                    System.out.println("Voltando...");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        } while (opcao != 0);
    }

    private static void adicionarRemedio() {
        System.out.println("\n========");

        try {
            System.out.print("Nome: ");
            String nome = scanner.nextLine();

            System.out.print("Laboratorio: ");
            String laboratorio = scanner.nextLine();

            System.out.print("Preço: R$");
            double preco = scanner.nextDouble();

            System.out.print("Quantidade em estoque: ");
            int quantidadeEstoque = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Precisa de receita (sim ou nao): ");
            String receitaString = scanner.nextLine();

            boolean receita = (receitaString == "sim") ? true : false;

            System.out.print("\nValidade (DD/MM/YYYY): ");
            String validadestr = scanner.nextLine();

            SimpleDateFormat validadeFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date validade = validadeFormat.parse(validadestr);


            Remedio remedio = new Remedio(nome, laboratorio, preco, quantidadeEstoque, receita, validade);
            remedioDAO.adicionarRemedio(remedio);

        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private static void editarRemedio() {
        try {
            System.out.println("\n========");
            System.out.print("\nDigite o nome do remedio que deseja editar: ");
            String nome = scanner.nextLine();

            remedioDAO.editarRemedio(nome);

        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void listarRemedios() {
        try {
            List<Remedio> listRemedio = remedioDAO.listarRemedios();

            if (listRemedio.isEmpty()) {
                System.out.println("Nenhum remédio encontrado.");
                return;
            }

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            for (Remedio remedio : listRemedio) {
                System.out.println("\n========");
                System.out.println("Nome: " + remedio.getNome());
                System.out.println("Laboratório: " + remedio.getLaboratorio());
                System.out.println("Preço: R$" + remedio.getPreco());
                System.out.println("Quantidade no estoque: " + remedio.getQuantidadeEmEstoque());
                System.out.println("Precisa de Receita: " + remedio.isNecessitaReceita());

                if (remedio.getValidade() != null) {
                    System.out.println("Data de validade: " + sdf.format(remedio.getValidade()));
                } else {
                    System.out.println("Data de validade: N/A");
                }

                System.out.println("========");

            }
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void excluirRemedio() {
        System.out.println("\n========");
        System.out.print("\nDigite o remedio que deseja excluir: ");
        String nome = scanner.nextLine();

        remedioDAO.excluirRemedio(nome);
    }

     // ================== MENU CLIENTE ==================
    public static void menuClientes() {
        int opcao;
        do {
            System.out.println("\n==== MENU CLIENTE ====");
            System.out.println("1 - Adicionar Cliente");
            System.out.println("2 - Editar Cliente");
            System.out.println("3 - Listar Cliente");
            System.out.println("4 - Excluir Cliente");
            System.out.println("0 - Voltar");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine(); // Limpar buffer

            switch (opcao) {
                case 1: adicionarCliente(); break;
                case 2: editarCliente(); break;
                case 3: listarCliente(); break;
                case 4: excluirCliente(); break;
                case 0: System.out.println("Voltando..."); break;
                default: System.out.println("Opção inválida!");
            }
        } while (opcao != 0);
    }

    private static void editarCliente() {
        ClienteDAO clienteDAO = new ClienteDAO();
        Scanner scanner = new Scanner(System.in);
    
        System.out.print("Qual o CPF do cliente que deseja alterar? ");
        String cpf = scanner.nextLine();
    
        try {
            clienteDAO.editarCliente(cpf);
        } catch (ExecutionException | InterruptedException e) {
            System.out.println("Erro ao editar cliente: " + e.getMessage());
            e.printStackTrace(); // Opcional: Mostra detalhes do erro
        }
    }
    

    private static void adicionarCliente() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n========");
    
        try {
            System.out.print("Nome: ");
            String nome = scanner.nextLine();
    
            System.out.print("CPF: ");
            String cpf = scanner.nextLine();
    
            System.out.print("Idade: ");
            int idade = scanner.nextInt();
            scanner.nextLine(); // Consumir quebra de linha
    
            System.out.print("Telefone: ");
            String telefone = scanner.nextLine();
    
            System.out.print("Possui plano de saúde? (S/N): ");
            String respostaPlano = scanner.nextLine().trim().toUpperCase();
            boolean possuiPlano = respostaPlano.equals("S");
    
            
            Cliente cliente = new Cliente(nome, cpf, idade, telefone, possuiPlano);
            clienteDAO.adicionarcliente(cliente);
    
            System.out.println("Cliente adicionado com sucesso!");
    
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void listarCliente() {

        try {
            List<Cliente> listClientes = clienteDAO.listarClientes();
    
            if (listClientes.isEmpty()) {
                System.out.println("Nenhum cliente encontrado.");
                return;
            }
    
            for (Cliente cliente : listClientes) {
                System.out.println("\n========");
                System.out.println("Nome: " + cliente.getNome());
                System.out.println("CPF: " + cliente.getCpf());
                System.out.println("Idade: " + cliente.getIdade());
                System.out.println("Telefone: " + cliente.getTelefone());
                System.out.println("Plano de Saúde: " + (cliente.isPlanoSaude() ? "Sim" : "Não"));
                System.out.println("========");
            }
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void excluirCliente() {
        ClienteDAO clienteDAO = new ClienteDAO();
        Scanner scanner = new Scanner(System.in);
    
        System.out.print("Digite o CPF do cliente que deseja excluir: ");
        String cpf = scanner.nextLine();
    
        try {
            clienteDAO.excluirCliente(cpf);
        } catch (ExecutionException | InterruptedException e) {
            System.out.println("Erro ao excluir cliente: " + e.getMessage());
        }
    }
    

    // ================== MENU FUNCIONARIO ==================
    public static void menuFuncionarios() {
        int opcao;
        do {
            System.out.println("\n==== MENU FUNCIONARIO ====");
            System.out.println("1 - Adicionar Funcionario");
            System.out.println("2 - Editar Funcionario");
            System.out.println("3 - Listar Funcionario");
            System.out.println("4 - Excluir Funcionario");
            System.out.println("0 - Voltar");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine(); // Limpar buffer

            switch (opcao) {
                case 1: adicionarFuncionario(); break;
                case 2: editarFuncionario(); break;
                case 3: listarFuncionario(); break;
                case 4: excluirFuncionario(); break;
                case 0: System.out.println("Voltando..."); break;
                default: System.out.println("Opção inválida!");
            }
        } while (opcao != 0);
    }

    private static void adicionarFuncionario() {

        System.out.println("\n========");

        try {
            System.out.print("Nome: ");
            String nome = scanner.nextLine();

            System.out.print("CPF: ");
            String cpf = scanner.nextLine();

            System.out.print("Idade: ");
            int idade = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Telefone: ");
            String telefone = scanner.nextLine();

            System.out.print("Cargo: ");
            String cargo = scanner.nextLine();

            System.out.print("Salario: R$");
            Double salario = scanner.nextDouble();

            Funcionario funcionario = new Funcionario(nome, cpf, idade, telefone, cargo, salario);
            funcionarioDAO.adicionarFuncionario(funcionario);

        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void editarFuncionario() {
    }

    private static void listarFuncionario() {
        try {
            List<Funcionario> listFuncionario = funcionarioDAO.listarFuncionario();

            if (listFuncionario.isEmpty()) {
                System.out.println("Nenhum funcionario encontrado.");
                return;
            }

            for (Funcionario funcionario : listFuncionario) {
                System.out.println("\n========");
                System.out.println("Nome: " + funcionario.getNome());
                System.out.println("CPF: " + funcionario.getCpf());
                System.out.println("Idade: " + funcionario.getIdade());
                System.out.println("Telefone: " + funcionario.getTelefone());
                System.out.println("Cargo: " + funcionario.getCargo());
                System.out.println("Salario: R$" + funcionario.getSalario());
                System.out.println("========");

            }
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void excluirFuncionario(){

    }
}
