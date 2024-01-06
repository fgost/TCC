DO $$
BEGIN
    -- Checks if there are already records in the table
    IF EXISTS (SELECT 1 FROM types LIMIT 1) THEN
        RAISE NOTICE 'The table already has records.';
    ELSE
        -- Insert 31 records into the table
        INSERT INTO types (code, type_name)
        VALUES
            ('be7ce71e-c37b-4771-8279-1687ba326b02', 0),
            ('b1a2ce39-eef3-45f3-8e99-8bef011879b2', 1),
            ('7ca17d31-03de-432f-8a0e-2461cec10ff7', 2),
            ('bb70dd05-572f-42aa-bce6-7844c63288fc', 3);
    END IF;
END $$;
