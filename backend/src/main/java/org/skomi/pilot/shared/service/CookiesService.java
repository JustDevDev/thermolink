package org.skomi.pilot.shared.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.websocket.server.HandshakeRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Handling cookies
 */
@Service
public class CookiesService {

    public static final String COOKIE_JWT = "jwt";
    private static final String COOKIE_NAME = "Cookie";

    @Value("${jwt.token.expiration:3600}")
    private int tokenExpiration;

    /**
     * Retrieves the value of a specific cookie from a cookie header string.
     * Searches for the cookie with the given name within the cookie header,
     * which may contain multiple cookies separated by semicolons.
     *
     * @param cookieHeader the string containing all cookies, typically from an HTTP cookie header
     * @return the value of the specified cookie if found, otherwise null
     */
    private static String getCookieFromHeader(String cookieHeader) {
        // split multiple cookies (typically separated by semicolons)
        String[] cookies = cookieHeader.split(";");
        String cookieQualifier = CookiesService.COOKIE_JWT + "=";
        for (String cookie : cookies) {
            cookie = cookie.trim();
            // if the cookie starts with "token=", extract the token value
            if (cookie.startsWith(cookieQualifier)) return cookie.substring(cookieQualifier.length());
        }
        return null;
    }

    /**
     * Extracts the JWT token from the cookies in an incoming HTTP request.
     * Searches for a cookie named "jwt" and retrieves its value.
     *
     * @param request the HttpServletRequest containing the cookies
     * @return the JWT token as a String if found, otherwise null
     */
    public String extractJwtFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (COOKIE_JWT.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    /**
     * Extracts the JWT token from the cookies present in a given WebSocket HandshakeRequest.
     * Searches for cookies in the request headers and attempts to find a cookie named "jwt".
     *
     * @param request the HandshakeRequest containing the headers with cookies
     * @return the JWT token as a String if it is found in the cookies, otherwise null
     */
    public static String extractJwtFromCookies(HandshakeRequest request) {
        // Retrieve the headers from the handshake request
        Map<String, List<String>> headers = request.getHeaders();
        List<String> cookieHeaders = headers.get(COOKIE_NAME);

        if (cookieHeaders != null) {
            // iterate through all cookie header values
            for (String cookieHeader : cookieHeaders) {
                String cookie = getCookieFromHeader(cookieHeader);
                if (cookie != null) return cookie;
            }
        }
        return null;
    }

    /**
     * Creates a new HTTP cookie containing a JSON Web Token (JWT) with a default expiration time.
     * The cookie is configured to be HTTP-only and accessible through the root path.
     *
     * @param token the JWT token to be included in the cookie
     * @return a Cookie object containing the JWT token with default expiration time
     */
    public Cookie buildJwtCookie(String token) {
        return buildJwtCookie(token, tokenExpiration);
    }

    /**
     * Creates a new HTTP cookie containing a JSON Web Token (JWT) with a specified expiration time.
     * The cookie is configured to be HTTP-only, accessible through the root path, and not secure by default.
     *
     * @param token  the JWT token to be included in the cookie
     * @param expiry the maximum age of the cookie in seconds
     * @return a Cookie object containing the JWT token with the specified expiration time
     */
    public Cookie buildJwtCookie(String token, int expiry) {
        Cookie cookie = new Cookie(COOKIE_JWT, token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(expiry);
        cookie.setSecure(false);
        return cookie;
    }

    /**
     * Deletes the JWT cookie by creating a new cookie with an empty value,
     * setting its maximum age to 0, and ensuring it is HTTP-only.
     *
     * @return a Cookie object that represents the deleted JWT cookie.
     */
    public Cookie deleteJwtCookie() {
        return buildJwtCookie("", 0);
    }
}
