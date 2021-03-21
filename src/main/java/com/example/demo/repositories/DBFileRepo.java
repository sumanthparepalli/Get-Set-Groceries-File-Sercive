package com.example.demo.repositories;

import com.example.demo.models.DBFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DBFileRepo extends JpaRepository<DBFile, String> {
}
