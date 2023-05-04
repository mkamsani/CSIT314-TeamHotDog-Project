package com.hotdog.ctbs.repository;

import com.hotdog.ctbs.entity.Screening;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ScreeningRepository extends JpaRepository<Screening, UUID> {

    Screening findScreeningById(UUID id);



}
