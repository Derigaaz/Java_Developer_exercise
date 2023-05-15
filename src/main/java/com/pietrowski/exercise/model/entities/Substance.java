package com.pietrowski.exercise.model.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
public class Substance implements Serializable {
    @Id
    private String indexNo;
    @OneToMany(mappedBy="substance")
    @ToString.Exclude
    private List<SubstanceUpdateEntry> substanceUpdates;
    @Lob
    private String intChemId;
    @Lob
    private String ecNo;
    @Lob
    private String casNo;
    private List<String> hazardClasses;
    private List<String> hazardStatementCodes;
}
