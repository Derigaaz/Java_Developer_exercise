package com.pietrowski.exercise.model.services;

import com.pietrowski.exercise.model.dao.SubstanceDAO;
import com.pietrowski.exercise.model.entities.Substance;
import com.pietrowski.exercise.model.entities.SubstanceUpdateEntry;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SubstanceServiceTest {

    private static final String INDEX_NO = "indexNo";
    private static final String INT_CHEM_ID = "intChemId";
    private static final String EC_NO = "ecNo";
    private static final String CAS_NO = "casNo";
    private static final String HAZARD_CLASS = "hazardClass";
    private static final String HAZARD_CLASS_2 = "hazardClass2";
    private static final String HAZARD_CLASS_3 = "hazardClass3";
    private static final String HAZARD_STATEMENT_CODE = "hazardStatementCode";
    private static final String HAZARD_STATEMENT_CODE_2 = "hazardStatementCode2";
    private static final String HAZARD_STATEMENT_CODE_3 = "hazardStatementCode3";
    private final SubstanceService substanceService = new SubstanceService();

    @Mock
    private SubstanceDAO substanceDAO;

    @Captor
    ArgumentCaptor<SubstanceUpdateEntry> substanceUpdateEntryArgumentCaptor;

    @Captor
    ArgumentCaptor<Substance> substanceArgumentCaptor;

    @Mock
    private Row row;

    @Mock
    private Cell cell0;
    @Mock
    private Cell cell1;
    @Mock
    private Cell cell2;
    @Mock
    private Cell cell3;
    @Mock
    private Cell cell4;
    @Mock
    private Cell cell5;

    @Mock
    SubstanceUpdateEntryService substanceUpdateEntryService;

    @Test
    void buildSubstance() {
        //given
        when(row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)).thenReturn(cell0);
        when(row.getCell(1, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)).thenReturn(cell1);
        when(row.getCell(2, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)).thenReturn(cell2);
        when(row.getCell(3, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)).thenReturn(cell3);
        when(row.getCell(4, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)).thenReturn(cell4);
        when(row.getCell(5, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK)).thenReturn(cell5);
        when(cell0.getStringCellValue()).thenReturn(INDEX_NO);
        when(cell1.getStringCellValue()).thenReturn(INT_CHEM_ID);
        when(cell2.getStringCellValue()).thenReturn(EC_NO);
        when(cell3.getStringCellValue()).thenReturn(CAS_NO);
        when(cell4.getStringCellValue()).thenReturn(HAZARD_CLASS);
        when(cell5.getStringCellValue()).thenReturn(HAZARD_STATEMENT_CODE);
        //when
        Substance substance = SubstanceService.buildSubstance(row);
        //then
        assertEquals(INDEX_NO, substance.getIndexNo());
        assertEquals(INT_CHEM_ID, substance.getIntChemId());
        assertEquals(EC_NO, substance.getEcNo());
        assertEquals(CAS_NO, substance.getCasNo());
        assertEquals(1, substance.getHazardClasses().size());
        assertEquals(HAZARD_CLASS, substance.getHazardClasses().get(0));
        assertEquals(1, substance.getHazardStatementCodes().size());
        assertEquals(HAZARD_STATEMENT_CODE, substance.getHazardStatementCodes().get(0));

    }

    @Test
    void processNewSubstanceWhenUpdateIsRequired() {
        //given
        substanceService.setSubstanceDAO(substanceDAO);
        substanceService.setSubstanceUpdateEntryService(substanceUpdateEntryService);
        when(substanceDAO.update(any())).thenAnswer(i -> i.getArguments()[0]);
        doNothing().when(substanceUpdateEntryService).update(any());
        List<Substance> substances = createSubstanceList();
        Substance newSubstance = createNewSubstance();
        //when
        substanceService.processNewSubstance(substances, newSubstance);
        //then
        verify(substanceUpdateEntryService, times(1)).update(substanceUpdateEntryArgumentCaptor.capture());
        SubstanceUpdateEntry substanceUpdateEntry = substanceUpdateEntryArgumentCaptor.getValue();
        verify(substanceDAO, times(1)).update(substanceArgumentCaptor.capture());
        Substance substanceAfterUpdate = substanceArgumentCaptor.getValue();
        assertAll(
                () -> assertEquals(substanceUpdateEntry.getSubstance(), substanceAfterUpdate),
                () -> assertEquals(substanceUpdateEntry.getAddedHazardClasses(), List.of(HAZARD_CLASS_3)),
                () -> assertEquals(substanceUpdateEntry.getRemovedHazardClasses(), List.of(HAZARD_CLASS, HAZARD_CLASS_2)),
                () -> assertEquals(substanceUpdateEntry.getAddedHazardStatementCodes(), List.of(HAZARD_STATEMENT_CODE_3)),
                () -> assertEquals(substanceUpdateEntry.getRemovedHazardStatementCodes(), List.of(HAZARD_STATEMENT_CODE, HAZARD_STATEMENT_CODE_2))
        );
    }

    @Test
    void findAll() {
        substanceService.setSubstanceDAO(substanceDAO);
        //given
        when(substanceDAO.findAll()).thenReturn(List.of(new Substance()));
        //when
        List<Substance> substances = substanceService.findAll();
        //then
        assertNotNull(substances);
        assertEquals(1, substances.size());
        verify(substanceDAO, times(1)).findAll();
    }

    @Test
    void deleteAll() {
        substanceService.setSubstanceDAO(substanceDAO);
        //given
        doNothing().when(substanceDAO).deleteAll();
        //when
        substanceService.deleteAll();
        //then
        verify(substanceDAO, times(1)).deleteAll();
    }

    private List<Substance> createSubstanceList() {
        Substance substance = Substance.builder().indexNo(INDEX_NO).intChemId(INT_CHEM_ID).casNo(CAS_NO).ecNo(EC_NO).hazardClasses(List.of(HAZARD_CLASS, HAZARD_CLASS_2)).hazardStatementCodes(List.of(HAZARD_STATEMENT_CODE, HAZARD_STATEMENT_CODE_2)).build();
        return List.of(substance);
    }

    private Substance createNewSubstance() {
        return Substance.builder().indexNo(INDEX_NO).intChemId(INT_CHEM_ID).casNo(CAS_NO).ecNo(EC_NO).hazardClasses(List.of(HAZARD_CLASS_3)).hazardStatementCodes(List.of(HAZARD_STATEMENT_CODE_3)).build();
    }
}