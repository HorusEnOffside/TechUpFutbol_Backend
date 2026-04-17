package com.escuela.techcup.core.service;

import com.escuela.techcup.core.model.SoccerField;

public interface SoccerFieldService {

    SoccerField createSoccerField(String name, String location);
    SoccerField getSoccerFieldById(String id);
}
