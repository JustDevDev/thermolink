package org.skomi.pilot.ui.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Domain model representing a diagram source.
 * Designed to store a catalog of diagram records in a database using Spring Data JDBC.
 */
@SuppressWarnings("ALL")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "diagram_source")
public class DiagramSource {

    @Id
    private String userEmail;

    /**
     * The diagram content stored as a text.
     */
    private String diagram;
}