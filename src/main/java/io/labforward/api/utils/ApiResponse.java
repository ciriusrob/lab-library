package io.labforward.api.utils;

import io.labforward.api.models.Error;
import io.labforward.api.models.Pager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * Created by: Robert Wilson
 * Date:  19/04/2021
 * Project: api
 * Package: io.labforward.api.models
 * Class: ApiResponse
 */
public class ApiResponse<T> extends ResponseEntity<T>
{
    public ApiResponse(HttpStatus status, T body, List<Error> errors)
    {
        super((T) new CustomBody<>(status, body, errors), status);
    }

    public ApiResponse( HttpStatus status, Pager pager, T body, List<Error> errors)
    {
        super((T) new CustomBody<>(status, pager, body, errors), status);
    }

    public ApiResponse( HttpStatus status, List<Error> errors)
    {
        super((T) new CustomBody<>(status, null, null, errors), status);
    }
}