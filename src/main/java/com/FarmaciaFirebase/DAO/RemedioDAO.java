package com.FarmaciaFirebase.DAO;

import com.FarmaciaFirebase.Etidades.Remedio;
import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class RemedioDAO {
    private static final String COLLECTION_NAME = "remedios"; // Nome da coleção no Firestore

    // Adicionar remedio
    public void adicionarRemedio(Remedio remedio) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference docRef = db.collection(COLLECTION_NAME).document(remedio.getNome());

        Map<String, Object> remedioData = new HashMap<>();
        remedioData.put("nome", remedio.getNome());
        remedioData.put("laboratorio", remedio.getLaboratorio());
        remedioData.put("preco", remedio.getPreco());
        remedioData.put("quantidadeEmEstoque", remedio.getQuantidadeEmEstoque());
        remedioData.put("necessitaReceita", remedio.isNecessitaReceita());
        remedioData.put("validade", remedio.getValidade());

        ApiFuture<WriteResult> future = docRef.set(remedioData);
        WriteResult result = future.get();
        System.out.println("Remédio adicionado com sucesso em: " + result.getUpdateTime());
    }

    //Editar remedio
    public void editarRemedio(String nome) throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference docRef = db.collection(COLLECTION_NAME).document(nome);
        Scanner scanner = new Scanner(System.in);

        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();

        if (!document.exists()) {
            System.out.println("Remédio não encontrado.");
            return;
        }
        Map<String, Object> remedioData = document.getData();
        String validadeFormatada = "N/A";

        if (remedioData.get("validade") instanceof Timestamp) {
            Timestamp validadeTimestamp = (Timestamp) remedioData.get("validade");
            Date validadeDate = validadeTimestamp.toDate();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            validadeFormatada = sdf.format(validadeDate);
        }

        System.out.println("\n=== EDITAR REMÉDIO: " + nome + " ===");
        System.out.println("1 - Nome: " + remedioData.get("nome"));
        System.out.println("2 - Laboratório: " + remedioData.get("laboratorio"));
        System.out.println("3 - Preço: R$" + remedioData.get("preco"));
        System.out.println("4 - Quantidade em Estoque: " + remedioData.get("quantidadeEmEstoque"));
        System.out.println("5 - Necessita Receita? " + remedioData.get("necessitaReceita"));
        System.out.println("6 - Data de Validade: " + validadeFormatada);
        System.out.println("0 - Cancelar");

        Map<String, Object> novosDados = new HashMap<>(remedioData);

        System.out.print("\nEscolha o número do atributo que deseja modificar: ");
        int opcao = scanner.nextInt();
        scanner.nextLine();

        switch (opcao) {
            case 1:
                System.out.print("Novo Nome: ");
                novosDados.put("nome", scanner.nextLine());
                break;
            case 2:
                System.out.print("Novo Laboratório: ");
                novosDados.put("laboratorio", scanner.nextLine());
                break;
            case 3:
                System.out.print("Novo Preço: ");
                novosDados.put("preco", scanner.nextDouble());
                scanner.nextLine();
                break;
            case 4:
                System.out.print("Nova Quantidade em Estoque: ");
                novosDados.put("quantidadeEmEstoque", scanner.nextInt());
                scanner.nextLine();
                break;
            case 5:
                System.out.print("Necessita Receita? (S/N): ");
                String receitaString = scanner.nextLine().trim().toUpperCase();
                boolean receita = (receitaString == "S") ? true : false;
                novosDados.put("necessitaReceita", receita);
                scanner.nextLine();
                break;
            case 6:
                System.out.print("Nova Data de Validade (dd/MM/yyyy): ");
                novosDados.put("validade", scanner.nextLine());
                break;
            case 0:
                System.out.println(" Edição cancelada.");
                return;
            default:
                System.out.println(" Opção inválida.");
                return;
        }

        ApiFuture<WriteResult> futureUpdate = docRef.set(novosDados);
        System.out.println(" Remédio atualizado com sucesso em: " + futureUpdate.get().getUpdateTime());
    }

    //listar remedio
    public List<Remedio> listarRemedios() throws ExecutionException, InterruptedException {
        Firestore db = FirestoreClient.getFirestore();
        CollectionReference remediosRef = db.collection(COLLECTION_NAME);
        ApiFuture<QuerySnapshot> future = remediosRef.get();

        List<Remedio> listaRemedios = new ArrayList<>();
        for (DocumentSnapshot document : future.get().getDocuments()) {
            listaRemedios.add(document.toObject(Remedio.class));
        }

        return listaRemedios;
    }

    //Excluir remedio
    public void excluirRemedio(String nome) {
        Firestore db = FirestoreClient.getFirestore();
        DocumentReference docRef = db.collection(COLLECTION_NAME).document(nome);

        try {
            ApiFuture<DocumentSnapshot> futureDoc = docRef.get();
            DocumentSnapshot document = futureDoc.get();

            if (!document.exists()) {
                System.out.println("O remédio '" + nome + "' não existe no banco de dados.");
                return;
            }

            ApiFuture<WriteResult> futureDelete = docRef.delete();
            System.out.println("Remédio '" + nome + "' excluído com sucesso em: " + futureDelete.get().getUpdateTime());

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}

