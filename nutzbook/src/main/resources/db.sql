# create the database
create database nutzbook default character set utf8mb4 collate utf8mb4_general_ci;

# create default database user
create user `nutz`@`%` identified by 'nutz';
grant all on nutzbook.* to `nutz`@`%`;
