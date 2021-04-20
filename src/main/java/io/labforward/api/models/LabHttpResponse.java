package io.labforward.api.models;

import lombok.Data;

import java.util.List;

/**
 * Created by: Robert Wilson
 * Date:  20/04/2021
 * Project: api
 * Package: io.labforward.api.models
 * Class: LabHttpResponse
 */
public class LabHttpResponse<T>
{
    private int code;

    private String message;

    private T data;

    private List<Error> errors;

    private Object pager;

    public LabHttpResponse()
    {
    }

    public LabHttpResponse( int code, String message, T data, List<Error> errors, Object pager )
    {
        this.code = code;
        this.message = message;
        this.data = data;
        this.errors = errors;
        this.pager = pager;
    }

    public int getCode()
    {
        return code;
    }

    public void setCode( int code )
    {
        this.code = code;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage( String message )
    {
        this.message = message;
    }

    public T getData()
    {
        return data;
    }

    public void setData( T data )
    {
        this.data = data;
    }

    public List<Error> getErrors()
    {
        return errors;
    }

    public void setErrors( List<Error> errors )
    {
        this.errors = errors;
    }

    public Object getPager()
    {
        return pager;
    }

    public void setPager( Object pager )
    {
        this.pager = pager;
    }
}
