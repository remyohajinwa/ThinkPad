package myboss.remy.com.opad.com.example.database;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.List;

import myboss.remy.com.opad.Controller;
import myboss.remy.com.opad.com.example.activity.MainActivity;

/**
 * Created by Chimere on 9/18/2016.
 */
public class Category extends GeneralModel {



    //private ContentResolver contentResolver;


    private ContentValues contentValues;

    private String name;
    private List<Note> notes;



    public static final String TABLE_NAME = "categorys";

    public static final String COLUMN_ID = GeneralModel.COLUMN_ID;
    public static final String COLUMN_CREATEDTIME = GeneralModel.COLUMN_CREATEDTIME;
    public static final String COLUMN_MODIFIEDTIME = GeneralModel.COLUMN_MODIFIEDTIME;
    public static final String COLUMN_LOCKED = GeneralModel.COLUMN_LOCKED;
    public static final String COLUMN_NAME = "name";

    public static MainActivity activity = new MainActivity();

    private Context context ;
    private static ContentResolver contentResolver;




    public Category(Context context){
        this.context = context;
         contentResolver  = this.context.getContentResolver();

    }
    public Category(long id) {
        this.id = id;


    }

    public static String getSQL() {
         String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +" (" +
                GeneralModel.getSQL() +
                COLUMN_NAME + " TEXT" +
                ")";

        return CREATE_TABLE;
    }


    public void update(ContentValues contentValues) {
        contentValues = new ContentValues();
        super.update(contentValues);
        contentValues.put(COLUMN_NAME, name == null ? "" : name);
        contentResolver.update(MyContentProvider.CONTENT_URI_CATEGORY, contentValues, COLUMN_ID + "= ?", new String[]{String.valueOf(id)});
    }

    public Uri save(ContentValues contentValues) {
        super.save(contentValues);
        contentResolver = context.getContentResolver();
        contentValues = new ContentValues();
        contentValues.clear();
        super.save(contentValues);
        contentValues.put(COLUMN_NAME, name == null ? "" : name);
        return contentResolver.insert(MyContentProvider.CONTENT_URI_CATEGORY, contentValues);
    }

    public boolean load(Uri uri) {
        String columns[] = {COLUMN_ID, COLUMN_CREATEDTIME, COLUMN_LOCKED, COLUMN_MODIFIEDTIME, COLUMN_NAME};
       // Cursor cursor = contentResolver.query(TABLE_NAME, null, COLUMN_ID+" = ?", new String[]{String.valueOf(id)}, null, null, null);
        Cursor cursor = contentResolver.query(uri, columns, COLUMN_ID+ " = ?", new String[]{String.valueOf(id)}, null);
        try {
            if (cursor.moveToFirst()) {
                reset();
                super.load(cursor);
                name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                return true;
            }
            return false;
        }finally {
            cursor.close();
        }
    }

    public static Cursor list(Uri uri) {
        String[] columns = {COLUMN_ID, COLUMN_LOCKED, COLUMN_NAME};
        String selection = !Controller.showLocked() ? COLUMN_LOCKED+ " <> 1" : null;

        return contentResolver.query(uri, columns, selection, null, COLUMN_CREATEDTIME + " ASC");
    }

    public boolean delete(Uri uri) {
        boolean status = false;
        String[] selectionArgs = new String[] {String.valueOf(id)};
        String where = String.format(Attachment.COLUMN_NOTEID, " IN (SELECT ", Note.COLUMN_ID, " FROM ",Note.TABLE_NAME, " WHERE ", Note.COLUMN_CATEGORYID);

        try {
            contentResolver.delete(MyContentProvider.CONTENT_URI_ATTACHMENT, where, selectionArgs);
            contentResolver.delete(MyContentProvider.CONTENT_URI_CHECKITEM, where, selectionArgs);
            status = contentResolver.delete(uri, COLUMN_ID + " = ?", selectionArgs) == 1 ? true : false;


        }catch (Exception e) {
            e.printStackTrace();
        }

        return status;
    }

    public void reset() {
        super.reset();
        name = null;
        notes = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Note> getNotes() {
        return notes;
    }


}
