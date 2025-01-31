package com.FarmaciaFirebase.Etidades;

public class Funcionario extends Pessoa {
    private String cargo;
    private double salario;

    public Funcionario(String nome, String cpf, int idade, String telefone, String cargo, double salario) {
        super(nome, cpf, idade, telefone);
        this.cargo = cargo;
        this.salario = salario;
    }

    // Getters e Setters
    public String getCargo() { return cargo; }
    public void setCargo(String cargo) { this.cargo = cargo; }

    public double getSalario() { return salario; }
    public void setSalario(double salario) { this.salario = salario; }
}
