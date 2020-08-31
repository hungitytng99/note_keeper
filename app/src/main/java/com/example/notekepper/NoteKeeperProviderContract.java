package com.example.notekepper;

import android.net.Uri;
import android.provider.BaseColumns;

public final class NoteKeeperProviderContract {
    private NoteKeeperProviderContract(){

    }

    public static final String AUTHORITY = "com.example.notekepper.provider";
    public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);

    protected interface CourseIdColumns {
        public static final String COLUMN_COURSE_ID = "course_id";
    }

    protected interface CoursesColumns {
        public static final String COLUMN_COURSE_TITLE = "course_title";
    }

    protected interface NotesColumns{
        public static final String COLUMN_NOTE_TITLE = "note_title";
        public static final String COLUMN_NOTE_TEXT = "note_text";
    }


    public static final class Courses implements CoursesColumns, BaseColumns {
        public static final String PATH = "courses";
//        content://com.jwhh.jim.notekepper.provider/coursee
        public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI,PATH);
    }

    public static final class Notes implements NotesColumns, BaseColumns , CoursesColumns, CourseIdColumns{
        public static final String PATH = "notes";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI,PATH);
        public static final String PATH_EXPANDED = "notes_expanded";
        public static final Uri PATH_EXPANDED_URI = Uri.withAppendedPath(AUTHORITY_URI,PATH_EXPANDED);
    }

}
