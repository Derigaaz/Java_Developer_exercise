package com.pietrowski.exercise.model.dao;

import com.pietrowski.exercise.model.Substance;
import org.springframework.stereotype.Repository;

@Repository
public class SubstanceUpdateEntryDAO extends AbstractJpaDAO<Substance> {
    public SubstanceUpdateEntryDAO() {
        setClazz(Substance.class);
    }
}
