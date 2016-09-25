package myboss.remy.com.opad.com.example.database;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

/**
 * Created by Chimere on 9/19/2016.
 */
public class Attachment extends GeneralModel{
    public static final String TABLE_NAME = "attachment";

    public static final String COLUMN_ID = GeneralModel.COLUMN_ID;
    public static final String COLUMN_NOTEID = GeneralModel.COLUMN_ID;
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_URI = "uri";
    private static ContentResolver contentResolver;

    //public ContentResolver contentResolver;
    private long noteId;
    private String name;
    private String uri;

    public Attachment() {}

    public Attachment(long id) {
        this.id = id;
    }

    public static String getSQL() {
        return String.format("CREATE TABLE ", TABLE_NAME, " (",
                COLUMN_ID, " INTEGER PRIMARY KEY AUTOINCREMENT, ",
                COLUMN_NOTEID, " INTEGER, ",
                COLUMN_NAME, " TEXT, ",
                COLUMN_URI, " TEXT, ",
                 ");");
    }

    public Uri save(ContentValues contentValues) {
        contentValues = new ContentValues();
        contentValues.put(COLUMN_NOTEID, noteId);
        contentValues.put(COLUMN_NAME, name == null ? "" : uri);
        contentResolver.insert(MyContentProvider.CONTENT_URI_ATTACHMENT, contentValues);
        return null;
    }

    public void update(ContentValues contentValues) {
        contentValues = new ContentValues();
        contentValues.put(COLUMN_ID, id);
        if (noteId > 0)
            contentValues.put(COLUMN_NOTEID, noteId);
        if (name != null)
            contentValues.put(COLUMN_NAME, name);
        if (uri != null)
            contentValues.put(COLUMN_URI, uri);

        contentResolver.update(MyContentProvider.CONTENT_URI_ATTACHMENT, contentValues, COLUMN_ID + "= ?", new String[]{String.valueOf(id)});
    }

    public boolean load(Uri uri) {
        Cursor cursor = contentResolver.query(uri, null, COLUMN_ID + " = ?", new String[]{String.valueOf(id)}, null);
        try {
            if (cursor.moveToFirst()) {
                reset();
                id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID));
                noteId = cursor.getLong(cursor.getColumnIndex(COLUMN_NOTEID));
                name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                this.uri = cursor.getString(cursor.getColumnIndex(COLUMN_URI));
                return true;
            }
            return false;
        }finally {
            cursor.close();
        }
    }

    public static Cursor list(Uri uri, String noteId) {
        if (noteId != null)
             return contentResolver.query(uri, null, COLUMN_NOTEID + "= ?", new String[] {noteId}, null);
        else
            return null;
    }

    public boolean delete(Uri uri) {
        return contentResolver.delete(uri, COLUMN_ID + " = ?", new String[]{String.valueOf(id)}) == 1 ? true : false;
    }

    public void reset() {
        id = 0;
        noteId = 0;
        name = null;
        uri = null;
    }

    public void setNoteId(long noteId) {
        this.noteId = noteId;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
