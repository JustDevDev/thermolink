package org.skomi.pilot.ui.service;

import lombok.RequiredArgsConstructor;
import org.skomi.pilot.shared.model.Plc;
import org.skomi.pilot.ui.model.PlcDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service class for managing diagram data (PLCs and Sensors).
 */
@Service
@RequiredArgsConstructor
public class PlcMapper {

    /**
     * Maps a list of PLCDTO to a list of PLC entities.
     *
     * @param plcDtos the list of PLCDTOs to map
     * @return a list of mapped PLC entities
     */
    List<Plc> mapToPlcs(List<PlcDTO> plcDtos) {
        return plcDtos.stream()
                .map(dto -> {
                    Plc plc = new Plc();
                    plc.setId(dto.id() != null ? dto.id() : UUID.randomUUID());
                    plc.setName(dto.name());
                    return plc;
                })
                .collect(Collectors.toList());
    }
}