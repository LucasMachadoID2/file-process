package br.com.fiap.file_process.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/test")
public class TestTokenController {

    @GetMapping("/public")
    public ResponseEntity<?> publicEndpoint() {
        Map<String, Object> body = new HashMap<>();
        body.put("message", "public ok");
        return ResponseEntity.ok(body);
    }

    @GetMapping("/protected")
    public ResponseEntity<?> protectedEndpoint() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Map<String, Object> body = new HashMap<>();
        body.put("authenticated", authentication != null && authentication.isAuthenticated());

        if (authentication != null) {
            body.put("principalClass", authentication.getPrincipal() != null ? authentication.getPrincipal().getClass().getName() : null);
            body.put("email", authentication.getName());
        }

        return ResponseEntity.ok(body);
    }
}
