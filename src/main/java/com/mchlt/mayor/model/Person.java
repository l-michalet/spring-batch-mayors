package com.mchlt.mayor.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@Entity
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String surname;
    private String name;
    private LocalDate birthDate;
    private String job;
    private String jobType;
    private LocalDate hiringDate;
    private LocalDate jobStartDate;
    private String jobCategory;
    private String jobCategorySubType;
    private String nationality;
    private String sex;
    private String area;
    private String department;
    private String city;

    public Person(String surname, String name, LocalDate birthDate, String job, String jobType, LocalDate hiringDate, LocalDate jobStartDate, String jobCategory, String jobCategorySubType, String nationality, String sex, String area, String department, String city) {
        this.surname = surname;
        this.name = name;
        this.birthDate = birthDate;
        this.job = job;
        this.jobType = jobType;
        this.hiringDate = hiringDate;
        this.jobStartDate = jobStartDate;
        this.jobCategory = jobCategory;
        this.jobCategorySubType = jobCategorySubType;
        this.nationality = nationality;
        this.sex = sex;
        this.area = area;
        this.department = department;
        this.city = city;
    }
}
