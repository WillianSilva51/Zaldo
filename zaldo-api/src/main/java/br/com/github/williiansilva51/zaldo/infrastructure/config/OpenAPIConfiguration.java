package br.com.github.williiansilva51.zaldo.infrastructure.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(
        title = "Zaldo API",
        version = "v0.0.1",
        description = "Documentação técnica dos serviços do Zaldo API",
        contact = @Contact(name = "Willian Silva", email = "williansilva@alu.ufc.br")))
public class OpenAPIConfiguration {

}
