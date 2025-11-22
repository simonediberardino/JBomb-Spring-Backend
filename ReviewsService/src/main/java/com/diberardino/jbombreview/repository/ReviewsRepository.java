package com.diberardino.jbombreview.repository;

import com.diberardino.jbombreview.data.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewsRepository extends JpaRepository<ReviewEntity, Long> {

}
