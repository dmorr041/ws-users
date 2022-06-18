package com.example.udemy_spring_boot_microservices.photoapp.api.users.PhotoAppApiUsers.security;

import com.example.udemy_spring_boot_microservices.photoapp.api.users.PhotoAppApiUsers.model.LoginRequestModel;
import com.example.udemy_spring_boot_microservices.photoapp.api.users.PhotoAppApiUsers.service.UsersService;
import com.example.udemy_spring_boot_microservices.photoapp.api.users.PhotoAppApiUsers.shared.UserDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Spring will perform user auth when any request is made using this filter given you have
 * overridden attemptAuthentication with an appropriate implementation. This filter must be registered with
 * your security configuration, in this case, WebSecurity.class
 */
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private UsersService usersService;
    private Environment environment;

    public AuthenticationFilter(
        UsersService usersService,
        Environment environment,
        AuthenticationManager authenticationManager
    ) {
        this.usersService = usersService;
        this.environment = environment;
        super.setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(
        HttpServletRequest req, HttpServletResponse res
    ) throws AuthenticationException {
        try {
            LoginRequestModel creds = new ObjectMapper().readValue(req.getInputStream(), LoginRequestModel.class);
            return getAuthenticationManager().authenticate(
                new UsernamePasswordAuthenticationToken(creds.getEmail(), creds.getPassword(), new ArrayList<>()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Called by Spring Security upon successful authentication. We pull the username from the Authentication object,
     * then use the username to pull the entire user from the DB via our usersService, and then use that user to set
     * fields in the JWT and sign the JWT. Then, we add the token and userID (not username) to the headers of the
     * HTTP response
     * @param req
     * @param res
     * @param chain
     * @param auth
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void successfulAuthentication(
        HttpServletRequest req, HttpServletResponse res, FilterChain chain, Authentication auth
    ) throws IOException, ServletException {

        // Upon successful auth, we want to pull the users info from the DB and store it in a UserDTO
        String username = ((User) auth.getPrincipal()).getUsername();
        UserDTO userDTO = usersService.getUserDetailsByEmail(username);

        // Generate JWT token - requires io.jsonwebtoken:jjwt dependency
        String token = Jwts.builder()
            .setSubject(userDTO.getUserID())
            .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(environment.getProperty("token.expiration_time"))))
            .signWith(SignatureAlgorithm.HS512, environment.getProperty("token.secret"))
            .compact();

        // Add this token to the response header
        res.addHeader("token", token);
        res.addHeader("userID", userDTO.getUserID());
    }
}
