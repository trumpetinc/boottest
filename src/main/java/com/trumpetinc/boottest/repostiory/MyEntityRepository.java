package com.trumpetinc.boottest.repostiory;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trumpetinc.boottest.entity.MyEntity;

public interface MyEntityRepository extends JpaRepository<MyEntity, Long> {
}
