-- Clear existing data (optional, careful with production!)
TRUNCATE TABLE user_role_new, product, user_front_address, user_front, user_role RESTART IDENTITY CASCADE;

-- 1. Insert Roles
INSERT INTO user_role (role_name) VALUES 
('ADMIN'),
('COMPANY'),
('BRANCH')
ON CONFLICT (role_name) DO NOTHING;

-- 2. Insert Users (Companies and Branches)
-- Password is 'password' (BCrypt hash: $2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlNBxbetINUjP)

INSERT INTO user_front (name, password, parent_company_id, gst_no, phone_no) VALUES
('admin', 'password', NULL, 'GST-000', '9999999999'), -- ID 1
('TechCorp', 'password', NULL, 'GST-100', '9876543210'), -- ID 2 (Company)
('RetailHub', 'password', NULL, 'GST-200', '1234567890'), -- ID 3 (Company)
('TechCorp_Branch_A', 'password', 2, 'GST-101', '1111111111'), -- ID 4 (Branch of TechCorp)
('TechCorp_Branch_B', 'password', 2, 'GST-102', '2222222222'), -- ID 5 (Branch of TechCorp)
('RetailHub_Branch_X', 'password', 3, 'GST-201', '3333333333'); -- ID 6 (Branch of RetailHub)


-- 3. Insert Addresses
INSERT INTO user_front_address (user_front_id, name, address_line_1, address_line_2, city, state, country) VALUES
(1, 'admin', 'Admin HQ', 'Room 101', 'AdminCity', 'AdminState', 'AdminCountry'),
(2, 'TechCorp', 'Tech Park', 'Building 1', 'Silicon Valley', 'CA', 'USA'),
(3, 'RetailHub', 'Market Street', 'Shop 5', 'New York', 'NY', 'USA'),
(4, 'TechCorp_Branch_A', 'North Zone', 'Sector 4', 'Austin', 'TX', 'USA'),
(5, 'TechCorp_Branch_B', 'South Zone', 'Sector 9', 'Miami', 'FL', 'USA'),
(6, 'RetailHub_Branch_X', 'Downtown', 'Mall Road', 'Chicago', 'IL', 'USA');

-- 4. Assign Roles (user_role_new)
-- Assuming Role IDs: 1=ADMIN, 2=COMPANY, 3=BRANCH. 
-- User IDs: 1=admin, 2=TechCorp, 3=RetailHub, 4=TechCorp_Branch_A, 5=TechCorp_Branch_B, 6=RetailHub_Branch_X
INSERT INTO user_role_new (role_id, user_front_id) VALUES
(1, 1), -- admin -> ADMIN
(2, 2), -- TechCorp -> COMPANY
(2, 3), -- RetailHub -> COMPANY
(3, 4), -- TechCorp_Branch_A -> BRANCH
(3, 5), -- TechCorp_Branch_B -> BRANCH
(3, 6); -- RetailHub_Branch_X -> BRANCH

-- 5. Insert Products
-- TechCorp (ID 2): Products
INSERT INTO product (product_name, company_id, item_code, mrp, selling_price, description, stock_quantity) VALUES
('Laptop Pro', 2, 'TC-001', 1500.00, 1400.00, 'High perfromance laptop', 50),
('Enterprise Server', 2, 'TC-002', 5000.00, 4800.00, 'Rack server', 10);

-- TechCorp_Branch_A (ID 4): Products
INSERT INTO product (product_name, company_id, item_code, mrp, selling_price, description, stock_quantity) VALUES
('Wireless Mouse', 4, 'TCA-001', 50.00, 45.00, 'Ergonomic mouse', 100),
('Mechanical Keyboard', 4, 'TCA-002', 120.00, 110.00, 'RGB Keyboard', 80);

-- TechCorp_Branch_B (ID 5): Products
INSERT INTO product (product_name, company_id, item_code, mrp, selling_price, description, stock_quantity) VALUES
('4K Monitor', 5, 'TCB-001', 300.00, 280.00, '27 inch display', 30);

-- RetailHub (ID 3): Products
INSERT INTO product (product_name, company_id, item_code, mrp, selling_price, description, stock_quantity) VALUES
('Cotton T-Shirt', 3, 'RH-001', 20.00, 15.00, '100% Cotton', 500);

-- RetailHub_Branch_X (ID 6): Products
INSERT INTO product (product_name, company_id, item_code, mrp, selling_price, description, stock_quantity) VALUES
('Blue Jeans', 6, 'RHX-001', 60.00, 50.00, 'Slim fit jeans', 200);

