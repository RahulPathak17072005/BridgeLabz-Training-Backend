package com.greet;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Spring Boot Greeting API Integration Tests (REST Assured)")
public class SpringBootGreetingApplicationTests {
    @LocalServerPort
    private int port;
    private static String adminToken;
    private static String userToken;
    private static Long createdGreetingId;

    private static String uniqueAdminUser;
    private static String uniqueStandardUser;
    @BeforeAll
    public static void initTestData() {
        String uniqueId = UUID.randomUUID().toString().substring(0, 8);
        uniqueAdminUser = "admin_" + uniqueId;
        uniqueStandardUser = "user_" + uniqueId;
    }
    @BeforeEach
    public void setup() {
        RestAssured.port = port;
        RestAssured.basePath = "/api";
    }
    // ==========================================
    // 1. REGISTRATION & AUTHENTICATION TESTS
    // ==========================================
    @Test
    @Order(1)
    @DisplayName("Register Admin User -> Expected: 200 OK")
    public void registerAdminUser() {
        Map<String, Object> adminPayload = new HashMap<>();
        adminPayload.put("username", uniqueAdminUser);
        adminPayload.put("password", "adminPassword123");
        adminPayload.put("email", uniqueAdminUser + "@example.com");
        adminPayload.put("role", "ADMIN");
        given()
                .contentType(ContentType.JSON)
                .body(adminPayload)
                .when()
                .post("/auth/register")
                .then()
                .statusCode(200)
                .body("message", equalTo("User registered successfully"));
    }
    @Test
    @Order(2)
    @DisplayName("Register Standard User -> Expected: 200 OK")
    public void registerStandardUser() {
        Map<String, Object> userPayload = new HashMap<>();
        userPayload.put("username", uniqueStandardUser);
        userPayload.put("password", "userPassword123");
        userPayload.put("email", uniqueStandardUser + "@example.com");
        userPayload.put("role", "USER");
        given()
                .contentType(ContentType.JSON)
                .body(userPayload)
                .when()
                .post("/auth/register")
                .then()
                .statusCode(200)
                .body("message", equalTo("User registered successfully"));
    }
    @Test
    @Order(3)
    @DisplayName("Authenticate Admin -> Expected: Token Received")
    public void authenticateAdmin() {
        Map<String, String> loginPayload = new HashMap<>();
        loginPayload.put("username", uniqueAdminUser);
        loginPayload.put("password", "adminPassword123");
        Response response = given()
                .contentType(ContentType.JSON)
                .body(loginPayload)
                .when()
                .post("/auth/login");
        response.then()
                .statusCode(200)
                .body("message", equalTo("Login successful"))
                .body("token", startsWith("Bearer "))
                .body("role", equalTo("ADMIN"));
        adminToken = response.jsonPath().getString("token");
        Assertions.assertNotNull(adminToken);
    }
    @Test
    @Order(4)
    @DisplayName("Authenticate Standard User -> Expected: Token Received")
    public void authenticateStandardUser() {
        Map<String, String> loginPayload = new HashMap<>();
        loginPayload.put("username", uniqueStandardUser);
        loginPayload.put("password", "userPassword123");
        Response response = given()
                .contentType(ContentType.JSON)
                .body(loginPayload)
                .when()
                .post("/auth/login");
        response.then()
                .statusCode(200)
                .body("message", equalTo("Login successful"))
                .body("token", startsWith("Bearer "))
                .body("role", equalTo("USER"));
        userToken = response.jsonPath().getString("token");
        Assertions.assertNotNull(userToken);
    }
    // ==========================================
    // 2. AUTHORIZATION & SECURITY PROTECTION TESTS
    // ==========================================
    @Test
    @Order(5)
    @DisplayName("Fetch Greetings List without Token -> Expected: 401 Unauthorized")
    public void getGreetingsWithoutToken() {
        given()
                .when()
                .get("/greetings")
                .then()
                .statusCode(401)
                .body("error", equalTo("Missing or invalid Authorization header token"));
    }
    @Test
    @Order(6)
    @DisplayName("Fetch Greetings List with Invalid Token -> Expected: 401 Unauthorized")
    public void getGreetingsWithInvalidToken() {
        given()
                .header("Authorization", "Bearer invalidTokenValues")
                .when()
                .get("/greetings")
                .then()
                .statusCode(401)
                .body("error", equalTo("Invalid or expired JWT token"));
    }
    @Test
    @Order(7)
    @DisplayName("Create Greeting with Standard USER Token -> Expected: 403 Forbidden")
    public void createGreetingAsUserRole() {
        Map<String, String> payload = new HashMap<>();
        payload.put("message", "Attempting unauthorized write");
        given()
                .header("Authorization", userToken)
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .post("/greetings")
                .then()
                .statusCode(403)
                .body("error", equalTo("Access denied. Admin role required."));
    }
    // ==========================================
    // 3. GREETING CRUD OPERATIONS (ADMIN REQUIRED)
    // ==========================================
    @Test
    @Order(8)
    @DisplayName("Create Greeting as ADMIN -> Expected: 201 Created")
    public void createGreetingAsAdmin() {
        Map<String, String> payload = new HashMap<>();
        payload.put("message", "REST Assured Integration Greeting!");
        Response response = given()
                .header("Authorization", adminToken)
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .post("/greetings");
        response.then()
                .statusCode(201)
                .body("message", equalTo("REST Assured Integration Greeting!"))
                .body("id", notNullValue())
                .body("createdByUsername", equalTo(uniqueAdminUser));
        createdGreetingId = response.jsonPath().getLong("id");
    }
    @Test
    @Order(9)
    @DisplayName("Read Greetings List with USER Token -> Expected: 200 OK & List Contained")
    public void getGreetingsAsUser() {
        given()
                .header("Authorization", userToken)
                .when()
                .get("/greetings")
                .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(1))
                .body("message", hasItem("REST Assured Integration Greeting!"));
    }
    @Test
    @Order(10)
    @DisplayName("Update Greeting with Standard USER Token -> Expected: 403 Forbidden")
    public void updateGreetingAsUser() {
        Map<String, String> payload = new HashMap<>();
        payload.put("message", "Standard user trying to change message");
        given()
                .header("Authorization", userToken)
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .put("/greetings/" + createdGreetingId)
                .then()
                .statusCode(403)
                .body("error", equalTo("Access denied. Admin role required."));
    }
    @Test
    @Order(11)
    @DisplayName("Update Greeting with ADMIN Token -> Expected: 200 OK")
    public void updateGreetingAsAdmin() {
        Map<String, String> payload = new HashMap<>();
        payload.put("message", "Updated Greeting Message via REST Assured!");
        given()
                .header("Authorization", adminToken)
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .put("/greetings/" + createdGreetingId)
                .then()
                .statusCode(200)
                .body("message", equalTo("Updated Greeting Message via REST Assured!"));
    }
    @Test
    @Order(12)
    @DisplayName("Delete Greeting with Standard USER Token -> Expected: 403 Forbidden")
    public void deleteGreetingAsUser() {
        given()
                .header("Authorization", userToken)
                .when()
                .delete("/greetings/" + createdGreetingId)
                .then()
                .statusCode(403)
                .body("error", equalTo("Access denied. Admin role required."));
    }
    @Test
    @Order(13)
    @DisplayName("Delete Greeting with ADMIN Token -> Expected: 200 OK")
    public void deleteGreetingAsAdmin() {
        given()
                .header("Authorization", adminToken)
                .when()
                .delete("/greetings/" + createdGreetingId)
                .then()
                .statusCode(200)
                .body("message", equalTo("Greeting deleted successfully"));
    }
    @Test
    @Order(14)
    @DisplayName("Verify Deleted Greeting is Not Found on Update -> Expected: 404 Not Found")
    public void verifyDeletedGreeting() {
        Map<String, String> payload = new HashMap<>();
        payload.put("message", "Updating non-existent greeting");
        given()
                .header("Authorization", adminToken)
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .put("/greetings/" + createdGreetingId)
                .then()
                .statusCode(404);
    }
}