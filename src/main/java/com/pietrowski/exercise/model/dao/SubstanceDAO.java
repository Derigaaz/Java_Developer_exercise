package com.pietrowski.exercise.model.dao;

import com.pietrowski.exercise.model.Substance;
import org.springframework.stereotype.Repository;

@Repository
public class SubstanceDAO extends AbstractJpaDAO<Substance> {
    public SubstanceDAO() {
        setClazz(Substance.class);
    }
}
