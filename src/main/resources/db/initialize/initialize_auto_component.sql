DO $$
BEGIN
    -- Checks if there are already records in the table
    IF EXISTS (SELECT 1 FROM auto_components LIMIT 1) THEN
        RAISE NOTICE 'The table already has records.';
    ELSE
        -- Insert 31 records into the table
        INSERT INTO auto_components (code, component_name)
        VALUES
            ('ac832e7d-1373-4107-b248-99a7d61bce73', 'CONFORTO E INTERIOR'),
            ('a91467c7-edd2-4e83-a4bd-52ba3ce76911', 'SISTEMA ELÉTRICO'),
            ('be33e5e2-5a6f-4172-a784-680d4af6c785', 'MOTOR'),
            ('be33e5e2-5a6f-4172-a581-680d4af6c853', 'AR CONDICIONADOS'),
            ('273265a5-91a8-4d25-bf7b-FSFASDF787F7', 'FLUIDOS'),
            ('273265a5-91a8-4d25-bf7b-251c9ee7fb6b', 'TRANSMISSÃO E SUSPENSÃO'),
            ('48e3b49a-9081-4d69-9a14-432FSAFASW3N', 'FREIOS E SEGURANÇA'),
            ('cfd71c4f-9e7f-4078-bf33-424c754f96e1', 'LATARIA'),
            ('ce3fa45b-0aa5-4aed-affa-aeeb7179ffcd', 'RODAS/PNEUS'),
            ('ce3fa45b-0aa5-4aed-affa-421RAGFASF24', 'OUTRAS ESPECIALIDADES');

    END IF;
END $$;
