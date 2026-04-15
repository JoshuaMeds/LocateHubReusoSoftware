package com.locatehub.demo.model;

public class UserLocatario extends User {
    // Atributos específicos
    private String formaPagamentoPrincipal;
    private int totalAlugueisRealizados;

    public UserLocatario(String id, String nome, String email) {
        super(id, nome, email);
    }

    // Implementação obrigatória do passo do Template Method
    @Override
    protected void carregarConfiguracoesEspecificas() {
        System.out.println("Carregando Histórico de Reservas e Preferências do Locatário.");
    }

    // Métodos específicos
    public void buscarProduto(String categoria) {
        System.out.println("Pesquisando itens na categoria: " + categoria);
    }

    public void definirPagamento(String metodo) {
        this.formaPagamentoPrincipal = metodo;
        System.out.println("Método de pagamento atualizado para: " + metodo);
    }

    public void registrarAluguel() {
        this.totalAlugueisRealizados++;
        System.out.println("Aluguel registrado. Total agora: " + this.totalAlugueisRealizados);
    }

    public void mostrarResumoLocatario() {
        System.out.println("Forma de pagamento: " + this.formaPagamentoPrincipal);
        System.out.println("Total de aluguéis realizados: " + this.totalAlugueisRealizados);
    }
}