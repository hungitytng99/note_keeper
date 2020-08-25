package com.example.notekepper.ui.courses;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notekepper.CourseInfo;
import com.example.notekepper.CourseRecyclerAdapter;
import com.example.notekepper.DataManager;
import com.example.notekepper.NoteInfo;
import com.example.notekepper.NoteRecyclerAdapter;
import com.example.notekepper.R;

import java.util.List;

public class CoursesFragment extends Fragment {

    private CoursesViewModel mCoursesViewModel;
    private RecyclerView mRecyclerView;
    private CourseRecyclerAdapter mCourseRecyclerAdapter;
    private View mRoot;
    private GridLayoutManager mCoursesLayoutManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mCoursesViewModel =
                ViewModelProviders.of(this).get(CoursesViewModel.class);
        mRoot = inflater.inflate(R.layout.fragment_courses, container, false);
        initializeDisplayContent();
        return mRoot;
    }

    private void initializeDisplayContent() {
        mRecyclerView = (RecyclerView) mRoot.findViewById(R.id.list_courses);
        mCoursesLayoutManager = new GridLayoutManager(getContext(), getResources().getInteger(R.integer.course_grid_span));
        mRecyclerView.setLayoutManager(mCoursesLayoutManager);

        List<CourseInfo> courses = DataManager.getInstance().getCourses();
        mCourseRecyclerAdapter = new CourseRecyclerAdapter(getContext(), courses );
        mRecyclerView.setAdapter(mCourseRecyclerAdapter);
    }
}