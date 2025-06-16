package org.skomi.pilot.shared.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class CookiesServiceTest {

    @InjectMocks
    private CookiesService cookiesService;

    @Mock
    private HttpServletRequest mockRequest;

    @Test
    void shouldExtractJwtFromCookies_whenJwtCookieIsPresent() {
        // given
        Cookie jwtCookie = new Cookie(CookiesService.COOKIE_JWT, "testJwtToken");
        Cookie[] cookies = {jwtCookie};
        given(mockRequest.getCookies()).willReturn(cookies);

        // when
        String jwtToken = cookiesService.extractJwtFromCookies(mockRequest);

        // then
        assertThat(jwtToken, is("testJwtToken"));
    }

    @Test
    void shouldReturnNull_whenJwtCookieIsNotPresent() {
        // given
        Cookie someOtherCookie = new Cookie("other", "testValue");
        Cookie[] cookies = {someOtherCookie};
        given(mockRequest.getCookies()).willReturn(cookies);

        // when
        String jwtToken = cookiesService.extractJwtFromCookies(mockRequest);

        // then
        assertThat(jwtToken, is(nullValue()));
    }

    @Test
    void shouldReturnNull_whenCookiesAreNull() {
        // given
        given(mockRequest.getCookies()).willReturn(null);

        // when
        String jwtToken = cookiesService.extractJwtFromCookies(mockRequest);

        // then
        assertThat(jwtToken, is(nullValue()));
    }

    @Test
    void shouldReturnNull_whenJwtCookieIsEmpty() {
        // given
        Cookie jwtCookie = new Cookie(CookiesService.COOKIE_JWT, "");
        Cookie[] cookies = {jwtCookie};
        given(mockRequest.getCookies()).willReturn(cookies);

        // when
        String jwtToken = cookiesService.extractJwtFromCookies(mockRequest);

        // then
        assertThat(jwtToken, is(""));
    }

    @Test
    void shouldBuildJwtCookieWithCorrectProperties() {
        // given
        String token = "testJwtToken";

        // when
        Cookie jwtCookie = cookiesService.buildJwtCookie(token);

        // then
        assertThat(jwtCookie.getName(), is(CookiesService.COOKIE_JWT));
        assertThat(jwtCookie.getValue(), is(token));
        assertThat(jwtCookie.getPath(), is("/"));
        assertThat(jwtCookie.isHttpOnly(), is(true));
        assertThat(jwtCookie.getMaxAge(), is(0)); // default expiration value
        assertThat(jwtCookie.getSecure(), is(false));
    }

    @Test
    void shouldBuildJwtCookieWithCustomExpiry() {
        // given
        String token = "testJwtToken";
        int customExpiry = 7200;

        // when
        Cookie jwtCookie = cookiesService.buildJwtCookie(token, customExpiry);

        // then
        assertThat(jwtCookie.getName(), is(CookiesService.COOKIE_JWT));
        assertThat(jwtCookie.getValue(), is(token));
        assertThat(jwtCookie.getPath(), is("/"));
        assertThat(jwtCookie.isHttpOnly(), is(true));
        assertThat(jwtCookie.getMaxAge(), is(customExpiry));
        assertThat(jwtCookie.getSecure(), is(false));
    }
}