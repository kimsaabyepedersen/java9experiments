package service;

import java.util.ServiceLoader;

import api.MyApi;

public class MyService {

    public static void main(String[] args) {
        MyApi myApi = ServiceLoader.load(MyApi.class).findFirst().get();
        System.out.println(myApi.count("Kim"));
    }

}
