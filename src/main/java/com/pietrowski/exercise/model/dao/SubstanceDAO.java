package com.pietrowski.exercise.model.dao;

import com.pietrowski.exercise.model.entities.Substance;
import org.springframework.stereotype.Repository;

@Repository
public class SubstanceDAO extends AbstractJpaDAO<Substance> {
    public SubstanceDAO() {
        setClazz(Substance.class);
    }
}
