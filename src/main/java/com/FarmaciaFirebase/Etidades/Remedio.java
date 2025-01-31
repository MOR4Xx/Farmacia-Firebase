package com.FarmaciaFirebase.Etidades;

import java.util.Date;

public class Remedio {
    private String nome;
    private String laboratorio;
    private double preco;
    private int quantidadeEmEstoque;
    private boolean necessitaReceita;
    private Date validade;

    public Remedio() {}

    public Remedio(String nome, String laboratorio, double preco, int quantidadeEmEstoque, boolean necessitaReceita, Date validade) {
        this.nome = nome;
        this.laboratorio = laboratorio;
        this.preco = preco;
        this.quantidadeEmEstoque = quantidadeEmEstoque;
        this.necessitaReceita = necessitaReceita;
        this.validade = validade;
    }

    // Getters e Setters
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getLaboratorio() { return laboratorio; }
    public void setLaboratorio(String laboratorio) { this.laboratorio = laboratorio; }

    public double getPreco() { return preco; }
    public void setPreco(double preco) { this.preco = preco; }

    public int getQuantidadeEmEstoque() { return quantidadeEmEstoque; }
    public void setQuantidadeEmEstoque(int quantidadeEmEstoque) { this.quantidadeEmEstoque = quantidadeEmEstoque; }

    public boolean isNecessitaReceita() { return necessitaReceita; }
    public void setNecessitaReceita(boolean necessitaReceita) { this.necessitaReceita = necessitaReceita; }

    public Date getValidade() { return validade; }
    public void setValidade(Date validade) { this.validade = validade; }
}
