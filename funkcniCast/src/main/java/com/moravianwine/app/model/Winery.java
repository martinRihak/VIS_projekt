package com.moravianwine.app.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Winery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer wineryId;
    private String name;
    private String address;
    private String website;
    private String phone;

}
