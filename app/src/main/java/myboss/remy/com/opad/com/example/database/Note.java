package myboss.remy.com.opad.com.example.database;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.List;

import myboss.remy.com.opad.Controller;

/**
 * Created by Chimere on 9/19/2016.
 */
public class Note extends GeneralModel {
    public static final String TABLE_NAME = "notes";

    public static final String COLUMN_ID = GeneralModel.COLUMN_ID;
    public static final String COLUMN_CREATEDTIME = GeneralModel.COLUMN_CREATEDTIME;
    public static final String COLUMN_MODIFIEDTIME = GeneralModel.COLUMN_MODIFIEDTIME;
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_CONTENT = "content";
    public static final String COLUMN_LOCKED = GeneralModel.COLUMN_LOCKED;
    public static final String COLUMN_CATEGORYID = "category_id";

    public static final String GENERAL = "general";
    public static final String CHECKLIST = "checklist";
    public static final String SNAPSHOT = "snapshot";
    private static ContentResolver contentResolver;

    //public ContentResolver contentResolver;
    public ContentValues contentValues;

    private long categoryId;
    private String title;
    private String content;
    private String type;
    private List<Attachment> attachments;
    private List<CheckItem> checklist;
    private Context context = null;

    public Note(){}
    public Note(long id) {
        this.id = id;
        contentResolver = context.getContentResolver();
    }

    public static String getSQL() {
         String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +" ( " +
                GeneralModel.getSQL() +
                COLUMN_CATEGORYID +" INTEGER,"+
                COLUMN_TITLE +" TEXT, "+
                COLUMN_TYPE  +" TEXT" +
                ")";

        return CREATE_TABLE;
    }

    public Uri save(ContentValues contentValues) {
        contentValues = new ContentValues();
        super.save(contentValues);
        contentValues.put(COLUMN_CATEGORYID, categoryId);
        contentValues.put(COLUMN_TITLE, title == null ? "" : title);
        contentValues.put(COLUMN_CONTENT, content == null ? "" : content);
        contentValues.put(COLUMN_TYPE, type == null ? GENERAL : type);

        contentResolver.insert(MyContentProvider.CONTENT_URI_NOTE, contentValues);
        return null;
    }

    public void update(ContentValues contentValues) {
        contentValues = new ContentValues();
        super.update(contentValues);
        if (categoryId > 0)
            contentValues.put(COLUMN_CATEGORYID, categoryId);
        if (title != null)
            contentValues.put(COLUMN_TITLE, title);
        if (content != null)
            contentValues.put(COLUMN_CONTENT, content);
        if (type != null)
            contentValues.put(COLUMN_TYPE, type);

        contentResolver.update(MyContentProvider.CONTENT_URI_NOTE, contentValues, COLUMN_ID + "= ?", new String[]{String.valueOf(id)});
    }

    public boolean load(Uri uri) {
        String columns[] = {COLUMN_ID, COLUMN_CREATEDTIME, COLUMN_LOCKED, COLUMN_MODIFIEDTIME};
        Cursor cursor = contentResolver.query(uri, columns, COLUMN_ID + "= ?", new String[]{String.valueOf(id)}, null);
        try {
            if (cursor.moveToFirst()) {
                reset();
                super.load(cursor);
                categoryId = cursor.getLong(cursor.getColumnIndex(COLUMN_CATEGORYID));
                title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE));
                content = cursor.getString(cursor.getColumnIndex(COLUMN_CONTENT));
                type = cursor.getString(cursor.getColumnIndex(COLUMN_TYPE));
                return true;
            }
            return false;
        }finally {
            cursor.close();
        }
    }

    public static Cursor list(Uri uri, String... args) {
        String categoryId = args != null ? args[0] : null;

        String[] columns = {COLUMN_ID, COLUMN_LOCKED, COLUMN_TITLE, COLUMN_TYPE, COLUMN_MODIFIEDTIME, COLUMN_CREATEDTIME};
        String selection = "1 = 1";
        selection += !Controller.showLocked() ? " AND " + COLUMN_LOCKED + " <> 1" : "";
        selection += categoryId != null ? " AND " + COLUMN_CATEGORYID + " = " + categoryId : "";
        String sort = (args != null && args.length > 1) ? args[1] : categoryId != null ? COLUMN_CREATEDTIME + "ASC" : COLUMN_MODIFIEDTIME + " DESC";

        return contentResolver.query(MyContentProvider.CONTENT_URI_NOTE, columns, selection, null, sort);
    }

    public boolean delete(Uri uri) {
        boolean status = false;
        String where[] = new String [] {String.valueOf(id)};

        try {
            contentResolver.delete(MyContentProvider.CONTENT_URI_ATTACHMENT, Attachment.COLUMN_NOTEID + " = ?", where);
            contentResolver.delete(MyContentProvider.CONTENT_URI_CHECKITEM, CheckItem.COLUMN_NOTEID + " = ?", where);
            status = contentResolver.delete(uri, COLUMN_ID + " = ?", where) == 1 ? true : false;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return status;
    }

    public void reset() {
        super.reset();
        categoryId = 0;
        title = null;
        content = null;
        type = null;
        attachments = null;
        checklist = null;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }


    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return  type;
    }


}
