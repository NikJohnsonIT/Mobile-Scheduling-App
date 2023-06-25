package com.example.schedulerv8.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import com.example.schedulerv8.Adapters.CourseAdapter;
import com.example.schedulerv8.DAO.TermDao;
import com.example.schedulerv8.Database.Repository;
import com.example.schedulerv8.Entities.Course;
import com.example.schedulerv8.Entities.Term;
import com.example.schedulerv8.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TermDetails extends AppCompatActivity {
    Repository repository;
    EditText termDetailsTermIdFromDB;
    EditText termDetailsTermName;
    EditText termDetailsTermStartDate;
    EditText termDetailsTermEndDate;
    DatePickerDialog.OnDateSetListener termStartDate;
    DatePickerDialog.OnDateSetListener termEndDate;
    final Calendar termStartCalendar = Calendar.getInstance();
    final Calendar termEndCalendar = Calendar.getInstance();

    Button saveTermButton;

    Button addCourseButton;

    int termId;
    Term term;
    Term currentTerm;
    String termName;
    String aStartDate;
    String aEndDate;

    RecyclerView associatedCoursesRecyclerView;

    int numCourses;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //add button for saving term
        //load CONTROLS/fields with default
        super.onCreate(savedInstanceState);
        repository = new Repository(getApplication());
        setContentView(R.layout.activity_term_details);
        String myFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);


        //findviewID first, set references for controls.
        //on click
        //date set
        termDetailsTermIdFromDB = findViewById(R.id.termEditDetailsTermIDNonEditField);
        termDetailsTermName = findViewById(R.id.termEditDetailsTermNameEdit);
        termDetailsTermStartDate = findViewById(R.id.termEditDetailsTermStartDatePicker);
        termDetailsTermEndDate = findViewById(R.id.termEditDetailsTermEndDatePicker);
        associatedCoursesRecyclerView = findViewById(R.id.associatedCoursesRecyclerView);


        saveTermButton = findViewById(R.id.termSaveBtn);

        addCourseButton = findViewById(R.id.addCourseBtn);
        termId = getIntent().getIntExtra("term_id", -1);
        aStartDate = getIntent().getStringExtra("term_start_date");
        aEndDate = getIntent().getStringExtra("term_end_date");
        termName = getIntent().getStringExtra("term_title");

        termDetailsTermStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date date;
                String info = termDetailsTermStartDate.getText().toString();
                try {
                    termStartCalendar.setTime(sdf.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new DatePickerDialog(TermDetails.this, termStartDate, termStartCalendar.get(Calendar.YEAR),
                        termStartCalendar.get(Calendar.MONTH), termStartCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        termStartDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                termStartCalendar.set(Calendar.YEAR, year);
                termStartCalendar.set(Calendar.MONTH, monthOfYear);
                termStartCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateStartLabel();
            }
        };

        termDetailsTermEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date date;
                String info = termDetailsTermEndDate.getText().toString();
                try {
                    termEndCalendar.setTime(sdf.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new DatePickerDialog(TermDetails.this, termEndDate, termEndCalendar.get(Calendar.YEAR),
                        termEndCalendar.get(Calendar.MONTH), termEndCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        termEndDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                termEndCalendar.set(Calendar.YEAR, year);
                termEndCalendar.set(Calendar.MONTH, monthOfYear);
                termEndCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateEndLabel();

            }
        };


        addCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TermDetails.this, CourseDetails.class);
                startActivity(intent);
            }
        });

        saveTermButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (termId == -1) {
                    term = new Term(0, termDetailsTermName.getText().toString(), termDetailsTermStartDate.getText().toString(), termDetailsTermEndDate.getText().toString());
                    repository.insert(term);

                } else {
                    term = new Term(termId, termDetailsTermName.getText().toString(), termDetailsTermStartDate.getText().toString(), termDetailsTermEndDate.getText().toString());
                    repository.update(term);
                }
                Intent intent = new Intent(TermDetails.this, TermsList.class);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        //this is where I put the DATA in the fields/controls

        if (termId > 0) {
            String schedulerDateFormat = "MM/dd/yyyy";
            SimpleDateFormat sdf = new SimpleDateFormat(schedulerDateFormat, Locale.US);
            RecyclerView associatedCoursesRecyclerView = findViewById(R.id.associatedCoursesRecyclerView);
            final CourseAdapter courseAdapter = new CourseAdapter(this);
            associatedCoursesRecyclerView.setAdapter(courseAdapter);
            associatedCoursesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            List<Course> associatedCourses = repository.getAssociatedCourses(termId);
            courseAdapter.setAllCourses(associatedCourses);
            courseAdapter.notifyDataSetChanged();



            termId = getIntent().getIntExtra("term_id", -1);
            termDetailsTermIdFromDB.setText(String.valueOf(termId));
            termDetailsTermName.setText(termName);
            if (aStartDate != "") {
                termDetailsTermStartDate.setText(aStartDate);
            } else {
                termDetailsTermStartDate.setText(sdf.format(new Date()));
            }
            if (aEndDate != "") {
                termDetailsTermEndDate.setText(aEndDate);
            } else {
                termDetailsTermEndDate.setText(sdf.format(new Date()));
            }
        }


    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.delete_term, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.deleteTerm:
                for (Term term : repository.getAllTerms()) {
                    if (term.getTermId() == termId) currentTerm = term;
                }
                numCourses = 0;
                for (Course course : repository.getAllCourses()) {
                    if (course.getTermId() == termId) ++numCourses;
                }
                if (numCourses == 0) {
                    repository.delete(currentTerm);
                    Toast.makeText(TermDetails.this, currentTerm.getTermTitle() + " was deleted", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(TermDetails.this, TermsList.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(TermDetails.this, "Can't delete Term with Associated Courses", Toast.LENGTH_LONG).show();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateStartLabel() {
        String myFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        termDetailsTermStartDate.setText(sdf.format(termStartCalendar.getTime()));
    }

    private void updateEndLabel() {
        String myFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        termDetailsTermEndDate.setText(sdf.format(termEndCalendar.getTime()));
    }
}
