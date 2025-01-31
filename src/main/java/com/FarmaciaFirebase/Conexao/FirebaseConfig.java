package com.FarmaciaFirebase.Conexao;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import java.io.FileInputStream;
import java.io.IOException;

public class FirebaseConfig {
    private static boolean isInitialized = false;

    public static void initializeFirebase() {
        if (!isInitialized) {
            try {
                FileInputStream serviceAccount = new FileInputStream("C:/Users/jarab/OneDrive/Área de Trabalho/farmacia-63410-firebase-adminsdk-fbsvc-b9d3db4da9.json");

                FirebaseOptions options = new FirebaseOptions.Builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .build();

                FirebaseApp.initializeApp(options);
                isInitialized = true;

                System.out.println("Firebase conectado com sucesso!");

            } catch (IOException e) {
                System.err.println("Erro ao conectar ao Firebase: " + e.getMessage());
            }
        } else {
            System.out.println("Firebase já foi inicializado.");
        }
    }
}
