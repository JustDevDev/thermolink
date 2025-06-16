package org.skomi.pilot.shared.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

/**
 * Represents a Programmable Logic Controller (PLC), a crucial component in industrial automation.
 */
@Getter
@Setter
@NoArgsConstructor
@Table(name = "plc")
public class Plc {

    /**
     * Unique identifier for the Programmable Logic Controller (PLC).
     */
    @Id
    @Column("id")
    private UUID id;


    /**
     * Name of the Programmable Logic Controller (PLC).
     */
    @Column("name")
    private String name;
}
