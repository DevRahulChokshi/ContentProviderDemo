package in.ebc.contentproviderdemo;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import in.ebc.contentproviderdemo.data.NationContract;

public class NationEditActivity extends AppCompatActivity implements View.OnClickListener{

    EditText editCountry;
    EditText editContinent;

    Button btnInsert;
    Button btnUpdate;
    Button btnDelete;

    int id;
    String country;
    String continent;


    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_nation_edit);

        editCountry=findViewById (R.id.etCountry);
        editContinent=findViewById (R.id.etContinent);

        btnInsert=findViewById (R.id.btnInsert);
        btnUpdate=findViewById (R.id.btnUpdate);
        btnDelete=findViewById (R.id.btnDelete);

        btnInsert.setOnClickListener (this);
        btnUpdate.setOnClickListener (this);
        btnDelete.setOnClickListener (this);

        getBundleData();
    }

    private void getBundleData () {
        Intent intent=getIntent ();
        if (intent!=null){

            id=intent.getIntExtra (NationContract.NationEntry._ID,0);
            country=intent.getStringExtra (NationContract.NationEntry.COLUMN_COUNTRY);
            continent=intent.getStringExtra (NationContract.NationEntry.COLUMN_CONTINENT);

            Log.i (NationEditActivity.class.getSimpleName (),"ID:"+id);
            Log.i (NationEditActivity.class.getSimpleName (),"Country:"+country);
            Log.i (NationEditActivity.class.getSimpleName (),"Continent:"+continent);

            if (id!=0){
                editCountry.setText (country);
                editContinent.setText (continent);
                btnInsert.setVisibility (View.GONE);
            }else {
                btnDelete.setVisibility (View.GONE);
                btnUpdate.setVisibility (View.GONE);
            }
        }
    }

    @Override
    public void onClick (View v) {
        int id=v.getId ();
        switch (id){
            case R.id.btnInsert:{
                insert();
                break;
            }
            case R.id.btnUpdate:{
                update();
                break;
            }
            case R.id.btnDelete:{
                delete();
                break;
            }
        }
    }

    private void insert () {

        ContentValues contentValues=new ContentValues ();
        contentValues.put (NationContract.NationEntry.COLUMN_COUNTRY,editCountry.getText ().toString ());
        contentValues.put (NationContract.NationEntry.COLUMN_CONTINENT,editContinent.getText ().toString ());

        Uri uri= NationContract.NationEntry.CONTENT_URI;
        Uri insertUri=getContentResolver ().insert (uri,contentValues);

        Log.i (NationEditActivity.class.getSimpleName (),"Inserted Uri:"+insertUri);
    }

    private void update () {

        ContentValues contentValues=new ContentValues ();
        contentValues.put (NationContract.NationEntry.COLUMN_COUNTRY,editCountry.getText ().toString ());
        contentValues.put (NationContract.NationEntry.COLUMN_CONTINENT,editContinent.getText ().toString ());

        String selection= NationContract.NationEntry._ID+" =?";
        String [] selectionArgs={String.valueOf (id)};

        Uri uri= NationContract.NationEntry.CONTENT_URI;
        int updated=getContentResolver ().update (uri,contentValues,selection,selectionArgs);

        Log.i (NationEditActivity.class.getSimpleName (),"Updated row:"+updated);
    }

    private void delete () {

        String selection= NationContract.NationEntry._ID+" =?";
        String[] selectionArgs={String.valueOf (id)};

        Uri uri= ContentUris.withAppendedId (NationContract.NationEntry.CONTENT_URI,id);
        int rowDeleted=getContentResolver ().delete (uri,selection,selectionArgs);

        Log.i (NationEditActivity.class.getSimpleName (),"Row Deleted:"+rowDeleted);
    }
}
