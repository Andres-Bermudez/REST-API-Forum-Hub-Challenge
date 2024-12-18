package com.alura.restapiforohubchallenge.domain.topic;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface TopicRepository extends JpaRepository<TopicEntity, Long> {

    Boolean existsByTitle(String title);
    Boolean existsByMessage(String message);
    Page<TopicEntity> findByActiveStatusTrue(Pageable pageable);
}
