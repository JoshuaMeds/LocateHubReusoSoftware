package com.locatehub.demo.config;

import com.locatehub.demo.model.UserLocador;
import com.locatehub.demo.model.UserLocatario;
import com.locatehub.demo.model.AtivoAutomovel;
import com.locatehub.demo.model.AtivoImovel;
import com.locatehub.demo.model.AtivoItem;
import com.locatehub.demo.repository.UserRepository;
import com.locatehub.demo.repository.AtivoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner init(UserRepository userRepository, AtivoRepository ativoRepository) {
        return args -> {

            // 1. Usuário Admin de teste
            if (userRepository.findByEmail("user@gmail.com").isEmpty()) {
                UserLocador user = new UserLocador(); 
                user.setEmail("user@gmail.com");
                user.setNome("user");
                user.setDocumento("Doc");
                user.setSenha("user");
                userRepository.save(user);
                System.out.println("Usuário inicial criado!");
            }

            // 2. Locatário de teste
            if (userRepository.findByEmail("locatario@teste.com").isEmpty()) {
                UserLocatario locatario = new UserLocatario();
                locatario.setNome("Locatário Teste");
                locatario.setEmail("locatario@teste.com");
                locatario.setDocumento("111.111.111-11");
                locatario.setSenha("123456");
                userRepository.save(locatario);
                System.out.println("✅ Usuário Locatário de teste criado com sucesso!");
            }

            // 3. Locador de teste
            if (userRepository.findByEmail("locador@teste.com").isEmpty()) {
                UserLocador locador = new UserLocador();
                locador.setNome("Locador Teste");
                locador.setEmail("locador@teste.com");
                locador.setDocumento("222.222.222-22");
                locador.setSenha("123456");
                locador.setSaldoDisponivel(0.0);
                userRepository.save(locador);
                System.out.println("✅ Usuário Locador de teste criado com sucesso!");
            }

            // ==========================================
            // 4. CRIAÇÃO DOS ITENS FICTÍCIOS PARA ALUGAR
            // ==========================================
            
            // Verifica se o banco de ativos está vazio para não criar itens duplicados ao reiniciar
            if (ativoRepository.count() == 0) {
                
                // Busca o ID do locador que acabamos de criar
                Optional<UserLocador> locadorOpt = userRepository.findByEmail("locador@teste.com")
                    .map(u -> (UserLocador) u);
                
                if (locadorOpt.isPresent()) {
                    Long donoId = locadorOpt.get().getId();

                    // Criando um Automóvel
                    AtivoAutomovel carro = new AtivoAutomovel(null, "Jeep Renegade 2023", new BigDecimal("180.00"), new BigDecimal("35.00"), donoId);
                    carro.setDescricao("SUV compacto automático, perfeito para viagens curtas e longas. Seguro completo incluso.");

                    // Criando um Imóvel
                    AtivoImovel apartamento = new AtivoImovel(null, "Apartamento Frente ao Mar", new BigDecimal("350.00"), new BigDecimal("120.00"), donoId);
                    apartamento.setDescricao("Lindo apartamento de 2 quartos com vista para o mar. Wi-fi e ar-condicionado.");

                    // Criando um Item Geral
                    AtivoItem furadeira = new AtivoItem(null, "Furadeira de Impacto Bosch", new BigDecimal("25.00"), true, new BigDecimal("100.00"), donoId);
                    furadeira.setDescricao("Furadeira potente para alvenaria e madeira. Acompanha maleta com brocas.");

                    // Salva todos de uma vez no banco de dados
                    ativoRepository.saveAll(List.of(carro, apartamento, furadeira));
                    
                    System.out.println("✅ Itens fictícios criados com sucesso e vinculados ao Locador Teste!");
                }
            }
        };
    }
}