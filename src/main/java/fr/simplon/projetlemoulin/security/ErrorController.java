package fr.simplon.projetlemoulin.security;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {

    @GetMapping("/error")
    public String handleError() {
        return "error"; // Renvoie la page HTML d'erreur
    }
}
