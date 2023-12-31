package com.example.schedulerv8.UI;

import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.Spinner;
import android.widget.Toast;

import com.example.schedulerv8.Database.Repository;
import com.example.schedulerv8.Entities.Assessment;
import com.example.schedulerv8.Entities.Course;
import com.example.schedulerv8.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AssessmentDetails extends AppCompatActivity {
    Repository repository;
    Spinner courseIDSpinner;
    EditText assessmentIdET;
    EditText assessmentNameET;
    EditText assessmentStart;
    EditText assessmentEnd;
    Spinner assessmentTypeSpinner;
    DatePickerDialog.OnDateSetListener assessmentStartListener;
    DatePickerDialog.OnDateSetListener assessmentEndListener;
    final Calendar assessmentStartCal = Calendar.getInstance();
    final Calendar assessmentEndCal = Calendar.getInstance();
    Button saveAssessmentButton;
    Button deleteAssessmentButton;

    int courseIdVal;

    int assessmentIdVal;
    String assessmentNameVal;
    String assessmentStartVal;
    String assessmentEndVal;
    //TODO check this value, should it be a string or the enum?
    String assessmentType;
    Assessment assessment;
    Assessment currentAssessment;
    ArrayAdapter<CharSequence> assessTypeAdapter;
    ArrayAdapter<Course> courseArrayAdapter;


    //TODO still need to better my understanding of onCreate and onResume
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        repository = new Repository(getApplication());
        setContentView(R.layout.activity_assessment_details);
        String myFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        assessmentIdET = findViewById(R.id.assessIDEdit);
        assessmentNameET = findViewById(R.id.assessNameEdit);
        assessmentStart = findViewById(R.id.assessStartDatePicker);
        assessmentEnd = findViewById(R.id.assessEndDatePicker);
        assessmentTypeSpinner = findViewById(R.id.assessTypeSpinner);
        saveAssessmentButton = findViewById(R.id.saveAssessmentBtn);

        assessmentIdVal = getIntent().getIntExtra("assessment_id", -1);
        assessmentNameVal = getIntent().getStringExtra("assessment_title");
        assessmentStartVal = getIntent().getStringExtra("assessment_start_date");
        assessmentEndVal = getIntent().getStringExtra("assessment_end_date");
        assessmentType = getIntent().getStringExtra("assessment_type");
        courseIdVal = getIntent().getIntExtra("assessment_course_id", -1);

        courseIDSpinner = findViewById(R.id.assessCourseIDSpinner);
        courseArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, repository.getAllCourses());
        courseIDSpinner.setAdapter(courseArrayAdapter);
        int coursePosition = getCoursePosition(courseIdVal);
        if (coursePosition != -1) {
            courseIDSpinner.setSelection(coursePosition);
        }
        courseIDSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Course selectedCourse = (Course) adapterView.getSelectedItem();
                if (selectedCourse != null) {
                    courseIdVal = selectedCourse.getCourseId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        assessmentStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date date;
                String info = assessmentStart.getText().toString();
                try {
                    assessmentStartCal.setTime(sdf.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new DatePickerDialog(AssessmentDetails.this, assessmentStartListener, assessmentStartCal.get(Calendar.YEAR),
                        assessmentStartCal.get(Calendar.MONTH), assessmentStartCal.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        assessmentStartListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                assessmentStartCal.set(Calendar.YEAR, year);
                assessmentStartCal.set(Calendar.MONTH, monthOfYear);
                assessmentStartCal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        assessmentEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date date;
                String info = assessmentEnd.getText().toString();
                try {
                    assessmentEndCal.setTime(sdf.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new DatePickerDialog(AssessmentDetails.this, assessmentEndListener, assessmentEndCal.get(Calendar.YEAR),
                        assessmentEndCal.get(Calendar.MONTH), assessmentEndCal.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        assessmentEndListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                assessmentEndCal.set(Calendar.YEAR, year);
                assessmentEndCal.set(Calendar.MONTH, monthOfYear);
                assessmentEndCal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateEndLabel();
            }
        };

//        //TODO check this method, added following example from courseDetails.
        assessmentTypeSpinner = findViewById(R.id.assessTypeSpinner);
        assessTypeAdapter = ArrayAdapter.createFromResource(this, R.array.assessment_type_array, android.R.layout.simple_spinner_item);
        assessTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        assessmentTypeSpinner.setAdapter(assessTypeAdapter);
        if (assessmentType == null) {
            assessmentType = "OBJECTIVE";
        }

        assessmentTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                assessmentType = assessmentTypeSpinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                assessmentType = "OBJECTIVE";
            }
        });

        saveAssessmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO check this constructor as well.
                if (assessmentIdVal == -1) {
                    assessment = new Assessment(0, courseIdVal, assessmentNameET.getText().toString(), assessmentStart.getText().toString(), assessmentEnd.getText().toString(), assessmentType);
                    repository.insert(assessment);
                } else {
                    assessment = new Assessment(assessmentIdVal, courseIdVal, assessmentNameET.getText().toString(), assessmentStart.getText().toString(), assessmentEnd.getText().toString(), assessmentType);
                    repository.update(assessment);
                }
                Intent intent = new Intent(AssessmentDetails.this, AssessmentList.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //TODO Update this with corrections to ensure pages operate as expected.

        if (assessmentIdVal > 0) {
            String schedulerDateFormat = "MM/dd/yyyy";
            SimpleDateFormat sdf = new SimpleDateFormat(schedulerDateFormat, Locale.US);

            courseIdVal = getIntent().getIntExtra("assessment_course_id", -1);
            int coursePosition = getCoursePosition(courseIdVal);
            if (coursePosition != -1) {
                courseIDSpinner.setSelection(coursePosition);
            }



            assessmentNameET.setText(assessmentNameVal);
            if (assessmentStartVal != "") {
                assessmentStart.setText(assessmentStartVal);
            } else {
                assessmentStart.setText(sdf.format(new Date()));
            }
            if (assessmentEndVal != "") {
                assessmentEnd.setText(assessmentEndVal);
            } else {
                assessmentEnd.setText(sdf.format(new Date()));
            }
            assessmentIdVal = getIntent().getIntExtra("assessment_id", -1);
            assessmentIdET.setText(String.valueOf(assessmentIdVal));

            if (assessmentType == null) {
                assessmentType = "OBJECTIVE";
            }
            int spinnerPosition = assessTypeAdapter.getPosition(assessmentType);
            assessmentTypeSpinner.setSelection(spinnerPosition);

        }
    }

    private int getCoursePosition(int courseIdVal) {
        for (int i = 0; i < courseArrayAdapter.getCount(); i++) {
            Course course = courseArrayAdapter.getItem(i);
            if (course != null && course.getCourseId() == courseIdVal) {
                return i;
            }
        }
        return -1;
    }

    //TODO similar to CourseDetails, this probable needs an update label method for the end date as well..

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_assessment_details, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        String myFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.notifyStart:
                String assessmentStartDate = assessmentStart.getText().toString();
                Date myDate = null;
                try {
                    myDate = sdf.parse(assessmentStartDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Long trigger = myDate.getTime();
                Intent intent = new Intent(AssessmentDetails.this, MyReceiver.class);
                intent.putExtra("key", assessmentStartDate + " should trigger");
                PendingIntent sender = PendingIntent.getBroadcast(AssessmentDetails.this, ++MainActivity.numAlert, intent, PendingIntent.FLAG_IMMUTABLE);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
                return true;
            case R.id.notifyEnd:
                String assessmentEndDate = assessmentEnd.getText().toString();
                try {
                    myDate = sdf.parse(assessmentEndDate);
                    trigger = myDate.getTime();
                    intent = new Intent(AssessmentDetails.this, MyReceiver.class);
                    intent.putExtra("key", assessmentEndDate + " should trigger");
                    sender = PendingIntent.getBroadcast(AssessmentDetails.this, ++MainActivity.numAlert, intent, PendingIntent.FLAG_IMMUTABLE);
                    alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return true;
            case R.id.removeAssessment:
                for (Assessment assessment : repository.getAllAssessments()) {
                    if (assessment.getAssessmentId() == assessmentIdVal)
                        currentAssessment = assessment;
                }
                repository.delete(currentAssessment);
                Toast.makeText(AssessmentDetails.this, currentAssessment.getAssessmentTitle() + " was removed", Toast.LENGTH_LONG).show();
                Intent intentForDelete = new Intent(AssessmentDetails.this, AssessmentList.class);
                startActivity(intentForDelete);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        assessmentStart.setText(sdf.format(assessmentStartCal.getTime()));
    }

    private void updateEndLabel() {
        String myFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        assessmentEnd.setText(sdf.format(assessmentEndCal.getTime()));
    }
}