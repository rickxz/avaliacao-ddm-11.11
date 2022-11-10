package com.example.avaliacao_ddm_1111;

public class Item {
    private String produto;
    private boolean atendido;
    private double preco;

    public Item(String produto, double preco) {
        this.produto = produto;
        this.preco = preco;
        this.atendido = false;
    }

    public Item() {}

    public String getProduto() {
        return produto;
    }

    public void setProduto(String produto) {
        this.produto = produto;
    }

    public boolean isAtendido() {
        return atendido;
    }

    public void setAtendido(boolean atendido) {
        this.atendido = atendido;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }
}
