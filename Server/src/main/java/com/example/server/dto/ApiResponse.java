package com.example.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * A generic response wrapper for API responses.
 * This class is used to wrap the data returned by API endpoints.
 *
 * @param <T> the type of the data being wrapped in the response
 */
@AllArgsConstructor
@Data
public class ApiResponse<T> {

    /**
     * The data returned in the response.
     *
     * @param data the actual data returned by the API endpoint
     */
    private T data;
}
