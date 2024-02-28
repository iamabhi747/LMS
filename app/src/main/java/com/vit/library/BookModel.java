package com.vit.library;

public class BookModel {
    public String name, author;
    public int cover,copies, position;
    public int lends = 0;
    public int id    = -1;

    public BookModel(String name, String author, int position, int copies, int cover, int lends, int id)
    {
        this.cover  = cover;
        this.name   = name;
        this.author = author;
        this.copies = copies;
        this.lends  = lends;
        this.id     = id;
        this.position = position;
    }
    public BookModel(String name, String author, int position, int copies, int cover, int lends)
    {
        this.cover = cover;
        this.name  = name;
        this.author = author;
        this.copies = copies;
        this.lends  = lends;
        this.position = position;
    }

    public BookModel(String name, String author, int position, int copies, int cover)
    {
        this.cover = cover;
        this.name  = name;
        this.author = author;
        this.copies = copies;
        this.position = position;
    }
}
