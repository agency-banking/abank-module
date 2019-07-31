package com.agencybanking.security.auth;

import com.agencybanking.security.auth.data.AuthenticationRequest;
import com.agencybanking.security.auth.data.OptimusAuthentication;
import com.agencybanking.security.users.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/authentication")
@CrossOrigin("*")
public class AuthenticationController {
    private AuthenticationService authService;
    private UserService userService;

    public AuthenticationController(AuthenticationService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    @PostMapping("/password")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest authRequest, HttpServletRequest http){
//    	System.out.println(authRequest);
        return ResponseEntity.ok(this.authService.authenticate(authRequest));
    }

    @GetMapping("signout")
    public ResponseEntity<?> logout(@RequestHeader(AuthenticationService.TOKEN_HEADER) String token) {
//        authService.logout(token);
        return ResponseEntity.ok("Logout successful");
    }

    @PostMapping("/updatepassword")
    public ResponseEntity<?> updatePassword() {
        return ResponseEntity.ok("");
    }

    @RequestMapping("ping")
    public ResponseEntity<?> ping() {
        OptimusAuthentication auth = (OptimusAuthentication) SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(auth);
    }
}
