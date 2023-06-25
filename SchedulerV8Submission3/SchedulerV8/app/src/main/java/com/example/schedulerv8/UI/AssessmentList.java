package com.example.schedulerv8.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.schedulerv8.Adapters.AssessmentAdapter;
import com.example.schedulerv8.Database.Repository;
import com.example.schedulerv8.Entities.Assessment;
import com.example.schedulerv8.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class AssessmentList extends AppCompatActivity {
    private Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_list);
        RecyclerView assessmentsRV = findViewById(R.id.assessListAllRV);
        final AssessmentAdapter assessmentAdapter = new AssessmentAdapter(this);
        assessmentsRV.setAdapter(assessmentAdapter);
        assessmentsRV.setLayoutManager(new LinearLayoutManager(this));
        repository = new Repository(getApplication());
        List<Assessment> allAssessments = repository.getAllAssessments();
        assessmentAdapter.setAllAssessments(allAssessments);
        FloatingActionButton addAssessmentFAB = findViewById(R.id.assessListAddNewFAB);
        addAssessmentFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AssessmentList.this, AssessmentDetails.class);
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        List<Assessment> allAssessments = repository.getAllAssessments();
        RecyclerView assessmentsRV = findViewById(R.id.assessListAllRV);
        final AssessmentAdapter assessmentAdapter = new AssessmentAdapter(this);
        assessmentsRV.setAdapter(assessmentAdapter);
        assessmentsRV.setLayoutManager(new LinearLayoutManager(this));
        assessmentAdapter.setAllAssessments(allAssessments);
    }
}