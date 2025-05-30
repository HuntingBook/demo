-- Sample Airport Data
INSERT INTO flightdb.airport (code, name, city) VALUES
('JFK', 'John F. Kennedy International Airport', 'New York'),
('LAX', 'Los Angeles International Airport', 'Los Angeles'),
('LHR', 'London Heathrow Airport', 'London'),
('CDG', 'Charles de Gaulle Airport', 'Paris');

-- Sample Airline Company Data
INSERT INTO flightdb.airline_companies (name, logo_url) VALUES
('American Airlines', 'https://example.com/logos/aa.png'),
('British Airways', 'https://example.com/logos/ba.png'),
('Air France', 'https://example.com/logos/af.png'),
('United Airlines', 'https://example.com/logos/ua.png'),
('Virgin Atlantic', 'https://example.com/logos/vs.png'),
('Delta Air Lines', 'https://example.com/logos/dl.png'),
('Lufthansa', 'https://example.com/logos/lh.png'),
('Emirates', 'https://example.com/logos/ek.png');

-- Sample Flight Data
-- Flights from New York (JFK)
INSERT INTO flightdb.flight (flight_number, airline_id, departure_airport_id, destination_airport_id, departure_date, departure_time, arrival_time, price, is_direct, cabin_class)
VALUES ('AA101', 1, 1, 2, '2025-07-15', '08:00:00', '11:00:00', 350.00, TRUE, 'ECONOMY');

INSERT INTO flightdb.flight (flight_number, airline_id, departure_airport_id, destination_airport_id, departure_date, departure_time, arrival_time, price, is_direct, cabin_class)
VALUES ('BA245', 2, 1, 3, '2025-07-16', '19:00:00', '07:00:00', 550.00, TRUE, 'BUSINESS'); -- Next day arrival

INSERT INTO flightdb.flight (flight_number, airline_id, departure_airport_id, destination_airport_id, departure_date, departure_time, arrival_time, price, is_direct, cabin_class)
VALUES ('AF009', 3, 1, 4, '2025-07-17', '22:00:00', '12:00:00', 600.00, FALSE, 'ECONOMY'); -- Next day arrival

-- Flights from Los Angeles (LAX)
INSERT INTO flightdb.flight (flight_number, airline_id, departure_airport_id, destination_airport_id, departure_date, departure_time, arrival_time, price, is_direct, cabin_class)
VALUES ('UA302', 4, 2, 1, '2025-07-18', '10:30:00', '18:30:00', 340.00, TRUE, 'ECONOMY');

INSERT INTO flightdb.flight (flight_number, airline_id, departure_airport_id, destination_airport_id, departure_date, departure_time, arrival_time, price, is_direct, cabin_class)
VALUES ('VS008', 5, 2, 3, '2025-07-19', '17:45:00', '11:45:00', 580.00, TRUE, 'FIRST'); -- Next day arrival

-- Flights from London (LHR)
INSERT INTO flightdb.flight (flight_number, airline_id, departure_airport_id, destination_airport_id, departure_date, departure_time, arrival_time, price, is_direct, cabin_class)
VALUES ('BA244', 2, 3, 1, '2025-07-20', '10:00:00', '13:00:00', 530.00, TRUE, 'BUSINESS');

INSERT INTO flightdb.flight (flight_number, airline_id, departure_airport_id, destination_airport_id, departure_date, departure_time, arrival_time, price, is_direct, cabin_class)
VALUES ('AF1681', 3, 3, 4, '2025-07-21', '14:30:00', '16:30:00', 120.00, TRUE, 'ECONOMY');

-- Flights from Paris (CDG)
INSERT INTO flightdb.flight (flight_number, airline_id, departure_airport_id, destination_airport_id, departure_date, departure_time, arrival_time, price, is_direct, cabin_class)
VALUES ('AF008', 3, 4, 1, '2025-07-22', '11:00:00', '14:00:00', 590.00, FALSE, 'ECONOMY');

INSERT INTO flightdb.flight (flight_number, airline_id, departure_airport_id, destination_airport_id, departure_date, departure_time, arrival_time, price, is_direct, cabin_class)
VALUES ('DL8553', 6, 4, 2, '2025-07-23', '09:15:00', '12:15:00', 620.00, TRUE, 'ECONOMY');

-- Additional Flights
INSERT INTO flightdb.flight (flight_number, airline_id, departure_airport_id, destination_airport_id, departure_date, departure_time, arrival_time, price, is_direct, cabin_class)
VALUES ('LH100', 7, 1, 4, '2025-08-01', '12:00:00', '02:00:00', 610.00, TRUE, 'FIRST'); -- Next day arrival

INSERT INTO flightdb.flight (flight_number, airline_id, departure_airport_id, destination_airport_id, departure_date, departure_time, arrival_time, price, is_direct, cabin_class)
VALUES ('EK204', 8, 3, 1, '2025-08-05', '21:30:00', '05:30:00', 560.00, TRUE, 'BUSINESS'); -- Next day arrival
