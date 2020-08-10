# Open Weather API challenge

## Project Specifications

This project connects to the Open Weather REST APIs to check weather for next Thursday
- Contains a single test file ```FollowerTest.java``` which is located inside the ```test``` folder 

## Setup for the project

This is a Java maven project that uses RestAssured with Hamcrest to test Open Weather REST API.

```bash
mvn clean install
```

## Tests

- Calls the ```forecast``` endpoint on Open Weather server, Request is made in ```metric``` unit system
 ```(NOTE: daily endpoint does not work anymore. It gives API key error)```
 
 Checks the call returns a response for Sydney city
 
 ```$xslt
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
```

- Calls and collects the forecast of days
- Filters the API response for next Thursday (Unix UTC timestamp) 
  as the API return the timestamp as UNIX UTC in field ```"dt"``` inside the ```list```
  
### Sample Response

```json5
{
    "cod": "200",
    "message": 0,
    "cnt": 2,
    "list": [
        {
            "dt": 1596985200,
            "main": {
                "temp": 12.84,
                "feels_like": 7.18,
                "temp_min": 12.84,
                "temp_max": 13.21,
                "pressure": 1005,
                "sea_level": 1009,
                "grnd_level": 1001,
                "humidity": 88,
                "temp_kf": -0.37
            },
            "weather": [
                {
                    "id": 500,
                    "main": "Rain",
                    "description": "light rain",
                    "icon": "10n"
                }
            ],
            "clouds": {
                "all": 95
            },
            "wind": {
                "speed": 8.51,
                "deg": 147
            },
            "visibility": 8300,
            "pop": 0.7,
            "rain": {
                "3h": 1.43
            },
            "sys": {
                "pod": "n"
            },
            "dt_txt": "2020-08-09 15:00:00"
        },
        {
            "dt": 1596996000,
            "main": {
                "temp": 13.52,
                "feels_like": 5.14,
                "temp_min": 13.52,
                "temp_max": 13.81,
                "pressure": 1008,
                "sea_level": 1010,
                "grnd_level": 1002,
                "humidity": 85,
                "temp_kf": -0.29
            },
            "weather": [
                {
                    "id": 501,
                    "main": "Rain",
                    "description": "moderate rain",
                    "icon": "10n"
                }
            ],
            "clouds": {
                "all": 94
            },
            "wind": {
                "speed": 12.46,
                "deg": 148
            },
            "visibility": 7346,
            "pop": 0.96,
            "rain": {
                "3h": 4.31
            },
            "sys": {
                "pod": "n"
            },
            "dt_txt": "2020-08-09 18:00:00"
        }
    ],
    "city": {
        "id": 2147714,
        "name": "Sydney",
        "coord": {
            "lat": -33.8679,
            "lon": 151.2073
        },
        "country": "AU",
        "population": 1000000,
        "timezone": 36000,
        "sunrise": 1597005566,
        "sunset": 1597044106
    }
}
```

- Finds the next Thursday and converts to Unix UTC

```bash
    private String nextThursdayAsEpoch(){
        LocalDate date = LocalDate.now();
        LocalDate nextThursday = date.with( TemporalAdjusters.next( DayOfWeek.THURSDAY ) ) ;
        ZoneId zoneId = ZoneId.of("UTC");
        long epoch = nextThursday.atStartOfDay(zoneId).toEpochSecond();
        return Long.toString(epoch);
    }
```

- Compares if the temperature for this date is greater than 10 degrees

## How to run

From Commandline 

```bash
mvn test
```

This runs both the tests

Tests can also be run from the editor by either right-click on the file or individual test cases




