package com.FarmaciaFirebase.DAO;

import com.FarmaciaFirebase.Etidades.Funcionario;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;

import java.util.*;
import java.util.concurrent.ExecutionException;

public class FuncionarioDAO {
    private static final String COLLECTION_NAME = "funcionarios";

    // Adicionar funcionario
    // Método para adicionar um funcionário ao Firestore
    public void adicionarFuncionario(Funcionario funcionario) throws ExecutionException, InterruptedException {

        // Obtém a instância do Firestore
        Firestore db = FirestoreClient.getFirestore();

        // Referência ao documento que será criado/atualizado (o CPF será usado como ID)
        DocumentReference docRef = db.collection(COLLECTION_NAME).document(funcionario.getCpf());

        // Verifica se já existe um funcionário com o mesmo CPF
        ApiFuture<DocumentSnapshot> futureDoc = docRef.get();
        DocumentSnapshot document = futureDoc.get();

        // Se o documento já existir, exibe uma mensagem e encerra o método
        if (document.exists()) {
            System.out.println("O CPF " + funcionario.getCpf() + " já está cadastrado!");
            return; // Sai do método, impedindo a duplicação
        }

        /* Cria um mapa para armazenar os dados do funcionário antes de enviá-los ao Firestore,
        map é usado para ficar parecido com o formato JSON usado pelo firebase */
        Map<String, Object> funcionarioData = new HashMap<>();
        funcionarioData.put("nome", funcionario.getNome());
        funcionarioData.put("cpf", funcionario.getCpf());
        funcionarioData.put("idade", funcionario.getIdade());
        funcionarioData.put("telefone", funcionario.getTelefone());
        funcionarioData.put("cargo", funcionario.getCargo());
        funcionarioData.put("salario", funcionario.getSalario());

        // Envia os dados para o Firestore (criando um novo documento caso não exista)
        ApiFuture<WriteResult> future = docRef.set(funcionarioData);

        // Aguarda a conclusão da operação e obtém a data/hora da atualização no banco
        WriteResult result = future.get();

        // Exibe uma mensagem indicando que o funcionário foi adicionado com sucesso
        System.out.println("Funcionário adicionado com sucesso em: " + result.getUpdateTime());
    }

    // Editar funcionario
    public void editarFuncionario(String cpf) throws ExecutionException, InterruptedException {

        // Obtém a instância do Firestore
        Firestore db = FirestoreClient.getFirestore();

        // Referência ao documento do funcionário com o CPF fornecido
        DocumentReference docRef = db.collection(COLLECTION_NAME).document(cpf);

        // Scanner para capturar entrada do usuário no terminal
        Scanner scanner = new Scanner(System.in);

        // Busca o documento no Firestore
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();

        // Se o funcionário não existir, exibe mensagem e encerra o método
        if (!document.exists()) {
            System.out.println("Funcionário não encontrado.");
            return;
        }

        // Obtém os dados atuais do funcionário
        Map<String, Object> funcionarioData = document.getData();

        // Exibe os dados do funcionário antes da edição
        System.out.println("\n=== EDITAR FUNCIONÁRIO: " + funcionarioData.get("nome") + " ===");
        System.out.println("1 - Nome: " + funcionarioData.get("nome"));
        System.out.println("2 - CPF: " + funcionarioData.get("cpf"));
        System.out.println("3 - Idade: " + funcionarioData.get("idade"));
        System.out.println("4 - Telefone: " + funcionarioData.get("telefone"));
        System.out.println("5 - Cargo: " + funcionarioData.get("cargo"));
        System.out.println("6 - Salário: " + funcionarioData.get("salario"));
        System.out.println("0 - Cancelar");

        // Cria um novo mapa para armazenar os dados atualizados
        Map<String, Object> novosDados = new HashMap<>(funcionarioData);

        // Solicita ao usuário qual atributo deseja modificar
        System.out.print("\nEscolha o número do atributo que deseja modificar: ");
        int opcao = scanner.nextInt();
        scanner.nextLine(); // Consumir a quebra de linha após o número

        switch (opcao) {
            case 1:
                // Modifica o nome do funcionário
                System.out.print("Novo Nome: ");
                novosDados.put("nome", scanner.nextLine());
                break;
            case 2:
                // Modifica o CPF (o ideal seria evitar isso, pois o CPF é a chave primária)
                System.out.print("Novo CPF: ");
                novosDados.put("cpf", scanner.nextLine());
                break;
            case 3:
                // Modifica a idade
                System.out.print("Nova Idade: ");
                novosDados.put("idade", scanner.nextInt());
                scanner.nextLine(); // Consumir quebra de linha
                break;
            case 4:
                // Modifica o telefone
                System.out.print("Novo Telefone: ");
                novosDados.put("telefone", scanner.nextLine());
                break;
            case 5:
                // Modifica o cargo
                System.out.print("Novo Cargo: ");
                novosDados.put("cargo", scanner.nextLine());
                break;
            case 6:
                // Modifica o salário
                System.out.print("Novo Salário: ");
                novosDados.put("salario", scanner.nextDouble());
                scanner.nextLine(); // Consumir quebra de linha
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

        // Confirmação da atualização com a data/hora de alteração
        System.out.println("Funcionário atualizado com sucesso em: " + futureUpdate.get().getUpdateTime());
    }

    // Editar funcionario
    public List<Funcionario> listarFuncionario() throws ExecutionException, InterruptedException {
        // Obtém a instância do Firestore
        Firestore db = FirestoreClient.getFirestore();

        // Referência à coleção de funcionários
        CollectionReference remediosRef = db.collection(COLLECTION_NAME);

        // Obtém os documentos da coleção de forma assíncrona
        ApiFuture<QuerySnapshot> future = remediosRef.get();

        // Lista que armazenará os funcionários recuperados do banco de dados
        List<Funcionario> listaFuncionarios = new ArrayList<>();

        // Percorre todos os documentos retornados e os converte para objetos Funcionario
        for (DocumentSnapshot document : future.get().getDocuments()) {
            listaFuncionarios.add(document.toObject(Funcionario.class));
        }

        // Retorna a lista de funcionários recuperados
        return listaFuncionarios;
    }

    // Excluir Funcionario
    public void exclurFuncionario(String cpf) {
        // Obtém a instância do Firestore
        Firestore db = FirestoreClient.getFirestore();

        // Referência ao documento do funcionário com base no CPF fornecido
        DocumentReference docRef = db.collection(COLLECTION_NAME).document(cpf);

        try {
            // Obtém o documento do funcionário de forma assíncrona
            ApiFuture<DocumentSnapshot> futureDoc = docRef.get();
            DocumentSnapshot document = futureDoc.get();

            // Verifica se o documento existe no banco de dados
            if (!document.exists()) {
                System.out.println("O funcionario '" + cpf + "' não existe no banco de dados.");
                return; // Encerra o método caso o funcionário não seja encontrado
            }

            // Se o funcionário existir, procede com a exclusão do documento
            ApiFuture<WriteResult> futureDelete = docRef.delete();

            // Exibe a confirmação da exclusão com a data/hora da operação
            System.out.println("Funcionario '" + cpf + "' excluído com sucesso em: " + futureDelete.get().getUpdateTime());

        } catch (InterruptedException e) {
            // Lança uma exceção caso ocorra uma interrupção durante a execução
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            // Lança uma exceção caso ocorra um erro na execução da operação
            throw new RuntimeException(e);
        }
    }

}
