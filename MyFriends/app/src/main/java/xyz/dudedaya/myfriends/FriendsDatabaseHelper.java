package xyz.dudedaya.myfriends;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FriendsDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "VK";
    private static final int DB_VERSION = 1;


    FriendsDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE FRIENDS (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "NAME TEXT, "
                + "STATUS TEXT, "
                + "IMAGE_URL TEXT);");
    }

    public void insertFriend(SQLiteDatabase db, String name, String status, int img_url) {
        ContentValues friendValues = new ContentValues();
        friendValues.put("NAME", name);
        friendValues.put("STATUS", status);
        friendValues.put("IMAGE_URL", img_url);
        db.insert("FRIENDS", null, friendValues);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

}
