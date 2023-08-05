package com.github.mimiknight.monkey.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenApi配置类
 * <p>
 * https://127.0.0.1:8443/swagger-ui/index.html
 *
 * @author victor2015yhm@gmail.com
 * @since 2023-05-22 07:52:39
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openApi() {
        String title = "Spring Doc OpenAPI";
        String version = "v0.0.1";
        String contactName = "victor";
        String contactEmail = "victor2015yhm@gmail.com";
        String contactUrl = "https://www.open-kuca.com";
        String description = "KucaMonkeyService Application";

        Contact contact = new Contact();
        contact.setName(contactName);
        contact.setUrl(contactUrl);
        contact.setEmail(contactEmail);


        Info info = new Info()
                .title(title)
                .contact(contact)
                .version(version)
                .description(description);
        return new OpenAPI().info(info);
    }

}
