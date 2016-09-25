package myboss.remy.com.opad.com.example.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

/**
 * Created by Chimere on 9/18/2016.
 */
public class MyContentProvider extends ContentProvider {
    public static final String AUTHORITY = "com.example.database.MyContentProvider";

    //tables
    public static final String TABLE_NOTE = "note";
    public static final String TABLE_CATEGORY = "category";
    public static final String TABLE_ATTACHMENT = "attachment";
    public static final String TABLE_CHECKITEM = "checkitem";

    //Uris
    public static final Uri CONTENT_URI_NOTE = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NOTE);
    public static final Uri CONTENT_URI_CATEGORY = Uri.parse("content://" + AUTHORITY + "/" + TABLE_CATEGORY);
    public static final Uri CONTENT_URI_CHECKITEM= Uri.parse("content://" + AUTHORITY + "/" + TABLE_CHECKITEM);
    public static final Uri CONTENT_URI_ATTACHMENT = Uri.parse("content://" + AUTHORITY + "/" + TABLE_ATTACHMENT);

    //IDs
    public static final int NOTE = 1;
    public static final int NOTE_ID = 2;
    public static final int CATEGORY = 3;
    public static final int CATEGORY_ID = 4;
    public static final int CHECKITEM = 5;
    public static final int CHECKITEM_ID = 6;
    public static final int ATTACHMENT = 7;
    public static final int ATTACHMENT_ID = 8;

    private DbHelper dbHelper;



    public static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY, TABLE_NOTE, NOTE);
        uriMatcher.addURI(AUTHORITY, TABLE_NOTE + "/#", NOTE_ID);

        uriMatcher.addURI(AUTHORITY, TABLE_ATTACHMENT, ATTACHMENT);
        uriMatcher.addURI(AUTHORITY, TABLE_ATTACHMENT + "/#", ATTACHMENT_ID);

        uriMatcher.addURI(AUTHORITY, TABLE_CATEGORY, CATEGORY);
        uriMatcher.addURI(AUTHORITY, TABLE_CATEGORY + "/#", CATEGORY_ID);

        uriMatcher.addURI(AUTHORITY, TABLE_CHECKITEM, CHECKITEM);
        uriMatcher.addURI(AUTHORITY, TABLE_CHECKITEM + "/#", CHECKITEM_ID);
    }




    @Override
    public boolean onCreate() {
        dbHelper = new DbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        int uriType = uriMatcher.match(uri);
        switch (uriType) {
            case NOTE_ID:
                queryBuilder.setTables(Note.TABLE_NAME);
                queryBuilder.appendWhere(Note.COLUMN_ID + "=" + uri.getLastPathSegment());
                break;

            case CHECKITEM_ID:
                queryBuilder.setTables(CheckItem.TABLE_NAME);
                queryBuilder.appendWhere(CheckItem.COLUMN_ID + "=" + uri.getLastPathSegment());
                break;

            case CATEGORY_ID:
                queryBuilder.setTables(Category.TABLE_NAME);
                queryBuilder.appendWhere(Category.COLUMN_ID + "=" + uri.getLastPathSegment());
                break;

            case ATTACHMENT_ID:
                queryBuilder.setTables(Attachment.TABLE_NAME);
                queryBuilder.appendWhere(Attachment.COLUMN_ID + "=" + uri.getLastPathSegment());
                break;

            case ATTACHMENT:
            case NOTE:
            case CATEGORY:
            case CHECKITEM:
                break;
            default:
                throw new IllegalArgumentException("Unknown Uri:");
        }

        Cursor cursor = queryBuilder.query(dbHelper.getReadableDatabase(), projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri myUri = null;
        int uriType = uriMatcher.match(uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long id = 0;
        switch (uriType) {
            case NOTE:
            id  = db.insert(Note.TABLE_NAME, null, values);
                //if successfully added
                if (id > 0) {
                    myUri = ContentUris.withAppendedId(CONTENT_URI_NOTE, id);
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                break;
            case CHECKITEM:
                id = db.insert(CheckItem.TABLE_NAME, null, values);
                if (id > 0) {
                    myUri = ContentUris.withAppendedId(CONTENT_URI_CHECKITEM, id);
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                break;
            case ATTACHMENT:
                id = db.insert(Attachment.TABLE_NAME, null, values);
                if (id > 0) {
                    myUri = ContentUris.withAppendedId(CONTENT_URI_ATTACHMENT, id);
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                break;
            case CATEGORY:
                id = db.insert(Category.TABLE_NAME, null, values );
                if (id > 0) {
                    myUri = ContentUris.withAppendedId(CONTENT_URI_CATEGORY, id);
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown Uri: " + uri);
        }
        return myUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = uriMatcher.match(uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsDeleted = 0;
        String id;

        switch (uriType) {
            case NOTE:
                rowsDeleted = db.delete(Note.TABLE_NAME, selection, selectionArgs);
                break;

            case NOTE_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = db.delete(Note.TABLE_NAME, Note.COLUMN_ID + "=" + id, null);
                }else {
                    rowsDeleted = db.delete(Note.TABLE_NAME, Note.COLUMN_ID + "=" + id + " and " + selection, selectionArgs);
                }
                break;

            case CATEGORY:
                rowsDeleted = db.delete(Category.TABLE_NAME, selection, selectionArgs);
                break;

            case CATEGORY_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = db.delete(Category.TABLE_NAME, Category.COLUMN_ID + "=" + id, null);
                }else {
                    rowsDeleted = db.delete(Category.TABLE_NAME, Category.COLUMN_ID + "=" + id + " and " + selection, selectionArgs);
                }
                break;

            case CHECKITEM:
                rowsDeleted = db.delete(CheckItem.TABLE_NAME, selection, selectionArgs);
                break;

            case CHECKITEM_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = db.delete(CheckItem.TABLE_NAME, CheckItem.COLUMN_ID + "=" + id, null);
                }else {
                    rowsDeleted = db.delete(CheckItem.TABLE_NAME, CheckItem.COLUMN_ID + "=" + id + " and " + selection, selectionArgs);
                }
                break;

            case ATTACHMENT:
                rowsDeleted = db.delete(Attachment.TABLE_NAME, selection, selectionArgs);
                break;

            case ATTACHMENT_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = db.delete(Attachment.TABLE_NAME, Attachment.COLUMN_ID + "=" + id, null);
                }else {
                    rowsDeleted = db.delete(Attachment.TABLE_NAME, Attachment.COLUMN_ID + "=" + id + " and " + selection, selectionArgs);
                }
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        int uriType = uriMatcher.match(uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsUpdated = 0;
        String id;

        switch (uriType) {
            case NOTE:
                rowsUpdated = db.update(Note.TABLE_NAME, values, selection, selectionArgs);
                break;
            case NOTE_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = db.update(Note.TABLE_NAME, values, Note.COLUMN_ID + " = " + id ,  null);
                }else {
                    rowsUpdated = db.update(Note.TABLE_NAME, values, Note.COLUMN_ID + "=" + id + " and " + selection, selectionArgs);
                }
                break;


            case CATEGORY:
                rowsUpdated = db.update(Category.TABLE_NAME, values, selection, selectionArgs);
                break;
            case CATEGORY_ID:
                 id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = db.update(Category.TABLE_NAME, values, Category.COLUMN_ID + " = " + id ,  null);
                }else {
                    rowsUpdated = db.update(Note.TABLE_NAME, values, Category.COLUMN_ID + "=" + id + " and " + selection, selectionArgs);
                }
                break;

            case CHECKITEM:
                rowsUpdated = db.update(CheckItem.TABLE_NAME, values, selection, selectionArgs);
                break;
            case CHECKITEM_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = db.update(CheckItem.TABLE_NAME, values, CheckItem.COLUMN_ID + " = " + id ,  null);
                }else {
                    rowsUpdated = db.update(CheckItem.TABLE_NAME, values, CheckItem.COLUMN_ID + "=" + id + " and " + selection, selectionArgs);
                }
                break;

            case ATTACHMENT:
                rowsUpdated = db.update(Attachment.TABLE_NAME, values, selection, selectionArgs);
                break;
            case ATTACHMENT_ID:
                id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = db.update(Attachment.TABLE_NAME, values, Attachment.COLUMN_ID + " = " + id ,  null);
                }else {
                    rowsUpdated = db.update(Attachment.TABLE_NAME, values, Attachment.COLUMN_ID + "=" + id + " and " + selection, selectionArgs);
                }
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }
}
