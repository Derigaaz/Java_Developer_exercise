package com.pietrowski.exercise.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Entity
@Table
@AllArgsConstructor
@Builder
@ToString
public class Substance implements Serializable {
    @Id
    private String indexNo;
    private String intChemId;
    private String ecNo;
    private String casNo;
    private List<String> hazardClasses;
    private List<String> hazardStatementCodes;
}
