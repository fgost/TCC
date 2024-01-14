DO $$
BEGIN
    -- Checks if there are already records in the table
    IF EXISTS (SELECT 1 FROM cars LIMIT 1) THEN
        RAISE NOTICE 'The table already has records.';
    ELSE
        -- Insert 31 records into the table
        INSERT INTO cars (code, car_model, auto_maker, year, color, licence_plate, motor, type, mileage, user_owner)
        VALUES
            ('be7ce71e-c37b-4771-8279-1687ba326b02', 'Corolla', 'Toyota', '2021', 'Preto', 'ENH-5235', 1,25985.69, 1),
            ('b1a2ce39-eef3-45f3-8e99-8bef011879b2', 'Golf GTI', 'Volkswagen', '2021', 'Branco', 0,25985.69, 1),
            ('7ca17d31-03de-432f-8a0e-2461cec10ff7', 'Civic', 'Honda', '2022', 'Prata', 1,25985.69, 1),
            ('bb70dd05-572f-42aa-bce6-7844c63288fc', 'Tucson', 'Hyundai', '2022', 'Vermelho', 2,25985.69, 1),
            ('777eb0a4-1c06-46ea-9915-4891e715db5f', 'Onix', 'Chevrolet', '2022', 'Preto', 0,25985.69, 1),
            ('32eb6681-dfa5-4fdb-bfd7-27a24e691bac', 'Corolla Cross', 'Toyota', '2022', 'Branco', 2,25985.69, 1),
            ('e9979e10-aed8-4525-966f-d5a95c46b346', 'T-Cross', 'Volkswagen', '2021', 'Azul', 2,25985.69, 1),
            ('e178f4ac-640b-46d2-bedc-ff3897dfc98f', 'Compass', 'Jeep', '2022', 'Preto', 2,25985.69, 1),
            ('976cb094-00d7-44cd-a701-94aa16954ea7', 'Tracker', 'Chevrolet', '2021', 'Prata', 2,25985.69, 1),
            ('ba12d4fb-799f-474f-81c1-e64a588ce9ab', 'Creta', 'Hyundai', '2022', 'Preto', 2,25985.69, 1),
            ('293b0a7b-a184-458a-9e94-dff477f01527', 'Renegade', 'Jeep', '2021', 'Vermelho', 2,25985.69, 1),
            ('ba5f75a6-3ce2-4692-a69b-33c56c348296', 'Toro', 'Fiat', '2022', 'Preto', 3,25985.69, 1),
            ('fd36bdbe-82aa-4f6d-ad78-df7742bbc58c', 'S10', 'Chevrolet', '2021', 'Branco', 3,25985.69, 1),
            ('78fa04bc-6ef6-422e-9119-6b0bed2415b9', 'Nivus', 'Volkswagen', '2021', 'Preto', 3,25985.69, 1),
            ('f32a35f5-eeb5-4086-9623-39da8cc3e3b2', 'Gol', 'Volkswagen', '2021', 'Vermelho', 0,25985.69, 1),
            ('c8f6f329-b6fb-4ad4-a4c4-8d3ee33a4d4a', 'Hilux', 'Toyota', '2022', 'Prata', 3,25985.69, 1),
            ('0d3f4708-3b3f-4ef2-aab6-38a8f1c2129d', 'Camaro', 'Chevrolet', '2021', 'Amarelo', 0,25985.69, 1),
            ('bdf8a16c-3edf-4f7c-8721-ee915c04f30d', 'Virtus', 'Volkswagen', '2022', 'Preto', 2,25985.69, 1),
            ('da26b5df-c93e-4961-935f-8a3a9f3c0de5', 'Fusion', 'Ford', '2021', 'Prata', 1,25985.69, 1),
            ('7c2e9039-5b62-4d60-bdc1-2b2c192f7d68', 'Renault Kwid', 'Renault', '2021', 'Branco', 0,25985.69, 1),
            ('10e8343c-2d80-4a07-8a98-9d26058d6a1c', 'Tiggo 3x', 'Chery', '2022', 'Vermelho', 2,25985.69, 1),
            ('1f6d0d6a-77f2-4e60-8810-729f999cb7fa', 'Soul', 'Kia', '2021', 'Branco', 1,25985.69, 1),
            ('5a5d5d22-45af-4a9d-9f2a-49d262dfda8b', 'Cruze', 'Chevrolet', '2021', 'Preto', 1,25985.69, 1),
            ('f02d5b93-48d5-48e1-9e9a-7ecdfb32f401', 'Range Rover Evoque', 'Land Rover', '2022', 'Branco', 2,25985.69, 1),
            ('f44fb42c-118e-4313-97ba-ee899a95fca3', 'V90', 'Volvo', '2021', 'Cinza', 3,25985.69, 1),
            ('34de032b-523f-429f-a83a-100f184a08b7', 'Fit', 'Honda', '2021', 'Branco', 0,25985.69, 1),
            ('57d38a1c-2f80-4c38-9d8e-5b5a5a5a5e5e', 'Duster', 'Renault', '2021', 'Preto', 2,25985.69, 1),
            ('c4adfb6f-4b4a-4d3f-a6de-b3b00cfa6ba9', 'Nissan Versa', 'Nissan', '2021', 'Branco', 1,25985.69, 1),
            ('b6c131d6-fa9d-49c6-bda5-5a5a5a5a5a5a', 'Mobi', 'Fiat', '2021', 'Azul', 0,25985.69, 1),
            ('f0a57b11-7dc7-4a00-9c9a-d19b44c3e51e', 'Compass', 'Jeep', '2021', 'Vermelho', 2,25985.69, 1),
            ('d9a512e1-6781-4310-b0a7-101d57b0af7e', 'T-Cross', 'Volkswagen', '2021', 'Prata', 2,25985.69, 1),
            ('f637de6a-2a81-4b72-b1cc-14da1620b556', 'HB20', 'Hyundai', '2022', 'Branco', 0,25985.69, 1);
    END IF;
END $$;