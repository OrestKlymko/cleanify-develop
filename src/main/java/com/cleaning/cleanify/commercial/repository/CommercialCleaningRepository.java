package com.cleaning.cleanify.commercial.repository;


import com.cleaning.cleanify.commercial.dto.AllCommercialResponse;
import com.cleaning.cleanify.commercial.model.CommercialCleaning;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommercialCleaningRepository extends JpaRepository<CommercialCleaning, Long> {

	@Query(value = """
				SELECT cc.id as id,
			        cc.company_name as companyName,
			        cc.status as status,
			        cc.date_time as date,
			        cc.address as address,
			        cc.square_ft as squareFootage,
			        cc.contact_person_name contactPersonName,
			        cc.phone_number as contactPhoneNumber,
			        cc.additional_instructions as additionalInstructions
			 FROM commercial_cleaning cc
			         INNER JOIN users u on u.id = cc.user_id
			 WHERE u.id=:id
			""", nativeQuery = true)
	List<AllCommercialResponse> getAllCommercialCleaningByUser(@Param("id") Long id);


	@Query(value = """
			SELECT cc.id as id,
			       cc.company_name as companyName,
			       cc.status as status,
			       cc.date_time as date,
			       cc.address as address,
			       cc.square_ft as squareFootage,
			       cc.contact_person_name contactPersonName,
			       cc.phone_number as contactPhoneNumber,
			       cc.additional_instructions as additionalInstructions
			       FROM commercial_cleaning cc;
			""", nativeQuery = true)
	List<AllCommercialResponse> getAllCommercialCleaning();
}
