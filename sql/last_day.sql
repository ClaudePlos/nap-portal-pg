create function last_day(date) returns date
    immutable
    strict
    language sql
as
$$
SELECT (date_trunc('MONTH', $1) + INTERVAL '1 MONTH - 1 day')::date;
$$;

alter function last_day(date) owner to portal;