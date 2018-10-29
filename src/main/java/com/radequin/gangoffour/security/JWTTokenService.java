package com.radequin.gangoffour.security;

import com.google.common.collect.ImmutableMap;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.compression.GzipCompressionCodec;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;
import java.util.function.Supplier;

import static io.jsonwebtoken.SignatureAlgorithm.HS256;
import static io.jsonwebtoken.impl.TextCodec.BASE64;
import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.PRIVATE;
import static org.apache.commons.lang3.StringUtils.substringBeforeLast;

@Service
@FieldDefaults(level = PRIVATE, makeFinal = true)
final class JWTTokenService implements Clock, TokenService {
    private static final String DOT = ".";
    private static final GzipCompressionCodec COMPRESSION_CODEC = new GzipCompressionCodec();

    String issuer;
    int expirationSec;
    int clockSkewSec;
    String secretKey;

    JWTTokenService(@Value("${jwt.issuer:octoperf}") String issuer,
                    @Value("${jwt.expiration-sec:86400}") int expirationSec,
                    @Value("${jwt.clock-skew-sec:300}") int clockSkewSec,
                    @Value("${jwt.secret:secret}") String secret) {
        super();
        this.issuer = requireNonNull(issuer);
        this.expirationSec = requireNonNull(expirationSec);
        this.clockSkewSec = requireNonNull(clockSkewSec);
        secretKey = BASE64.encode(requireNonNull(secret));
    }

    @Override
    public String permanent(Map<String, String> attributes) {
        return newToken(attributes, 0);
    }

    @Override
    public String expiring(Map<String, String> attributes) {
        return newToken(attributes, expirationSec);
    }

    private String newToken(Map<String, String> attributes, int expiresInSec) {
        LocalDateTime nowLocalDateTime = LocalDateTime.now();
        Date now = Date
                .from(nowLocalDateTime.atZone(ZoneId.systemDefault())
                        .toInstant());
        Claims claims = Jwts
                .claims()
                .setIssuer(issuer)
                .setIssuedAt(now);

        if (expiresInSec > 0) {
            Date expiresAt = Date
                    .from(nowLocalDateTime.plusSeconds(expiresInSec).atZone(ZoneId.systemDefault())
                            .toInstant());
            claims.setExpiration(expiresAt);
        }
        claims.putAll(attributes);

        return Jwts
                .builder()
                .setClaims(claims)
                .signWith(HS256, secretKey)
                .compressWith(COMPRESSION_CODEC)
                .compact();
    }

    @Override
    public Map<String, String> verify(String token) {
        JwtParser parser = Jwts
                .parser()
                .requireIssuer(issuer)
                .setClock(this)
                .setAllowedClockSkewSeconds(clockSkewSec)
                .setSigningKey(secretKey);
        return parseClaims(() -> parser.parseClaimsJws(token).getBody());
    }

    @Override
    public Map<String, String> untrusted(String token) {
        JwtParser parser = Jwts
                .parser()
                .requireIssuer(issuer)
                .setClock(this)
                .setAllowedClockSkewSeconds(clockSkewSec);

        // See: https://github.com/jwtk/jjwt/issues/135
        String withoutSignature = substringBeforeLast(token, DOT) + DOT;
        return parseClaims(() -> parser.parseClaimsJwt(withoutSignature).getBody());
    }

    private static Map<String, String> parseClaims(Supplier<Claims> toClaims) {
        try {
            Claims claims = toClaims.get();
            ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
            for (Map.Entry<String, Object> e : claims.entrySet()) {
                builder.put(e.getKey(), String.valueOf(e.getValue()));
            }
            return builder.build();
        } catch (IllegalArgumentException | JwtException e) {
            return ImmutableMap.of();
        }
    }

    @Override
    public Date now() {
        return Date
                .from(LocalDateTime.now().atZone(ZoneId.systemDefault())
                        .toInstant());
    }
}