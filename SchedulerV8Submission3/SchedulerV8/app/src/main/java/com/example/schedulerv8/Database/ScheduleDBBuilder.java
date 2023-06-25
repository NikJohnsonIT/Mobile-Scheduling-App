package com.example.schedulerv8.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.schedulerv8.DAO.AssessmentDao;
import com.example.schedulerv8.DAO.CourseDao;
import com.example.schedulerv8.DAO.TermDao;
import com.example.schedulerv8.Entities.Assessment;
import com.example.schedulerv8.Entities.Course;
import com.example.schedulerv8.Entities.Term;

@Database(entities = {Term.class, Course.class, Assessment.class}, version=14, exportSchema = false)
public abstract class ScheduleDBBuilder extends RoomDatabase {
    public abstract TermDao termDao();
    public abstract CourseDao courseDao();
    public abstract AssessmentDao assessmentDao();

    private static volatile ScheduleDBBuilder INSTANCE;

    static ScheduleDBBuilder getDatabase(final Context context){
        if(INSTANCE == null) {
            synchronized (ScheduleDBBuilder.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), ScheduleDBBuilder.class, "SchedulerDB.db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
