package com.agencybanking.security.password;

import com.agencybanking.security.tokens.TokenType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/password")
@CrossOrigin("*")
public class PasswordController {

    private final PasswordService passwordService;

    public PasswordController(PasswordService passwordService) {
        this.passwordService = passwordService;
    }

    @GetMapping("/forgot/{user}")
    public ResponseEntity<?> login(@PathVariable("user") String username) throws Exception {
        return ResponseEntity.ok(passwordService.initForgotPassword(username));
    }

    @PostMapping("/reset")
    public ResponseEntity<?> resetPassword(@RequestBody PasswordReset passwordReset) throws Exception {
        if (TokenType.FIRST_LOGIN.equals(passwordReset.getTokenType())) {
            return ResponseEntity.ok(passwordService.firstLoginPasswordReset(passwordReset));
        } else {
            return ResponseEntity.ok(passwordService.forgotPasswordReset(passwordReset));
        }

    }

    @PostMapping("/update")
    public ResponseEntity<?> updatePassword(@RequestBody PasswordUpdate passwordUpdate) throws Exception {
        return ResponseEntity.ok(passwordService.updatePassword(passwordUpdate));
    }
}
