package com.vit.library;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Database {
    private SQLiteDatabase db;

    public Database()
    {
        db = SQLiteDatabase.openDatabase("library.db", null, SQLiteDatabase.CREATE_IF_NECESSARY);

        db.execSQL("CREATE TABLE IF NOT EXISTS `users`     (id INT NOT NULL, name VARCHAR(50), email VARCHAR(70), phone VARCHAR(15), fine INT, PRIMARY KEY (id));");
        db.execSQL("CREATE TABLE IF NOT EXISTS `books`     (id INT NOT NULL, name VARCHAR(50), author VARCHAR(50), position INT, copies INT, lends INT, PRIMARY KEY (id));");
        db.execSQL("CREATE TABLE IF NOT EXISTS `magazines` (id INT NOT NULL, name VARCHAR(50), author VARCHAR(50), position INT, copies INT, lends INT, PRIMARY KEY (id));");
        db.execSQL("CREATE TABLE IF NOT EXISTS `lends`     (userid INT     , bookid INT      , till TIMESTAMP    , FOREIGN KEY (userid) REFERENCES users(id), FOREIGN KEY (bookid) REFERENCES books(id));");
    }

    boolean addUser(String name, String email, String phone)
    {
        try
        {
            db.execSQL("INSERT INTO `users` (id, name, email, phone, fine) VALUES (NULL, ?, ?, ?, 0);", new Object[] { name, email, phone });
        }
        catch (Exception e)
        {
            return false;
        }
        return true;
    }

    boolean addBook(String name, String author, int position, int copies)
    {
        try
        {
            db.execSQL("INSERT INTO `books` (id, name, author, position, copies, lends) VALUES (NULL, ?, ?, ?, ?, 0);", new Object[] { name, author, position, copies });
        }
        catch (Exception e)
        {
            return false;
        }
        return true;
    }

    int getBookCount()
    {
        Cursor c = db.rawQuery("SELECT `id` FROM `books`;", null);
        return c.getCount();
    }
}



