package com.example.notekepper.ui.notes;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notekepper.NoteKeeperDatabaseContract.CourseInfoEntry;
import com.example.notekepper.NoteKeeperDatabaseContract.NoteInfoEntry;
import com.example.notekepper.NoteKeeperOpenHelper;
import com.example.notekepper.NoteRecyclerAdapter;
import com.example.notekepper.R;

public class NotesFragment extends Fragment {

    private NotesViewModel mNotesViewModel;
    private static RecyclerView mRecyclerView;
    private static View mRoot;
    private static NoteRecyclerAdapter mNoteRecyclerAdapter;
    private NoteKeeperOpenHelper mDbOpenHelper;
    private static Cursor mNoteCursor;
    private static Context sContext;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mNotesViewModel =
                ViewModelProviders.of(this).get(NotesViewModel.class);
        mRoot = inflater.inflate(R.layout.fragment_notes, container, false);
        mDbOpenHelper = new NoteKeeperOpenHelper(getContext());


        return mRoot;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mNoteCursor != null)
            initializeDisplayContent();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public static void getCursor(Cursor cursor, Context context)
    {
        mNoteCursor = cursor;
        sContext = context;
        initializeDisplayContent();
    }

    private static void initializeDisplayContent() {
        mRecyclerView = (RecyclerView) mRoot.findViewById(R.id.list_notes);
        LinearLayoutManager noteLayoutManager = new LinearLayoutManager(sContext);
        //loadNotes();
        mRecyclerView.setLayoutManager(noteLayoutManager);
        mNoteRecyclerAdapter = new NoteRecyclerAdapter(sContext, mNoteCursor);
        mRecyclerView.setAdapter(mNoteRecyclerAdapter);
    }


}