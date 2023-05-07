package com.pietrowski.exercise;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Substance {
    private String indexNo;
    private String intChemId;
    private String ecNo;
    private String casNo;
    private List<String> hazardClasses;
    private List<String> hazardStatementCodes;
}
