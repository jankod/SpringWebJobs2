package hr.bioinfo.swj.rest;

import hr.bioinfo.swj.model.Role;
import hr.bioinfo.swj.model.User;
import hr.bioinfo.swj.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/users")
@PreAuthorize("hasRole('SUPER_ADMIN')")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public String listUsers(Model model) {
        log.info("UserController.listUsers() called");

        List<User> users = userRepository.findAll();

        model.addAttribute("users", users);
        model.addAttribute("title", "Users - SpringWebJobs");
        model.addAttribute("pageTitle", "User Management");
        model.addAttribute("activeTab", "users");
        model.addAttribute("content", "users :: content");

        return "users";
    }

    @GetMapping("/new")
    public String newUserForm(Model model) {
        log.info("UserController.newUserForm() called");

        model.addAttribute("user", new User());
        model.addAttribute("roles", Role.values());
        model.addAttribute("title", "New User - SpringWebJobs");
        model.addAttribute("pageTitle", "Create New User");
        model.addAttribute("activeTab", "users");
        model.addAttribute("content", "user-form :: content");
        model.addAttribute("isEdit", false);

        return "user-form";
    }

    @PostMapping("/new")
    public String createUser(@ModelAttribute @Valid User user,
                             BindingResult result,
                             @RequestParam(value = "selectedRoles", required = false) Role[] selectedRoles,
                             RedirectAttributes redirectAttributes,
                             Model model) {
        log.info("UserController.createUser() called");

        if (result.hasErrors()) {
            model.addAttribute("roles", Role.values());
            model.addAttribute("title", "New User - SpringWebJobs");
            model.addAttribute("pageTitle", "Create New User");
            model.addAttribute("activeTab", "users");
            model.addAttribute("content", "user-form :: content");
            model.addAttribute("isEdit", false);
            return "user-form";
        }

        // Check if username already exists
        if (userRepository.existsByUsername(user.getUsername())) {
            result.rejectValue("username", "error.user", "Username already exists");
            model.addAttribute("roles", Role.values());
            model.addAttribute("title", "New User - SpringWebJobs");
            model.addAttribute("pageTitle", "Create New User");
            model.addAttribute("activeTab", "users");
            model.addAttribute("content", "user-form :: content");
            model.addAttribute("isEdit", false);
            return "user-form";
        }

        // Check if email already exists
        if (userRepository.existsByEmail(user.getEmail())) {
            result.rejectValue("email", "error.user", "Email already exists");
            model.addAttribute("roles", Role.values());
            model.addAttribute("title", "New User - SpringWebJobs");
            model.addAttribute("pageTitle", "Create New User");
            model.addAttribute("activeTab", "users");
            model.addAttribute("content", "user-form :: content");
            model.addAttribute("isEdit", false);
            return "user-form";
        }

        // Encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Add roles
        if (selectedRoles != null) {
            Arrays.stream(selectedRoles).forEach(user::addRole);
        } else {
            // Default role is USER
            user.addRole(Role.USER);
        }

        userRepository.save(user);

        redirectAttributes.addFlashAttribute("message", "User created successfully");
        return "redirect:/users";
    }

    @GetMapping("/edit/{id}")
    public String editUserForm(@PathVariable Long id, Model model) {
        log.info("UserController.editUserForm() called for id: {}", id);

        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty()) {
            return "redirect:/users";
        }

        User user = userOpt.get();

        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        model.addAttribute("title", "Edit User - SpringWebJobs");
        model.addAttribute("pageTitle", "Edit User");
        model.addAttribute("activeTab", "users");
        model.addAttribute("content", "user-form :: content");
        model.addAttribute("isEdit", true);

        return "user-form";
    }

    @PostMapping("/edit/{id}")
    public String updateUser(@PathVariable Long id,
                             @ModelAttribute @Valid User user,
                             BindingResult result,
                             @RequestParam(value = "selectedRoles", required = false) Role[] selectedRoles,
                             @RequestParam(value = "newPassword", required = false) String newPassword,
                             RedirectAttributes redirectAttributes,
                             Model model) {
        log.info("UserController.updateUser() called for id: {}", id);

        Optional<User> existingUserOpt = userRepository.findById(id);
        if (existingUserOpt.isEmpty()) {
            return "redirect:/users";
        }

        User existingUser = existingUserOpt.get();

        if (result.hasErrors()) {
            model.addAttribute("roles", Role.values());
            model.addAttribute("title", "Edit User - SpringWebJobs");
            model.addAttribute("pageTitle", "Edit User");
            model.addAttribute("activeTab", "users");
            model.addAttribute("content", "user-form :: content");
            model.addAttribute("isEdit", true);
            return "user-form";
        }

        // Check if username already exists (for another user)
        if (!existingUser.getUsername().equals(user.getUsername()) &&
            userRepository.existsByUsername(user.getUsername())) {
            result.rejectValue("username", "error.user", "Username already exists");
            model.addAttribute("roles", Role.values());
            model.addAttribute("title", "Edit User - SpringWebJobs");
            model.addAttribute("pageTitle", "Edit User");
            model.addAttribute("activeTab", "users");
            model.addAttribute("content", "user-form :: content");
            model.addAttribute("isEdit", true);
            return "user-form";
        }

        // Check if email already exists (for another user)
        if (!existingUser.getEmail().equals(user.getEmail()) &&
            userRepository.existsByEmail(user.getEmail())) {
            result.rejectValue("email", "error.user", "Email already exists");
            model.addAttribute("roles", Role.values());
            model.addAttribute("title", "Edit User - SpringWebJobs");
            model.addAttribute("pageTitle", "Edit User");
            model.addAttribute("activeTab", "users");
            model.addAttribute("content", "user-form :: content");
            model.addAttribute("isEdit", true);
            return "user-form";
        }

        // Update user details
        existingUser.setUsername(user.getUsername());
        existingUser.setEmail(user.getEmail());
        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setEnabled(user.isEnabled());

        // Update password if provided
        if (newPassword != null && !newPassword.trim().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(newPassword));
        }

        // Update roles
        existingUser.getRoles().clear();
        if (selectedRoles != null) {
            Arrays.stream(selectedRoles).forEach(existingUser::addRole);
        } else {
            // Default role is USER
            existingUser.addRole(Role.USER);
        }

        userRepository.save(existingUser);

        redirectAttributes.addFlashAttribute("message", "User updated successfully");
        return "redirect:/users";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        log.info("UserController.deleteUser() called for id: {}", id);

        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            userRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("message", "User deleted successfully");
        }

        return "redirect:/users";
    }
}