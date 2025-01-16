package edu.virginia.sde.reviews;


import java.sql.Timestamp;
import java.util.Optional;

public class Review {

    private final int rating;
    private final Timestamp timeStamp;
    private final User user;
    private final Course course;
    private final Optional<String> comment;

    private boolean deletable;

    public Review(int rating, Timestamp timeStamp, Course course, User user, String comment) {
        this.rating = rating;
        this.timeStamp = timeStamp;
        this.course = course;
        this.user = user;
        this.deletable = false;
        if (comment == null) {
            this.comment = Optional.empty();
        } else {
            this.comment = Optional.of(comment);
        }
    }

    //TODO: getters (no setters)
    // On edit a review should be deleted and recreated (not set in stone)
    public int getRating(){
        return rating;
    }

    public Course getCourse(){
        return course;
    }

    public User getUser(){
        return user;
    }

    public Timestamp getTimeStamp(){
        return timeStamp;
    }

    public Optional<String> getComment(){
        return comment;
    }

    public void setDeletable(boolean status){
        this.deletable = status;
    }

    public boolean getDeletable(){
        return deletable;
    }

    @Override
    public boolean equals(Object o){
        Review otherReview = (Review) o;
        if(this.course.equals(otherReview.getCourse()) &&
                this.user.equals(otherReview.getUser()) &&
                this.rating == otherReview.getRating() &&
                this.comment.equals(otherReview.getComment()) &&
                this.timeStamp.equals(otherReview.getTimeStamp())){
            return true;
        }
        return false;
    }
}
