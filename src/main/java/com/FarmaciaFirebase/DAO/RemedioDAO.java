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
        // Obtém a instância do Firestore
        Firestore db = FirestoreClient.getFirestore();

        // Cria uma referência ao documento do remédio na coleção definida, utilizando o nome como identificador único
        DocumentReference docRef = db.collection(COLLECTION_NAME).document(remedio.getNome());

        // Cria um mapa para armazenar os dados do remédio antes de enviá-los ao Firestore
        Map<String, Object> remedioData = new HashMap<>();
        remedioData.put("nome", remedio.getNome()); // Nome do remédio
        remedioData.put("laboratorio", remedio.getLaboratorio()); // Laboratório fabricante
        remedioData.put("preco", remedio.getPreco()); // Preço do remédio
        remedioData.put("quantidadeEmEstoque", remedio.getQuantidadeEmEstoque()); // Quantidade disponível no estoque
        remedioData.put("necessitaReceita", remedio.isNecessitaReceita()); // Indica se é necessária uma receita médica
        remedioData.put("validade", remedio.getValidade()); // Data de validade do remédio

        // Envia os dados para o Firestore e aguarda a confirmação da operação
        ApiFuture<WriteResult> future = docRef.set(remedioData);
        WriteResult result = future.get();

        // Exibe uma mensagem de sucesso informando a data/hora da atualização no banco
        System.out.println("Remédio adicionado com sucesso em: " + result.getUpdateTime());
    }

    //Editar remedio
    public void editarRemedio(String nome) throws ExecutionException, InterruptedException {
        // Obtém a instância do Firestore
        Firestore db = FirestoreClient.getFirestore();

        // Referência ao documento do remédio na coleção definida, utilizando o nome como identificador único
        DocumentReference docRef = db.collection(COLLECTION_NAME).document(nome);

        // Scanner para capturar a entrada do usuário no terminal
        Scanner scanner = new Scanner(System.in);

        // Obtém o documento do remédio de forma assíncrona
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();

        // Verifica se o remédio existe no banco de dados
        if (!document.exists()) {
            System.out.println("Remédio não encontrado.");
            return;
        }

        // Obtém os dados do remédio do Firestore
        Map<String, Object> remedioData = document.getData();
        String validadeFormatada = "N/A"; // Define um valor padrão caso a validade não esteja no formato esperado

        // Converte o campo de validade, caso ele esteja armazenado como Timestamp
        if (remedioData.get("validade") instanceof Timestamp) {
            Timestamp validadeTimestamp = (Timestamp) remedioData.get("validade");
            Date validadeDate = validadeTimestamp.toDate();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            validadeFormatada = sdf.format(validadeDate);
        }

        // Exibe os dados do remédio antes da edição
        System.out.println("\n=== EDITAR REMÉDIO: " + nome + " ===");
        System.out.println("1 - Nome: " + remedioData.get("nome"));
        System.out.println("2 - Laboratório: " + remedioData.get("laboratorio"));
        System.out.println("3 - Preço: R$" + remedioData.get("preco"));
        System.out.println("4 - Quantidade em Estoque: " + remedioData.get("quantidadeEmEstoque"));
        System.out.println("5 - Necessita Receita? " + remedioData.get("necessitaReceita"));
        System.out.println("6 - Data de Validade: " + validadeFormatada);
        System.out.println("0 - Cancelar");

        // Cria um novo mapa para armazenar os dados atualizados
        Map<String, Object> novosDados = new HashMap<>(remedioData);

        // Solicita ao usuário qual atributo deseja modificar
        System.out.print("\nEscolha o número do atributo que deseja modificar: ");
        int opcao = scanner.nextInt();
        scanner.nextLine(); // Consome a quebra de linha após a entrada numérica

        switch (opcao) {
            case 1:
                // Modifica o nome do remédio
                System.out.print("Novo Nome: ");
                novosDados.put("nome", scanner.nextLine());
                break;
            case 2:
                // Modifica o laboratório
                System.out.print("Novo Laboratório: ");
                novosDados.put("laboratorio", scanner.nextLine());
                break;
            case 3:
                // Modifica o preço
                System.out.print("Novo Preço: ");
                novosDados.put("preco", scanner.nextDouble());
                scanner.nextLine(); // Consome a quebra de linha
                break;
            case 4:
                // Modifica a quantidade em estoque
                System.out.print("Nova Quantidade em Estoque: ");
                novosDados.put("quantidadeEmEstoque", scanner.nextInt());
                scanner.nextLine(); // Consome a quebra de linha
                break;
            case 5:
                // Modifica a necessidade de receita médica
                System.out.print("Necessita Receita? (S/N): ");
                String receitaString = scanner.nextLine().trim().toUpperCase();
                boolean receita = receitaString.equals("S"); // Converte a resposta para boolean
                novosDados.put("necessitaReceita", receita);
                break;
            case 6:
                // Modifica a data de validade
                System.out.print("Nova Data de Validade (dd/MM/yyyy): ");
                novosDados.put("validade", scanner.nextLine());
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
        System.out.println("Remédio atualizado com sucesso em: " + futureUpdate.get().getUpdateTime());
    }

    //listar remedio
    public List<Remedio> listarRemedios() throws ExecutionException, InterruptedException {
        // Obtém a instância do Firestore
        Firestore db = FirestoreClient.getFirestore();

        // Referência à coleção de remédios no Firestore
        CollectionReference remediosRef = db.collection(COLLECTION_NAME);

        // Obtém os documentos da coleção de forma assíncrona
        ApiFuture<QuerySnapshot> future = remediosRef.get();

        // Lista que armazenará os remédios recuperados do banco de dados
        List<Remedio> listaRemedios = new ArrayList<>();

        // Percorre todos os documentos retornados e os converte para objetos da classe Remedio
        for (DocumentSnapshot document : future.get().getDocuments()) {
            listaRemedios.add(document.toObject(Remedio.class));
        }

        // Retorna a lista de remédios recuperados do Firestore
        return listaRemedios;
    }

    //Excluir remedio
    public void excluirRemedio(String nome) {
        // Obtém a instância do Firestore
        Firestore db = FirestoreClient.getFirestore();

        // Referência ao documento do remédio na coleção definida, utilizando o nome como identificador único
        DocumentReference docRef = db.collection(COLLECTION_NAME).document(nome);

        try {
            // Obtém o documento do remédio de forma assíncrona
            ApiFuture<DocumentSnapshot> futureDoc = docRef.get();
            DocumentSnapshot document = futureDoc.get();

            // Verifica se o remédio existe no banco de dados
            if (!document.exists()) {
                System.out.println("O remédio '" + nome + "' não existe no banco de dados.");
                return; // Encerra o método caso o remédio não seja encontrado
            }

            // Se o remédio existir, procede com a exclusão do documento
            ApiFuture<WriteResult> futureDelete = docRef.delete();

            // Exibe a confirmação da exclusão com a data/hora da operação
            System.out.println("Remédio '" + nome + "' excluído com sucesso em: " + futureDelete.get().getUpdateTime());

        } catch (InterruptedException e) {
            // Lança uma exceção caso ocorra uma interrupção durante a execução
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            // Lança uma exceção caso ocorra um erro na execução da operação
            throw new RuntimeException(e);
        }
    }
}

