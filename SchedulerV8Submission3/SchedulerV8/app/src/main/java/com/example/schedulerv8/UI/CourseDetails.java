package com.example.schedulerv8.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
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
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;


import com.example.schedulerv8.Adapters.AssessmentAdapter;
import com.example.schedulerv8.Database.Repository;
import com.example.schedulerv8.Entities.Assessment;
import com.example.schedulerv8.Entities.Course;
import com.example.schedulerv8.Entities.Term;
import com.example.schedulerv8.R;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CourseDetails extends AppCompatActivity {

    Repository repository;
    Spinner termSpinner;
    //should autogenerate
    EditText courseIDControl;
    EditText courseNameControl;
    EditText courseStartDate;
    EditText courseEndDate;
    DatePickerDialog.OnDateSetListener courseStartListener;
    DatePickerDialog.OnDateSetListener courseEndListener;
    final Calendar courseStartCalendar = Calendar.getInstance();
    final Calendar courseEndCalendar = Calendar.getInstance();
    Spinner courseStatus;
    EditText instructorNameControl;
    EditText instructorPhoneControl;
    EditText instructorEmailControl;
    RecyclerView assessmentsRV;
    EditText courseNotesControl;
    Button saveCourseButton;

    Button addAssessButton;

    int termId;
    int courseIdValue;
    String courseNameValue;
    String startDateValue;
    String endDateValue;
    String statusValue;
    String instructorNameVal;
    String instructorPhoneVal;
    String instructorEmailVal;
    String courseNotesVal;
    Course course;
    Course currentCourse;
    ArrayAdapter<CharSequence> statusAdapter;
    ArrayAdapter<Term> termArrayAdapter;
//TODO populate term spinner with available termID from DB, issue understanding how/where to do this.

    ArrayAdapter<Integer> termIdAdapter;
    Term selectedTerm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);
        repository = new Repository(getApplication());
        courseStatus = findViewById(R.id.courseStatusSpinner);
        statusAdapter = ArrayAdapter.createFromResource(this, R.array.course_status_array, android.R.layout.simple_spinner_item);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        courseStatus.setAdapter(statusAdapter);

        courseIDControl = findViewById(R.id.courseDetailsCourseIdField);
        courseNameControl = findViewById(R.id.courseDetailsCourseNameField);
        courseStartDate = findViewById(R.id.courseStartDatePicker);
        courseEndDate = findViewById(R.id.courseEndDatePicker);
        instructorNameControl = findViewById(R.id.courseInstructorNameField);
        instructorPhoneControl = findViewById(R.id.courseInstructorPhoneField);
        instructorEmailControl = findViewById(R.id.courseInstructorEmailField);
        assessmentsRV = findViewById(R.id.assocAssessRV);
        courseNotesControl = findViewById(R.id.courseNoteField);
        saveCourseButton = findViewById(R.id.saveCourseBtn);

        addAssessButton = findViewById(R.id.addAssessBtn);

        //only int have default value. will need to check later, if -1 is new course, other number is existing course.
        termId = getIntent().getIntExtra("course_term_id", -1);
        courseIdValue = getIntent().getIntExtra("course_id", -1);
        instructorNameVal = getIntent().getStringExtra("instructor_name");
        instructorPhoneVal = getIntent().getStringExtra("instructor_phone");
        instructorEmailVal = getIntent().getStringExtra("instructor_email");
        courseNotesVal = getIntent().getStringExtra("course_note");
        statusValue = getIntent().getStringExtra("status");
        courseNameValue = getIntent().getStringExtra("course_title");
        startDateValue = getIntent().getStringExtra("course_start_date");
        endDateValue = getIntent().getStringExtra("course_end_date");

        String myFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);



        //TODO fix this, something not right with onItemSelected.
        termSpinner = findViewById(R.id.courseDetailsTermIdSpinner);
        termArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, repository.getAllTerms());
        termSpinner.setAdapter(termArrayAdapter);
        termSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Term selectedTerm = (Term) adapterView.getSelectedItem();
                if (selectedTerm != null) {
                    termId = selectedTerm.getTermId();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });





        courseStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                statusValue = courseStatus.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //statusValue = "PLAN TO TAKE";
            }
        });


        courseStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date date;
                String info = courseStartDate.getText().toString();
                try {
                    courseStartCalendar.setTime(sdf.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new DatePickerDialog(CourseDetails.this, courseStartListener, courseStartCalendar.get(Calendar.YEAR),
                        courseStartCalendar.get(Calendar.MONTH), courseStartCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        courseStartListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                courseStartCalendar.set(Calendar.YEAR, year);
                courseStartCalendar.set(Calendar.MONTH, monthOfYear);
                courseStartCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        courseEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date date;
                String info = courseEndDate.getText().toString();
                try {
                    courseEndCalendar.setTime(sdf.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new DatePickerDialog(CourseDetails.this, courseEndListener, courseEndCalendar.get(Calendar.YEAR),
                        courseEndCalendar.get(Calendar.MONTH), courseEndCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        courseEndListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                courseEndCalendar.set(Calendar.YEAR, year);
                courseEndCalendar.set(Calendar.MONTH, monthOfYear);
                courseEndCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateEndLabel();
            }
        };

        addAssessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CourseDetails.this, AssessmentDetails.class);
                startActivity(intent);
            }
        });

        saveCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO check this constructor, most concerned with the status.

                if (courseIdValue == -1) {

                course = new Course(termId, 0, courseNameControl.getText().toString(), courseStartDate.getText().toString(),
                        courseEndDate.getText().toString(), courseStatus.getSelectedItem().toString(), instructorNameControl.getText().toString(), instructorPhoneControl.getText().toString(),
                        instructorEmailControl.getText().toString(), courseNotesControl.getText().toString());
                repository.insert(course);
                } else {
                    course = new Course(termId, courseIdValue, courseNameControl.getText().toString(), courseStartDate.getText().toString(),
                            courseEndDate.getText().toString(), courseStatus.getSelectedItem().toString(), instructorNameControl.getText().toString(), instructorPhoneControl.getText().toString(),
                            instructorEmailControl.getText().toString(), courseNotesControl.getText().toString());
                    repository.update(course);
                }
                Intent intent = new Intent(CourseDetails.this, CourseList.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        //TODO making updates to align with TermDetails work.

        if (courseIdValue > 0) {

            String schedulerDateFormat = "MM/dd/yyyy";
            SimpleDateFormat sdf = new SimpleDateFormat(schedulerDateFormat, Locale.US);
            RecyclerView assessmentsRV = findViewById(R.id.assocAssessRV);
            final AssessmentAdapter assessmentAdapter = new AssessmentAdapter(this);
            assessmentsRV.setAdapter(assessmentAdapter);
            assessmentsRV.setLayoutManager(new LinearLayoutManager(this));
            List<Assessment> assocAssess = repository.getAssocAssess(courseIdValue);
            assessmentAdapter.setAllAssessments(assocAssess);
            assessmentAdapter.notifyDataSetChanged();

            //TODO check this on Thurday with Juan. Possible issue with saving TermID value.
            termId = getIntent().getIntExtra("course_term_id", -1);
            termSpinner = findViewById(R.id.courseDetailsTermIdSpinner);
            termArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, repository.getAllTerms());
            termSpinner.setAdapter(termArrayAdapter);
            int termPosition = getTermPosition(termId);
            termSpinner.setSelection(termPosition);




            termSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    Term selectedTerm = (Term) adapterView.getSelectedItem();
                    if (selectedTerm != null) {
                        termId = selectedTerm.getTermId();
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            courseIdValue = getIntent().getIntExtra("course_id", -1);
            courseIDControl.setText(String.valueOf(courseIdValue));
            courseNameControl.setText(courseNameValue);
            if (startDateValue != ""){
                courseStartDate.setText(startDateValue);
            } else {
                courseStartDate.setText(sdf.format(new Date()));
            }
            if (endDateValue != "") {
                courseEndDate.setText(endDateValue);
            } else {
                courseEndDate.setText(sdf.format(new Date()));
            }
            instructorNameControl.setText(instructorNameVal);
            instructorPhoneControl.setText(instructorPhoneVal);
            instructorEmailControl.setText(instructorEmailVal);
            courseNotesControl.setText(courseNotesVal);


            if (statusValue == null) {
                statusValue = "PLAN TO TAKE";
            }
            //find position of status value, set spinner to that.
            int spinnerPosition = statusAdapter.getPosition(statusValue);
            courseStatus.setSelection(spinnerPosition);
        }


        //vale.setvalue goes down here.
        //when I load a course from repository, courseId


    }

    private int getTermPosition(int termId) {
        for (int i = 0; i < termArrayAdapter.getCount(); i++){
            Term term = termArrayAdapter.getItem(i);
            if (term != null && term.getTermId() == termId) {
                return i;
            }
        }
        return 0;
    }

    //add onresume here, data loading piece goes here
    //TODO probably need more than one update label as they only have the one calendar.getTime(). what exactly is the update label doing? updating the text that displays for the field?? video use button, I want a text field.
    private void updateLabel() {
        String myFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        courseStartDate.setText(sdf.format(courseStartCalendar.getTime()));
    }

    private void updateEndLabel() {
        String myFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        courseEndDate.setText(sdf.format(courseEndCalendar.getTime()));
    }

    //TODO check these menu options to ensure I made them correctly and they accomplish the correct task. NotifyEnd definitely has issues.
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_course_details, menu);
        return true;
    }



    @SuppressLint("NonConstantResourceId")
    public boolean onOptionsItemSelected(MenuItem item) {
        String myFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, courseNotesControl.getText().toString());
                sendIntent.putExtra(Intent.EXTRA_TITLE, "Message Title");
                sendIntent.setType("text/plain");
                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
                return true;
            case R.id.notifyStart:
                String startDateFromScreen = courseStartDate.getText().toString();
                String courseName = courseNameControl.getText().toString();
                Date myDate = null;
                try {
                    myDate = sdf.parse(startDateFromScreen);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Long trigger = myDate.getTime();
                Intent intent = new Intent(CourseDetails.this, MyReceiver.class);
                //TODO add a toast. change the intent.putExtra line to contain more information in message. Notifications must be configured in phone settings, make this note
                Toast.makeText(CourseDetails.this, currentCourse.getCourseTitle() + " begins " + currentCourse.getCourseStartDate(), Toast.LENGTH_LONG).show();
                intent.putExtra("key", courseName + " begins on " + startDateFromScreen + "!");
                PendingIntent sender = PendingIntent.getBroadcast(CourseDetails.this, ++MainActivity.numAlert, intent, PendingIntent.FLAG_IMMUTABLE);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
                return true;
            case R.id.notifyEnd:
                String endDateFromScreen = courseEndDate.getText().toString();
                courseName = courseNameControl.getText().toString();
                try {
                    myDate = sdf.parse(endDateFromScreen);
                    trigger = myDate.getTime();
                    intent = new Intent(CourseDetails.this, MyReceiver.class);
                    Toast.mak
                    intent.putExtra("key", courseName + " ends on " + endDateFromScreen + "!");
                    sender = PendingIntent.getBroadcast(CourseDetails.this, ++MainActivity.numAlert, intent, PendingIntent.FLAG_IMMUTABLE);
                    alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return true;
            case R.id.removeCourse:
                for (Course course : repository.getAllCourses()) {
                    if (course.getCourseId() == courseIdValue) currentCourse = course;
                }
                repository.delete(currentCourse);
                Toast.makeText(CourseDetails.this, currentCourse.getCourseTitle() + " was removed", Toast.LENGTH_LONG).show();
                Intent intentForDelete = new Intent(CourseDetails.this, CourseList.class);
                startActivity(intentForDelete);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}