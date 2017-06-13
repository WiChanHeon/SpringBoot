package com.chase.springboot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chase.springboot.MyData;

@Repository
public interface MyDataRepository extends JpaRepository<MyData, Long>{
	public MyData findById(Long name);
}
