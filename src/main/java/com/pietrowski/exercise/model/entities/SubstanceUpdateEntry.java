package com.pietrowski.exercise.model.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@NoArgsConstructor
@Builder
@ToString
public class SubstanceUpdateEntry implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private List<String> addedHazardClasses;
    private List<String> removedHazardClasses;
    private List<String> addedHazardStatementCodes;
    private List<String> removedHazardStatementCodes;
    private LocalDateTime updateTime;

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "substance_index_no", nullable = false)
    private Substance substance;

}
