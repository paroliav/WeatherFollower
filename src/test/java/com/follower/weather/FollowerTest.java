package com.follower.weather;



import io.restassured.RestAssured;
import io.restassured.response.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertTrue;

public class FollowerTest {

    @Before
    public void setup() {
        RestAssured.baseURI = "https://api.openweathermap.org/data/2.5/";
        RestAssured.port = 443;
    }

    @Test
    public void testResponseIsForSydney() {
        given().
            pathParam("q","Sydney").
            pathParam("cnt", "40").
            pathParam("units", "metric").
            pathParam("appid", "e31e96ba2778a85558cf36c96cb38838").
        when().
            get("forecast?q={q}&cnt={cnt}&units={units}&appid={appid}").
        then().
            statusCode(200).
            assertThat()
                .body("city.name", equalTo("Sydney"));
    }

    @Test
    public void testNextThursdayIsWarmerThan10Deegrees() {
        Response response = RestAssured.get("forecast?q=Sydney&cnt=40&units=metric&appid=e31e96ba2778a85558cf36c96cb38838");
        List<Map<String, Object>> weathers = response.jsonPath().getList("list");
        List<Map<String, Object>> epoch_weather = weathers.stream()
                      .filter(weather -> weather.get("dt").toString().equals(nextThursdayAsEpoch()))
                      .collect(Collectors.toList());

        ObjectMapper oMapper = new ObjectMapper();
        Map<String, Float> weather_main;
        epoch_weather.stream().map(map -> map.get("main"))
                              .forEach(obj -> assertTrue(getMinTemperature(obj) > 10.0));

    }

    private Double getMinTemperature(Object obj){
        ObjectMapper oMapper = new ObjectMapper();
        Map<String, Double> map = oMapper.convertValue(obj, Map.class);
        return map.get("temp_min");
    }

    private String nextThursdayAsEpoch(){
        LocalDate date = LocalDate.now();
        LocalDate nextThursday = date.with( TemporalAdjusters.next( DayOfWeek.THURSDAY ) ) ;
        ZoneId zoneId = ZoneId.of("UTC");
        long epoch = nextThursday.atStartOfDay(zoneId).toEpochSecond();
        return Long.toString(epoch);
    }

}
