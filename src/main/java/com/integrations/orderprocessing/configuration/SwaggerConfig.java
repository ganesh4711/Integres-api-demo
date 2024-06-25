package com.integrations.orderprocessing.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

//@OpenAPIDefinition(info = @Info(
//        title = "Integrations API",
//        description = "description",
//        summary = "summary",
//        termsOfService = "termsOfService",
//        contact = @Contact(name = "DevelopByShiva", email = "shiva.siyyadri@imaginnovate.com", url = "https://imaginnovate.com/"),
//        license = @License(name = "License"),
//        version = "v1"),
//        servers = {
//			@Server(description = "StageEnv", url = "https://inbound-api-2sl7ooyo4a-uc.a.run.app"),
//			@Server(description = "DevEnv", url = "http://localhost:8088")
//		  },
//        security = @SecurityRequirement(name="IntegrationsSecurity")
//)
@OpenAPIDefinition(info = @Info(
        title = "StackEnable Integrations API",
        description = "",
        summary = "",
        termsOfService = "",
        contact = @Contact(name = "", email = "", url = ""),
        license = @License(name = ""),
        version = "v1"),
        servers = {
			@Server(description = "${app.swagger.server.info.description}", url = "${app.swagger.server.info.url}")
		  },
        security = @SecurityRequirement(name="StackEnableIntegrationsSecurity")
)
@SecurityScheme(
		name = "StackEnableIntegrationsSecurity",
		in= SecuritySchemeIn.HEADER,
		type = SecuritySchemeType.HTTP,
		bearerFormat = "JWT",
		scheme = "bearer",
		description = "This is StackEnable Integrations API Basic Security"
)
public class SwaggerConfig {

}
