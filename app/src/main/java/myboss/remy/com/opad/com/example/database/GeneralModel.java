package myboss.remy.com.opad.com.example.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.Settings;

/**
 * Created by Chimere on 9/18/2016.
 */
public abstract class GeneralModel  {
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_CREATEDTIME = "created_time";
    public static final String COLUMN_MODIFIEDTIME = "modified_time";
    public static final String COLUMN_LOCKED = "locked";

    protected  long id;

    protected long createdTime;
    protected long modifiedTime;
    protected Boolean isLocked;

    public static String getSQL() {
        return String.format(COLUMN_ID, "INTEGER PRIMARY KEY AUTOINCREMENT, ",
                COLUMN_CREATEDTIME, "INTEGER, ",
                COLUMN_MODIFIEDTIME, "INTEGER, ",
                COLUMN_LOCKED, "INTEGER");
    }

    public Uri save(ContentValues contentValues) {
        contentValues.put(COLUMN_ID, id);
        contentValues.put(COLUMN_MODIFIEDTIME, System.currentTimeMillis());
        if (isLocked != null)
            contentValues.put(COLUMN_LOCKED, isLocked ? 1 : 0);
        return null;
    }

    public void load(Cursor cursor) {
        id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID));
        createdTime = cursor.getLong(cursor.getColumnIndex(COLUMN_CREATEDTIME));
        modifiedTime = cursor.getLong(cursor.getColumnIndex(COLUMN_MODIFIEDTIME));
        isLocked = cursor.getInt(cursor.getColumnIndex(COLUMN_LOCKED)) == 1 ? true : false;
    }

    public boolean isLocked() {
        return isLocked != null ? isLocked:false;
    }

    public void setIsLocked(boolean isLocked) {
        this.isLocked = isLocked;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void reset() {
        id = 0;
        createdTime = 0;
        modifiedTime = 0;
        isLocked = null;
    }

   public void  update(ContentValues contentValues) {
       contentValues.put(COLUMN_ID, id);
       contentValues.put(COLUMN_MODIFIEDTIME, System.currentTimeMillis());
       if (isLocked != null)
           contentValues.put(COLUMN_LOCKED, isLocked ? 1 : 0);

   }
   // abstract long save(SQLiteDatabase db);

    /*public long persist(SQLiteDatabase db) {
        if (id > 0)
            return update(db) ? id : 0;
        else
            return save(db);
    }*/

}
