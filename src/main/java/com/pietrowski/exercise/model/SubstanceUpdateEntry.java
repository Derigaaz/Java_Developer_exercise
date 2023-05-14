package com.pietrowski.exercise.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table
@AllArgsConstructor
@Builder
@ToString
@NoArgsConstructor
public class SubstanceUpdateEntry implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String indexNo;
    private List<String> addedHazardClasses;
    private List<String> removedHazardClasses;
    private List<String> addedHazardStatementCodes;
    private List<String> removedHazardStatementCodes;
    private LocalDateTime updateTime;
}
