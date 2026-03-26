package co.edu.uniquindio.proyecto.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import co.edu.uniquindio.proyecto.domain.service.HistorialService;
import co.edu.uniquindio.proyecto.domain.service.SolicitudDomainService;

@Configuration
public class DomainServiceConfig {
    @Bean
    public SolicitudDomainService solicitudDomainService() {
        return new SolicitudDomainService();
    }

    @Bean
    public HistorialService historialService() {
        return new HistorialService();
    }
}
