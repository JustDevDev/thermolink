package org.skomi.pilot.googleapi.controller;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.skomi.pilot.googleapi.model.CityResponse;
import org.skomi.pilot.googleapi.service.CitySearchService;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

public class CitySearchControllerTest {

    @Mock
    private CitySearchService citySearchService;

    @InjectMocks
    private CitySearchController citySearchController;

    public CitySearchControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldReturnListOfCitiesWhenPlaceIsValid() {
        // given
        String place = "New York";
        List<CityResponse> mockCities = new ArrayList<>();
        mockCities.add(new CityResponse("New York City", "USA"));
        given(citySearchService.searchCities(place)).willReturn(mockCities);

        // when
        ResponseEntity<List<CityResponse>> response = citySearchController.getCities(place);

        // then
        verify(citySearchService).searchCities(place);
        assertThat(response.getStatusCodeValue(), is(200));
        assertThat(response.getBody(), is(mockCities));
    }

    @Test
    public void shouldReturnEmptyListWhenNoCitiesFound() {
        // given
        String place = "UnknownPlace";
        given(citySearchService.searchCities(place)).willReturn(new ArrayList<>());

        // when
        ResponseEntity<List<CityResponse>> response = citySearchController.getCities(place);

        // then
        verify(citySearchService).searchCities(place);
        assertThat(response.getStatusCodeValue(), is(200));
        assertThat(response.getBody().isEmpty(), is(true));
    }
}