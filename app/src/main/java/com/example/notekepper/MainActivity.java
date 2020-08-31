package com.example.notekepper;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;

import com.example.notekepper.NoteKeeperDatabaseContract.CourseInfoEntry;
import com.example.notekepper.NoteKeeperDatabaseContract.NoteInfoEntry;
import com.example.notekepper.NoteKeeperProviderContract.Courses;
import com.example.notekepper.ui.courses.CoursesFragment;
import com.example.notekepper.ui.notes.NotesFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    public static final String M_DB_OPEN_HELPER_BUNDLE = "mDbOpenHelper_bundle";
    public static final int LOADER_NOTEKEEPER_COURSES = 0;
    public static final int CONTENT_EXPANDED_URI = 1;
    private AppBarConfiguration mAppBarConfiguration;
    private NavController mNavController;
    private NavigationView mNavigationView;
    private NoteKeeperOpenHelper mDbOpenHelper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDbOpenHelper = new NoteKeeperOpenHelper(this);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, NoteActivity.class));
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_notes, R.id.nav_courses)
                .setDrawerLayout(drawer)
                .build();
        mNavController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, mNavController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(mNavigationView, mNavController);
        //DataManager.loadFromDatabase(mDbOpenHelper);
        getLoaderManager().initLoader(CONTENT_EXPANDED_URI, null, this);
        getLoaderManager().initLoader(LOADER_NOTEKEEPER_COURSES,null, this);
    }

    private void printDbContent(NoteKeeperOpenHelper dbOpenHelper, String tableName) {
        String TAG = "DBContent";
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + tableName,null);
        String[] columnName = cursor.getColumnNames();
        String colName = "";
        for(int i = 0; i < columnName.length; i ++)
        {
            colName = colName.concat(columnName[i]);
            colName = colName.concat("   |   ");
        }
        Log.i(TAG, colName );
        while (cursor.moveToNext()){
            int  i = 0;
            String dbcontent = "";
            while(i < columnName.length){
                dbcontent = dbcontent.concat(cursor.getString(i));
                dbcontent = dbcontent.concat("   |   ");
                i++;
            }
            Log.i(TAG, dbcontent);
        }
        cursor.close();
        db.close();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateNavHeader();

        printDbContent(mDbOpenHelper, CourseInfoEntry.TABLE_NAME);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDbOpenHelper.close();
    }

    private void updateNavHeader() {
        View headerView = mNavigationView.getHeaderView(0);
        TextView textUserName = (TextView) headerView.findViewById(R.id.text_user_name);
        TextView textEmailAddress = (TextView) headerView.findViewById(R.id.text_email_address);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        String userName = pref.getString("user_display_name","");
        String emailAddress = pref.getString("user_email_address","");

        textUserName.setText(userName);
        textEmailAddress.setText(emailAddress);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_settings)
        {
            startActivity(new Intent(this,SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(mNavController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        if(id == LOADER_NOTEKEEPER_COURSES) {
            Uri uri = Courses.CONTENT_URI;
            String[] column = {CourseInfoEntry._ID, CourseInfoEntry.COLUMN_COURSE_TITLE, CourseInfoEntry.COLUMN_COURSE_ID};
            return new CursorLoader(this, uri, column, null, null, CourseInfoEntry.COLUMN_COURSE_TITLE);
        } else if(id == CONTENT_EXPANDED_URI)
        {
            final String[] noteColumns = {
                    NoteInfoEntry.getQName(NoteInfoEntry._ID),
                    NoteInfoEntry.COLUMN_NOTE_TITLE,
                    CourseInfoEntry.COLUMN_COURSE_TITLE
            };
            final String noteOrderBy = CourseInfoEntry.getQName(CourseInfoEntry.COLUMN_COURSE_TITLE) + "," + NoteInfoEntry.COLUMN_NOTE_TITLE;
            Uri uri = NoteKeeperProviderContract.Notes.PATH_EXPANDED_URI;
            return new CursorLoader(this, uri, noteColumns, null, null, noteOrderBy);
        } else
            return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
            //send data to course fragment;
        if(loader.getId() == LOADER_NOTEKEEPER_COURSES)
            CoursesFragment.getCursor(cursor);
        else if(loader.getId() == CONTENT_EXPANDED_URI)
            NotesFragment.getCursor(cursor , this);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
            CoursesFragment.getCursor(null);
    }
}