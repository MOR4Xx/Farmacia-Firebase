package com.FarmaciaFirebase.DAO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

import com.FarmaciaFirebase.Etidades.Cliente;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;



public class ClienteDAO {
    private static final String COLLECTION_NAME = "clientes";


    public void adicionarcliente(Cliente cliente) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference docRef = db.collection(COLLECTION_NAME).document(cliente.getCpf());
    
        ApiFuture<DocumentSnapshot> futureDoc = docRef.get();
        DocumentSnapshot document = futureDoc.get();
    
        if (document.exists()) {
            System.out.println("O CPF " + cliente.getCpf() + " já está cadastrado!");
            return;
        }
    
        // Criando o mapa com os dados do cliente, incluindo o plano de saúde
        Map<String, Object> clienteData = new HashMap<>();
        clienteData.put("nome", cliente.getNome());
        clienteData.put("cpf", cliente.getCpf());
        clienteData.put("idade", cliente.getIdade());
        clienteData.put("telefone", cliente.getTelefone());
        clienteData.put("planoSaude", cliente.isPlanoSaude());

        ApiFuture<WriteResult> future = docRef.set(clienteData);
        WriteResult result = future.get();
        System.out.println("Cliente adicionado com sucesso em: " + result.getUpdateTime());
    }

    public List<Cliente> listarClientes() throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference clientesRef = db.collection("clientes"); // Substitua pelo nome correto da coleção
        ApiFuture<QuerySnapshot> future = clientesRef.get();
    
        List<Cliente> listaClientes = new ArrayList<>();
        for (DocumentSnapshot document : future.get().getDocuments()) {
            listaClientes.add(document.toObject(Cliente.class));
        }
    
        return listaClientes;
    }

    public void editarCliente(String cpf) throws ExecutionException, InterruptedException {
    Firestore db = FirestoreClient.getFirestore();
    DocumentReference docRef = db.collection("clientes").document(cpf); // Certifique-se que "clientes" é o nome correto da coleção no Firestore
    Scanner scanner = new Scanner(System.in);

    ApiFuture<DocumentSnapshot> future = docRef.get();
    DocumentSnapshot document = future.get();

    if (!document.exists()) {
        System.out.println("Cliente não encontrado.");
        return;
    }

    Map<String, Object> clienteData = document.getData();
    
    System.out.println("\n=== EDITAR Cliente: " + cpf + " ===");
    System.out.println("1 - Nome: " + clienteData.get("nome"));
    System.out.println("2 - CPF: " + clienteData.get("cpf"));
    System.out.println("3 - Idade: " + clienteData.get("idade"));
    System.out.println("4 - Telefone: " + clienteData.get("telefone"));
    System.out.println("5 - Plano de Saúde: " + (clienteData.get("planoSaude") != null && (boolean) clienteData.get("planoSaude") ? "Sim" : "Não"));
    System.out.println("0 - Cancelar");

    Map<String, Object> novosDados = new HashMap<>(clienteData);

    System.out.print("\nEscolha o número do atributo que deseja modificar: ");
    int opcao = scanner.nextInt();
    scanner.nextLine(); // Consumir a quebra de linha

    switch (opcao) {
        case 1:
            System.out.print("Novo Nome: ");
            novosDados.put("nome", scanner.nextLine());
            break;
        case 2:
            System.out.print("Novo CPF: ");
            novosDados.put("cpf", scanner.nextLine());
            break;
        case 3:
            System.out.print("Nova Idade: ");
            novosDados.put("idade", scanner.nextInt());
            scanner.nextLine();
            break;
        case 4:
            System.out.print("Novo Telefone: ");
            novosDados.put("telefone", scanner.nextLine());
            break;
        case 5:
            System.out.print("Possui Plano de Saúde? (S/N): ");
            String respostaPlano = scanner.nextLine().trim().toUpperCase();
            boolean possuiPlano = respostaPlano.equals("S");
            novosDados.put("planoSaude", possuiPlano);
            break;
        case 0:
            System.out.println("Edição cancelada.");
            return;
        default:
            System.out.println("Opção inválida.");
            return;
    }

    ApiFuture<WriteResult> futureUpdate = docRef.set(novosDados);
    System.out.println("Cliente atualizado com sucesso em: " + futureUpdate.get().getUpdateTime());
}

public void excluirCliente(String cpf) throws ExecutionException, InterruptedException {
    Firestore db = FirestoreClient.getFirestore();
    DocumentReference docRef = db.collection("clientes").document(cpf); // Certifique-se de que "clientes" é o nome correto da coleção

    ApiFuture<DocumentSnapshot> future = docRef.get();
    DocumentSnapshot document = future.get();

    if (!document.exists()) {
        System.out.println("Cliente com CPF " + cpf + " não encontrado.");
        return;
    }

    ApiFuture<WriteResult> futureDelete = docRef.delete();
    System.out.println("Cliente excluído com sucesso em: " + futureDelete.get().getUpdateTime());
}


    
    

}
