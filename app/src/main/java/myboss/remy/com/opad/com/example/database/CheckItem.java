package myboss.remy.com.opad.com.example.database;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

/**
 * Created by Chimere on 9/19/2016.
 */
public class CheckItem extends GeneralModel {
    public static final String TABLE_NAME  = "checkitems";

    public static final String COLUMN_ID  = GeneralModel.COLUMN_ID;
    public static final String COLUMN_NOTEID  = "note_id";
    public static final String COLUMN_NAME  = "name";
    public static final String COLUMN_STATUS  = "status";
    private static ContentResolver contentResolver;
    Context context = null;

    private long noteId;
    private String name;
    private Boolean status = Boolean.FALSE;

    public CheckItem() {}
    public CheckItem(long id) {
        this.id = id;
        contentResolver = context.getContentResolver();
    }


    public static String getSQL() {
         String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +" (" +
                COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NOTEID +" INTEGER, " +
                COLUMN_NAME + " TEXT, " +

                COLUMN_STATUS + " INTEGER" + ")";

        return CREATE_TABLE;
    }

    public Uri save(ContentValues contentValues) {
        contentValues = new ContentValues();
        contentValues.put(COLUMN_NOTEID, noteId);
        contentValues.put(COLUMN_NAME, name == null ? "" : name);
        contentValues.put(COLUMN_STATUS, status ? 1  : 0);

        contentResolver.insert(MyContentProvider.CONTENT_URI_CHECKITEM, contentValues);

        return null;
    }

    public void update(ContentValues contentValues) {
        contentValues = new ContentValues();
        contentValues.put(COLUMN_ID, id);
        if (noteId > 0)
            contentValues.put(COLUMN_NOTEID, noteId);
        if (status != null)
            contentValues.put(COLUMN_STATUS, status ? 1 : 0);
        if (name != null)
            contentValues.put(COLUMN_NAME, name);

        contentResolver.update(MyContentProvider.CONTENT_URI_NOTE, contentValues, COLUMN_ID + "= ?", new String[]{String.valueOf(id)});
    }

    public boolean load(Uri uri) {
        String columns[] = {COLUMN_ID, COLUMN_CREATEDTIME, COLUMN_LOCKED, COLUMN_MODIFIEDTIME};
        Cursor cursor = contentResolver.query(uri, columns, COLUMN_ID + "= ?", new String[]{String.valueOf(id)}, null);
        try {
            if (cursor.moveToFirst()) {
                reset();
                id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID));
                noteId = cursor.getLong(cursor.getColumnIndex(COLUMN_ID));
                name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                status = cursor.getInt(cursor.getColumnIndex(COLUMN_STATUS)) == 1 ? true : false;
                return true;
            }
            return false;
        }finally {
            cursor.close();
        }
    }

    public static Cursor list(Uri uri, String noteId) {
        if (noteId == null)
            return contentResolver.query(MyContentProvider.CONTENT_URI_CHECKITEM, null, COLUMN_NOTEID+" = ?", new String[]{noteId}, null);
        else
            return null;
    }

    public boolean delete(Uri uri) {
        return contentResolver.delete(MyContentProvider.CONTENT_URI_CHECKITEM, COLUMN_ID + " = ?", new String[]{String.valueOf(id)}) == 1 ? true : false;
    }

    public void reset() {
        id = 0;
        noteId = 0;
        name = null;
        status = Boolean.FALSE;
    }

    public void setNoteId(long noteId) {
        this.noteId = noteId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
