package repository;

import entity.Room;
// ENSURE THIS IMPORT EXISTS:
import org.springframework.data.jpa.repository.JpaRepository; 
public interface RoomRepository extends JpaRepository<Room, Long> {
    
}