package com.x64tech.meserver.controllers;

import com.x64tech.meserver.configs.CustomUserDetails;
import com.x64tech.meserver.models.MessageModel;
import com.x64tech.meserver.models.UserModel;
import com.x64tech.meserver.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("profile")
public class ProfileCont {

    @Autowired
    private UserService userService;

    @GetMapping("get")
    public String profile(Model model, @AuthenticationPrincipal CustomUserDetails userDetails){
        model.addAttribute("user", userService.getUserPro(userDetails.getUserID()));
        model.addAttribute("additional", new UserModel.Additional());
        return "profile";
    }

    @PostMapping("update-basic")
    public String updateBasic(@ModelAttribute("user") UserModel userModel,
                              @AuthenticationPrincipal CustomUserDetails userDetails,
                              Model model, HttpSession session){

        try {
            userService.updatePro(userDetails.getUserID(), userModel);
            session.setAttribute("message", new MessageModel("Profile Updated Successfully", "alert-success"));
        } catch (Exception e) {
            session.setAttribute("message", new MessageModel("Failed to Updated profile "+e.getMessage(), "alert-danger"));
        }
        return "redirect:get";
    }

    @PostMapping("update-additional")
    public String updateAdditional(@ModelAttribute("additional") UserModel.Additional additional,
                                   @AuthenticationPrincipal CustomUserDetails userDetails,
                                   Model model, HttpSession session){

        try {
            userService.updateAdd(userDetails.getUserID(), additional);
            session.setAttribute("message", new MessageModel("Additional Data Updated Successfully", "alert-success"));
        } catch (Exception e) {
            session.setAttribute("message", new MessageModel("Failed to Updated Additional "+e.getMessage(), "alert-danger"));
        }
        return "redirect:get";
    }
}
