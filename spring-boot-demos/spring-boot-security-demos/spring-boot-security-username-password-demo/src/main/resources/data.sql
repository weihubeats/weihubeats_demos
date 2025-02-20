CREATE TABLE IF NOT EXISTS users (
                                     id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                     username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL
    );

INSERT INTO users (username, password, role) VALUES
                                                ('user', '$2a$10$.zTsUmN6bTbM2kMRxnny6eCFDBr3/DkrapKb4Ed403tmnukOm9eGy', 'ROLE_USER'),
                                                ('admin', '$2a$10$.zTsUmN6bTbM2kMRxnny6eCFDBr3/DkrapKb4Ed403tmnukOm9eGy', 'ROLE_ADMIN');
