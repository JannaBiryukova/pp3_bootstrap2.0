package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Controller
@RequestMapping("/admin")
public class AdminController {


    private final UserService userService;

    private final RoleService roleService;


    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping()
    public String userPage2(Principal principal, Model model) {

        model.addAttribute("newUser", new User());
        Set<Role> roles = new HashSet<>(roleService.getAllRoles());
        model.addAttribute("allRoles", roles);
        User user = userService.findByEmail(principal.getName());
        model.addAttribute("usera", user);

        model.addAttribute("users", userService.getAllUsers());
        return "admin";
    }

    @GetMapping("/admin")
    public String showAdminPage(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("allUsers", userService.getAllUsers());
        model.addAttribute("user", user);
        model.addAttribute("roles", roleService.getAllRoles());
        return "adminpage";
    }

    //create
    @GetMapping("/new")
    public String getCreateUser(Model model) {
        model.addAttribute("user", new User());
        Set<Role> roles = new HashSet<>(roleService.getAllRoles());
        model.addAttribute("allroles", roles);
        return "/new";
    }

    @PostMapping("/create_user")
    public String getAddUser(@ModelAttribute("user") User user) {
        userService.addUser(user);
        return "redirect:/admin";
    }

    //upd
    @GetMapping("/update_user/{id}")
    public String getUpdateUserForm(@RequestParam("id") long userId, Model model, @PathVariable String id) {
        model.addAttribute("userUpdate", userService.getUser(userId));
        model.addAttribute("allRolesUpdate", roleService.getAllRoles());
        return "update_user";
    }

    @PostMapping("/update_user")
    public String updateUserData(@ModelAttribute("userUpdate") User user) {
        userService.updateUser(user.getId(), user);
        return "redirect:/admin";
    }


    //delete
    @GetMapping("/delete")
    public String deleteUser(@RequestParam("id") long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}