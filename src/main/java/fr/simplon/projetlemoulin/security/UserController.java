package fr.simplon.projetlemoulin.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.sql.DataSource;

@Controller
public class UserController {

    private DataSource dataSource;
    private PasswordEncoder passwordEncoder;
    private UserDetailsManager userDetailsManager;

    @Autowired
    public UserController(
            DataSource pDataSource, PasswordEncoder pPasswordEncoder, UserDetailsManager pUserDetailsManager) {
        dataSource = pDataSource;
        passwordEncoder = pPasswordEncoder;
        userDetailsManager = pUserDetailsManager;
    }

    @GetMapping("/login")
    String login() {
        return "login";
    }


    @GetMapping(path = "/deconnexion")
    public String logout(HttpServletRequest request) {
        SecurityContextHolder.getContext().setAuthentication(null);
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }
}
