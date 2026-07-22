# Sam.Loong Java 8 production stack

## Structure

- `web/`: Nuxt 4 public website (Vue 3, SSR for SEO)
- `admin/`: Vue 3 + Element Plus management console
- `backend/`: Java 8 + Spring Boot 2.7.18 REST API
- `deploy/`: Nginx reverse-proxy configuration
- `docker-compose.yml`: MySQL 8, API, website, admin and gateway

## Local startup

1. Copy `.env.example` to `.env` and replace both passwords.
2. Run `docker compose up --build`.
3. Website: `http://127.0.0.1/`
4. Admin: `http://127.0.0.1/admin/`
5. Health: `http://127.0.0.1/actuator/health`

The old Vinext prototype remains in the repository root temporarily for visual comparison. Remove it only after the Nuxt version is accepted.

## Production

Use a Singapore Linux server with Docker Compose. Terminate TLS at Nginx or a cloud load balancer, keep MySQL unexposed, store secrets only in `.env`, and back up the MySQL volume daily.

## Java 8 support note

Spring Boot 2.7.18 is the last open-source Spring Boot line compatible with Java 8. Its open-source maintenance has ended. Use commercial security patches or plan a later upgrade to Java 21.
