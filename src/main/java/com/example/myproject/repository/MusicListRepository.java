package com.example.myproject.repository;

import com.example.myproject.domain.MusicList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MusicListRepository extends JpaRepository<MusicList, Long> {


}
