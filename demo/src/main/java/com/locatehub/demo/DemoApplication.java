package com.locatehub.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;

import com.locatehub.demo.dashboard.DashboardFinanceiroTemplate;
import com.locatehub.demo.dashboard.DashboardLocador;
import com.locatehub.demo.dashboard.DashboardLocatario;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);

		DashboardFinanceiroTemplate painelLocatario = new DashboardLocatario();
		painelLocatario.gerarDashboard();
		System.out.println("\n-----------------------------------\n");
		DashboardFinanceiroTemplate painelLocador = new DashboardLocador();
		painelLocador.gerarDashboard();
	}
}
