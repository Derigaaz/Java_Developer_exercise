package com.pietrowski.exercise.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table
@AllArgsConstructor
@Builder
@ToString
public class SubstanceUpdateEntry {
    @Id
    private String indexNo;
    private List<String> addedHazardClasses;
    private List<String> removedHazardClasses;
    private List<String> addedHazardStatementCodes;
    private List<String> removedHazardStatementCodes;
    private LocalDateTime updateTime;
}
