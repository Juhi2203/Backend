package cts.travelpackagebookingsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cts.travelpackagebookingsystem.entity.Booking;
@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
	
	

}
