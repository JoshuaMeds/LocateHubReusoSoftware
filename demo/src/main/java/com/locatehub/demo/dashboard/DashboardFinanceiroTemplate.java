package com.locatehub.demo.dashboard;

public abstract class DashboardFinanceiroTemplate 
{
    // Define the template painel 
    public final void gerarDashboard()
    {
        exibirPerfilBasico();
        exibirResumoFinanceiro();
        exibirConfiguracoesPagamento();
    }


    // commum comportament for all locatehub 
    protected void exibirPerfilBasico() {
        System.out.println("--- Dados do Perfil ---");
        System.out.println("-> Exibindo dados brutos do usuário...");
        System.out.println("-> Opção: Redefinir Senha de Acesso");
    }

    // subclass specific behavior
    protected abstract void exibirResumoFinanceiro();
    protected abstract void exibirConfiguracoesPagamento();
}