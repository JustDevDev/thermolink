package org.skomi.pilot.shared.model;

public record ConnectedPlcDTO(
        Long id,
        String name,
        int port
) {
}
