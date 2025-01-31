package com.FarmaciaFirebase.DAO;

import com.FarmaciaFirebase.Etidades.Funcionario;
import com.FarmaciaFirebase.Etidades.Remedio;
import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class FuncionarioDAO {
    private static final String COLLECTION_NAME = "funcionarios";

    public void adicionarFuncionario(Funcionario funcionario) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference docRef = db.collection(COLLECTION_NAME).document(funcionario.getCpf());

        ApiFuture<DocumentSnapshot> futureDoc = docRef.get();
        DocumentSnapshot document = futureDoc.get();

        if (document.exists()) {
            System.out.println("O CPF " + funcionario.getCpf() + " já está cadastrado!");
            return;
        }

        Map<String, Object> funcionarioData = new HashMap<>();
        funcionarioData.put("nome", funcionario.getNome());
        funcionarioData.put("cpf", funcionario.getCpf());
        funcionarioData.put("idade", funcionario.getIdade());
        funcionarioData.put("telefone", funcionario.getTelefone());
        funcionarioData.put("cargo", funcionario.getCargo());
        funcionarioData.put("salario", funcionario.getSalario());

        ApiFuture<WriteResult> future = docRef.set(funcionarioData);
        WriteResult result = future.get();
        System.out.println("Funcionário adicionado com sucesso em: " + result.getUpdateTime());
    }

    public void editarFuncionario(String nome) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference docRef = db.collection(COLLECTION_NAME).document(nome);
        Scanner scanner = new Scanner(System.in);

        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();

        if (!document.exists()) {
            System.out.println("Funcionário não encontrado.");
            return;
        }
        Map<String, Object> funcionarioData = document.getData();

        System.out.println("\n=== EDITAR Funcionario: " + nome + " ===");
        System.out.println("1 - Nome: " + funcionarioData.get("nome"));
        System.out.println("2 - CPF: " + funcionarioData.get("cpf"));
        System.out.println("3 - Idade: " + funcionarioData.get("idade"));
        System.out.println("4 - Telefone: " + funcionarioData.get("telefone"));
        System.out.println("5 - Cargo: " + funcionarioData.get("cargo"));
        System.out.println("6 - Salario: " + funcionarioData.get("salario"));
        System.out.println("0 - Cancelar");

        Map<String, Object> novosDados = new HashMap<>(funcionarioData);

        System.out.print("\nEscolha o número do atributo que deseja modificar: ");
        int opcao = scanner.nextInt();
        scanner.nextLine();

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
                scanner.nextLine();
                break;
            case 5:
                System.out.print("Novo Cargo ");
                novosDados.put("necessitaReceita", scanner.nextLine());
                scanner.nextLine();
                break;
            case 6:
                System.out.print("Novo Salário: ");
                novosDados.put("validade", scanner.nextDouble());
                break;
            case 0:
                System.out.println(" Edição cancelada.");
                return;
            default:
                System.out.println(" Opção inválida.");
                return;
        }

        ApiFuture<WriteResult> futureUpdate = docRef.set(novosDados);
        System.out.println(" Funcionário atualizado com sucesso em: " + futureUpdate.get().getUpdateTime());
    }

    public List<Funcionario> listarFuncionario() throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference remediosRef = db.collection(COLLECTION_NAME);
        ApiFuture<QuerySnapshot> future = remediosRef.get();

        List<Funcionario> listaFuncionarios = new ArrayList<>();
        for (DocumentSnapshot document : future.get().getDocuments()) {
            listaFuncionarios.add(document.toObject(Funcionario.class));
        }

        return listaFuncionarios;

    }

    public void exclurFuncionario(String nome) {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference docRef = db.collection(COLLECTION_NAME).document(nome);

        try {
            ApiFuture<DocumentSnapshot> futureDoc = docRef.get();
            DocumentSnapshot document = futureDoc.get();

            if (!document.exists()) {
                System.out.println("O funcionario '" + nome + "' não existe no banco de dados.");
                return;
            }

            ApiFuture<WriteResult> futureDelete = docRef.delete();
            System.out.println("Funcionario '" + nome + "' excluído com sucesso em: " + futureDelete.get().getUpdateTime());

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

}
