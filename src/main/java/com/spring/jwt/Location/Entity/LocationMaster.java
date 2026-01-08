package com.spring.jwt.Location.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@AllArgsConstructor
@Table(
        name = "location_master",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"state", "city", "locality"})
        }
)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class LocationMaster {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_id", nullable = false)
    private Integer locationId;

    @Column(name = "state", nullable = false)
    private String state;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "locality", nullable = false)
    private String locality;

    public LocationMaster() {
    }

}
