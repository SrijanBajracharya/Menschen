package com.achiever.menschenfahren.security.jwt;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.achiever.menschenfahren.constants.Constants;
import com.achiever.menschenfahren.entities.users.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtil implements Serializable {

    private static final long serialVersionUID   = -2550185165626007488L;

    public static final long  JWT_TOKEN_VALIDITY = 5 * 60 * 60;

    @Value("${jwt.secret}")
    private String            secret;

    // retrieve username from jwt token
    public String getSubjectByToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    // retrieve expiration date from jwt token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Returns all the claims defined in the token.
     *
     * @param token
     * @return
     */
    public Claims fetchAllClaimsFromToken(String token) {
        return getAllClaimsFromToken(token);
    }

    // for retrieveing any information from token we will need the secret key
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    // check if the token has expired
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    // generate token for user
    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(Constants.TOKEN.CLAIM_ROLE, Constants.ROLE.ROLE_USER);
        claims.put(Constants.TOKEN.CLAIM_USERNAME, user.getUsername());
        return doGenerateToken(claims, user.getEmail());
    }

    // while creating the token -
    // 1. Define claims of the token, like Issuer, Expiration, Subject, and the ID
    // 2. Sign the JWT using the HS512 algorithm and secret key.
    // 3. According to JWS Compact Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
    // compaction of the JWT to a URL-safe string
    private String doGenerateToken(Map<String, Object> claims, String subject) {

        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis())).setIssuer(Constants.TOKEN.CLAIM_ISSUER)
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000)).signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    // validate token
    public Boolean validateToken(String token, User user) {
        final String email = getSubjectByToken(token);
        Claims claims = fetchAllClaimsFromToken(token);
        String username = claims.get(Constants.TOKEN.CLAIM_USERNAME).toString();
        return (email.equals(user.getEmail()) && username.equals(user.getUsername()) && !isTokenExpired(token));
    }
}
