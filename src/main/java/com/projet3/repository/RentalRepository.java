package com.projet3.repository;

import com.projet3.model.Message;
import com.projet3.model.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RentalRepository extends JpaRepository <Rental, Long> {

}
