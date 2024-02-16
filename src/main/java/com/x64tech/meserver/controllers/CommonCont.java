package com.x64tech.meserver.controllers;

import com.x64tech.meserver.models.MessageModel;
import com.x64tech.meserver.models.UserModel;
import com.x64tech.meserver.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Controller
public class CommonCont {
    @Autowired
    private UserService userService;

    @GetMapping("")
    public String home(){
        return "index";
    }


    @GetMapping("login")
    public String getLogin(Model model){
        UserModel userModel = new UserModel();
        model.addAttribute("user", userModel);
        return "login";
    }


    @GetMapping("register")
    public String getRegister(Model model){
        UserModel userModel = new UserModel();
        model.addAttribute("user", userModel);
        return "register";
    }

    @PostMapping("register")
    public String register(Model model, @ModelAttribute("user")  UserModel userModel, HttpSession session){
        if (userModel.getName()== null || userModel.getUsername() == null || userModel.getPassword() == null ){
            model.addAttribute("user", userModel);
            session.setAttribute("message", new MessageModel("all fields required", "alert-warning"));
            return "redirect:register";
        }
        try {
            userService.registerUser(userModel);
            return "redirect:login";
        } catch (Exception e) {
            System.out.println(e);
            session.setAttribute("message", new MessageModel(e.getMessage(), "alert-danger"));
            return "redirect:register";
        }
    }

    @GetMapping("/error")
    public String gotError(){
        return "error";
    }
}
