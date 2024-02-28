package com.vit.library;

public class LendModel
{
    public BookModel bm;
    public UserModel um;
    public int id;

    public LendModel(int id, BookModel bm, UserModel um)
    {
        this.bm = bm;
        this.um = um;
        this.id = id;
    }

}
