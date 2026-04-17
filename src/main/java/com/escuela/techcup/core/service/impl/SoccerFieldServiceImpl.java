package com.escuela.techcup.core.service.impl;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.escuela.techcup.core.exception.InvalidInputException;
import com.escuela.techcup.core.exception.SoccerFieldNotFoundException;
import com.escuela.techcup.core.model.SoccerField;
import com.escuela.techcup.core.service.SoccerFieldService;
import com.escuela.techcup.core.util.IdGeneratorUtil;
import com.escuela.techcup.persistence.entity.tournament.SoccerFieldEntity;
import com.escuela.techcup.persistence.mapper.tournament.SoccerFieldMapper;
import com.escuela.techcup.persistence.repository.tournament.SoccerFieldRepository;

@Service
public class SoccerFieldServiceImpl implements SoccerFieldService {

    private static final Logger log = LoggerFactory.getLogger(SoccerFieldServiceImpl.class);
    private final SoccerFieldRepository soccerFieldRepository;

    public SoccerFieldServiceImpl(SoccerFieldRepository soccerFieldRepository) {
        this.soccerFieldRepository = soccerFieldRepository;
    }

    @Override
    @Transactional
    public SoccerField createSoccerField(String name, String location) {
        log.info("Attempting to create soccer field with name: {}", name);
        notDuplicateName(name);
        log.info("Creating soccer field with name: {}, location: {}", name, location);
        SoccerField soccerField = new SoccerField(idGenerator(), name, location);

        SoccerFieldEntity entity = SoccerFieldMapper.toEntity(soccerField);

        soccerFieldRepository.save(entity);
        log.info("Soccer field created with ID: {}", soccerField.getId());
        return soccerField;
    }

    @Override
    @Transactional(readOnly = true)
    public SoccerField getSoccerFieldById(String id) {
        log.info("Fetching soccer field with ID: {}", id);
        SoccerFieldEntity entity = soccerFieldRepository.findById(id).orElse(null);
        if (entity == null) {
            log.warn("Soccer field not found with ID: {}", id);
            throw new SoccerFieldNotFoundException(id);
        }
        log.info("Soccer field found with ID: {}", entity.getId());
        return SoccerFieldMapper.toModel(entity);
    }

    private void notDuplicateName(String name) {
        if (soccerFieldRepository.existsByNameIgnoreCase(name)) {
            throw new InvalidInputException("Soccer field with name '" + name + "' already exists");
        }
    }

    private String idGenerator() {
        return IdGeneratorUtil.generateId();
    }

}
