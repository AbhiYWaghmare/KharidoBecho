package com.spring.jwt.Location.Repository;

import com.spring.jwt.Location.Entity.LocationMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LocationRepository extends JpaRepository<LocationMaster, Integer> {

    @Query("SELECT DISTINCT l.state FROM LocationMaster l")
    List<String> findDistinctStates();

    @Query("SELECT DISTINCT l.city FROM LocationMaster l WHERE l.state = :state")
    List<String> findCitiesByState(String state);

    @Query("SELECT DISTINCT l.locality FROM LocationMaster l WHERE l.state = :state AND l.city = :city")
    List<String> findLocalitiesByStateAndCity(String state, String city);
}
