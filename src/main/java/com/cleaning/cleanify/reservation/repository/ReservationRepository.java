package com.cleaning.cleanify.reservation.repository;

import com.cleaning.cleanify.reservation.dto.RegularReservationResponse;
import com.cleaning.cleanify.reservation.dto.ReservationFullResponse;
import com.cleaning.cleanify.reservation.dto.UserReservationResponse;
import com.cleaning.cleanify.reservation.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
					r.cleaning_frequency   as cleaningFrequency,
					r.next_cleaning_date   as nextCleaningDate,
					r.is_regular_cleaning  as isRegularCleaning,
					r.payment_method       as paymentMethod,
			        array_agg(aserv.value) as additionalServices,
			        u.first_name           as firstName,
			        u.phone_number         as phoneNumber
			 FROM reservations r
			          LEFT JOIN cleaning_type ct on ct.id = r.cleaning_type_id
			          LEFT JOIN reservation_additional_service ras on r.id = ras.reservation_id
			          LEFT JOIN additional_service aserv on aserv.id = ras.additional_service_id
			          LEFT JOIN users u on r.user_id = u.id
			 group by r.apartment, r.id, ct.value,u.first_name, u.phone_number
			""", nativeQuery = true)
	List<ReservationFullResponse> getAllReservationsForAdmin();


	@Query(value = """
			SELECT
			                                                                    r.id AS id,
			                                                                    r.date_time AS dateTime,
			                                                                    r.price AS price,
			                                                                    u.customer_id AS customerId,
			                                                                    u.first_name AS firstName,
						                                                        r.cleaning_frequency AS cleaningFrequency,
			                                                                    u.email AS email
			                                                                FROM
			                                                                    reservations r
			                                                                        JOIN
			                                                                    users u ON u.id = r.user_id
			                                                                WHERE
			                                                                    r.status = 'REGULAR' AND r.payment_method='card'
			                                                                  AND r.date_time::date = CURRENT_DATE
			""", nativeQuery = true)
	List<RegularReservationResponse> getRegularReservationsOfCurrentDate();


	@Query(value = """
			UPDATE reservations
			SET next_cleaning_date = date_time + (:cleaningFrequency || ' days')::INTERVAL
			WHERE id = :id
			""", nativeQuery = true)
	void updateDateOfRegularReservation(@Param("id") Long id, @Param("cleaningFrequency") Integer dateTime);

}
