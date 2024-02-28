package com.vit.library;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {
    static final String TAG              = "DBAdapter";
    static final String DATABASE_NAME    = "Library7";
    static final int    DATABASE_VERSION = 1;
    final Context context;

    DatabaseHelper DBHelper;
    SQLiteDatabase db;

    public DBAdapter(Context ctx)
    {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            try {
                db.execSQL("CREATE TABLE IF NOT EXISTS `users`     (id     INTEGER     NOT NULL, name   VARCHAR(50) , email  VARCHAR(70), phone    VARCHAR(15), fine   INTEGER,                PRIMARY KEY (id));");
                db.execSQL("CREATE TABLE IF NOT EXISTS `books`     (id     INTEGER     NOT NULL, name   VARCHAR(50) , author VARCHAR(50), position INTEGER    , copies INTEGER, lends INTEGER, PRIMARY KEY (id));");
                db.execSQL("CREATE TABLE IF NOT EXISTS `magazines` (id     INTEGER     NOT NULL, name   VARCHAR(50) , author VARCHAR(50), position INTEGER    , copies INTEGER, lends INTEGER, PRIMARY KEY (id));");
                db.execSQL("CREATE TABLE IF NOT EXISTS `metadata`  (holder VARCHAR(50) NOT NULL, value  VARCHAR(100), PRIMARY KEY (holder));");
                db.execSQL("CREATE TABLE IF NOT EXISTS `lends`     (id     INTEGER     NOT NULL, userid INTEGER     , itemid INTEGER     , itemtype INTEGER      , till   INTEGER    , PRIMARY KEY (id), FOREIGN KEY (userid) REFERENCES users(id));");

                if (db.rawQuery("SELECT value FROM `metadata` WHERE holder='password'", null).getCount() == 0)
                {
                    db.execSQL("INSERT INTO `metadata` (holder, value) VALUES ('password', '000library')");
                }

                if (db.rawQuery("SELECT value FROM `metadata` WHERE holder='section_size'", null).getCount() == 0)
                {
                    db.execSQL("INSERT INTO `metadata` (holder, value) VALUES ('section_size', '1000')");
                }

                if (db.rawQuery("SELECT value FROM `metadata` WHERE holder='shelf_size'", null).getCount() == 0)
                {
                    db.execSQL("INSERT INTO `metadata` (holder, value) VALUES ('shelf_size', '50')");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
            onCreate(db);
        }
    }
    //opens the database
    public DBAdapter open() throws SQLException
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    public void close()
    {
        DBHelper.close();
    }

    public long newBook(String name, String author, int position, int copies)
    {
        ContentValues values = new ContentValues();
        values.put("name"    , name);
        values.put("author"  , author);
        values.put("position", position);
        values.put("copies"  , copies);
        values.put("lends"   , 0);

        return db.insert("books", null, values);
    }

    public long newMagazine(String name, String publisher, int position, int copies)
    {
        ContentValues values = new ContentValues();
        values.put("name"    , name);
        values.put("author"  , publisher);
        values.put("position", position);
        values.put("copies"  , copies);
        values.put("lends"   , 0);

        return db.insert("magazines", null, values);
    }

    public long newUser(String name, String email, String phone)
    {
        ContentValues values = new ContentValues();
        values.put("name" , name);
        values.put("email", email);
        values.put("phone", phone);
        values.put("fine" , 0);

        return db.insert("users", null, values);
    }

    public long newLend(int item_type, int item_id, int user_id)
    {
        if (item_type == 0)
        {
            // Book
            Cursor cu = getBook(item_id);
            int copies = cu.getInt(3);
            int lends  = cu.getInt(4);
            if (copies > lends)
            {
                updateBookLends(item_id, lends + 1);
            }
            else
            {
                return -1;
            }

        }
        else
        {
            // Magazine
            Cursor cu = getMagazine(item_id);
            int copies = cu.getInt(3);
            int lends  = cu.getInt(4);
            if (copies > lends)
            {
                updateMagazineLends(item_id, lends + 1);
            }
            else
            {
                return -1;
            }
        }

        ContentValues values = new ContentValues();
        values.put("userid", user_id);
        values.put("itemid", item_id);
        values.put("itemtype", item_type);
        values.put("till", (int)(System.currentTimeMillis()/10000) + 7*24*60*6); // adds 7 days to current time

        return db.insert("lends", null, values);
    }

    public boolean removeLend(long id)
    {
        Cursor lcu = getLend(id);
        int item_type = lcu.getInt(2);
        int item_id   = lcu.getInt(1);
        if (item_type == 0)
        {
            // Book
            Cursor cu = getBook(item_id);
            int lends  = cu.getInt(4);
            if (lends > 0)
            {
                updateBookLends(item_id, lends - 1);
            }
            else
            {
                return false;
            }

        }
        else
        {
            // Magazine
            Cursor cu = getMagazine(item_id);
            int lends  = cu.getInt(4);
            if (lends > 0)
            {
                updateMagazineLends(item_id, lends - 1);
            }
            else
            {
                return false;
            }
        }
        return db.delete("lends", "id=" + id, null) > 0;
    }

    public Cursor getBook(long id)
    {
        Cursor c = db.query("books", new String[] {"name", "author", "position", "copies", "lends"}, "id="+id, null, null, null, null);
        if (c != null)
        {
            c.moveToFirst();
        }
        return c;
    }

    public Cursor getMagazine(long id)
    {
        Cursor c = db.query("magazines", new String[] {"name", "author", "position", "copies", "lends"}, "id="+id, null, null, null, null);
        if (c != null)
        {
            c.moveToFirst();
        }
        return c;
    }

    public Cursor getUser(long id)
    {
        Cursor c = db.query("users", new String[] {"name", "email", "phone", "fine"}, "id="+id, null, null, null, null);
        if (c != null)
        {
            c.moveToFirst();
        }
        return c;
    }

    public Cursor getLend(long id)
    {
        Cursor c = db.query("lends", new String[] {"userid", "itemid", "itemtype"}, "id="+id, null, null, null, null);
        if (c != null)
        {
            c.moveToFirst();
        }
        return c;
    }

    public Cursor getAllBooks()
    {
        return db.query("books", new String[] {"id", "name", "author", "position", "copies", "lends"}, null, null, null, null, null);
    }

    public Cursor getAllUsers()
    {
        return db.query("users", new String[] {"id", "name", "email", "phone", "fine"}, null, null, null, null, null);
    }

    public Cursor getAllLends()
    {
        return db.query("lends", new String[] {"id", "userid", "itemid", "itemtype"}, null, null, null, null, null);
    }

    public boolean updateBook(long id, String name, String author, int position, int copies, int lends)
    {
        ContentValues values = new ContentValues();
        if (!name  .equals("")) { values.put("name"    , name    ); }
        if (!author.equals("")) { values.put("author"  , author  ); }
        if (position > -1     ) { values.put("position", position); }
        if (copies   > -1     ) { values.put("copies"  , copies  ); }
        if (lends    > -1     ) { values.put("lends"   , lends   ); }

        return db.update("books", values, "id=" + id, null) > 0;
    }

    public boolean updateMagazine(long id, String name, String author, int position, int copies, int lends)
    {
        ContentValues values = new ContentValues();
        if (!name  .equals("")) { values.put("name"    , name    ); }
        if (!author.equals("")) { values.put("author"  , author  ); }
        if (position > -1     ) { values.put("position", position); }
        if (copies   > -1     ) { values.put("copies"  , copies  ); }
        if (lends    > -1     ) { values.put("lends"   , lends   ); }

        return db.update("magazines", values, "id=" + id, null) > 0;
    }

    public boolean updateBookLends(long id, int lends) { return updateBook(id, "", "", -1, -1, lends); }

    public boolean updateMagazineLends(long id, int lends) { return updateMagazine(id, "", "", -1, -1, lends); }

    public int getUserLends(long id)
    {
        Cursor cu = db.query("lends", new String[] {"id"}, "userid="+id, null, null, null, null);
        return cu.getCount();
    }
    public int getUserFine(long id)
    {
        int fine = 0;
        Cursor cu = db.query("lends", new String[] {"till"}, "userid="+id, null, null, null, null);
        if (cu.moveToFirst())
        {
            do
            {
                int temp = (int)((((int)(System.currentTimeMillis()/1000) - cu.getInt(0) * 10)) / (24*60*60));
                if (temp > 0)
                {
                    fine += temp;
                }
            }
            while (cu.moveToNext());
        }
        return fine;
    }
    public String getMetadata(String key)
    {
        Cursor c = db.query("metadata", new String[] {"value"}, "holder=?", new String[] {key}, null, null, null);
        Log.i("count", Integer.toString(c.getCount()));
        if (c.getCount() > 0)
        {
            c.moveToFirst();
            return c.getString(0);
        }
        else
        {
            return "";
        }
    }

    public String getMetadata(String key, String _default)
    {
        Cursor c = db.query("metadata", new String[] {"value"}, "holder=?", new String[] {key}, null, null, null);
        if (c.getCount() > 0)
        {
            c.moveToFirst();
            return c.getString(0);
        }
        else
        {
            return _default;
        }
    }

    public int getLendCount()
    {
        Cursor cv = getAllLends();
        return cv.getCount();
    }

}