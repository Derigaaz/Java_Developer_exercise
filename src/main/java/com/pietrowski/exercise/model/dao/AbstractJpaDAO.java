package com.pietrowski.exercise.model.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

public abstract class AbstractJpaDAO<T extends Serializable> {

    @PersistenceContext
    EntityManager entityManager;
    private Class<T> clazz;

    public final void setClazz(Class<T> clazzToSet) {
        clazz = clazzToSet;
    }

    public List<T> findAll() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(clazz);
        cq.from(clazz);
        cq.distinct(true);
        TypedQuery<T> query = entityManager.createQuery(cq);
        return query.getResultList();
    }

    public void create(T entity) {
        entityManager.persist(entity);
    }

    public T update(T entity) {
        return entityManager.merge(entity);
    }

    public void delete(T entity) {
        entityManager.remove(entity);
    }

    @Transactional
    public void deleteAll() {
        List<T> entities = findAll();
        entities.forEach(this::delete);
    }
}
