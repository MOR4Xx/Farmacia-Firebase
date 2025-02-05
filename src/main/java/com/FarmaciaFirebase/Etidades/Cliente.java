package com.FarmaciaFirebase.Etidades;

public class Cliente extends Pessoa {
    private boolean planoSaude;

    public Cliente(){

    }

    public Cliente(String nome, String cpf, int idade, String telefone, boolean planoSaude) {
        super(nome, cpf, idade, telefone);
        this.planoSaude = planoSaude;
    }

    // Getters e Setters
    public boolean isPlanoSaude() { return planoSaude; }
    public void setPlanoSaude(boolean planoSaude) { this.planoSaude = planoSaude; }

}
