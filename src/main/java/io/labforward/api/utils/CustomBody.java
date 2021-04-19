package io.labforward.api.utils;

import io.labforward.api.models.Pager;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.List;

/**
 * Created by: Robert Wilson
 * Date:  19/04/2021
 * Project: api
 * Package: io.labforward.api.models
 * Class: CustomBody
 */
public class CustomBody<T> implements Serializable
{
    private int code;

    private String message;

    private T data;

    private List<Error> errors;

    private Pager pager;

    public CustomBody( HttpStatus code, T data, List<Error> errors )
    {
        this.code = code.value();
        this.message = code.getReasonPhrase();
        this.data = data;
        this.errors = errors;
    }

    public CustomBody( HttpStatus code, Pager pager, T data, List<Error> errors )
    {
        this.code = code.value();
        this.message = code.getReasonPhrase();
        this.pager = pager;
        this.data = data;
        this.errors = errors;
    }

    public int getCode()
    {
        return code;
    }

    public String getMessage()
    {
        return message;
    }

    public Pager getPager()
    {
        return pager;
    }

    public T getData()
    {
        return data;
    }

    public List<Error> getErrors()
    {
        return errors;
    }

    @Override
    public String toString()
    {
        return "CustomBody{" +
            "code=" + code +
            ", message='" + message + '\'' +
            ", data=" + data +
            ", errors=" + errors +
            ", pager=" + pager +
            '}';
    }
}

