-- THIS "КРЯКЭН" NOT FOR H2 TO IN.MEMORY

--DROP TABLE IF EXISTS request CASCADE;
--DROP TABLE IF EXISTS booking CASCADE;
--DROP TABLE IF EXISTS comments CASCADE;
--DROP TABLE IF EXISTS items CASCADE;
--DROP TABLE IF EXISTS users CASCADE;

CREATE TABLE IF NOT EXISTS users
(
    id    BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY UNIQUE,
    name  VARCHAR(255) NOT NULL,
    email VARCHAR(320) UNIQUE
);

CREATE TABLE IF NOT EXISTS request
(
    id           BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY UNIQUE,
    description  VARCHAR(2000)               NOT NULL,
    requester_id BIGINT                      NOT NULL,
    created_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT fk_request_to_user FOREIGN KEY (requester_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS items
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY UNIQUE,
    name        VARCHAR(255) NOT NULL,
    description VARCHAR(2000),
    available   BOOLEAN      NOT NULL,
    owner_id    BIGINT       NOT NULL,
    request_id  BIGINT,
    CONSTRAINT fk_items_to_users FOREIGN KEY (owner_id) REFERENCES users (id),
    CONSTRAINT fk_items_to_request FOREIGN KEY (request_id) REFERENCES request (id)
);

CREATE TABLE IF NOT EXISTS booking
(
    id              BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY UNIQUE,
    start_date_rent TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    end_date_rent   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    item_id         BIGINT                      NOT NULL,
    booker_id       BIGINT                      NOT NULL,
    status          VARCHAR(20)                 NOT NULL,
    CONSTRAINT fk_booking_to_item FOREIGN KEY (item_id) REFERENCES items (id),
    CONSTRAINT fk_booking_to_user FOREIGN KEY (booker_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS comments
(
    id           BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY UNIQUE,
    comment      VARCHAR(2000)               NOT NULL,
    item_id      BIGINT                      NOT NULL,
    author_id    BIGINT                      NOT NULL,
    created_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT fk_comments_to_item FOREIGN KEY (item_id) REFERENCES items (id),
    CONSTRAINT fk_comments_to_user FOREIGN KEY (author_id) REFERENCES users (id)
);