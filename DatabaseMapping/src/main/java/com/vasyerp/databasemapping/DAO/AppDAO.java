package com.vasyerp.databasemapping.DAO;

import com.vasyerp.databasemapping.entity.Course;
import com.vasyerp.databasemapping.entity.Instructor;
import com.vasyerp.databasemapping.entity.InstructorDetails;

import java.util.List;

public interface AppDAO {
    void save(Instructor theInstructor);

    Instructor findInstructorById(int theId);

    void deleteInstructorById(int theId);

    InstructorDetails findInstructorDetailById(int theId);

    void deleteInstructorDetailById(int theId);
    List<Course> findCoursesByInstructorId(int theId);
    Instructor findInstructorByIdJoinFetch(int theId);
    void update(Instructor tempInstructor);
    void update(Course tempCourse);
}
