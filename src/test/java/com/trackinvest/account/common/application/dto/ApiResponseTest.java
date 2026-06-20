package com.trackinvest.account.common.application.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ApiResponseTest {

    @Test
    void shouldCreateSuccessResponse() {
        String data = "test-data";
        String message = "Operation successful";

        ApiResponse<String> response = ApiResponse.success(data, message);

        assertTrue(response.success());
        assertEquals(message, response.message());
        assertEquals(data, response.data());
        assertNull(response.errors());
        assertNotNull(response.timestamp());
    }

    @Test
    void shouldCreateErrorResponse() {
        String message = "Operation failed";
        String errors = "error-detail";

        ApiResponse<String> response = ApiResponse.error(message, errors);

        assertFalse(response.success());
        assertEquals(message, response.message());
        assertNull(response.data());
        assertEquals(errors, response.errors());
        assertNotNull(response.timestamp());
    }

    @Test
    void shouldCreateErrorResponseWithNullErrors() {
        ApiResponse<String> response = ApiResponse.error("error", null);

        assertFalse(response.success());
        assertNull(response.errors());
    }

    @Test
    void shouldCreateSuccessResponseWithNullData() {
        ApiResponse<String> response = ApiResponse.success(null, "ok");

        assertTrue(response.success());
        assertNull(response.data());
    }
}
