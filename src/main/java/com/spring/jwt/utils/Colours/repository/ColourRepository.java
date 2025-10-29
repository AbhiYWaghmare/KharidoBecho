package com.spring.jwt.utils.Colours.repository;

import com.spring.jwt.utils.Colours.entity.Colour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ColourRepository extends JpaRepository<Colour,Long> {
    boolean existsByColourName(String colourName);
}

