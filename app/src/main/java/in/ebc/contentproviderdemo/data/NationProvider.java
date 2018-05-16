package in.ebc.contentproviderdemo.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

public class NationProvider extends ContentProvider {

    private static final String TAG=NationProvider.class.getSimpleName ();
    private NationDbHelper nationDbHelper;
    private static final UriMatcher URI_MATCHER=new UriMatcher (UriMatcher.NO_MATCH);


    private static final int COUNTRIES =1;
    private static final int COUNTRIES_COUNTRIES_NAME =2;
    private static final int COUNTRIES_COUNTRIES_ID =3;

    static {
        URI_MATCHER.addURI (NationContract.CONTENT_AUTHORITY,NationContract.PATH_COUNTRIES,COUNTRIES);
        URI_MATCHER.addURI (NationContract.CONTENT_AUTHORITY,NationContract.PATH_COUNTRIES+"/#",COUNTRIES_COUNTRIES_ID);
        URI_MATCHER.addURI (NationContract.CONTENT_AUTHORITY,NationContract.PATH_COUNTRIES+"/*",COUNTRIES_COUNTRIES_NAME);
    }

    public boolean onCreate () {

        nationDbHelper=new NationDbHelper (getContext ());

        return true;
    }

    @Nullable
    public Cursor query (@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        SQLiteDatabase database=nationDbHelper.getReadableDatabase ();
        Cursor cursor;
        switch (URI_MATCHER.match (uri)){
            case COUNTRIES:{
                cursor = database.query(NationContract.NationEntry.TABLE_NAME,projection,null,null,null,null,sortOrder);
                break;
            }

            case COUNTRIES_COUNTRIES_ID:{
                selection= NationContract.NationEntry._ID+"=?";
                selectionArgs=new String[]{String.valueOf (ContentUris.parseId (uri))};
		        cursor = database.query(NationContract.NationEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            }

            default:{
                throw new IllegalArgumentException (TAG+"Insert UnKnown URI:"+uri);
            }
        }
        cursor.setNotificationUri (getContext ().getContentResolver (),uri);
        return cursor;
    }

    @Nullable
    public String getType (@NonNull Uri uri) {
        return null;
    }

    @Nullable
    public Uri insert (@NonNull Uri uri, @Nullable ContentValues values) {
        switch (URI_MATCHER.match (uri)){

            case COUNTRIES:
                return  insertRecord(uri,values,NationContract.NationEntry.TABLE_NAME);


            default:{
                throw new IllegalArgumentException (TAG+" Insert inknown URI: "+uri);
            }
        }
    }

    private Uri insertRecord (Uri uri, ContentValues values, String tableName) {
        SQLiteDatabase sqLiteDatabase=nationDbHelper.getWritableDatabase ();
        long rowId = sqLiteDatabase.insert(tableName,null,values);

        if (rowId==-1){
            Log.e (TAG,"Insert error for URI "+uri);
            return null;
        }else {
            getContext ().getContentResolver ().notifyChange (uri,null);
        }
        return ContentUris.withAppendedId (uri,rowId);
    }

    public int delete (@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        switch (URI_MATCHER.match (uri)){

            case COUNTRIES:{
                return deleteRecord(uri,null,null, NationContract.NationEntry.TABLE_NAME);
            }

            case COUNTRIES_COUNTRIES_ID:{
                selection= NationContract.NationEntry._ID+"=?";
                selectionArgs=new String[]{String.valueOf (ContentUris.parseId (uri))};
                return deleteRecord(uri,selection,selectionArgs, NationContract.NationEntry.TABLE_NAME);
            }

            case COUNTRIES_COUNTRIES_NAME:{
                selection= NationContract.NationEntry.COLUMN_COUNTRY+"=?";
                selectionArgs=new String[]{uri.getLastPathSegment ()};
                return deleteRecord(uri,selection,selectionArgs, NationContract.NationEntry.TABLE_NAME);
            }
            default:{
                throw new IllegalArgumentException (TAG+" Row deleted:"+uri);
            }
        }
    }

    private int deleteRecord (Uri uri,String selection, String[] selectionArgs, String tableName) {
        SQLiteDatabase sqLiteDatabase=nationDbHelper.getWritableDatabase ();
        int rowsDeleted = sqLiteDatabase.delete(tableName, selection, selectionArgs);

        if (rowsDeleted!=0){
            getContext ().getContentResolver ().notifyChange (uri,null);
        }

        return rowsDeleted;
    }

    public int update (@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {

        switch (URI_MATCHER.match (uri)){
            case COUNTRIES:{
                return updateRecord(uri,values,selection,selectionArgs, NationContract.NationEntry.TABLE_NAME);
            }
            default:{
                throw new IllegalArgumentException(TAG+" Insert unknown URI:"+uri);
            }
        }

    }

    private int updateRecord (Uri uri,ContentValues values, String selection, String[] selectionArgs, String tableName) {
        SQLiteDatabase sqLiteDatabase=nationDbHelper.getWritableDatabase ();
        int rowsUpdated = sqLiteDatabase.update(tableName,values, selection, selectionArgs);

        if (rowsUpdated!=0){
            getContext ().getContentResolver ().notifyChange (uri,null);
        }
        return rowsUpdated;
    }
}
