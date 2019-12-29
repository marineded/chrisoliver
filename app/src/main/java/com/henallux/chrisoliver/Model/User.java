package com.henallux.chrisoliver.Model;


import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable{
    private String Name;
    private String FirstName;
    private String EMail;
    private String Password;

    public User(String mail, String name, String firstName,  String password) {
        this.Name = name;
        this.FirstName = firstName;
        this.EMail = mail;
        this.Password = password;
    }

    protected User(Parcel in) {
        Name = in.readString();
        FirstName = in.readString();
        EMail = in.readString();
        Password = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getMail() {
        return EMail;
    }

    public String getFirstName() {
        return FirstName;
    }

    public String getPassword() {
        return Password;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(Name);
        parcel.writeString(FirstName);
        parcel.writeString(EMail);
        parcel.writeString(Password);
    }
}
