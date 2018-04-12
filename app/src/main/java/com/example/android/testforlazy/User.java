package com.example.android.testforlazy;

import android.support.annotation.NonNull;
import java.io.IOException;

public class User{

    /** Debugging Variables */
    private final static String TAG = User.class.getSimpleName();

    /** User Input Variables */
    private String email;
    private String password;
    private boolean loggedIn;


    /** ONLY constructor for User class
     * @param email email to be set to the user
     * @param password Password to be set to the user
     * @throws IOException if the password does not meet the requirements, at this point, it can't be empty.
     */
    public User(@NonNull String email,@NonNull String password) /**throws IOException */{

        //Checking validation of the details given, both might throw IOException if the information is invalid
        //authentication.checkEmailValidation(email);
//        authentication.checkPasswordValidation(password);
        //TODO Remove the comments from validation functions.

        this.email = email;
        this.password = password;
        loggedIn = false;
    }
    public void setEmail(String email){
        this.email = email;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }
}
