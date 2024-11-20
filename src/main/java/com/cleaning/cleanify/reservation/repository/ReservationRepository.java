package com.cleaning.cleanify.reservation.repository;

import com.cleaning.cleanify.reservation.dto.ReservationFullResponse;
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

	@Query(value = """
				SELECT r.id                   as id,
			        r.address              as address,
			        r.date_time            as dateTime,
			        r.number_of_bathrooms  as numberOfBathrooms,
			        r.number_of_rooms      as numberOfRooms,
			        r.price                as price,
			        r.comment              as comment,
			        r.estimated_time_hours as estimatedTimeHours,
			        r.key_location         as keyLocation,
			        r.status               as status,
			        ct.value               as service,
			        r.floor                as floor,
			        r.apartment            as apartment,
			        array_agg(aserv.value) as additionalServices
			 FROM reservations r
			          LEFT JOIN cleaning_type ct on ct.id = r.cleaning_type_id
			          LEFT JOIN reservation_additional_service ras on r.id = ras.reservation_id
			          LEFT JOIN additional_service aserv on aserv.id = ras.additional_service_id
			 group by r.apartment, r.id, ct.value
			""", nativeQuery = true)
	List<ReservationFullResponse> getAllReservationsForAdmin();

}
