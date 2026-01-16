package com.vasyerp.ioc.coupling;

public class WebServiceDataProvider implements UserDataProvider {

    @Override
    public String getUserDetails() {
        return "Fetching Data From WebService";
    }
}
