package com.neodohae_spring_boot.neodohae_spring_boot.repository;

import com.neodohae_spring_boot.neodohae_spring_boot.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Integer> {
}
