package com.achiever.menschenfahren.security.jwt;

import java.io.IOException;

import javax.annotation.Nonnull;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.achiever.menschenfahren.dao.UserDaoInterface;
import com.achiever.menschenfahren.entities.users.User;
import com.achiever.menschenfahren.security.model.ExtendedUserDetails;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtUserDetailsService jwtUserDetailsService;

    private final JwtTokenUtil          jwtTokenUtil;

    @Autowired
    private UserDaoInterface            userDao;

    @Autowired
    public JwtRequestFilter(@Nonnull final JwtTokenUtil jwtTokenUtil, @Nonnull final JwtUserDetailsService jwtUserDetailsService) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.jwtUserDetailsService = jwtUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        final String requestTokenHeader = request.getHeader("Authorization");

        String email = null;
        String jwtToken = null;
        // JWT Token is in the form "Bearer token". Remove Bearer word and get
        // only the Token
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                email = jwtTokenUtil.getSubjectByToken(jwtToken);

            } catch (IllegalArgumentException e) {
                log.error("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                log.error("JWT Token has expired");
            }
        } else {
            log.warn("JWT Token does not begin with Bearer String");
        }

        // Once we get the token validate it.
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            User user = userDao.findByEmail(email);

            // if token is valid configure Spring Security to manually set
            // authentication
            if (jwtTokenUtil.validateToken(jwtToken, user)) {

                ExtendedUserDetails userDetails = this.jwtUserDetailsService.loadByEmail(user);

                EmailPasswordAuthenticationToken emailPasswordAuthenticationToken = new EmailPasswordAuthenticationToken(userDetails, null,
                        userDetails.getAuthorities());

                emailPasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(emailPasswordAuthenticationToken);
            }
        }
        chain.doFilter(request, response);
    }

}
