package org.skomi.pilot.ui.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skomi.pilot.auth.service.JwtTokenService;
import org.skomi.pilot.shared.model.DashboardContinentDto;
import org.skomi.pilot.shared.model.DashboardKpisDto;
import org.skomi.pilot.shared.model.DashboardPlcsDto;
import org.skomi.pilot.shared.model.User;
import org.skomi.pilot.shared.repository.UserRepository;
import org.skomi.pilot.shared.service.CookiesService;
import org.skomi.pilot.ui.model.DashboardPlcWithSensorsDto;
import org.skomi.pilot.ui.service.DashboardKpiService;
import org.skomi.pilot.ui.service.DashboardPlcService;
import org.skomi.pilot.ui.service.DashboardSensorService;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class DashboardControllerTest {

    @Mock
    private CookiesService cookiesService;

    @Mock
    private JwtTokenService jwtTokenService;

    @Mock
    private DashboardPlcService dashboardPlcService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private DashboardSensorService dashboardSensorService;

    @Mock
    private DashboardKpiService dashboardKpiService;

    @Test
    void testGetPlcInfos_ReturnsListOfPlcs() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        String dummyToken = "dummyToken";
        String dummyEmail = "user@example.com";
        UUID userId = UUID.randomUUID();
        DashboardPlcsDto mockPlc1 = new DashboardPlcsDto("1", "1");
        DashboardPlcsDto mockPlc2 = new DashboardPlcsDto("2", "2");
        List<DashboardPlcsDto> mockPlcs = List.of(mockPlc1, mockPlc2);

        User mockUser = new User();
        mockUser.setId(userId);

        given(cookiesService.extractJwtFromCookies(request)).willReturn(dummyToken);
        given(jwtTokenService.getUserEmailFromToken(dummyToken)).willReturn(dummyEmail);
        given(userRepository.findByUserEmail(dummyEmail)).willReturn(Optional.of(mockUser));
        given(dashboardPlcService.getAllPlcs(userId)).willReturn(mockPlcs);

        DashboardController controller = new DashboardController(cookiesService, jwtTokenService, dashboardPlcService, userRepository, null, null);

        // when
        ResponseEntity<List<DashboardPlcsDto>> response = controller.getPlcInfos(request);

        // then
        assertThat(response.getStatusCodeValue(), is(200));
        assertThat(response.getBody(), is(notNullValue()));
        assertThat(response.getBody(), hasSize(2));
        assertThat(response.getBody(), contains(mockPlc1, mockPlc2));
    }

    @Test
    void testGetPlcInfosForPlcWithSensors_ReturnsPlcWithSensors() throws InterruptedException {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        String dummyToken = "dummyToken";
        String dummyEmail = "user@example.com";
        UUID plcId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        DashboardPlcWithSensorsDto mockPlcWithSensors = mock(DashboardPlcWithSensorsDto.class);

        User mockUser = new User();
        mockUser.setId(userId);

        given(cookiesService.extractJwtFromCookies(request)).willReturn(dummyToken);
        given(jwtTokenService.getUserEmailFromToken(dummyToken)).willReturn(dummyEmail);
        given(userRepository.findByUserEmail(dummyEmail)).willReturn(Optional.of(mockUser));
        given(dashboardPlcService.getPlcTemperaturesMeasuredOnSensors(plcId, userId)).willReturn(mockPlcWithSensors);

        DashboardController controller = new DashboardController(cookiesService, jwtTokenService, dashboardPlcService, userRepository, null, null);

        // when
        ResponseEntity<DashboardPlcWithSensorsDto> response = controller.getPlcChartInfos(request, plcId);

        // then
        assertThat(response.getStatusCodeValue(), is(200));
        assertThat(response.getBody(), is(notNullValue()));
        assertThat(response.getBody(), is(mockPlcWithSensors));
    }

    @Test
    void testGetPlcInfosForPlcWithSensors_ThrowsWhenPlcNotFound() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        String dummyToken = "dummyToken";
        String dummyEmail = "user@example.com";
        UUID plcId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        User mockUser = new User();
        mockUser.setId(userId);

        given(cookiesService.extractJwtFromCookies(request)).willReturn(dummyToken);
        given(jwtTokenService.getUserEmailFromToken(dummyToken)).willReturn(dummyEmail);
        given(userRepository.findByUserEmail(dummyEmail)).willReturn(Optional.of(mockUser));
        given(dashboardPlcService.getPlcTemperaturesMeasuredOnSensors(plcId, userId))
                .willThrow(new IllegalArgumentException("PLC with sensors not found"));

        DashboardController controller = new DashboardController(cookiesService, jwtTokenService, dashboardPlcService, userRepository, null, null);

        // when // then
        Exception exception = org.junit.jupiter.api.Assertions.assertThrows(IllegalArgumentException.class,
                () -> controller.getPlcChartInfos(request, plcId));
        assertThat(exception.getMessage(), is("PLC with sensors not found"));
    }

    @Test
    void testGetPlcInfosForPlcWithSensors_ThrowsWhenUserNotFound() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        String dummyToken = "dummyToken";
        String dummyEmail = "user@example.com";
        UUID plcId = UUID.randomUUID();

        given(cookiesService.extractJwtFromCookies(request)).willReturn(dummyToken);
        given(jwtTokenService.getUserEmailFromToken(dummyToken)).willReturn(dummyEmail);
        given(userRepository.findByUserEmail(dummyEmail)).willReturn(Optional.empty());

        DashboardController controller = new DashboardController(cookiesService, jwtTokenService, dashboardPlcService, userRepository, null, null);

        // when // then
        Exception exception = org.junit.jupiter.api.Assertions.assertThrows(NoSuchElementException.class,
                () -> controller.getPlcChartInfos(request, plcId));
        assertThat(exception.getMessage(), is("No value present"));
    }

    @Test
    void testGetContinentsChart_ReturnsListOfContinents() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        String dummyToken = "dummyToken";
        String dummyEmail = "user@example.com";
        UUID userId = UUID.randomUUID();
        DashboardPlcsDto mockPlc1 = new DashboardPlcsDto("1", "1");
        DashboardPlcsDto mockPlc2 = new DashboardPlcsDto("2", "2");
        List<DashboardContinentDto> continents = List.of(new DashboardContinentDto("continent1", 2), new DashboardContinentDto("continent2", 1));

        User mockUser = new User();
        mockUser.setId(userId);

        given(cookiesService.extractJwtFromCookies(request)).willReturn(dummyToken);
        given(jwtTokenService.getUserEmailFromToken(dummyToken)).willReturn(dummyEmail);
        given(userRepository.findByUserEmail(dummyEmail)).willReturn(Optional.of(mockUser));
        given(dashboardSensorService.getContinentCountsOnUserSensor(userId)).willReturn(continents);

        DashboardController controller = new DashboardController(
                cookiesService,
                jwtTokenService,
                dashboardPlcService,
                userRepository,
                dashboardSensorService,
                null
        );

        // when
        ResponseEntity<List<DashboardContinentDto>> response = controller.getContinentsChart(request);

        // then
        assertThat(response.getStatusCodeValue(), is(200));
        assertThat(response.getBody(), is(notNullValue()));
        assertThat(response.getBody(), hasSize(2));
        assertThat(response.getBody(), contains(continents.get(0), continents.get(1)));
    }

    @Test
    void testGetContinentsChart_ThrowsWhenUserNotFound() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        String dummyToken = "dummyToken";
        String dummyEmail = "user@example.com";

        given(cookiesService.extractJwtFromCookies(request)).willReturn(dummyToken);
        given(jwtTokenService.getUserEmailFromToken(dummyToken)).willReturn(dummyEmail);
        given(userRepository.findByUserEmail(dummyEmail)).willReturn(Optional.empty());

        DashboardController controller = new DashboardController(
                cookiesService,
                jwtTokenService,
                dashboardPlcService,
                userRepository,
                dashboardSensorService,
                null
        );

        // when // then
        Exception exception = org.junit.jupiter.api.Assertions.assertThrows(NoSuchElementException.class,
                () -> controller.getContinentsChart(request));

        assertThat(exception.getMessage(), is("No value present"));
    }

    @Test
    void testGetDashboardKpis_ReturnsKpis() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        String dummyToken = "dummyToken";
        String dummyEmail = "user@example.com";
        UUID userId = UUID.randomUUID();
        DashboardKpisDto mockKpis = mock(DashboardKpisDto.class);

        User mockUser = new User();
        mockUser.setId(userId);

        given(cookiesService.extractJwtFromCookies(request)).willReturn(dummyToken);
        given(jwtTokenService.getUserEmailFromToken(dummyToken)).willReturn(dummyEmail);
        given(userRepository.findByUserEmail(dummyEmail)).willReturn(Optional.of(mockUser));
        given(dashboardKpiService.getKpis(userId)).willReturn(mockKpis);

        DashboardController controller = new DashboardController(
                cookiesService,
                jwtTokenService,
                dashboardPlcService,
                userRepository,
                dashboardSensorService,
                dashboardKpiService
        );

        // when
        ResponseEntity<DashboardKpisDto> response = controller.getDashboardKpis(request);

        // then
        assertThat(response.getStatusCodeValue(), is(200));
        assertThat(response.getBody(), is(notNullValue()));
        assertThat(response.getBody(), is(mockKpis));
    }

    @Test
    void testGetDashboardKpis_ThrowsWhenUserNotFound() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        String dummyToken = "dummyToken";
        String dummyEmail = "user@example.com";

        given(cookiesService.extractJwtFromCookies(request)).willReturn(dummyToken);
        given(jwtTokenService.getUserEmailFromToken(dummyToken)).willReturn(dummyEmail);
        given(userRepository.findByUserEmail(dummyEmail)).willReturn(Optional.empty());

        DashboardController controller = new DashboardController(
                cookiesService,
                jwtTokenService,
                dashboardPlcService,
                userRepository,
                dashboardSensorService,
                dashboardKpiService
        );

        // when // then
        Exception exception = org.junit.jupiter.api.Assertions.assertThrows(NoSuchElementException.class,
                () -> controller.getDashboardKpis(request));

        assertThat(exception.getMessage(), is("No value present"));
    }
}