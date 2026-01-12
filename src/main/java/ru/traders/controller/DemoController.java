package ru.traders.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("Hello User");
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> helloA() {
        var auth = SecurityContextHolder.getContext().getAuthentication();

        System.out.println("AUTH = " + auth);
        System.out.println("AUTHORITIES = " + auth.getAuthorities());

        return ResponseEntity.ok("Hello Admin");
    }
}
