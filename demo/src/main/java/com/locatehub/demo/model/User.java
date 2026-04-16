package com.locatehub.demo.model;

public abstract class User {
    
    // Atributos gerais
    protected String id;
    protected String nome;
    protected String email;
    protected String documento;

    public User(String id, String nome, String email) {
        this.id = id;
        this.nome = nome;
        this.email = email;
    }

    public final void inicializarPerfil() {
        validarAcesso();
        carregarConfiguracoesEspecificas(); // Passo que pode ser mudado
        notificarSucesso();
    }

    private void validarAcesso() {
        System.out.println("Validando credenciais de: " + this.email);
    }

    private void notificarSucesso() {
        System.out.println("Perfil de " + this.nome + " carregado com sucesso.");
    }

    // Método abstrato que as subclasses DEVEM implementar
    protected abstract void carregarConfiguracoesEspecificas();
}