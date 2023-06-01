package fr.simplon.projetlemoulin.security;

import fr.simplon.projetlemoulin.dto.CreateUserForm;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.Collection;

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


    @GetMapping("/inscription")
    public String subscribe(Model model) {
        model.addAttribute("user", new CreateUserForm());
        return "registration";
    }

    @PostMapping("/inscription")
    @Transactional
    public String subscribe(
            @ModelAttribute(name = "user") @Valid CreateUserForm user, BindingResult validation, Model model) {
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            user.setConfirmPassword("");
            validation.addError(new FieldError("user", "confirmPassword",
                    "Les mots de passe ne correspondent pas"));
        }
        if (userDetailsManager.userExists(user.getLogin())) {
            user.setLogin("");
            validation.addError(new ObjectError("user", "Cet utilisateur existe déjà"));
        }
        if (validation.hasErrors()) {
            return "/registration";
        }
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        // Roles for new user
        Collection<? extends GrantedAuthority> roles = Arrays.asList(new SimpleGrantedAuthority("USER"));
        UserDetails userDetails = new User(user.getLogin(), encodedPassword, roles);
        // Create the account in database with all its roles
        userDetailsManager.createUser(userDetails);
        return "redirect:/login";
    }


}
