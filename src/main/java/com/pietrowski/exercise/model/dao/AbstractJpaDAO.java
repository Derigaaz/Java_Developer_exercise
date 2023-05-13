package com.pietrowski.exercise.model.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.transaction.Transactional;

import java.io.Serializable;
import java.util.List;

@Transactional
public abstract class AbstractJpaDAO<T extends Serializable> {

    @PersistenceContext
    EntityManager entityManager;
    private Class<T> clazz;

    public final void setClazz(Class<T> clazzToSet) {
        clazz = clazzToSet;
    }

    public T findById(String id) {
        return entityManager.find(clazz, id);
    }

    public List<T> findAll() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(clazz);
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

    public void deleteById(String entityId) {
        T entity = findById(entityId);
        delete(entity);
    }

    public void deleteAll(){
        List<T> entities = findAll();
        entities.forEach(this::delete);
    }
}
