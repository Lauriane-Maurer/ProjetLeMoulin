package fr.simplon.projetlemoulin.restcontroller;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(info = @Info(title = "Open API specifications",
        description = "Points d'entrée de l'API du Moulin"),
        servers = @Server(url = "http://localhost:8085/api"))
class OpenApiConfiguration
{
}
