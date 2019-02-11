package com.sbt.friend;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.sbt.friend.configuration.props.PropertySourceLoader;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public final class RestUtils {

    public static final String REST_AUTH_URI = "auth_rest_url";
    public static final String BASE_URL = "base_url";
    public static final String USER = "frinduser";
    public static final String PASSWORD = "password";
    public static final String JSESSIONID = "cooki_session_id";

    public static final String REST_GET_TEMPLATE = "template";
    public static final String REST_GET_PROTOCOLS = "protocols";
    public static final String REST_GET_NOTIFICATIONS = "notifications";
    public static final String REST_SAVE_REQUEST = "save_request";
    public static final String REST_GET_REQUEST = "get_request";

    static public String getUrl(String base, String path) {
        return String.format("%s%s", base, path);
    }

    static public RequestSpecification getRequestSpecification(PropertySourceLoader psl) {
        String url = RestUtils.getUrl(psl.getProperty(BASE_URL), psl.getProperty(REST_AUTH_URI));
        String body = String.format("{\"arg0\":\"%s\",  \"arg1\":\"%s\"}", psl.getProperty(USER), psl.getProperty(PASSWORD));

        Response post = RestAssured.given().contentType(ContentType.JSON).body(body).post(url);
        String cookie = post.getCookie(psl.getProperty(JSESSIONID));

        System.out.println("-----------------------------------");
        System.out.println(String.format("Authentication rest request. url=[%s], body=[%s], cookie=[%s]", url, body, cookie));

        assertThat(post.getStatusCode()).isEqualTo(200);

        return RestAssured.given().cookie(psl.getProperty(JSESSIONID), cookie);
    }

    static public ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return  objectMapper;
    }

    static public String getJsonObject(String body, PropertySourceLoader psl, String urlPath, ObjectMapper objectMapper, RequestSpecification requestSpecification) throws IOException {
        String url = RestUtils.getUrl(psl.getProperty(BASE_URL), psl.getProperty(urlPath));
        final Response response = requestSpecification.body(body).contentType(ContentType.JSON).
                post(url);
        System.out.println("-----------------------------------");
        System.out.println(String.format("Rest request. url=[%s], body=[%s]", url, body));
        assertEquals(200, response.getStatusCode());
        JsonNode jsonNodeRoot = objectMapper.readTree(response.asString());
        assertNotNull(jsonNodeRoot);
        return jsonNodeRoot.get("object").toString();
    }
}
