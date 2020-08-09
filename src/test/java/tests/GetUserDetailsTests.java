package tests;

import io.restassured.response.Response;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Steps;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import steps.UsersSearchSteps;
import java.util.ArrayList;
import java.util.Map;
import static org.junit.Assert.assertEquals;

/* Tests were written using RestAssured, hamcrest, serenity & JUnit libraries
    To view the reports, run the project using maven "mvn clean verify"
    and the get the report from /target/site/serenity/index.html
* */
@RunWith(SerenityRunner.class)
public class GetUserDetailsTests {
    private static int totalNoOfUsers = 1000;
    private Response userByIdResponse;
    private Response allUsersResponse;
    private Response userByCityResponse;
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String ipAddress;
    private Float latitude;
    private Float longitude;
    private String city;
    private int citySearchId = 101;
    @Steps
    UsersSearchSteps usersSearchSteps;

    /* This test verifies the response got correct total number of users
     * */
    @Test
    public void verifyTotalNumberOfUsers() {
        ArrayList<Map<String, ?>> userArrayList;
        allUsersResponse = usersSearchSteps.searchAllUsers();
        allUsersResponse.then().statusCode(200);
        userArrayList = usersSearchSteps.getAllUsersInArrayList();
        assertEquals(userArrayList.size(), totalNoOfUsers);
    }

    /* This test verifies the response got correct user details,
    when dynamically get the user from total number of users
     * */
    @Test
    public void verifyThatWeCanFindCorrectUserByUsingId() {
        ArrayList<Map<String, ?>> userArrayList;
        usersSearchSteps.searchAllUsers();
        userArrayList = usersSearchSteps.getAllUsersInArrayList();
        if (userArrayList.size() > 0) {
            Map<String, ?> expectedSingleUser = userArrayList.get(0);
            id = (Integer) (expectedSingleUser.get("id"));
            firstName = (String) expectedSingleUser.get("first_name");
            lastName = (String) expectedSingleUser.get("last_name");
            email = (String) expectedSingleUser.get("email");
            ipAddress = (String) expectedSingleUser.get("ip_address");
            latitude = (Float) expectedSingleUser.get("latitude");
            longitude = (Float) expectedSingleUser.get("longitude");
            userByIdResponse = usersSearchSteps.searchUserById(id);
            userByIdResponse.then().statusCode(200);
            userByIdResponse.then().assertThat().body("id", Matchers.equalTo(id));
            userByIdResponse.then().assertThat().body("first_name", Matchers.equalTo(firstName));
            userByIdResponse.then().assertThat().body("last_name", Matchers.equalTo(lastName));
            userByIdResponse.then().assertThat().body("email", Matchers.equalTo(email));
            userByIdResponse.then().assertThat().body("ip_address", Matchers.equalTo(ipAddress));
            userByIdResponse.then().assertThat().body("latitude", Matchers.equalTo(latitude));
            userByIdResponse.then().assertThat().body("longitude", Matchers.equalTo(longitude));
            userByIdResponse.then().assertThat().body("city", Matchers.notNullValue());
        }


    }

    /* This test verifies, status code 404 is received,
     when searched for an user which is not present
         * */
    @Test
    public void checkNonExistingId() {
        userByIdResponse = usersSearchSteps.searchUserById(12356);
        userByIdResponse.then().statusCode(404);
    }

    /* This test verifies the response got correct user details,
        when dynamically get the user details by city
         * */
    @Test
    public void verifyThatWeCanFindCorrectUserByUsingCity() {

        userByIdResponse = usersSearchSteps.searchUserById(citySearchId);
        id = userByIdResponse.jsonPath().getInt("id");
        firstName = userByIdResponse.jsonPath().getString("first_name");
        lastName = userByIdResponse.jsonPath().getString("last_name");
        email = userByIdResponse.jsonPath().getString("email");
        ipAddress = userByIdResponse.jsonPath().getString("ip_address");
        latitude = userByIdResponse.jsonPath().getFloat("latitude");
        longitude = userByIdResponse.jsonPath().getFloat("longitude");
        city = userByIdResponse.jsonPath().getString("city");
        userByCityResponse = usersSearchSteps.searchUserByCity(city);
        userByCityResponse.then().statusCode(200);
        userByCityResponse.then().assertThat().body("id", Matchers.hasItem(id));
        userByCityResponse.then().assertThat().body("first_name", Matchers.hasItem(firstName));
        userByCityResponse.then().assertThat().body("last_name", Matchers.hasItem(lastName));
        userByCityResponse.then().assertThat().body("email", Matchers.hasItem(email));
        userByCityResponse.then().assertThat().body("ip_address", Matchers.hasItem(ipAddress));
        userByCityResponse.then().assertThat().body("latitude", Matchers.hasItem(latitude));
        userByCityResponse.then().assertThat().body("longitude", Matchers.hasItem(longitude));

    }
}
