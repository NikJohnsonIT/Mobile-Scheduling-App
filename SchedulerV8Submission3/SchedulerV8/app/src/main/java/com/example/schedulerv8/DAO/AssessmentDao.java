package com.example.schedulerv8.DAO;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.example.schedulerv8.Entities.Assessment;
import com.example.schedulerv8.Entities.Course;

import java.util.List;

@Dao
public interface AssessmentDao {
    @Query("SELECT * FROM assessments WHERE assessment_course_id = :courseId")
    List<Assessment> getAssessmentsByCourseId(int courseId);

    @Query("SELECT * FROM assessments")
    List<Assessment> getAllAssessments();

    @Query("SELECT * FROM assessments WHERE assessment_course_id = :courseId")
    List<Assessment> getAssocAssess(int courseId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAssessment(Assessment assessment);

    @Delete
    void deleteAssessment(Assessment assessment);

    @Update
    void updateAssessment(Assessment assessment);
}
