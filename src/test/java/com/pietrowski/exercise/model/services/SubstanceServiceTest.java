package com.pietrowski.exercise.model.services;

import com.pietrowski.exercise.model.dao.SubstanceDAO;
import com.pietrowski.exercise.model.entities.Substance;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SubstanceServiceTest {

    public static final String INDEX_NO = "indexNo";
    public static final String INT_CHEM_ID = "intChemId";
    public static final String EC_NO = "ecNo";
    public static final String CAS_NO = "casNo";
    public static final String HAZARD_CLASSES = "hazardClasses";
    public static final String HAZARD_STATEMENT_CODES = "hazardStatementCodes";
    SubstanceService substanceService = new SubstanceService();

    @Mock
    SubstanceDAO substanceDAO;

    @Mock
    Row row;

    @Mock
    Cell cell0;
    @Mock
    Cell cell1;
    @Mock
    Cell cell2;
    @Mock
    Cell cell3;
    @Mock
    Cell cell4;
    @Mock
    Cell cell5;

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
        when(cell4.getStringCellValue()).thenReturn(HAZARD_CLASSES);
        when(cell5.getStringCellValue()).thenReturn(HAZARD_STATEMENT_CODES);
        //when
        Substance substance = SubstanceService.buildSubstance(row);
        //then
        assertEquals(INDEX_NO, substance.getIndexNo());
        assertEquals(INT_CHEM_ID, substance.getIntChemId());
        assertEquals(EC_NO, substance.getEcNo());
        assertEquals(CAS_NO, substance.getCasNo());
        assertEquals(1, substance.getHazardClasses().size());
        assertEquals(HAZARD_CLASSES, substance.getHazardClasses().get(0));
        assertEquals(1, substance.getHazardStatementCodes().size());
        assertEquals(HAZARD_STATEMENT_CODES, substance.getHazardStatementCodes().get(0));

    }

    @Test
    void processNewSubstance() {

    }

    @Test
    void findAll() {
        substanceService.substanceDAO = substanceDAO;
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
        substanceService.substanceDAO = substanceDAO;
        //given
        doNothing().when(substanceDAO).deleteAll();
        //when
        substanceService.deleteAll();
        //then
        verify(substanceDAO, times(1)).deleteAll();
    }
}