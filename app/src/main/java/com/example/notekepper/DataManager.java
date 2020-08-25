package com.example.notekepper;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import static com.example.notekepper.NoteKeeperDatabaseContract.*;

public class DataManager {
    private static DataManager ourInstance = null;
    private static Cursor courseCursor;
    private static Cursor noteCursor;

    private List<CourseInfo> mCourses = new ArrayList<>();
    private List<NoteInfo> mNotes = new ArrayList<>();

    public static DataManager getInstance() {
        if(ourInstance == null) {
            ourInstance = new DataManager();
        }
        return ourInstance;
    }

    public static void loadFromDatabase(NoteKeeperOpenHelper dbHelper){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        final String[] courseColumns= {CourseInfoEntry.COLUMN_COURSE_ID, CourseInfoEntry.COLUMN_COURSE_TITLE};
        courseCursor = db.query(CourseInfoEntry.TABLE_NAME, courseColumns, null, null, null, null, null);
        loadCourseFromDatabase(courseCursor);

        final String[] noteColumns = {NoteInfoEntry.COLUMN_NOTE_TITLE,NoteInfoEntry.COLUMN_NOTE_TEXT,NoteInfoEntry.COLUMN_COURSE_ID};
        noteCursor = db.rawQuery("Select * from " + NoteInfoEntry.TABLE_NAME,null);
        loadNoteFromDatabase(noteCursor);
    }

    private static void loadNoteFromDatabase(Cursor noteCursor) {
        //4 cot: _ID(-1),title(0) ,1,2
        String[] columnName = noteCursor.getColumnNames();
        DataManager dm = getInstance();
        dm.mNotes.clear();
        while (noteCursor.moveToNext())
        {
            int i = 0;
            String [] noteContent = new String[columnName.length];
            while (i < columnName.length){
                noteContent[i] = noteCursor.getString(i);
                i++;
            }
            CourseInfo noteCourse = dm.getCourse(noteContent[3]);
            NoteInfo note = new NoteInfo(Integer.parseInt(noteContent[0]), noteCourse, noteContent[1], noteContent[2]);
            dm.mNotes.add(note);
        }
        noteCursor.close();
    }

    private static void loadCourseFromDatabase(Cursor courseCursor) {
        int courseIdPos = courseCursor.getColumnIndex(CourseInfoEntry.COLUMN_COURSE_ID);
        int courseTitlPos= courseCursor.getColumnIndex(CourseInfoEntry.COLUMN_COURSE_TITLE);

        DataManager dm = getInstance();
        dm.mCourses.clear();
        while (courseCursor.moveToNext()){
            String courseId = courseCursor.getString(courseIdPos);
            String coursetitle = courseCursor.getString(courseTitlPos);
            CourseInfo courseInfo = new CourseInfo(courseId,coursetitle,null);
            dm.mCourses.add(courseInfo);
        }
        courseCursor.close();
    }


    public String getCurrentUserName() {
        return "Jim Wilson";
    }

    public String getCurrentUserEmail() {
        return "jimw@jwhh.com";
    }

    public List<NoteInfo> getNotes() {
        return mNotes;
    }

    public int createNewNote() {
        NoteInfo note = new NoteInfo(null, null, null);
        mNotes.add(note);
        return mNotes.size() - 1;
    }

    public int findNote(NoteInfo note) {
        for(int index = 0; index < mNotes.size(); index++) {
            if(note.equals(mNotes.get(index)))
                return index;
        }

        return -1;
    }

    public void removeNote(int index) {
        mNotes.remove(index);
    }

    public List<CourseInfo> getCourses() {
        return mCourses;
    }

    public CourseInfo getCourse(String id) {
        for (CourseInfo course : mCourses) {
            if (id.equals(course.getCourseId()))
                return course;
        }
        return null;
    }

    public List<NoteInfo> getNotes(CourseInfo course) {
        ArrayList<NoteInfo> notes = new ArrayList<>();
        for(NoteInfo note:mNotes) {
            if(course.equals(note.getCourse()))
                notes.add(note);
        }
        return notes;
    }

    public int getNoteCount(CourseInfo course) {
        int count = 0;
        for(NoteInfo note:mNotes) {
            if(course.equals(note.getCourse()))
                count++;
        }
        return count;
    }

    private DataManager() {
    }
}
