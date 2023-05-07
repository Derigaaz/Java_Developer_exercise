package com.pietrowski.exercise;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Substance {
    private String indexNo;
    private String internationalChemicalIdentification;
    private String casNo;
    private String ecNo;
    private List<String> hazardStatementCodes;
    private List<String> hazardClasses;
}
