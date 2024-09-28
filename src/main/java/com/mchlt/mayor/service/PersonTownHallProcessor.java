package com.mchlt.mayor.service;

import com.mchlt.mayor.model.Person;
import com.mchlt.mayor.model.TownHall;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class PersonTownHallProcessor implements ItemProcessor<String[], PersonTownHall> {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public PersonTownHall process(String[] fields) {
        Person person = new Person();
        person.setSurname(fields[0]);
        person.setName(fields[1]);
        person.setBirthDate(LocalDate.parse(fields[2], formatter));
        person.setJob(fields[3]);
        person.setJobType(fields[4]);
        person.setHiringDate(LocalDate.parse(fields[5], formatter));
        person.setJobStartDate(LocalDate.parse(fields[6], formatter));
        person.setJobCategory(fields[7]);
        person.setJobCategorySubType(fields[8]);
        person.setNationality(fields[9]);
        person.setSex(fields[10]);
        person.setArea(fields[11]);
        person.setDepartment(fields[13]);
        person.setCity(fields[19]);

        TownHall townHall = new TownHall();
        townHall.setArea(fields[11]);
        townHall.setAreaCode(fields[12]);
        townHall.setDepartment(fields[13]);
        townHall.setCity(fields[19]);
        townHall.setCityCode(fields[20]);

        return new PersonTownHall(person, townHall);
    }
}

