-- Create the table
DROP TABLE IF EXISTS example;
CREATE TABLE example
(
    uuid Uuid    DEFAULT uuid_generate_v4() PRIMARY KEY,
    name TEXT,
    num  SERIAL,
    b    BOOLEAN DEFAULT TRUE
);

-- Insert some dummy data
INSERT INTO example (name)
VALUES ('John Doe');
INSERT INTO example (name, b)
VALUES ('Jane Smith', FALSE);

SELECT *
FROM example;

-- Define the function to return the data as JSON
CREATE OR REPLACE FUNCTION select_all_as_json()
    RETURNS Json
    LANGUAGE plpgsql
AS
$function$
DECLARE
    result Json;
BEGIN
    SELECT JSON_AGG(JSON_BUILD_OBJECT('uuid', uuid, 'name', name, 'num', num, 'b', b)) INTO result FROM example;
    RETURN result;
    -- result jsonb; BEGIN SELECT jsonb_agg(json_build_object('uuid', uuid, 'name', name, 'num', num, 'b', b)) INTO result FROM example; RETURN result;
END;
$function$;
-- Call the function to return the data as JSON
SELECT select_all_as_json();