package com.mchlt.mayor.service;

import com.mchlt.mayor.model.Person;
import com.mchlt.mayor.model.TownHall;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PersonTownHall {

    private Person person;
    private TownHall townHall;

}

