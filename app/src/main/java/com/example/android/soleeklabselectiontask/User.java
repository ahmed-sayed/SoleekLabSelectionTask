package com.example.android.soleeklabselectiontask;

public class User {
    private String mEmail;
    private String mPassword;

    public User(String mEmail,String mPassword)
    {
        this.mEmail = mEmail;
        this.mPassword=mPassword;
    }

    public String getmEmail() {
        return mEmail;
    }

    public String getmPassword() {
        return mPassword;
    }
}
