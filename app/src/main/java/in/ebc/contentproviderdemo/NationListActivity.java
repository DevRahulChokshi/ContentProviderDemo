package in.ebc.contentproviderdemo;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import in.ebc.contentproviderdemo.data.NationContract;

public class NationListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private SimpleCursorAdapter simpleCursorAdapter;
    private Cursor cursor;
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_nation_list);

        String [] from ={NationContract.NationEntry.COLUMN_COUNTRY, NationContract.NationEntry.COLUMN_CONTINENT};
        int [] to ={R.id.txvCountryName,R.id.txvContinentName};

        getLoaderManager ().initLoader (10,null,this);

        simpleCursorAdapter=new SimpleCursorAdapter (this,R.layout.nation_list_item,null,from,to,0);

        ListView listView=findViewById (R.id.listView);
        listView.setAdapter (simpleCursorAdapter);
        FloatingActionButton floatingActionButton=findViewById (R.id.fanInsertData);

        floatingActionButton.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                    Intent intent=new Intent (NationListActivity.this,NationEditActivity.class);
                    intent.putExtra (NationContract.NationEntry._ID,0);
                    startActivity (intent);
            }
        });

        listView.setOnItemClickListener (new AdapterView.OnItemClickListener () {
            @Override
            public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
                cursor= (Cursor) parent.getItemAtPosition (position);

                int _ID=cursor.getInt (cursor.getColumnIndex (NationContract.NationEntry._ID));
                String CountryName=cursor.getString (cursor.getColumnIndex (NationContract.NationEntry.COLUMN_COUNTRY));
                String Continent=cursor.getString (cursor.getColumnIndex (NationContract.NationEntry.COLUMN_CONTINENT));

                Intent intent=new Intent (NationListActivity.this,NationEditActivity.class);
                intent.putExtra (NationContract.NationEntry._ID,_ID);
                intent.putExtra (NationContract.NationEntry.COLUMN_COUNTRY,CountryName);
                intent.putExtra (NationContract.NationEntry.COLUMN_CONTINENT,Continent);
                startActivity (intent);
            }
        });
        }


    @Override
    public Loader<Cursor> onCreateLoader (int id, Bundle args) {

        String [] projection={
                NationContract.NationEntry._ID,
                NationContract.NationEntry.COLUMN_COUNTRY,
                NationContract.NationEntry.COLUMN_CONTINENT
        };
        return new CursorLoader (this, NationContract.NationEntry.CONTENT_URI,projection,null,null,null);
    }

    @Override
    public void onLoadFinished (Loader<Cursor> loader, Cursor data) {
        simpleCursorAdapter.swapCursor (data);
    }

    @Override
    public void onLoaderReset (Loader<Cursor> loader) {
        simpleCursorAdapter.swapCursor (null);
    }
}
