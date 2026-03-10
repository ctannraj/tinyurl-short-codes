# TinyURL Short Code Service

A URL shortener implemented using **Spring Boot**, **Snowflake ID generation**, **ID scrambling**, and **Base62 encoding**.

This project demonstrates the core ideas behind systems like **TinyURL** and **Bitly** for generating short, unique, and non‑predictable URLs.

---

# Architecture

Flow:

Client → REST API → Snowflake ID Generator → ID Scrambler → Base62 Encoder → Short Code

Example request:

POST /api/v1/shorten

Request

{
"url": "[https://example.com/very/long/url](https://example.com/very/long/url)"
}

Response

{
"shortCode": "aZ91xQ3",
"url": "[https://example.com/very/long/url](https://example.com/very/long/url)"
}

Redirect request:

GET /api/v1/shorten/aZ91xQ3

Response:

HTTP/1.1 302 Found
Location: [https://example.com/very/long/url](https://example.com/very/long/url)

---

# Project Structure

src/main/java/com/mnc/tinyurl

controller

* ShortCodeController.java

service

* ShortCodeGeneratorService.java

generator

* SnowflakeIdGenerator.java

util

* Base62Encoder.java
* IdScrambler.java

model

* ShortCodeRequest.java

---

# Core Components

## Snowflake ID Generator

Generates globally unique 64‑bit IDs.

Bit layout:

| Bits | Purpose   |
| ---- | --------- |
| 41   | Timestamp |
| 10   | Node ID   |
| 12   | Sequence  |

Supports:

* 4096 IDs per millisecond per node
* Millions of IDs per second across distributed nodes

---

## ID Scrambler

Snowflake IDs are sequential. If encoded directly, short URLs become predictable.

Example sequence:

abc123
abc124
abc125

To prevent this, IDs are scrambled before Base62 encoding.

Example transformation:

123456789 → scrambled → 987234234

This prevents enumeration attacks.

---

## Base62 Encoder

Encodes numeric IDs using the alphabet:

abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789

Example:

123456789 → aZ91xQ3

Codes are padded to maintain fixed length.

---

# API Endpoints

## Create Short URL

POST /api/v1/shorten

Example:

curl -X POST [http://localhost:8080/api/v1/shorten](http://localhost:8080/api/v1/shorten)
-H "Content-Type: application/json"
-d '{"url":"[https://example.com"}](https://example.com%22})'

Response

{
"url": "[https://example.com](https://example.com)",
"shortCode": "aZ91xQ3"
}

---

## Redirect to Original URL

GET /api/v1/shorten/{shortCode}

Example:

curl -v [http://localhost:8080/api/v1/shorten/aZ91xQ3](http://localhost:8080/aZ91xQ3)

Response

HTTP/1.1 302 Found
Location: [https://example.com](https://example.com)

---

# Running the Application

Using Maven:

mvn spring-boot:run

Server starts at:

[http://localhost:8080](http://localhost:8080)

---

# Future Improvements

Production systems typically include:

* Redis caching layer
* Persistent database (PostgreSQL / DynamoDB)
* Collision detection
* Analytics and click tracking
* Rate limiting
* Expiring URLs
* Distributed node configuration

---

# Throughput

Snowflake supports:

* 4096 IDs per millisecond per node
* ~4 million IDs per second per node

This makes the system horizontally scalable.

---

# License

MIT
