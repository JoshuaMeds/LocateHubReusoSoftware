package com.locatehub.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;

import com.locatehub.demo.dashboard.DashboardFinanceiroTemplate;
import com.locatehub.demo.dashboard.DashboardLocador;
import com.locatehub.demo.dashboard.DashboardLocatario;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);

		DashboardFinanceiroTemplate painelLocatario = new DashboardLocatario();
		painelLocatario.gerarDashboard(); // Gera a tela de despesas

		DashboardFinanceiroTemplate painelLocador = new DashboardLocador();
		painelLocador.gerarDashboard(); // Gera a tela de ganhos
	}

}
