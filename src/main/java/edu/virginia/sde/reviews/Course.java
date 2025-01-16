package edu.virginia.sde.reviews;

import javafx.event.Event;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Course {

    private String subject;
    private final int courseNumber;

    //private int averageRating;

    private final String title;

    private double averageRating = -1.0;

    public Course(String subject, int courseNumber, String title) {
        this.subject = subject;
        this.courseNumber = courseNumber;
        this.title = title;
    }

    //TODO: Getters and add/delete review feature

    public String getSubject(){
        return subject;
    }

    public void setSubject(String subject){
        this.subject = subject;
    }

    public int getCourseNumber(){
        return courseNumber;
    }

    public String getTitle(){
        return title;
    }

    public double getAverageRating(){
        return averageRating;
    }

    public void setAverageRating(double rating){
        this.averageRating = rating;
    }

    @Override
    public boolean equals(Object o){
        Course otherCourse = (Course) o;
        if(this.subject.equals(otherCourse.getSubject()) &&
                this.courseNumber == otherCourse.getCourseNumber() &&
                this.title.equals(otherCourse.getTitle())){
            return true;
        }
        return false;
    }

}
