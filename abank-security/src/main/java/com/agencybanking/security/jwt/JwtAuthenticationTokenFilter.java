/**
 *
 */
package com.agencybanking.security.jwt;

import com.agencybanking.core.services.AppContextHolder;
import com.agencybanking.security.auth.AuthenticationService;
import com.agencybanking.security.auth.data.OptimusAuthentication;
import com.agencybanking.security.jwt.session.Session;
import com.agencybanking.security.jwt.session.SessionStore;
import com.agencybanking.security.users.User;
import com.agencybanking.security.users.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author dubic
 */
@Slf4j
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private AuthenticationService authService;
    @Autowired
    private SessionStore sessionStore;
    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        AppContextHolder.getInstance().setRequest(request);
        String authToken = request.getHeader(AuthenticationService.TOKEN_HEADER);
        // authToken.startsWith("Bearer ")

        JwtSubject subject = jwtTokenUtil.getDetailsFromToken(authToken);
        if (subject == null) {
            logger.debug("NO SUBJECT RETREIVED FROM TOKEN");
            filterChain.doFilter(request, response);
            return;
        }
// session exists?
        Session session = sessionStore.retrieveSession(subject.getUsername(), subject.getTokenCreation());
        if (session == null) {
            logger.debug("SESSION DOES NOT EXIST");
            filterChain.doFilter(request, response);
            return;
        }
 // session expired?
        if (sessionStore.sessionExpired(session, true)) {
            logger.debug("SESSION EXPIRED FOR TOKEN...");
            filterChain.doFilter(request, response);
            return;
        }

        OptimusAuthentication auth = new OptimusAuthentication();
        User user = userRepository.findByUsername(subject.getUsername());
        auth.setUser(user);

        authService.recreateAuthentication(auth, authToken, authService.getGrantedAuthorities(user.getUsername()));


        // if (username != null &&
        // SecurityContextHolder.getContext().getAuthentication() == null) {
        //
        // // It is not compelling necessary recipients load the use details from the
        // database. You could also store the information
        // // in the token and read it from it. It's up recipients you ;)
        // User userDetails = this.userService.loadUserByUsername(username);
        //
        // // For simple validation it is completely sufficient recipients just check
        // the token integrity. You don't have recipients call
        // // the database compellingly. Again it's up recipients you ;)
        // if (jwtTokenUtil.validateToken(authToken, userDetails)) {
        // System.out.println("Setting auth");
        // authenticationManager.setAuthentication(authToken);
        // }
        // }

        filterChain.doFilter(request, response);

    }

}
