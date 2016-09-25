package myboss.remy.com.opad.com.example.database;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import myboss.remy.com.opad.Controller;

/**
 * Created by Chimere on 9/18/2016.
 */
public class DbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "opad.db";

    private ContentResolver contentResolver;


    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        contentResolver = context.getContentResolver();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Category.getSQL());
        db.execSQL(Note.getSQL());
        db.execSQL(Attachment.getSQL());
        db.execSQL(CheckItem.getSQL());

        //loadData();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Category.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Note.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Attachment.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CheckItem.TABLE_NAME);
        onCreate(db);
    }

    private void loadData(ContentValues db) {
        ContentValues contentValues = new ContentValues();
        //General
       /* Category category = new Category();
        category.setName("General");
        String value = category.getName();
        contentValues.put(value, "General");
        Uri categoryId = category.save(contentValues);
        //Controller.PUBLIC_CATEGORYID = categoryId;

        Note note = new Note();
        note.setCategoryId(categoryId);
        note.setTitle("Tasks");
        note.setType(Note.GENERAL);
        note.setContent("Go Chop");
        note.save(db);

        //Personal
        category.reset();
        category.setName("Personal");
        category.setIsLocked(true);
        categoryId = category.save(db);

        note.reset();
        note.setCategoryId(categoryId);
        note.setTitle("To do");
        note.setType(Note.CHECKLIST);
        Uri noteId = note.save(db);

        CheckItem checkItem = new CheckItem();
        checkItem.setNoteId(noteId);
        checkItem.setName("Remy Id getting better");
        checkItem.save(db);

    }*/
    }
}

