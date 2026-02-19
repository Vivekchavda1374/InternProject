package com.vasyerp.databasemapping.DAO;


import com.vasyerp.databasemapping.entity.Course;
import com.vasyerp.databasemapping.entity.Instructor;
import com.vasyerp.databasemapping.entity.InstructorDetails;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class AppDAOImpl implements AppDAO {

    // define field for entity manager
    private EntityManager entityManager;

    // inject entity manager using constructor injection
    @Autowired
    public AppDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public void save(Instructor theInstructor) {
        entityManager.persist(theInstructor);
    }
    @Override
    public Instructor findInstructorById(int theId) {
        return entityManager.find(Instructor.class, theId);
    }

    @Override
    @Transactional
    public void deleteInstructorById(int theId) {
        Instructor tempInstructor = entityManager.find(Instructor.class, theId);
        if (tempInstructor != null) {
            entityManager.remove(tempInstructor);
        } else {
            System.out.println("Instructor id " + theId + " not found, nothing to delete.");
        }
    }

    @Override
    public InstructorDetails findInstructorDetailById(int theId) {
        return entityManager.find(InstructorDetails.class, theId);
    }

    @Override
    @Transactional
    public void deleteInstructorDetailById(int theId) {
        InstructorDetails tempInstructorDetail = entityManager.find(InstructorDetails.class, theId);
        if (tempInstructorDetail != null) {
            Instructor associatedInstructor = tempInstructorDetail.getInstructor();
            if (associatedInstructor != null) {
                associatedInstructor.setInstructorDetail(null);
            }
            entityManager.remove(tempInstructorDetail);
        } else {
            System.out.println("InstructorDetail id " + theId + " not found, nothing to delete.");
        }
    }

    @Override
    public List<Course> findCoursesByInstructorId(int theId) {

        TypedQuery<Course> query = entityManager.createQuery("from Course where instructor.id = :data", Course.class);
        query.setParameter("data", theId);
        List<Course> courses = query.getResultList();
        return courses;
    }
    @Override
    public Instructor findInstructorByIdJoinFetch(int theId) {

        TypedQuery<Instructor> query = entityManager.createQuery(
                "select i from Instructor i "
                        + "JOIN FETCH i.courses "
                        + "where i.id = :data", Instructor.class);
        query.setParameter("data", theId);
        Instructor instructor = query.getSingleResult();
        return instructor;
    }
    @Override
    @Transactional
    public void update(Instructor tempInstructor) {
        entityManager.merge(tempInstructor);
    }
    @Override
    @Transactional
    public void update(Course tempCourse) {
        entityManager.merge(tempCourse);
    }
}
