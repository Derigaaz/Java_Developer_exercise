package com.pietrowski.exercise.model;

import jakarta.persistence.Basic;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class Substance implements Serializable {
    @Id
    private String indexNo;
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private String intChemId;
    private String ecNo;
    private String casNo;
    private List<String> hazardClasses;
    private List<String> hazardStatementCodes;
}
