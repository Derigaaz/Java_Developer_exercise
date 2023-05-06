package com.pietrowski.exercise;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private String hazardStatementCode;
    private String hazardClass;
}
