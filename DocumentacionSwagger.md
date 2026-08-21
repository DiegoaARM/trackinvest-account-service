=============================================
SWAGGER DOCUMENTATION
=============================================

1. ADD SWAGGER DEPENDENCIES TO THE POM.XML:
            - Add the following dependencies to the pom.xml to enable Swagger in the project:
                <dependency>
                	<groupId>org.springdoc</groupId>
                	<artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
                	<version>2.3.0</version>
                </dependency>

2. CONFIGURE SWAGGER IN THE PROJECT:
            - Create a configuration class for Swagger, infrastructure/config/OpenApiConfig.java, and add the
                necessary annotations to configure the API documentation. For example:
                        @Configuration
                        @OpenAPIDefinition(
                                info = @Info(title = "InverTrack API", version = "v1"),
                                security = @SecurityRequirement(name = "bearerAuth")
                        )
                        @SecurityScheme(
                                name = "bearerAuth",
                                type = SecuritySchemeType.HTTP,
                                scheme = "bearer",
                                bearerFormat = "JWT"
                        )

3. ANNOTATE THE CONTROLLERS AND ENDPOINTS:
            - Add Swagger annotations to the controllers and endpoints to describe their functionality. For example, in the AuthController:
                    @Tag(name = "Authentication", description = "Endpoints for user authentication")

5. TEST THE DOCUMENTATION:
            - Start the application and access the URL http://localhost:8080/swagger-ui/index.html
