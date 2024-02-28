package com.vit.library;

public class UserModel
{
    public String name, phone, email;
    public int id;

    public UserModel(int id, String name, String email, String phone)
    {
        this.id = id;
        this.name  = name;
        this.email = email;
        this.phone = phone;
    }
}
