package vttp.PAFWS.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CustomerController {
    
    @GetMapping(path = "/")
    public String home(){
        return "index";
    }

    @GetMapping(path = "/orderForm")
    public String form(){
        return "orderForm";
    }
}
