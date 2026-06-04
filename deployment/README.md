# Deployment

This directory contains the local deployment configuration for AI Resume Review.

## Services

- `postgres`: PostgreSQL database for recent analysis history.
- `backend`: Spring Boot API service.
- `frontend`: Next.js frontend service.
- `nginx`: reverse proxy exposed on host port `80`.
- `prometheus`: metrics collector exposed on host port `9090`.

## Start

```bash
docker compose up -d --build
```

After startup:

- Frontend: `http://localhost`
- Backend health through Nginx: `http://localhost/api/health`
- Actuator health through Nginx: `http://localhost/actuator/health`
- Prometheus metrics endpoint through Nginx: `http://localhost/actuator/prometheus`
- Prometheus UI: `http://localhost:9090`

## Prometheus

Prometheus loads `prometheus.yml` and scrapes the backend inside the Docker network:

```text
backend:8080/actuator/prometheus
```

To verify scraping in the Prometheus UI, open `http://localhost:9090` and query:

```promql
up{job="ai-resume-review-backend"}
```

The expected value is `1` when the backend is reachable.

## Stop

```bash
docker compose down
```

To remove local database and Prometheus data volumes as well:

```bash
docker compose down -v
```
