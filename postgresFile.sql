select * from flights;

-- To clear Bookings:
truncate table bookings restart identity cascade;

-- To make 25% seats booked:
update seats
set status = CASE
	when random()<0.25 then 'BOOKED'
	else 'AVAILABLE'
end,
locked_at = NULL,
booked_by = NULL;

-- To make normal flight seats:
Update flights f
set available_seats = (
	Select COUNT(*)
	FROM seats s
	WHERE s.flight_id = f.id
	AND s.status = 'Available'
);