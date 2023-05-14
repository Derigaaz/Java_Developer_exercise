package com.pietrowski.exercise.model.dao;

import com.pietrowski.exercise.model.entities.SubstanceUpdateEntry;
import org.springframework.stereotype.Repository;

@Repository
public class SubstanceUpdateEntryDAO extends AbstractJpaDAO<SubstanceUpdateEntry> {
    public SubstanceUpdateEntryDAO() {
        setClazz(SubstanceUpdateEntry.class);
    }
}
