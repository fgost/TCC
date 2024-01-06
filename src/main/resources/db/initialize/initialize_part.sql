DO $$
BEGIN
    -- Checks if there are already records in the table
    IF EXISTS (SELECT 1 FROM parts LIMIT 1) THEN
        RAISE NOTICE 'The table already has records.';
    ELSE
        -- Insert 31 records into the table
        INSERT INTO parts (code, name, description, serial_number, manufacturer, model, installation_date, life_span, cost, status, type, car)
        VALUES
            ('be7ce71e-c37b-4771-8279-1687ba326b02', 'Correia dentada', 'Correia para o motor', 'SN00001', 'WEG', 'Model A', '2022-01-01', 5, 2000.00, 0, 1, 1),
            ('b1a2ce39-eef3-45f3-8e99-8bef011879b2', 'Pastilha do Freio', 'Pastilha para o sistema de frenagem ', 'SN00002', 'DHL', 'Model B', '2022-02-01', 6, 2500.00, 1, 1, 1),
            ('7ca17d31-03de-432f-8a0e-2461cec10ff7', 'Escapamento', 'Escapamento do carro', 'SN00003', 'Venom', 'Model C', '2022-03-01', 7, 1500.00, 3, 2, 1),
            ('bb70dd05-572f-42aa-bce6-7844c63288fc', 'Volante', 'VOlante do carro', 'SN00004', 'Fiat', 'Model D', '2022-04-01', 8, 1000.00, 2, 2, 1);
    END IF;
END $$;
