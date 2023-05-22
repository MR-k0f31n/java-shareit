-- THIS "КРЯКЭН" NOT FOR H2 TO IM.MEM
--DROP TABLE booking;
--DROP TABLE CASCADE comments;
--DROP TABLE CASCADE items;
--DROP TABLE CASCADE users;

CREATE TABLE IF NOT EXISTS users (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY UNIQUE,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(320) UNIQUE
);

CREATE TABLE IF NOT EXISTS items (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY UNIQUE,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(2000),
    available BOOLEAN NOT NULL,
    owner_id BIGINT NOT NULL,
    CONSTRAINT fk_items_to_users FOREIGN KEY(owner_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS booking (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY UNIQUE,
    start_date_rent TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    end_date_rent TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    item_id BIGINT NOT NULL,
    booker_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL,
    CONSTRAINT fk_booking_to_item FOREIGN KEY(item_id) REFERENCES items(id),
    CONSTRAINT fk_booking_to_user FOREIGN KEY(booker_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS comments (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY UNIQUE,
    comment VARCHAR(2000) NOT NULL,
    item_id BIGINT NOT NULL,
    author_id BIGINT NOT NULL,
    created_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT fk_comments_to_item FOREIGN KEY(item_id) REFERENCES items(id),
    CONSTRAINT fk_comments_to_user FOREIGN KEY(author_id) REFERENCES users(id)
);