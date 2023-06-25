package com.example.schedulerv8.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.example.schedulerv8.Entities.Course;
import java.util.List;

@Dao
public interface CourseDao {
    @Query("SELECT * FROM courses")
    List<Course> getAllCourses();

    @Query("SELECT * FROM courses WHERE course_id = :courseId")
    Course getCourseById(int courseId);

    @Query("SELECT * FROM courses WHERE course_term_id = :termId")
    List<Course> getAssocCourses(int termId);

    //todo Check this to see if defTerm is correct; Can I use defTerm as a placeholder then set it to 0 later? or should the query use 0?
    @Query("SELECT * FROM courses WHERE course_term_id = :defTerm")
    List<Course> getAllAvailCourses(int defTerm);

    @Query("DELETE FROM courses")
    void deleteAllCourses();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCourse(Course course);

    @Update
    void updateCourse(Course course);

    @Delete
    void deleteCourse(Course course);
}
