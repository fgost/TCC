INSERT INTO categories_types (category_id, type_id)
SELECT c.id, t.id
FROM categories c
CROSS JOIN types t;