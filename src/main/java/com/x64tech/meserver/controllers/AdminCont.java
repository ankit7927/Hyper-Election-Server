package com.x64tech.meserver.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/admin")
public class AdminCont {


    @GetMapping("")
    public String dashboard() {
        return "admin/dashboard";
    }


}
