@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("User Management API")
                        .version("1.0.0")
                        .description("API для управления пользователями и их адресами"))
                .externalDocs(new ExternalDocs()
                        .description("Документация проекта")
                        .url("https://example.com/docs"));
    }
}