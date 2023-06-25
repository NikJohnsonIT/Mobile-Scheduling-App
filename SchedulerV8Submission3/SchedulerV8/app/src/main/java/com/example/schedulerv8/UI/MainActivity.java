package com.example.schedulerv8.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


import com.example.schedulerv8.Database.Repository;
import com.example.schedulerv8.Entities.Assessment;
import com.example.schedulerv8.Entities.Course;
import com.example.schedulerv8.Entities.Term;
import com.example.schedulerv8.R;

public class MainActivity extends AppCompatActivity {
    public static int numAlert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Repository repository = new Repository(getApplication());

        //Define Buttons. Only use floating action button if it is the only button as per android documentation.
        Button termButton = findViewById(R.id.mainScreenTermsButton);
        Button courseButton = findViewById(R.id.mainScreenCoursesButton);
        Button assessmentButton = findViewById(R.id.mainScreenAssessmentsButton);

        //add menu option to add sample data (in on Create)
        //onClickListeners for each button
        termButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //This is telling the app where it is and where it is going following the button being clicked/pushed.
                Intent intent = new Intent(MainActivity.this, TermsList.class);
                startActivity(intent);
            }
        });

        courseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CourseList.class);
                startActivity(intent);
            }
        });

        assessmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AssessmentList.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.addSampleDataTest){
            Repository repository = new Repository(getApplication());
            Term sampleTerm1 = new Term( "Sample Term 1", "06/20/2023", "06/30/2023");
            Course sampleCourse1 = new Course(1, 1, "Sample Course 1", "06/20/2023", "06/30/2023", "PLAN TO TAKE", "Ex Name", "Ex Phone", "Ex Email", "Ex Note Text");
            Assessment sampleAssessment1 = new Assessment("Sample Assessment 1", 1, "06/30/2023", "06/30/2023", "Objective Assessment");

            repository.insert(sampleTerm1);
            repository.insert(sampleCourse1);
            repository.insert(sampleAssessment1);

            //TODO Sample Data
            Term sampleTerm2 = new Term("Sample Term 2", "07/01/2023", "08/01/2023");
            Course sampleCourse2 = new Course(1, 2, "Sample Course 2", "07/01/2023", "08/01/2023", "PLAN TO TAKE", "Ins Name", "Ins Phone", "Ine Email", "Course Notes Sample Text");
            Assessment sampleAssessment2 = new Assessment("Sample Assessment 2", 2, "07/20/2023", "07/20/2023", "Performance Assessment");

            repository.insert(sampleTerm2);
            repository.insert(sampleCourse2);
            repository.insert(sampleAssessment2);

            Term sampleTerm3 = new Term("Sample Term 3", "08/02/2023", "12/31/2023");
            Course sampleCourse3 = new Course(1, 3, "Sample Course 3", "08/02/2023", "12/31/2023", "PLAN TO TAKE", "Instructor Name", "Instructor Phone", "Instructor Email", "Course Notes Sample Text");
            Assessment sampleAssessment3 = new Assessment("Sample Assessment 3", 3, "10/31/2023", "10/31/2023", "Performance Assessment");
            Assessment sampleAssessment4 = new Assessment("Sample Assessment 4", 3, "12/30/2023", "12/30/2023", "Objective Assessment");

            repository.insert(sampleTerm3);
            repository.insert(sampleCourse3);
            repository.insert(sampleAssessment3);
            repository.insert(sampleAssessment4);

            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

}
