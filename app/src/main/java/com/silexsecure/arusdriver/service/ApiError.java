package com.silexsecure.arusdriver.service;


import com.silexsecure.arusdriver.service.APIService;import com.silexsecure.arusdriver.service.ErrorMessage;import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

/**
 * Created by Habeeb-Bizzdesk on 4/13/18.
 */

public class ApiError {

    public static ErrorMessage parseError(Response<?> response) {
        Converter<ResponseBody, ErrorMessage> converter =
                APIService.retrofit.responseBodyConverter(ErrorMessage.class, new Annotation[0]);

        ErrorMessage error;

        try {
            error = converter.convert(response.errorBody());
        } catch (IOException e) {
            return new ErrorMessage();
        }

        return error;
    }
}
