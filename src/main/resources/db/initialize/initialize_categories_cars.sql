INSERT INTO categories_cars (category_id, car_id)
SELECT c.id, t.id
FROM categories c
CROSS JOIN cars t;