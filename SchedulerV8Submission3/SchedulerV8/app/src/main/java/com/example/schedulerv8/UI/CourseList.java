package com.example.schedulerv8.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.schedulerv8.Adapters.CourseAdapter;
import com.example.schedulerv8.Database.Repository;
import com.example.schedulerv8.Entities.Course;
import com.example.schedulerv8.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class CourseList extends AppCompatActivity {

    private Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);
        RecyclerView courseListRV = findViewById(R.id.coursesListAllCoursesRV);
        final CourseAdapter courseAdapter = new CourseAdapter(this);
        courseListRV.setAdapter(courseAdapter);
        courseListRV.setLayoutManager(new LinearLayoutManager(this));
        repository = new Repository(getApplication());
        List<Course> allCourses = repository.getAllCourses();
        courseAdapter.setAllCourses(allCourses);
        FloatingActionButton addCourseFAB = findViewById(R.id.courseListAddCourseFAB);
        addCourseFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CourseList.this, CourseDetails.class);
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onResume(){
        super.onResume();
        List<Course> allCourses = repository.getAllCourses();
        RecyclerView courseListRV = findViewById(R.id.coursesListAllCoursesRV);
        final CourseAdapter courseAdapter = new CourseAdapter(this);
        courseListRV.setAdapter(courseAdapter);
        courseListRV.setLayoutManager(new LinearLayoutManager(this));
        courseAdapter.setAllCourses(allCourses);

    }
}