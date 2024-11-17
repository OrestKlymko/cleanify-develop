package com.cleaning.cleanify.reservation.repository;

import com.cleaning.cleanify.reservation.dto.UserReservationResponse;
import com.cleaning.cleanify.reservation.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
	@Query(value = """
				SELECT r.id as id,
			           r.date_time as dateTime,
			           r.status as status,
			           r.address as address,
			           ct.value as service,
			           r.price as price,
			           r.comment as comment,
			           r.key_location as keyLocation,
			           r.estimated_time_hours as estimatedTimeHours
    FROM reservations r
             inner join public.cleaning_type ct on ct.id = r.cleaning_type_id
    where user_id = :userId
			""", nativeQuery = true)
	List<UserReservationResponse> getReservationsByUser(Long userId);

}
