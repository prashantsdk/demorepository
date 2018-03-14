package com.blueplanet.smartcookieteacher.communication;

/**
 * Created by web on 30-06-2015.
 * @author dhanashree.ghayal
 */
public class HTTPConstants {
    public static final int HTTP_COMM_SUCCESS = 200; // OK: Success
    public static final int HTTP_SUCCESS = 0; // OK: Success in case of forgot password
    public static final int HTTP_COM_NO_CONTENT = 204; // No Content

    // Bad Request: The web-service request was invalid.
    public static final int HTTP_COMM_ERR_BAD_REQUEST = 400;

    // Unauthorized: Authentication for the web-service request failed.
    // Invalid credentials.
    public static final int HTTP_COMM_ERR_UNAUTHORIZED = 401;

    // Forbidden: Web-service request is requesting a resource that the
    // server does not allow access to.
    public static final int HTTP_COMM_ERR_FORBIDDEN = 403;

    // Not Found: The requested resource is not found.
    public static final int HTTP_COMM_ERR_NOT_FOUND = 404;

    // Internal Server Error: An internal server error occurred while
    // processing the request.
    public static final int HTTP_COMM_ERR_INTERNAL_SERVER_ERROR = 500;

    // Bad Gateway: FFCC-FOS Web-service server is not reachable.
    public static final int HTTP_COMM_ERR_BAD_GETWAY = 502;

    public static final int HTTP_COMM_CONFLICT = 409;
    public static final int HTTP_INVALID_INPUT = 1000;

    // Service Unavailable: The server is currently unable to handle the
    // request due to a temporary overloading or maintenance of the server.
    // The implication is that this is a temporary condition which will be
    // alleviated after some delay.
    public static final int HTTP_COMM_ERR_SERVICE_UNAVAILABLE = 503;

    public static final int HTTP_COMM_ERR_NETWORK_TIMEOUT = 601;

    public static final int HTTP_NO_NETWORK = -1;

    public static final int HTTP_DESC_NOT_FOUND = -2;

    public static final int RULE_ENGINE = 1006;


}
