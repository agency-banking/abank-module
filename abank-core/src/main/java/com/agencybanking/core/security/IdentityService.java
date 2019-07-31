package com.agencybanking.core.security;

import java.util.List;
import java.util.Optional;

public interface IdentityService {

    String getAuthenticatedUser();


    /**
     * Get the email of this identity using the unique username identifier
     *
     * @param username unique username
     * @return the user's email or <tt>null</tt>
     */
    String getUserEmail(String username);

    boolean userExists(String username);
}
