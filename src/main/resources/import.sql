-- Insert initial categories
INSERT INTO categories (id, name, description, created_at, updated_at, url_image) VALUES
(1, 'Electronics', 'Electronic products like phones and laptops', NOW(), NOW(), 'electronics.jpg'),
(2, 'Home Appliances', 'Home appliances like refrigerators and washing machines', NOW(), NOW(), 'appliances.jpg'),
(3, 'Furniture', 'Different types of furniture', NOW(), NOW(), 'furniture.jpg');

-- Insert initial products
INSERT INTO products (id, category_id, name, description, price, created_at, updated_at, url_image, reserved) VALUES
(1, 1, 'Smartphone', 'Latest model smartphone with 128GB storage', 699.99, NOW(), NOW(), 'smartphone.jpg', FALSE),
(2, 1, 'Laptop', 'High-performance laptop for developers', 1299.99, NOW(), NOW(), 'laptop.jpg', FALSE),
(3, 2, 'Refrigerator', 'Energy-efficient refrigerator', 499.99, NOW(), NOW(), 'refrigerator.jpg', FALSE);

-- Insert initial characteristics for products
INSERT INTO characteristics (id, name, description, created_at, updated_at, product_id) VALUES
(1, 'Color', 'Black', NOW(), NOW(), 1),
(2, 'Storage Capacity', '128GB', NOW(), NOW(), 1),
(3, 'RAM', '16GB', NOW(), NOW(), 2),
(4, 'Energy Efficiency', 'A++', NOW(), NOW(), 3);

-- Insert initial roles
INSERT INTO roles (id, name) VALUES
(1, 'ROLE_ADMIN'),
(2, 'ROLE_USER');

-- Insert initial users
INSERT INTO users (first_name, last_name, address, email, password, phone, enabled, username, verification_code, verification_expiration) VALUES
 ('John', 'Doe', '123 Main St', 'user1@example.com', 'password1', '123-456-7890', true, 'user1', 'verificationCode1', '2024-12-31 23:59:59'),
 ('Jane', 'Smith', '456 Elm St', 'user2@example.com', 'password2', '987-654-3210', true, 'user2', 'verificationCode2', '2024-12-31 23:59:59');


-- Assign roles to users
INSERT INTO users_roles (user_id, role_id) VALUES
(1, 1), -- Admin user
(2, 2); -- Regular user