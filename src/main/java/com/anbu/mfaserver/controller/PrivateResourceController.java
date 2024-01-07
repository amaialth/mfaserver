package com.anbu.mfaserver.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PrivateResourceController {

    @GetMapping("/protectedString")
    public String getProtectedString(){
        return "You have successfully accessed the protected resource";
    }
}
