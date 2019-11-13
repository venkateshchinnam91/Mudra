package com.manuh.mudrabook;

public class APIUtils {

    private static final String BASE_URL = "http://125.62.194.124:8080/mudrabook/api/mudrabook/";

    private APIUtils() {}

    public static DataServices getAPIService() {

        return RetrofitClientInstance.getClient(BASE_URL).create(DataServices.class);
    }
}