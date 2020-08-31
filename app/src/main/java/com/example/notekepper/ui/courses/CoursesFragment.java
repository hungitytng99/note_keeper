package com.example.notekepper.ui.courses;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notekepper.CourseRecyclerAdapter;
import com.example.notekepper.NoteKeeperDatabaseContract.CourseInfoEntry;
import com.example.notekepper.NoteKeeperOpenHelper;
import com.example.notekepper.R;

public class CoursesFragment extends Fragment {

    private CoursesViewModel mCoursesViewModel;
    private RecyclerView mRecyclerView;
    private CourseRecyclerAdapter mCourseRecyclerAdapter;
    private View mRoot;
    private GridLayoutManager mCoursesLayoutManager;
    private static Cursor mCursor = null;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mCoursesViewModel =
                ViewModelProviders.of(this).get(CoursesViewModel.class);
        mRoot = inflater.inflate(R.layout.fragment_courses, container, false);
        initializeDisplayContent();
        return mRoot;
    }

    public static void getCursor(Cursor cursor) {
        mCursor = cursor;
    }

    private void initializeDisplayContent() {
        mRecyclerView = (RecyclerView) mRoot.findViewById(R.id.list_courses);
        mCoursesLayoutManager = new GridLayoutManager(getContext(), getResources().getInteger(R.integer.course_grid_span));
        mRecyclerView.setLayoutManager(mCoursesLayoutManager);

        mCourseRecyclerAdapter = new CourseRecyclerAdapter(getContext(), mCursor );
        mRecyclerView.setAdapter(mCourseRecyclerAdapter);
    }
}