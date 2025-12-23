package com.spring.jwt.utils.Colours.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="colours")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Colour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "colour_Id")
    private Long colourId;

    @Column(name = "colour_name")
    private String colourName;

    @Column(name = "hex_code")
    private String hexCode;

}
