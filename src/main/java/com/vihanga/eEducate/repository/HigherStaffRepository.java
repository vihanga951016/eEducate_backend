package com.vihanga.eEducate.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vihanga.eEducate.domain.HigherStaff;
//Database dealing
public interface HigherStaffRepository extends JpaRepository<HigherStaff, Long>{

	//Select staff member from user name in database
	HigherStaff findStaffMemberByUsername(String username);
	
	//Select staff member from email.
	HigherStaff findStaffMemberByEmail(String email);
}
