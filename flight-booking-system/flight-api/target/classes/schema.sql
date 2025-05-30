CREATE TABLE IF NOT EXISTS USER (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    country VARCHAR(100) NOT NULL,
    phone VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS AIRPORT (
    airport_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(10) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    city VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS AIRLINE_COMPANIES (
    airline_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    logo_url VARCHAR(255)
);
CREATE TABLE IF NOT EXISTS FLIGHT (
    flight_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    flight_number VARCHAR(10) NOT NULL,
    airline_id BIGINT,
    departure_airport_id BIGINT,
    destination_airport_id BIGINT,
    departure_date DATE NOT NULL,
    departure_time TIME NOT NULL,
arrival_time TIME NOT NULL,
    price DECIMAL(10,2) NOT NULL,
is_direct BOOLEAN NOT NULL DEFAULT TRUE,
    cabin_class VARCHAR(20),
    FOREIGN KEY (airline_id) REFERENCES AIRLINE_COMPANIES(airline_id),
    FOREIGN KEY (departure_airport_id) REFERENCES AIRPORT(airport_id),
    FOREIGN KEY (destination_airport_id) REFERENCES AIRPORT(airport_id)
);

CREATE TABLE IF NOT EXISTS BOOKING (
    booking_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    flight_id BIGINT,
    reference VARCHAR(20) NOT NULL UNIQUE,
    status VARCHAR(20) NOT NULL,
    booking_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    total_price DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES USER(user_id),
    FOREIGN KEY (flight_id) REFERENCES FLIGHT(flight_id)
);

CREATE TABLE IF NOT EXISTS PASSENGER (
    passenger_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    booking_id BIGINT,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL,
    FOREIGN KEY (booking_id) REFERENCES BOOKING(booking_id)
);
