-- Insert ADMIN user
INSERT INTO "user" (name, username, email, password)
VALUES ('Admin User', 'admin', 'admin@example.com', 'adminpassword');

-- Associate ADMIN user with ADMIN_ROLE
INSERT INTO user_roles (user_id, role_id)
VALUES ((SELECT id FROM "user" WHERE username = 'admin'), (SELECT id FROM role WHERE name = 'ADMIN_ROLE'));