package fr.simplon.projetlemoulin.security;

import fr.simplon.projetlemoulin.dto.ChangePasswordForm;
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
import java.security.Principal;
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


    /**
     * Access to log in
     * @return direct to the login page in the connexion file.
     */

    @GetMapping("/login")
    String login() {
        return "connexion/login";
    }


    /**
     * Logs out the currently authenticated user and invalidates the session.
     * @return A redirection to the home page.
     */

    @GetMapping(path = "/deconnexion")
    public String logout(HttpServletRequest request) {
        SecurityContextHolder.getContext().setAuthentication(null);
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }



    /**
     * Displays the user registration page.
     *
     * Handles the GET request for the "/inscription" endpoint and returns the registration form
     * "inscriptionMembre" in the connexion file.
     *
     * @param model The model object for passing data to the view.
     * @return The registration form.
     */

    @GetMapping("/inscription")
    public String subscribe(Model model) {
        model.addAttribute("user", new CreateUserForm());
        return "connexion/inscriptionMembre";
    }

    /**
     * Handles user registration form submission.
     *
     * Processes the POST request for the "/inscription" endpoint and validates the submitted
     * user registration form. If there are validation errors, returns the view name
     * "/connexion/inscriptionMembre" to display the form with error messages. Otherwise,
     * creates a new user account with the encoded password and "USER" role in the database,
     * and redirects to the login page.
     *
     * @param user       The submitted CreateUserForm object.
     * @param validation The BindingResult object for validation errors.
     * @param model      The model object for passing data to the view.
     * @return The user registration form in case of validation errors,
     *         otherwise a redirection to the login page.
     */
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
            return "/connexion/inscriptionMembre";
        }
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        // Roles for new user
        Collection<? extends GrantedAuthority> roles = Arrays.asList(new SimpleGrantedAuthority("USER"));
        UserDetails userDetails = new User(user.getLogin(), encodedPassword, roles);
        // Create the account in database with its role
        userDetailsManager.createUser(userDetails);

        model.addAttribute("Message", "Votre inscription a été réalisée avec succès ! Cliquez sur 'Se Connecter' dans la barre de navigation.");
        return "message";
    }



    @GetMapping("/modificationMdp")
    public String changePassword(Model model)
    {
        ChangePasswordForm userForm = new ChangePasswordForm();
        model.addAttribute("user", userForm);
        return "connexion/modificationMdp";
    }


    @PostMapping("/modificationMdp")
    public String changePassword(
            Principal principal,
            @ModelAttribute("user") @Valid ChangePasswordForm user,
            BindingResult validation,
            Model model) {
        UserDetails userDetails = userDetailsManager.loadUserByUsername(principal.getName());
        changePassword(userDetails, user, validation);
        if (validation.hasErrors()) {
            return "connexion/modificationMdp";
        } else
        {
            model.addAttribute("Message", "Votre mot de passe a été modifié avec succès !");
            return "message";
        }
    }

    private void changePassword(UserDetails userDetails, ChangePasswordForm form, BindingResult validation) {
        if (userDetails == null) {
            validation.addError(new FieldError("user", "username", "Utilisateur inconnu"));
        } else {
            String currentPassword = userDetails.getPassword();

            if (!passwordEncoder.matches(form.getOldPassword(), currentPassword)) {
                validation.addError(new FieldError("user", "oldPassword", "Mot de passe invalide"));
            }
            if (!form.getNewPassword().equals(form.getConfirmPassword())) {
                validation.addError(new FieldError("user", "confirmPassword", "Les mots de passe ne correspondent pas"));
            }
            if (!validation.hasErrors()) {
                userDetailsManager.changePassword(form.getOldPassword(), passwordEncoder.encode(form.getNewPassword()));
            }
        }
    }


    @GetMapping("/admin/gestionnaireAdmin")
    public String displayAdminPage(Model model) {
        return "admin/pageAdmin";
    }

}
