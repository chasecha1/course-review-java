package edu.virginia.sde.reviews;

import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class DatabaseDriver {
    private final String sqliteFilename;
    private Connection connection;

    /**
     * Initialize databaseDriver object with config file (see last homework)
     */
    public DatabaseDriver() {
        sqliteFilename = "course_review_database.sqlite";
    }

    /**
     * See last homework
     */
    public void connect() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            //throw new IllegalStateException("The connection is already opened");
            return;
        }
        connection = DriverManager.getConnection("jdbc:sqlite:" + sqliteFilename);
        //the next line enables foreign key enforcement - do not delete/comment out
        connection.createStatement().execute("PRAGMA foreign_keys = ON");
        //the next line disables auto-commit - do not delete/comment out
        connection.setAutoCommit(false);
    }

    /**
     * From HW5 Starter Code
     */
    public void commit() throws SQLException {
        connection.commit();
    }

    public void rollback() throws SQLException {
        connection.rollback();
    }

    public void disconnect() throws SQLException {
        connection.close();
    }

    /**
     * Lets build into here functionality to not create if they already exist
     * Ensure not create because we are ensuring they exist
     */
    public void ensureTables() throws SQLException{
        if(!this.hasTables()){
            this.createTables();
        }
    }

    public boolean hasTables() throws SQLException{
        DatabaseMetaData data = connection.getMetaData();
        ResultSet rs = data.getTables(null, null, null, new String[] {"TABLE"});
        return rs.next();
    }

    public void createTables() throws SQLException {
        String sqlCreateCourses = "CREATE TABLE Courses (ID INTEGER PRIMARY KEY, Subject TEXT, CourseNumber INT(4), Title TEXT);";
        Statement coursesStatement = connection.createStatement();
        coursesStatement.executeUpdate(sqlCreateCourses);

        String sqlCreateUsers = "CREATE TABLE Users (ID INTEGER PRIMARY KEY, Username TEXT, Password TEXT);";
        Statement usersStatement = connection.createStatement();
        usersStatement.executeUpdate(sqlCreateUsers);

        String sqlCreateReviews = "CREATE TABLE Reviews (ID INTEGER PRIMARY KEY, CourseID INT(20), UserID INT(20), Rating REAL, Timestamp TIMESTAMP, Comment TEXT, FOREIGN KEY (CourseID) REFERENCES Courses(ID), FOREIGN KEY (UserID) REFERENCES Users(ID));";
        Statement reviewsStatement = connection.createStatement();
        reviewsStatement.executeUpdate(sqlCreateReviews);
        this.commit();
    }

    /**
     * Ensure that if one table depends on another table (foreign keys) then we delete
     * in an order such that the one that depends is deleted first
     */
    public void clearTables() throws SQLException {
        String sqlClearReviews = "DELETE FROM Reviews;";
        Statement clearReviewsStatement = connection.createStatement();
        clearReviewsStatement.executeUpdate(sqlClearReviews);

        String sqlClearUsers = "DELETE FROM Users;";
        Statement clearUsersStatement = connection.createStatement();
        clearUsersStatement.executeUpdate(sqlClearUsers);

        String sqlClearCourses = "DELETE FROM Courses;";
        Statement clearCoursesStatement = connection.createStatement();
        clearCoursesStatement.executeUpdate(sqlClearCourses);
        this.commit();
    }

    public void addUser(User user) throws SQLException{
        try {
            Statement statement = this.connection.createStatement();
            String sql = String.format("INSERT INTO Users (Username, Password) VALUES ('%s','%s')",
                    user.getUsername().replace("'", "''"),
                    user.getPassword().replace("'", "''"));
            statement.execute(sql);
        }
        catch (SQLException e) {
            rollback();
            throw e;
        }
        this.commit();
    }

    public int getUserID(User user) throws SQLException{
        String sql_request = String.format("select * from Users where Username='%s' AND Password='%s'",
                user.getUsername().replace("'", "''"),
                user.getPassword().replace("'", "''"));
        Statement statement = connection.createStatement();
        ResultSet userResult = statement.executeQuery(sql_request);
        return userResult.getInt("ID");
    }

    public User getUserByID(int userID) throws SQLException{
        String sql_request = String.format("select * from Users where ID=%d", userID);
        Statement statement = connection.createStatement();
        ResultSet userResult = statement.executeQuery(sql_request);
        return new User(userResult.getString("Username"),
                userResult.getString("Password"));
    }

    public User getUserByUsername(String username) throws SQLException{
        String sql_request = String.format("select * from Users where Username='%s'", username);
        Statement statement = connection.createStatement();
        ResultSet userResult = statement.executeQuery(sql_request);
        return new User(userResult.getString("Username"),
                userResult.getString("Password"));
    }

    public void addCourse(Course course) throws SQLException{
        try {
            Statement statement = this.connection.createStatement();
            String sql = String.format("INSERT INTO Courses (Subject, CourseNumber, Title) VALUES ('%s', %d, '%s')",
                    course.getSubject().replace("'", "''"),
                    course.getCourseNumber(),
                    course.getTitle().replace("'", "''"));
            statement.execute(sql);
        }
        catch (SQLException e) {
            rollback();
            throw e;
        }
        this.commit();
    }

    public int getCourseID(Course course) throws SQLException{
        String sql_request = String.format("select * from Courses where Subject='%s' AND CourseNumber=%d AND Title='%s'",
                course.getSubject().replace("'", "''"),
                course.getCourseNumber(),
                course.getTitle().replace("'", "''"));
        Statement statement = connection.createStatement();
        ResultSet courseResult = statement.executeQuery(sql_request);
        return courseResult.getInt("ID");
    }

    public Course getCourseByID(int courseID) throws SQLException{
        String sql_request = String.format("select * from Courses where ID=%d", courseID);
        Statement statement = connection.createStatement();
        ResultSet courseResult = statement.executeQuery(sql_request);
        return new Course(courseResult.getString("Subject"),
                courseResult.getInt("CourseNumber"),
                courseResult.getString("Title"));
    }

    public Course getCourseBySubject(String subject) throws SQLException{
        String sql_request = String.format("select * from Courses where Subject LIKE '%s'", subject);
        Statement statement = connection.createStatement();
        ResultSet courseResult = statement.executeQuery(sql_request);
        return new Course(courseResult.getString("Subject"),
                courseResult.getInt("CourseNumber"),
                courseResult.getString("Title"));
    }

    public Course getCourseByTitle(String title) throws SQLException{
        String sql_request = String.format("select * from Courses where Title LIKE '%s'", title);
        Statement statement = connection.createStatement();
        ResultSet courseResult = statement.executeQuery(sql_request);
        return new Course(courseResult.getString("Subject"),
                courseResult.getInt("CourseNumber"),
                courseResult.getString("Title"));
    }

    public List<Course> getCourseByAll(String subject, String courseNumber, String title) throws SQLException{
        String sql_request = String.format("select * from Courses where Subject LIKE '%%%s%%' AND CourseNumber LIKE '%%%s%%' AND Title LIKE '%%%s%%'",
                subject, courseNumber, title);
        Statement statement = connection.createStatement();
        ResultSet courseResult = statement.executeQuery(sql_request);
        List<Course> courses = new ArrayList<>();
        while(courseResult.next()) {
            Course currCourse = new Course(courseResult.getString("Subject"),
                    courseResult.getInt("CourseNumber"),
                    courseResult.getString("Title"));
            courses.add(currCourse);
        }
        return courses;
    }

    public Course getCourseByCourseNumber(int courseNumber) throws SQLException{
        String sql_request = String.format("select * from Courses where CourseNumber LIKE %d", courseNumber);
        Statement statement = connection.createStatement();
        ResultSet courseResult = statement.executeQuery(sql_request);
        return new Course(courseResult.getString("Subject"),
                courseResult.getInt("CourseNumber"),
                courseResult.getString("Title"));
    }

    public void addReview(Review review) throws SQLException{
        try {
            Statement statement = this.connection.createStatement();
            String sql = String.format("INSERT INTO Reviews (CourseID, UserID, Rating, Timestamp, Comment) VALUES (%d, %d, %d, '%s', '%s')",
                    this.getCourseID(review.getCourse()),
                    this.getUserID(review.getUser()),
                    review.getRating(),
                    review.getTimeStamp(),
                    review.getComment().get().replace("'", "''"));
            statement.execute(sql);
        }
        catch (SQLException e) {
            rollback();
            throw e;
        }
        this.commit();
    }

    public List<User> getAllUsers() throws SQLException {
        String sql_request = "select * from Users";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql_request);
        List<User> users = new ArrayList<>();
        while(rs.next()) {
            String username = rs.getString("Username");
            String password = rs.getString("Password");
            User user = new User(username, password);
            users.add(user);
        }
        return users;
    }

    public List<Course> getAllCourses() throws SQLException {
        String sql_request = "select * from Courses";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql_request);
        List<Course> courses = new ArrayList<>();
        while(rs.next()) {
            String subject = rs.getString("Subject");
            int courseNumber = rs.getInt("CourseNumber");
            String title = rs.getString("Title");
            Course course = new Course(subject, courseNumber, title);
            courses.add(course);
        }
        return courses;
    }

    public List<Review> getReviewsForCourse(Course inputCourse) throws SQLException {
        int courseID = this.getCourseID(inputCourse);
        String sql_request = String.format("select * from Reviews where CourseID=%d", courseID);
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql_request);
        List<Review> reviews = new ArrayList<>();
        Course course = this.getCourseByID(rs.getInt("CourseID"));
        while(rs.next()) {
            int rating = rs.getInt("Rating");
            Timestamp timeStamp = rs.getTimestamp("Timestamp");
            User user = this.getUserByID(rs.getInt("UserID"));
            String comment = rs.getString("Comment");
            Review review = new Review(rating, timeStamp, course, user, comment);
            reviews.add(review);
        }
        return reviews;
    }



    //TO-DO
    public boolean userExists(User user) throws SQLException {
        String sql_request = String.format("select * from Users where Username='%s'", user.getUsername());
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql_request);
        if(rs.next()) {
            return true;
        }
        return false;
    }

    public void deleteReview(Review review) throws SQLException{
        String sqlDeleteReview = String.format("DELETE FROM Reviews where CourseID=%d AND UserID=%d",
                this.getCourseID(review.getCourse()),
                this.getUserID(review.getUser()));
        Statement deleteReviewStatement = connection.createStatement();
        deleteReviewStatement.executeUpdate(sqlDeleteReview);
        this.commit();
    }

    public void deleteCourse(Course course) throws SQLException{
        List<Review> reviews = this.getReviewsForCourse(course);
        for (Review review : reviews) {
            String sqlDeleteReview = String.format("DELETE FROM Reviews where CourseID=%d AND UserID=%d",
                    this.getCourseID(course),
                    this.getUserID(review.getUser()));
            Statement deleteReviewStatement = connection.createStatement();
            deleteReviewStatement.executeUpdate(sqlDeleteReview);
            this.commit();
        }
        String sqlDeleteCourse = String.format("DELETE FROM Courses where ID=%d",
                this.getCourseID(course));
        Statement deleteCourseStatement = connection.createStatement();
        deleteCourseStatement.executeUpdate(sqlDeleteCourse);
        this.commit();
    }

    public List<Review> getReviewsForUser(User user) throws SQLException {
        int userID = this.getUserID(user);
        String sql_request = String.format("select * from Reviews where UserID=%d", userID);
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql_request);
        List<Review> reviews = new ArrayList<>();
        while(rs.next()) {
            int rating = rs.getInt("Rating");
            Timestamp timeStamp = rs.getTimestamp("Timestamp");
            Course course = this.getCourseByID(rs.getInt("CourseID"));
            String comment = rs.getString("Comment");
            Review review = new Review(rating, timeStamp, course, user, comment);
            reviews.add(review);
        }
        return reviews;
    }

    public double getAverageRatingForCourse(Course course) throws SQLException {
        String sql_request = String.format("select AVG(Rating) from Reviews where CourseID='%d'", this.getCourseID(course));
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql_request);
        return rs.getDouble("AVG(Rating)");
    }

    public void updateReview(Review review) throws SQLException{
        try{
            Statement statement = connection.createStatement();
            String sql = String.format("UPDATE Reviews SET Rating = %d, Timestamp = '%s', Comment = '%s' WHERE CourseID = %d AND UserID = %d",
                    review.getRating(),
                    review.getTimeStamp(),
                    review.getComment().get().replace("'", "''"),
                    this.getCourseID(review.getCourse()),
                    this.getUserID(review.getUser()));
            statement.execute(sql);
        }catch (SQLException e){
            rollback();
            throw e;
        }
        this.commit();
    }

}
