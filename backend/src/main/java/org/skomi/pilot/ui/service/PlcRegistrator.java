package org.skomi.pilot.ui.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.skomi.pilot.shared.model.Plc;
import org.skomi.pilot.shared.repository.PlcRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Registering sensor with plc connections
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PlcRegistrator {

    private final PlcRepository plcRepository;

    /**
     * Registers a list of Programmable Logic Controllers (PLCs) by saving them to the repository.
     *
     * @param plcsToSave the list of PLC entities to be saved
     * @return a list of saved PLC entities, each with generated identifiers if they were new entries
     */
    public List<Plc> registerPLCs(List<Plc> plcsToSave) {
        return plcRepository.upsertAll(plcsToSave);
    }
}