package com.locatehub.demo.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;

@Entity
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = UserLocador.class, name = "userLocador"),
        @JsonSubTypes.Type(value = UserLocatario.class, name = "userLocatario")
})
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class User {
    
    // Atributos gerais
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    protected String nome;

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    protected String email;
    protected String documento;
    protected String senha;
    public final void inicializarPerfil() {
        validarAcesso();
        carregarConfiguracoesEspecificas(); // Passo que pode ser mudado
        notificarSucesso();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
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