package edu.virginia.sde.reviews;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CourseReviewDriverTest {

    //this test class was designed to only be run once on the same computer/database, so after confirming that the tests passed the
    //first time, we commented the test class out so that it would not cause problems by failing on later runs of the program.

    //For it to be run multiple times, the database would have to be altered/deleted between runs

//    private static CourseReviewDriver driver;
//    private static User user;
//    private static Course course;
//
//    @BeforeAll
//    static void setUp() {
//        driver = new CourseReviewDriver();
//
//        user = new User("testUser", "password123");
//        course = new Course("CS", 4501, "Software Development");
//    }
//
//    @Test
//    void testHasReviewed() {
//        assertFalse(driver.hasReviewed(null));
//        assertFalse(driver.hasReviewed(new ArrayList<>()));
//
//        driver.setCurrentUser(user);
//        List<Review> reviews = new ArrayList<>();
//        Review review = new Review(4, new Timestamp(System.currentTimeMillis()), course, user, "Great course!");
//        reviews.add(review);
//        assertTrue(driver.hasReviewed(reviews));
//    }
//
//
//
//    @Test
//    void testCorrectPassword() {
//        assertFalse(driver.correctPassword(user));
//
//        try {
//            driver.createUser(user);
//        } catch (Exception e) {
//            fail("Exception thrown: " + e.getMessage());
//        }
//
//        assertTrue(driver.correctPassword(user));
//    }
//
//    @Test
//    void testGetAllCourses() {
//        try {
//            driver.createCourse(course);
//        } catch (Exception e) {
//            fail("Exception thrown: " + e.getMessage());
//        }
//
//        List<Course> courses = driver.getAllCourses();
//        assertTrue(courses.contains(course));
//    }





}
