package com.pietrowski.exercise;

import lombok.*;

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
