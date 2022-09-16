import org.testng.Assert;
import org.testng.annotations.Test;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import static io.restassured.RestAssured.*;
import java.util.ArrayList;
import java.util.List;

public class NasaAPI {
	private static String baseUri = "https://api.nasa.gov/mars-photos/api/v1";
	public String path;
	public Response response;
	
	public String apikey = "yLqPRMojwllyUw1FKUAR6LVcGBTUuhh32eB1ZQlV";
	public int solvalue = 1000;
	public String earthDate;
	
	public List<Object> photoRecords;
	public List<Object> firstPhotos;
	

 @Test
  //Retrieve the first 10 Mars photos made by "Curiosity" on 1000 Martian sol.
  public void photosOnMartianSol() {
	  RestAssured.baseURI = baseUri;
	  path = "/rovers/curiosity/photos";
	  
	  response = 
		given().log().all().queryParams("sol", solvalue, "api_key", apikey)
		.when().get(path);
	  Assert.assertEquals(response.getStatusCode(), 200, "Unsuccessful request");
	  
	  photoRecords = response.jsonPath().getList("photos");
	  firstPhotos = new ArrayList<Object>();
	  for(int i=0; i<10; i++){
		  firstPhotos.add(photoRecords.get(i));
	  }
	  
	  System.out.println("\nThe first 10 Mars photos made by Curiosity on 1000 Martian sol are:");
	  firstPhotos.forEach(System.out::println);
  }
  
  
  @Test
  //Retrieve the first 10 Mars photos made by "Curiosity" on Earth date equal to 1000 Martian sol.
  public void photosOnEarthDate() {
	  RestAssured.baseURI = baseUri;
	  path = "/rovers/curiosity/photos";
	  
	  response =
		given().log().all().queryParams("sol", solvalue, "api_key", apikey)
		.when().get(path);
	  Assert.assertEquals(response.getStatusCode(), 200, "Unsuccessful request");
	  earthDate = response.jsonPath().getString("photos[1].earth_date");
	  
	  response =
		given().log().all().queryParams("earth_date", earthDate, "api_key", apikey)
		.when().get(path);
	  Assert.assertEquals(response.getStatusCode(), 200, "Unsuccessful request");
	  photoRecords = response.jsonPath().getList("photos");
	  firstPhotos = new ArrayList<Object>();
	  for(int i=0; i<10; i++){
		  firstPhotos.add(photoRecords.get(i));
	  }
	  
	  System.out.println("\nThe first 10 Mars photos made by Curiosity on " + earthDate + " are: ");
	  firstPhotos.forEach(System.out::println);  
  }
 
  @Test
  //Retrieve and compare the first 10 Mars photos made by "Curiosity" on 1000 sol and on Earth date equal to 1000 Martian sol.
  public void photoComparison() {
	  RestAssured.baseURI = baseUri;
	  path = "/rovers/curiosity/photos";
	  
	  response = 
		given().log().all().queryParams("sol", solvalue, "api_key", apikey)
		.when().get(path);
	  Assert.assertEquals(response.getStatusCode(), 200, "Unsuccessful request");
	  List<Object> solPhotos = response.jsonPath().getList("photos");
	  List<Object> firstSolPhotos = new ArrayList<Object>();
	  for(int i=0; i<10; i++){
		  firstSolPhotos.add(solPhotos.get(i));
	  }
	
	  earthDate = response.jsonPath().getString("photos[1].earth_date");
	  response =
		given().log().all().queryParams("earth_date", earthDate, "api_key", apikey)
		.when().get(path);
	  Assert.assertEquals(response.getStatusCode(), 200, "Unsuccessful request");
	  List<Object> earthDatePhotos = response.jsonPath().getList("photos");
	  List<Object> firstEarthDatePhotos = new ArrayList<Object>();
	  for(int i=0; i<10; i++){
		  firstEarthDatePhotos.add(earthDatePhotos.get(i));
	  }
	  
	  System.out.println("\nThe first 10 Mars photos made by Curiosity on 1000 Martian sol are:");
	  firstSolPhotos.forEach(System.out::println);
	  System.out.println("\nThe first 10 Mars photos made by Curiosity on " + earthDate + " are: ");
	  firstEarthDatePhotos.forEach(System.out::println);  
	  
	  Assert.assertEquals(firstSolPhotos, firstEarthDatePhotos, "The first 10 photos are not the same.");
  }
  
  @Test
  //Validate that the amounts of pictures that each "Curiosity" camera took on 1000 Mars sol is not greater than 10 times the amount taken by other cameras on the same date.
  public void pictureAmount() {
	  RestAssured.baseURI = baseUri;
	  
	  path = "/manifests/Curiosity";
	  response = 
		given().log().all().queryParams("api_key", apikey)
		.when().get(path);
	  Assert.assertEquals(response.getStatusCode(), 200, "Unsuccessful request");
	  String recordCuriosity = response.jsonPath().getString("photo_manifest.photos.find{it.sol=1000}");
	  int totalPhotosCuriosity;
	  if (recordCuriosity != null) {
		  totalPhotosCuriosity = response.jsonPath().getInt("photo_manifest.photos.find{it.sol==1000}.total_photos");
	  }
	  else {
		  totalPhotosCuriosity = 0;
	  }
	  
	  path = "/manifests/Opportunity";
	  response = 
				given().log().all().queryParams("api_key", apikey)
				.when().get(path);
	  Assert.assertEquals(response.getStatusCode(), 200, "Unsuccessful request");
	  String recordOpportunity = response.jsonPath().getString("photo_manifest.photos.find{it.earth_date=='2015-05-30'}");
	  int totalPhotosOpportunity;
	  if (recordOpportunity != null) {
		  totalPhotosOpportunity = response.jsonPath().getInt("photo_manifest.photos.find{it.earth_date=='2015-05-30'}.total_photos");
	  }
	  else {
		  totalPhotosOpportunity = 0;
	  }
	  
	  path = "/manifests/Spirit";
	  response = 
				given().log().all().queryParams("api_key", apikey)
				.when().get(path);
	  Assert.assertEquals(response.getStatusCode(), 200, "Unsuccessful request");
	  String recordSpirit = response.jsonPath().getString("photo_manifest.photos.find{it.earth_date=='2015-05-30'}");
	  int totalPhotosSpirit;
	  if (recordSpirit != null) {
		  totalPhotosSpirit = response.jsonPath().getInt("photo_manifest.photos.find{it.earth_date=='2015-05-30'}.total_photos");
	  }
	  else {
		  totalPhotosSpirit = 0;
	  }
	  
	  System.out.println("\nCuriosity took " + totalPhotosCuriosity + " photos");
	  System.out.println("Opportunity took " + totalPhotosOpportunity + " photos");
	  System.out.println("Spirit took " + totalPhotosSpirit + " photos");
	  
	  Assert.assertTrue(totalPhotosCuriosity < 10*(totalPhotosOpportunity + totalPhotosSpirit), "The number of Curiosity photos is not greater\n");
  }
}
