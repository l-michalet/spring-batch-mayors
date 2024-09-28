package com.mchlt.mayor.repository;

import com.mchlt.mayor.model.TownHall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TownHallRepository extends JpaRepository<TownHall, Long> {

}

