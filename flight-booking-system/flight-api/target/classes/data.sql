-- Sample Airport Data
INSERT INTO flightdb.airport (code, name, city) VALUES
('JFK', 'John F. Kennedy International Airport', 'New York'),
('LAX', 'Los Angeles International Airport', 'Los Angeles'),
('LHR', 'London Heathrow Airport', 'London'),
('CDG', 'Charles de Gaulle Airport', 'Paris');

-- Sample Flight Data
-- Flights from New York (JFK)
INSERT INTO flightdb.flight (flight_number, departure_airport_id, destination_airport_id, departure_date, departure_time, price)
VALUES ('AA101', 1, 2, '2024-07-15', '08:00:00', 350.00);

INSERT INTO flightdb.flight (flight_number, departure_airport_id, destination_airport_id, departure_date, departure_time, price)
VALUES ('BA245', 1, 3, '2024-07-16', '19:00:00', 550.00);

INSERT INTO flightdb.flight (flight_number, departure_airport_id, destination_airport_id, departure_date, departure_time, price)
VALUES ('AF009', 1, 4, '2024-07-17', '22:00:00', 600.00);

-- Flights from Los Angeles (LAX)
INSERT INTO flightdb.flight (flight_number, departure_airport_id, destination_airport_id, departure_date, departure_time, price)
VALUES ('UA302', 2, 1, '2024-07-18', '10:30:00', 340.00);

INSERT INTO flightdb.flight (flight_number, departure_airport_id, destination_airport_id, departure_date, departure_time, price)
VALUES ('VS008', 2, 3, '2024-07-19', '17:45:00', 580.00);

-- Flights from London (LHR)
INSERT INTO flightdb.flight (flight_number, departure_airport_id, destination_airport_id, departure_date, departure_time, price)
VALUES ('BA244', 3, 1, '2024-07-20', '10:00:00', 530.00);

INSERT INTO flightdb.flight (flight_number, departure_airport_id, destination_airport_id, departure_date, departure_time, price)
VALUES ('AF1681', 3, 4, '2024-07-21', '14:30:00', 120.00);

-- Flights from Paris (CDG)
INSERT INTO flightdb.flight (flight_number, departure_airport_id, destination_airport_id, departure_date, departure_time, price)
VALUES ('AF008', 4, 1, '2024-07-22', '11:00:00', 590.00);

INSERT INTO flightdb.flight (flight_number, departure_airport_id, destination_airport_id, departure_date, departure_time, price)
VALUES ('DL8553', 4, 2, '2024-07-23', '09:15:00', 620.00);

-- Additional Flights
INSERT INTO flightdb.flight (flight_number, departure_airport_id, destination_airport_id, departure_date, departure_time, price)
VALUES ('LH100', 1, 4, '2024-08-01', '12:00:00', 610.00);

INSERT INTO flightdb.flight (flight_number, departure_airport_id, destination_airport_id, departure_date, departure_time, price)
VALUES ('EK204', 3, 1, '2024-08-05', '21:30:00', 560.00);


UPDATE flightdb.flight SET departure_date = '2025-05-30';
