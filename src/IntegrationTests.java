import static io.restassured.RestAssured.given;
import org.testng.Assert;
import org.testng.annotations.Test;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class IntegrationTests {
	private static String baseUri = "https://api.nasa.gov/planetary/apod";
	public String apikey = "yLqPRMojwllyUw1FKUAR6LVcGBTUuhh32eB1ZQlV";
	public Response response;
	
  @Test
  //Validate the request to get the Astronomy Picture of the day is successful
  public void getAstronomyPictureOfTheDay() {
	  RestAssured.baseURI = baseUri;
	  response = 
			  given().log().all().queryParams("api_key", apikey)
			  .when().get();
	  int statusCode = response.getStatusCode();
	  Assert.assertEquals(statusCode, 200, "Incorrect status code returned, the request was not successful");
  }
  
  @Test
  //Validate a bad request is returned when the date is greater than the actual
  public void getAstronomyPictureOfInvalidDay() {
	  RestAssured.baseURI = baseUri;
	  String invalidDate = "2028-09-16";
	  response = 
			  given().log().all().queryParams("date", invalidDate, "api_key", apikey)
	  		  .when().get();
	  int statusCode = response.getStatusCode();
	  String statusLine = response.statusLine();
	  Assert.assertEquals(statusCode, 400, "Incorrect status code returned");
	  Assert.assertEquals("HTTP/1.1 400 Bad Request", statusLine, "Incorrect status line");
  }
  
  @Test
  //No response is returned if there is no API Key, this tests is designed to failed
  public void getPictureWithoutKey() {
	  RestAssured.baseURI = baseUri;
	  response = 
			  given().log().all()
	  		  .when().get();
	  int statusCode = response.getStatusCode();
	  String statusLine = response.statusLine();
	  Assert.assertEquals(statusCode, 200, "The request is not successful, the current status code is: " + statusLine + "\n");
  }
}
