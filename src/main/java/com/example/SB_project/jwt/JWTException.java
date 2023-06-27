package com.example.SB_project.jwt;

public class JWTException extends RuntimeException{

    /**
     *
     * */
    private static final long serialVersionID = 1L;

    public JWTException()
    {
        super();
    }

    public JWTException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public JWTException(String message)
    {
        super(message);
    }

    public JWTException(Throwable cause)
    {
        super(cause);
    }

}
