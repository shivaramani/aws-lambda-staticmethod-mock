package com.example.Model;

public class ProductHandlerException extends RuntimeException
{
    public ProductHandlerException(String message) {super(message);}

    public ProductHandlerException(String message, Throwable t) {super(message, t);}
}