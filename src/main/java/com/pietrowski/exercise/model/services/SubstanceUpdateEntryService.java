package com.pietrowski.exercise.model.services;

import com.pietrowski.exercise.model.dao.SubstanceUpdateEntryDAO;
import com.pietrowski.exercise.model.entities.Substance;
import com.pietrowski.exercise.model.entities.SubstanceUpdateEntry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class SubstanceUpdateEntryService {

    @Autowired
    SubstanceUpdateEntryDAO substanceUpdateEntryDAO;

    public void update(SubstanceUpdateEntry entry) {
        substanceUpdateEntryDAO.update(entry);
    }

    public List<SubstanceUpdateEntry> findAll(){
        return substanceUpdateEntryDAO.findAll();
    }

    public void deleteAll(){
        substanceUpdateEntryDAO.deleteAll();
    }

    public static SubstanceUpdateEntry buildSubstanceUpdateEntry(Substance substanceBeforeUpdate, Substance updatedSubstance) {
        return SubstanceUpdateEntry.builder()
                .substance(updatedSubstance)
                .updateTime(LocalDateTime.now())
                .removedHazardClasses(findDifferencesBetweenLists(substanceBeforeUpdate.getHazardClasses(), updatedSubstance.getHazardClasses()))
                .addedHazardClasses(findDifferencesBetweenLists(updatedSubstance.getHazardClasses(), substanceBeforeUpdate.getHazardClasses()))
                .removedHazardStatementCodes(findDifferencesBetweenLists(substanceBeforeUpdate.getHazardStatementCodes(), updatedSubstance.getHazardStatementCodes()))
                .addedHazardStatementCodes(findDifferencesBetweenLists(updatedSubstance.getHazardStatementCodes(), substanceBeforeUpdate.getHazardStatementCodes()))
                .build();
    }

    private static List<String> findDifferencesBetweenLists(List<String> listOne, List<String> listTwo) {
        List<String> differences = listOne != null ? new ArrayList<>(listOne): new ArrayList<>();
        if (listTwo == null) {
            return differences;
        }
        differences.removeAll(listTwo);
        return differences;
    }
}
