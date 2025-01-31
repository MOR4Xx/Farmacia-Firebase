package com.FarmaciaFirebase.Etidades;

public class Cliente extends Pessoa {
    private boolean possuiPlanoSaude;

    public Cliente(String nome, String cpf, int idade, String telefone, boolean possuiPlanoSaude) {
        super(nome, cpf, idade, telefone);
        this.possuiPlanoSaude = possuiPlanoSaude;
    }

    // Getters e Setters
    public boolean isPossuiPlanoSaude() { return possuiPlanoSaude; }
    public void setPossuiPlanoSaude(boolean possuiPlanoSaude) { this.possuiPlanoSaude = possuiPlanoSaude; }
}
