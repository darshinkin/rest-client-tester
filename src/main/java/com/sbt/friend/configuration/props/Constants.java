package com.sbt.friend.configuration.props;

public final class Constants {

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
}
