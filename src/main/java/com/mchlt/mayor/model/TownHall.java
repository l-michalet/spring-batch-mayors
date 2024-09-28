package com.mchlt.mayor.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class TownHall {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String area;
    private String areaCode;
    private String department;
    private String city;
    private String cityCode;

    public TownHall(String area, String areaCode, String department, String city, String cityCode) {
        this.area = area;
        this.areaCode = areaCode;
        this.department = department;
        this.city = city;
        this.cityCode = cityCode;
    }
}
