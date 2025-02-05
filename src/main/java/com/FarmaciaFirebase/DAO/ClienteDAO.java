package com.FarmaciaFirebase.DAO;

import com.FarmaciaFirebase.Etidades.Cliente;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;

import java.util.*;
import java.util.concurrent.ExecutionException;

public class ClienteDAO {
    private static final String COLLECTION_NAME = "clientes";

    // Adicionar CLiente
    public void adicionarcliente(Cliente cliente) throws ExecutionException, InterruptedException {
        // Obtém a instância do Firestore
        Firestore db = FirestoreClient.getFirestore();

        // Referência ao documento do cliente, utilizando o CPF como identificador único
        DocumentReference docRef = db.collection(COLLECTION_NAME).document(cliente.getCpf());

        // Verifica se já existe um cliente com o mesmo CPF no banco de dados
        ApiFuture<DocumentSnapshot> futureDoc = docRef.get();
        DocumentSnapshot document = futureDoc.get();

        // Se o documento já existir, exibe uma mensagem e encerra o método para evitar duplicação
        if (document.exists()) {
            System.out.println("O CPF " + cliente.getCpf() + " já está cadastrado!");
            return;
        }

        // Criando um mapa para armazenar os dados do cliente a serem inseridos no Firestore
        Map<String, Object> clienteData = new HashMap<>();
        clienteData.put("nome", cliente.getNome()); // Adiciona o nome do cliente
        clienteData.put("cpf", cliente.getCpf()); // Adiciona o CPF do cliente
        clienteData.put("idade", cliente.getIdade()); // Adiciona a idade do cliente
        clienteData.put("telefone", cliente.getTelefone()); // Adiciona o telefone do cliente
        clienteData.put("planoSaude", cliente.isPlanoSaude()); // Adiciona a informação sobre plano de saúde (true/false)

        // Envia os dados para o Firestore, criando um novo documento caso não exista
        ApiFuture<WriteResult> future = docRef.set(clienteData);

        // Aguarda a confirmação da operação e obtém a data/hora da atualização no banco
        WriteResult result = future.get();

        // Exibe uma mensagem de sucesso informando que o cliente foi cadastrado com sucesso
        System.out.println("Cliente adicionado com sucesso em: " + result.getUpdateTime());
    }

    // Listar Cliente
    public List<Cliente> listarClientes() throws ExecutionException, InterruptedException {
        // Obtém a instância do Firestore
        Firestore db = FirestoreClient.getFirestore();

        // Referência à coleção de clientes no Firestore
        CollectionReference clientesRef = db.collection("clientes"); // Substitua pelo nome correto da coleção caso necessário

        // Obtém os documentos da coleção de forma assíncrona
        ApiFuture<QuerySnapshot> future = clientesRef.get();

        // Lista que armazenará os clientes recuperados do banco de dados
        List<Cliente> listaClientes = new ArrayList<>();

        // Percorre todos os documentos retornados e os converte para objetos da classe Cliente
        for (DocumentSnapshot document : future.get().getDocuments()) {
            listaClientes.add(document.toObject(Cliente.class));
        }

        // Retorna a lista de clientes recuperados do Firestore
        return listaClientes;
    }

    // Editar Cliente
    public void editarCliente(String cpf) throws ExecutionException, InterruptedException {
        // Obtém a instância do Firestore
        Firestore db = FirestoreClient.getFirestore();

        // Referência ao documento do cliente na coleção "clientes" com base no CPF
        DocumentReference docRef = db.collection("clientes").document(cpf); // Certifique-se que "clientes" é o nome correto da coleção no Firestore

        // Scanner para capturar a entrada do usuário no terminal
        Scanner scanner = new Scanner(System.in);

        // Obtém o documento do cliente de forma assíncrona
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();

        // Verifica se o cliente existe no banco de dados
        if (!document.exists()) {
            System.out.println("Cliente não encontrado.");
            return;
        }

        // Obtém os dados do cliente do Firestore
        Map<String, Object> clienteData = document.getData();

        // Exibe os dados do cliente antes da edição
        System.out.println("\n=== EDITAR Cliente: " + cpf + " ===");
        System.out.println("1 - Nome: " + clienteData.get("nome"));
        System.out.println("2 - CPF: " + clienteData.get("cpf"));
        System.out.println("3 - Idade: " + clienteData.get("idade"));
        System.out.println("4 - Telefone: " + clienteData.get("telefone"));
        System.out.println("5 - Plano de Saúde: " + (clienteData.get("planoSaude") != null && (boolean) clienteData.get("planoSaude") ? "Sim" : "Não"));
        System.out.println("0 - Cancelar");

        // Cria um novo mapa para armazenar os dados atualizados
        Map<String, Object> novosDados = new HashMap<>(clienteData);

        // Solicita ao usuário qual atributo deseja modificar
        System.out.print("\nEscolha o número do atributo que deseja modificar: ");
        int opcao = scanner.nextInt();
        scanner.nextLine(); // Consome a quebra de linha após a entrada numérica

        switch (opcao) {
            case 1:
                // Modifica o nome do cliente
                System.out.print("Novo Nome: ");
                novosDados.put("nome", scanner.nextLine());
                break;
            case 2:
                // Modifica o CPF (não recomendado pois o CPF é a chave primária)
                System.out.print("Novo CPF: ");
                novosDados.put("cpf", scanner.nextLine());
                break;
            case 3:
                // Modifica a idade
                System.out.print("Nova Idade: ");
                novosDados.put("idade", scanner.nextInt());
                scanner.nextLine(); // Consome a quebra de linha
                break;
            case 4:
                // Modifica o telefone
                System.out.print("Novo Telefone: ");
                novosDados.put("telefone", scanner.nextLine());
                break;
            case 5:
                // Modifica a informação sobre o plano de saúde
                System.out.print("Possui Plano de Saúde? (S/N): ");
                String respostaPlano = scanner.nextLine().trim().toUpperCase();
                boolean possuiPlano = respostaPlano.equals("S"); // Converte a resposta para boolean
                novosDados.put("planoSaude", possuiPlano);
                break;
            case 0:
                // Cancela a edição
                System.out.println("Edição cancelada.");
                return;
            default:
                // Opção inválida
                System.out.println("Opção inválida.");
                return;
        }

        // Atualiza os dados no Firestore
        ApiFuture<WriteResult> futureUpdate = docRef.set(novosDados);

        // Exibe uma mensagem de sucesso informando a data/hora da atualização no banco
        System.out.println("Cliente atualizado com sucesso em: " + futureUpdate.get().getUpdateTime());
    }

    // Excluir Cliente
    public void excluirCliente(String cpf) throws ExecutionException, InterruptedException {
        // Obtém a instância do Firestore
        Firestore db = FirestoreClient.getFirestore();

        // Referência ao documento do cliente na coleção "clientes" com base no CPF
        DocumentReference docRef = db.collection("clientes").document(cpf);

        // Obtém o documento do cliente de forma assíncrona
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();

        // Verifica se o cliente existe no banco de dados
        if (!document.exists()) {
            System.out.println("Cliente com CPF " + cpf + " não encontrado.");
            return; // Encerra o método caso o cliente não seja encontrado
        }

        // Se o cliente existir, procede com a exclusão do documento
        ApiFuture<WriteResult> futureDelete = docRef.delete();

        // Exibe a confirmação da exclusão com a data/hora da operação
        System.out.println("Cliente excluído com sucesso em: " + futureDelete.get().getUpdateTime());
    }

}
