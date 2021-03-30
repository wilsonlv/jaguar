package org.jaguar.core.web;

public enum ResultCode {

    OK(200, "OK"),

    BAD_REQUEST(400, "Bad Request"),

    UNAUTHORIZED(401, "Unauthorized"),

    FORBIDDEN(403, "Forbidden"),

    METHOD_NOT_ALLOWED(405, "Method Not Allowed"),

    CONFLICT(409, "Conflict"),

    TOO_MANY_REQUESTS(429, "Too Many Requests"),

    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),

    BAD_GATEWAY(502, "Bad Gateway"),

    DATA_CRUD_EXCEPTION(1001, "Data Crud Exception");

    private final int value;
    private final String reasonPhrase;

    ResultCode(int value, String reasonPhrase) {
        this.value = value;
        this.reasonPhrase = reasonPhrase;
    }

    public int getValue() {
        return value;
    }

    public String getReasonPhrase() {
        return reasonPhrase;
    }
}
