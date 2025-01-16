package edu.virginia.sde.reviews;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * This class should handle ALL logic
 * Will serve as middleman between frontend and backend
 * For Example:
 *  Front-end user goes to add a course, front-end sends request (in form of Course object) here
 *  CourseReviewDriver verifies the course doesn't already exist
 *  If already exists: tell front-end and do nothing
 *  If not: tell front-end success and create course
 *  Another Example:
 *  Front-end user wants to see all courses, front-end asks CRD for courses, CRD finds from
 *  databaseDriver and then passes along
 */
public class CourseReviewDriver {

    private DatabaseDriver driver;

    //TODO: always track who the current user is so any action tied to them
    private User currentUser;

    private boolean failed = false;

    public CourseReviewDriver() {
        try{
            driver = new DatabaseDriver();
            driver.connect();
            driver.ensureTables();
            driver.disconnect();
        } catch(SQLException e){
            failed = true;
        }
    }

    public boolean hasReviewed(List<Review> reviews) {
        if (reviews == null) {
            return false;
        }
        if (currentUser == null) {
            return false;
        }
        for (Review review: reviews) {
            if (review.getUser().equals(currentUser))
                return true;
        }
        return false;
    }

    /**
     * Example of what this is gonna look like
     */
    private boolean verifyPassword(User user){
        if (user.getPassword().length() < 8) {
            return false;
        }
        return true;
    }

    public boolean userExists(User user) {
        try {
            driver.connect();
            List<User> userList = driver.getAllUsers();
            driver.disconnect();
            for (User u : userList) {
                if (u.getUsername().equals(user.getUsername())) {
                    return true;
                }
            }
            return false;
        } catch (SQLException e) {
            return true;
        }
    }

    public ReviewCreationCode createReview(String comment, String ratingScore, Course course) {
        try{
            int rating = Integer.parseInt(ratingScore);
            Timestamp ts = new Timestamp(System.currentTimeMillis());
            Review review = new Review(rating, ts, course, this.currentUser, comment);
            if(rating <= 5 && rating >= 1){
                driver.connect();
                driver.addReview(review);
                driver.disconnect();
                return ReviewCreationCode.SUCCESS;
            }else{
                return ReviewCreationCode.RATING_NOT_IN_RANGE;
            }
        }catch (SQLException e) {
            return ReviewCreationCode.FAILED;
        }catch (NumberFormatException e1) {
            try{
                Double.parseDouble(ratingScore);
                return ReviewCreationCode.NOT_AN_INT;
            }catch (NumberFormatException e2){
                return ReviewCreationCode.NOT_A_NUMBER;
            }
        }
    }

    public ReviewCreationCode deleteReview(Review review){
        try{
            driver.connect();
            driver.deleteReview(review);
            driver.disconnect();
            return ReviewCreationCode.SUCCESS;
        }catch (SQLException e){
            return ReviewCreationCode.FAILED;
        }
    }

    public boolean correctPassword(User user) {
        if (!userExists(user)) return false;
        try {
            driver.connect();
            User new_user = driver.getUserByUsername(user.getUsername());
            driver.disconnect();
            return user.getPassword().equals(new_user.getPassword());
        } catch (SQLException e) {
            return false;
        }
    }

    public UserCreationCode createUser(User user) {
        try{
            if (userExists(user)) {
                return UserCreationCode.USERNAME_TAKEN;
            }
            if (!verifyPassword(user)) {
                return UserCreationCode.PASSWORD_NOT_8_CHARACTERS;
            }
            driver.connect();
            driver.addUser(user);
            driver.disconnect();
            return UserCreationCode.USER_ADDED;
        } catch(SQLException e){
            return UserCreationCode.SQL_EXCEPTION;
        }
    }

    private boolean courseAlreadyExists(Course course) {
        try{
            driver.connect();
            List<Course> courseList = driver.getAllCourses();
            driver.disconnect();
            for (Course c : courseList) {
                if (c.getTitle().equals(course.getTitle()) && c.getCourseNumber() == course.getCourseNumber()
                        && c.getSubject().equals(course.getSubject())) {
                    return true;
                }
            }
            return false;
        } catch(SQLException e){
            return true;
        }
    }

    private boolean isAllLetters(String subject){
        return subject.matches("[a-zA-Z]+");
    }
    public CourseCreationCode createCourse(Course course) {
        try{
            if (courseAlreadyExists(course)) {
                return CourseCreationCode.FAILURE_COURSE_ALREADY_EXISTS;
            }
            if (course.getSubject().length() > 4 || course.getSubject().length() < 2 || !isAllLetters(course.getSubject())) {
                return CourseCreationCode.IMPROPER_COURSE_DEPARTMENT;
            }
            if (String.valueOf(course.getCourseNumber()).length() != 4) {
                return CourseCreationCode.IMPROPER_COURSE_NUMBER;
            }
            if (course.getTitle().length() > 50 || course.getTitle().isEmpty()){
                return CourseCreationCode.IMPROPER_COURSE_TITLE;
            }
            driver.connect();
            course.setSubject(course.getSubject().toUpperCase());
            driver.addCourse(course);
            driver.disconnect();
            return CourseCreationCode.SUCCESS;
        } catch (SQLException e){
            return CourseCreationCode.SQL_EXCEPTION;
        }
    }

    public CourseDeletionCode deleteCourse(Course course){
        try{
            if (!courseAlreadyExists(course)) {
                return CourseDeletionCode.COURSE_DOES_NOT_EXIST;
            }
            driver.connect();
            driver.deleteCourse(course);
            driver.disconnect();
            return CourseDeletionCode.SUCCESS;
        } catch(SQLException e){
            return CourseDeletionCode.SQL_EXCEPTION;
        }
    }

    public List<Course> getAllCourses(){
        try {
            driver.connect();
            List<Course> temp_list = driver.getAllCourses();
            driver.disconnect();
            return temp_list;
        }catch(SQLException e){
            return new ArrayList<>();
        }
    }

    //TO-DO - Update Error Handling on This
    public List<Review> getReviewsForCourse(Course course){
        try {
            driver.connect();
            List<Review> reviewList = driver.getReviewsForCourse(course);
            driver.disconnect();
            for(Review review : reviewList){
                if(review.getUser().equals(currentUser)){
                    review.setDeletable(true);
                }
            }
            reviewList = sortTime(reviewList);
            return reviewList;
        }catch(SQLException e){
            return new ArrayList<>();
        }
    }

    private List<Review> sortTime(List<Review> l) {
        l.sort((review, o) -> {
            if (review.getUser().equals(currentUser)) {
                return -1;
            } else if (o.getUser().equals(currentUser)) {
                return 1;
            } else {
                return review.getTimeStamp().compareTo(o.getTimeStamp());
            }
        });
        return l;
    }

    public List<Review> getReviewsForUser(){
        try{
            driver.connect();
            List<Review> temp_list = sortTime(driver.getReviewsForUser(this.currentUser));
            driver.disconnect();
            return temp_list;
        }catch (SQLException e){
            return new ArrayList<>();
        }
    }

    public void updateRating(Course course){
        try{
            driver.connect();
            List<Review> reviewList = driver.getReviewsForCourse(course);
            if (reviewList.isEmpty()){
                course.setAverageRating(-1.0);
            }else{
                double avgRating = driver.getAverageRatingForCourse(course);
                course.setAverageRating(avgRating);
            }
            driver.disconnect();
        }catch (SQLException e){
        }
    }

    public ReviewCreationCode updateReview(String comment, String ratingScore, Course course){
        try{
            int rating = Integer.parseInt(ratingScore);
            Timestamp ts = new Timestamp(System.currentTimeMillis());
            Review review = new Review(rating, ts, course, this.currentUser, comment);
            if (rating <= 5 && rating >= 1){
                driver.connect();
                driver.updateReview(review);
                driver.disconnect();
                return ReviewCreationCode.SUCCESS;
            }else {
                return ReviewCreationCode.RATING_NOT_IN_RANGE;
            }
        }catch (SQLException e){
            return ReviewCreationCode.FAILED;
        }catch (NumberFormatException e1){
            try{
                Double.parseDouble(ratingScore);
                return ReviewCreationCode.NOT_AN_INT;
            }catch (NumberFormatException e2){
                return ReviewCreationCode.NOT_A_NUMBER;
            }
        }
    }

    //TO-DO - Update Error Handling on This
    public List<Course> courseSearch(String subject, String courseNumber, String title){
        try {
            driver.connect();
            List<Course> temp_list = driver.getCourseByAll(subject, courseNumber, title);
            driver.disconnect();
            return temp_list;
        }catch(SQLException e){
            return new ArrayList<>();
        }
    }



    public void setCurrentUser(User user){
        this.currentUser = user;
    }

    public User getCurrentUser(){
        return this.currentUser;
    }

    public boolean didFail(){
        return failed;
    }
}
