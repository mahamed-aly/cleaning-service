
INSERT INTO vehicles (plate_number) VALUES
('ABC1234'),
('DEF5678'),
('GHI9012'),
('JKL3456'),
('MNO7890');

INSERT INTO cleaning_professionals (name, vehicle_id) VALUES
('Jack Peter', (SELECT id FROM vehicles WHERE plate_number = 'ABC1234')),
('Jane Smith', (SELECT id FROM vehicles WHERE plate_number = 'ABC1234')),
('Alice Johnson', (SELECT id FROM vehicles WHERE plate_number = 'ABC1234')),
('Bob Brown', (SELECT id FROM vehicles WHERE plate_number = 'ABC1234')),
('Charlie Davis', (SELECT id FROM vehicles WHERE plate_number = 'ABC1234')),

('David Evans', (SELECT id FROM vehicles WHERE plate_number = 'DEF5678')),
('Emily White', (SELECT id FROM vehicles WHERE plate_number = 'DEF5678')),
('Frank Moore', (SELECT id FROM vehicles WHERE plate_number = 'DEF5678')),
('Grace Taylor', (SELECT id FROM vehicles WHERE plate_number = 'DEF5678')),
('Hannah Miller', (SELECT id FROM vehicles WHERE plate_number = 'DEF5678')),

('Ian Harris', (SELECT id FROM vehicles WHERE plate_number = 'GHI9012')),
('Jack Wilson', (SELECT id FROM vehicles WHERE plate_number = 'GHI9012')),
('Karen Lee', (SELECT id FROM vehicles WHERE plate_number = 'GHI9012')),
('Laura Clark', (SELECT id FROM vehicles WHERE plate_number = 'GHI9012')),
('Mark Lewis', (SELECT id FROM vehicles WHERE plate_number = 'GHI9012')),

('Nancy Walker', (SELECT id FROM vehicles WHERE plate_number = 'JKL3456')),
('Olivia Hall', (SELECT id FROM vehicles WHERE plate_number = 'JKL3456')),
('Paul Young', (SELECT id FROM vehicles WHERE plate_number = 'JKL3456')),
('Quincy King', (SELECT id FROM vehicles WHERE plate_number = 'JKL3456')),
('Rachel Adams', (SELECT id FROM vehicles WHERE plate_number = 'JKL3456')),

('Steven Wright', (SELECT id FROM vehicles WHERE plate_number = 'MNO7890')),
('Tracy Scott', (SELECT id FROM vehicles WHERE plate_number = 'MNO7890')),
('Ursula Green', (SELECT id FROM vehicles WHERE plate_number = 'MNO7890')),
('Victor Baker', (SELECT id FROM vehicles WHERE plate_number = 'MNO7890')),
('Wendy Campbell', (SELECT id FROM vehicles WHERE plate_number = 'MNO7890'));

INSERT INTO customers (name, address, phone_number) VALUES ("Smith", "LA", "123456");
