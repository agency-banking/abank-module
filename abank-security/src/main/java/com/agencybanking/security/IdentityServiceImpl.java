package com.agencybanking.security;

import com.agencybanking.core.data.AuditDetails;
import com.agencybanking.core.security.IdentityService;
import com.agencybanking.security.auth.AuthenticationService;
import com.agencybanking.security.users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class IdentityServiceImpl implements IdentityService {

    @Autowired
    private UserRepository userRepository;


    @Override
    public String getAuthenticatedUser() {
        AuditDetails auditDetails = AuthenticationService.currentAuditDetails();
        return auditDetails.getCreatedBy();
    }


    @Override
    public String getUserEmail(String username) {
        Optional<String> opt = userRepository.findEmailByUsername(username);
        return opt.orElse(null);
    }

    @Override
    public boolean userExists(String username) {
        return userRepository.findByUsername(username) != null;
    }

}
