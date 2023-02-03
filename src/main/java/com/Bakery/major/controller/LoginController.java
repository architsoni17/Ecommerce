package com.Bakery.major.controller;

import com.Bakery.major.global.GlobalData;
import com.Bakery.major.model.Role;
import com.Bakery.major.model.User;
import com.Bakery.major.repository.RoleRepository;
import com.Bakery.major.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class LoginController {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;

    @GetMapping("/login")
    public String login()
    {
        GlobalData.cart.clear();
        return "login";
    }

    @GetMapping("/register")
    public String getRegister()
    {
        return "register";
    }

    @PostMapping("/register")
    public String postRegister(@ModelAttribute("user") User user, HttpServletRequest httpServletRequest) throws ServletException
    {
        String password = user.getPassword();
        user.setPassword(bCryptPasswordEncoder.encode(password));
        List<Role> roles = new ArrayList<>();
        roles.add(roleRepository.findById(2).get());
        user.setRoles(roles);
        userRepository.save(user);
        httpServletRequest.login(user.getEmail(), password);
        return "redirect:/";
    }


}