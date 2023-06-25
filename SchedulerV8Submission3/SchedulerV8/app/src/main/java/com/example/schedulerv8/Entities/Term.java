package com.example.schedulerv8.Entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "terms")
public class Term {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "term_id")
    private int termId;
    @ColumnInfo(name = "term_title")
    private String termTitle;
    @ColumnInfo(name = "term_start_date")
    private String startDate;
    @ColumnInfo(name = "term_end_date")
    private String endDate;

    public Term(String termTitle, String startDate, String endDate) {
        this.termTitle = termTitle;
        this.startDate = startDate;
        this.endDate = endDate;
    }
    public Term(){
    }

    public Term(int termId, String termTitle, String startDate, String endDate){
        this.termId=termId;
        this.termTitle=termTitle;
        this.startDate=startDate;
        this.endDate=endDate;
    }

    public String getTermTitle() {
        return termTitle;
    }

    public void setTermTitle(String termTitle) {
        this.termTitle = termTitle;
    }

    public int getTermId() {
        return termId;
    }

    public void setTermId(int termId) {
        this.termId = termId;
    }


    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    //TODO check this toString to see if the green is supposed to be the titles of the column, and what exactly is going on here.
    //I want the spinner to have the existing termIDs and possible the term title.
    public String toString() {

        return termId + " " + termTitle;
    }
}
