package com.niuniu.user.vo;

import lombok.Builder;
import lombok.Data;

@Data
public class Response<T> {
    private Boolean result;
    private String errorMsg;
    private T body;

    public Response(){

    }

    public Response(Boolean result, String errorMsg){
        this.result = result;
        this.errorMsg = errorMsg;
    }

    public Response(Boolean result, T body){
        this.result = result;
        this.body = body;
    }

    public static Response ok(){
        return new Response(true, null);
    }

    public static <T> Response<T> ok(T t){
        return new Response<>(true, t);
    }

    public static Response fail(String errorMsg){
        return new Response<>(false, errorMsg);
    }
}
