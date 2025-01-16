package edu.virginia.sde.reviews;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseDriverTest {

    static DatabaseDriver driver;
    static User user1;
    static User user2;
    static User user3;
    static Course course1;
    static Course course2;
    static Course course3;
    static Review review1;
    static Review review2;
    static Review review3;
    static Review review4;

    @BeforeAll
    static void before() throws SQLException, InterruptedException {
        driver = new DatabaseDriver();
        driver.connect();
        driver.ensureTables();
        driver.clearTables();
        user1 = new User("person1", "password1");
        user2 = new User("person2", "password2");
        user3 = new User("person3", "password3");
        driver.addUser(user1);
        driver.addUser(user2);
        driver.addUser(user3);
        course1 = new Course("subject1", 0001, "title1");
        course2 = new Course("subject2", 0002, "title2");
        course3 = new Course("subject3", 0003, "title3");
        driver.addCourse(course1);
        driver.addCourse(course2);
        driver.addCourse(course3);
        review1 = new Review(1,
                Timestamp.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date())),
                course1, user1, "this is a comment1");
        TimeUnit.SECONDS.sleep(5);
        review2 = new Review(2,
                Timestamp.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date())),
                course2, user2, "this is a comment2");
        review3 = new Review(3,
                Timestamp.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date())),
                course3, user3, "this is a comment3");
        review4 = new Review(4,
                Timestamp.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date())),
                course3, user1, "");
        driver.addReview(review1);
        driver.addReview(review2);
        driver.addReview(review3);
        driver.addReview(review4);
    }
    @Test
    void testUserExists() throws SQLException {
        assertEquals(true, driver.userExists(new User("person1", "password1")));
        assertEquals(false, driver.userExists(new User("person4", "password1")));
    }

    @Test
    void testDeleteCourse() throws SQLException {
        driver.deleteCourse(course2);
    }

    @Test
    void testDeleteReview() throws SQLException {
        driver.deleteReview(review2);
    }

    @Test
    void testGetCourseByID() throws SQLException {
        assertEquals(course1.getSubject(), driver.getCourseByID(1).getSubject());
        assertEquals(course1.getTitle(), driver.getCourseByID(1).getTitle());
        assertEquals(course1.getCourseNumber(), driver.getCourseByID(1).getCourseNumber());
        assertTrue(course1.equals(driver.getCourseByID(1)));
    }

    @Test
    void testGetAverageRatingForCourse() throws SQLException {
        assertEquals(3.5, driver.getAverageRatingForCourse(course3));
    }

    @Test
    void testGetCourseByAll() throws SQLException {
        List<Course> list = driver.getCourseByAll("","","");
        for (Course curr : list){
            //System.out.println(curr.getTitle());
        }
        //System.out.println("test");
    }


}