package com.example.olleuback.common.security;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.example.olleuback.common.exception.OlleUException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtProvider {
	@Value("${jwt.secret}")
	private String secretKey;
	@Value("${jwt.access-token-expiration-time}")
	private Long accessTokenValidTime;
	@Value("${jwt.refresh-token-expiration-time}")
	private Long refreshTokenValidTime;
	private static final int ms = 1000;

	private Key getKey() {
		byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	public String generateAccessToken(String email){
		Claims claims = Jwts.claims();
		claims.put("email", email);
		Date now = new Date();
		Date expiration = new Date(now.getTime() + accessTokenValidTime * ms);

		return Jwts.builder()
			.setClaims(claims)
			.setHeaderParam(Header.TYPE, Header.JWT_TYPE)
			.setIssuedAt(now)
			.setExpiration(expiration)
			.signWith(this.getKey(), SignatureAlgorithm.HS256)
			.compact();
	}

	public String generateRefreshToken(String email) {
		Claims claims = Jwts.claims();
		claims.put("email", email);
		Date now = new Date();
		Date expiration = new Date(now.getTime() + refreshTokenValidTime * ms);

		return Jwts.builder()
			.setClaims(claims)
			.setHeaderParam(Header.TYPE, Header.JWT_TYPE)
			.setIssuedAt(now)
			.setExpiration(expiration)
			.signWith(this.getKey(), SignatureAlgorithm.HS256)
			.compact();
	}

	public String generateAccessTokenWithRefreshToken(String refreshToken) {
		Jws<Claims> refreshTokenClaims = validateToken(refreshToken);
		String email = refreshTokenClaims.getBody().get("email", String.class);

		Claims claims = Jwts.claims();
		claims.put("email", email);
		claims.put("refresh_token", refreshToken);
		Date now = new Date();
		Date expiration = new Date(now.getTime() + accessTokenValidTime * ms);

		return Jwts.builder()
			.setClaims(claims)
			.setHeaderParam(Header.TYPE, Header.JWT_TYPE)
			.setIssuedAt(now)
			.setExpiration(expiration)
			.signWith(this.getKey(), SignatureAlgorithm.HS256)
			.compact();
	}


	public String resolveToken(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
			throw new OlleUException(401, "Invalid token", HttpStatus.UNAUTHORIZED);
		}
		return bearerToken.substring(7);
	}

	public Jws<Claims> validateToken(String token) {
		return Jwts.parserBuilder().setSigningKey(this.getKey()).build().parseClaimsJws(token);
	}

	public Authentication getAuthentication(String token) {
		String email = (String) Jwts.parserBuilder().setSigningKey(this.getKey()).build()
			.parseClaimsJws(token).getBody().get("email");
		UserDetails userDetails = new OlleUUserDetails(email);
		return new UsernamePasswordAuthenticationToken(userDetails, "", null);
	}
}
