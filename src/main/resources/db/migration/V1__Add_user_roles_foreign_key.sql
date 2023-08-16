ALTER TABLE IF EXISTS user_roles
  ADD CONSTRAINT FK_user_roles_user_id FOREIGN KEY (user_id) REFERENCES "user";