/**
 *
 */
package com.agencybanking.config

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.AuthenticationException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.AuthenticationEntryPoint
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


/**
 * @author dubic
 */
@EnableGlobalMethodSecurity(securedEnabled = true)
@EnableWebSecurity
@Configuration
class SecurityConfig : WebSecurityConfigurerAdapter() {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    //    @Autowired
    //    private AuthManager authProvider;

    //    @Value("${jwt.route.authentication.path}")
    //    private String AUTH_PATH;
    //
    //    @Value("${jwt.session.store}")
    //    private String SESSION_STORE;

    //    @Override
    //    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    //        auth.authenticationProvider(authProvider);// .userDetailsService(userDetailsService);
    //    }

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http.csrf().disable()
                //        http.cors()
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint()).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeRequests()
                .anyRequest().permitAll()
        //
        //                .antMatchers("/authentication/password", "/", "/signup", "/login", "/authentication/savepassword", "/password/reset", "/assets/**", "/favicon.ico", "/**/*.html", "/**/*.gif", "/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg")
        //                .permitAll()
        //                .requestMatchers(EndpointRequest.toAnyEndpoint()).permitAll()
        //                .antMatchers("/" + AUTH_PATH + "/**", "/signout", "/test/**", "/socket-messages/**", "/sample/storage/**", "/password/forgot/**", "/mbcp/**").permitAll()
        //                .anyRequest().fullyAuthenticated();

        //        http.addFilterBefore(new CorsConfig(), UsernamePasswordAuthenticationFilter.class)
        //                .addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
    }

    //    @Bean
    //    public JwtAuthenticationTokenFilter authenticationTokenFilterBean() throws Exception {
    //        return new JwtAuthenticationTokenFilter();
    //    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    //    @Bean
    //    public SessionStore tokenStore() {
    //        logger.info("Database Session store selected for registration");
    //        return new DBSessionStore();
    //    }

    @Bean
    fun authenticationEntryPoint(): AuthenticationEntryPoint {
        return AuthenticationEntryPoint { _, resp, _ -> resp?.sendError(HttpStatus.UNAUTHORIZED.value(), "Secured URL Hit") }
    }
}
