FROM postgres:16

ENV POSTGRES_DB=dashboard_db \
    POSTGRES_USER=postgres \
    POSTGRES_PASSWORD=password

VOLUME ["/var/lib/postgresql/data"]
