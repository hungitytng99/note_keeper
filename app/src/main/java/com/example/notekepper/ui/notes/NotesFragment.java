package com.example.notekepper.ui.notes;

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
    private RecyclerView mRecyclerView;
    private View mRoot;
    private NoteRecyclerAdapter mNoteRecyclerAdapter;
    private NoteKeeperOpenHelper mDbOpenHelper;
    private Cursor mNoteCursor;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mNotesViewModel =
                ViewModelProviders.of(this).get(NotesViewModel.class);
        mRoot = inflater.inflate(R.layout.fragment_notes, container, false);
        mDbOpenHelper = new NoteKeeperOpenHelper(getContext());

        //initializeDisplayContent();
        return mRoot;
    }

    @Override
    public void onResume() {
        super.onResume();
        initializeDisplayContent();
    }

    private void loadNotes() {
        SQLiteDatabase db = mDbOpenHelper.getReadableDatabase();
        final String[] noteColumns = {
                NoteInfoEntry.getQName(NoteInfoEntry._ID),
                NoteInfoEntry.COLUMN_NOTE_TITLE,
                CourseInfoEntry.COLUMN_COURSE_TITLE
        };
        String noteOrderBy = CourseInfoEntry.getQName(CourseInfoEntry.COLUMN_COURSE_TITLE) + "," + NoteInfoEntry.COLUMN_NOTE_TITLE;
        String tablesWithJoin = NoteInfoEntry.TABLE_NAME + " JOIN " + CourseInfoEntry.TABLE_NAME + " ON " + NoteInfoEntry.getQName(NoteInfoEntry.COLUMN_COURSE_ID) + " = " + CourseInfoEntry.getQName(CourseInfoEntry.COLUMN_COURSE_ID);
        mNoteCursor = db.query(tablesWithJoin, noteColumns, null, null, null, null, noteOrderBy);
    }

    private void initializeDisplayContent() {
        mRecyclerView = (RecyclerView) mRoot.findViewById(R.id.list_notes);
        LinearLayoutManager noteLayoutManager = new LinearLayoutManager(getContext());
        loadNotes();
        mRecyclerView.setLayoutManager(noteLayoutManager);
        mNoteRecyclerAdapter = new NoteRecyclerAdapter(getContext(), mNoteCursor);
        mRecyclerView.setAdapter(mNoteRecyclerAdapter);
    }


}