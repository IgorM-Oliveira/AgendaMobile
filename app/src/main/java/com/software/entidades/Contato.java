package com.software.entidades;

public class Contato {
    Integer id;
    String nome;
    String telefone;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return (nome);
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getTelefone() {
        return (telefone);
    }

    @Override
    public String toString() {
        return (String.format("Nome: %s | Telefone: %s", nome, telefone));
    }
}
