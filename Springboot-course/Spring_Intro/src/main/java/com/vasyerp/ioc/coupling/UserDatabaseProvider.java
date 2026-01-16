package com.vasyerp.ioc.coupling;

public class UserDatabaseProvider implements UserDataProvider {
    @Override
    public String getUserDetails(){
        // Directly access database here
        return "User Details From Database";
    }
}
