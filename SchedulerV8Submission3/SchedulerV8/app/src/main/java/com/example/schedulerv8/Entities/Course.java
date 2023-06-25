package com.example.schedulerv8.Entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "courses")
public class Course {
    @ColumnInfo(name = "course_term_id")
    private int termId;
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "course_id")
    private int courseId;

    @NonNull
    @ColumnInfo(name = "course_title")
    private String courseTitle;

    @ColumnInfo(name = "course_start_date")
    private String courseStartDate;
    @ColumnInfo(name = "course_end_date")
    private String courseEndDate;

    public Course(int termId, int courseId, String courseTitle, String courseStartDate, String courseEndDate, String status, String courseInstructorName, String courseInstructorPhone, String courseInstructorEmail, String courseNote) {
        this.termId = termId;
        this.courseId = courseId;
        this.courseTitle = courseTitle;
        this.courseStartDate = courseStartDate;
        this.courseEndDate = courseEndDate;
        this.status = status;
        this.courseInstructorName = courseInstructorName;
        this.courseInstructorPhone = courseInstructorPhone;
        this.courseInstructorEmail = courseInstructorEmail;
        this.courseNote = courseNote;
    }

    @ColumnInfo(name = "status")
    private String status;
    @ColumnInfo(name = "instructor_name")
    private String courseInstructorName;
    @ColumnInfo(name = "instructor_phone")
    private String courseInstructorPhone;
    @ColumnInfo(name = "instructor_email")
    private String courseInstructorEmail;
    @ColumnInfo(name = "course_note")
    private String courseNote;

    public Course(){

    }

    public Course(@NonNull String courseTitle, String courseStartDate, String courseEndDate, @NonNull String status, String courseInstructorName, String courseInstructorPhone, String courseInstructorEmail, String courseNote) {
        this.courseTitle=courseTitle;
        this.courseStartDate=courseStartDate;
        this.courseEndDate=courseEndDate;
        this.status=status;
        this.courseInstructorName=courseInstructorName;
        this.courseInstructorPhone=courseInstructorPhone;
        this.courseInstructorEmail=courseInstructorEmail;
    }

    public int getTermId(){return termId;}
    public void setTermId(int termId) {this.termId=termId;}
    public int getCourseId(){
        return courseId;
    }

    public void setCourseId(int courseId){
        this.courseId=courseId;
    }

    public String getCourseTitle(){
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle){
        this.courseTitle=courseTitle;
    }

    public String getCourseStartDate(){
        return courseStartDate;
    }

    public void setCourseStartDate(String courseStartDate){
        this.courseStartDate=courseStartDate;
    }

    public String getCourseEndDate(){
        return courseEndDate;
    }
    public void setCourseEndDate(String courseEndDate){
        this.courseEndDate=courseEndDate;
    }

    public String getStatus(){
        return status;
    }

    public void setStatus(String status){
        this.status=status;
    }

    public String getCourseInstructorName(){
        return courseInstructorName;
    }

    public void setCourseInstructorName(String courseInstructorName){
        this.courseInstructorName=courseInstructorName;
    }

    public String getCourseInstructorPhone(){
        return courseInstructorPhone;
    }

    public void setCourseInstructorPhone(String courseInstructorPhone){
        this.courseInstructorPhone=courseInstructorPhone;
    }

    public String getCourseInstructorEmail(){
        return courseInstructorEmail;
    }

    public void setCourseInstructorEmail(String courseInstructorEmail){
        this.courseInstructorEmail=courseInstructorEmail;
    }

    public String getCourseNote(){return courseNote;}

    public void setCourseNote(String courseNote){
        this.courseNote=courseNote;
    }

    @Override
    public String toString() {
        return this.courseTitle;
    }
}