-- 1. System Service (시스템 공통/변동 데이터 관리)
CREATE DATABASE IF NOT EXISTS nova_system_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER IF NOT EXISTS 'nova_system'@'%' IDENTIFIED BY 'm1m2m3';
CREATE USER IF NOT EXISTS 'nova_system'@'localhost' IDENTIFIED BY 'm1m2m3';
GRANT ALL PRIVILEGES ON nova_system_db.* TO 'nova_system'@'%';
GRANT ALL PRIVILEGES ON nova_system_db.* TO 'nova_system'@'localhost';

-- 2. Notification Service (알림/메시징 관리)
CREATE DATABASE IF NOT EXISTS nova_notification_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER IF NOT EXISTS 'nova_notification'@'%' IDENTIFIED BY 'm1m2m3';
CREATE USER IF NOT EXISTS 'nova_notification'@'localhost' IDENTIFIED BY 'm1m2m3';
GRANT ALL PRIVILEGES ON nova_notification_db.* TO 'nova_notification'@'%';
GRANT ALL PRIVILEGES ON nova_notification_db.* TO 'nova_notification'@'localhost';

-- 3. Auth Service (인증/권한 관리)
CREATE DATABASE IF NOT EXISTS nova_auth_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER IF NOT EXISTS 'nova_auth'@'%' IDENTIFIED BY 'm1m2m3';
CREATE USER IF NOT EXISTS 'nova_auth'@'localhost' IDENTIFIED BY 'm1m2m3';
GRANT ALL PRIVILEGES ON nova_auth_db.* TO 'nova_auth'@'%';
GRANT ALL PRIVILEGES ON nova_auth_db.* TO 'nova_auth'@'localhost';

-- 4. User Service (사용자 도메인 관리)
CREATE DATABASE IF NOT EXISTS nova_user_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER IF NOT EXISTS 'nova_user'@'%' IDENTIFIED BY 'm1m2m3';
CREATE USER IF NOT EXISTS 'nova_user'@'localhost' IDENTIFIED BY 'm1m2m3';
GRANT ALL PRIVILEGES ON nova_user_db.* TO 'nova_user'@'%';
GRANT ALL PRIVILEGES ON nova_user_db.* TO 'nova_user'@'localhost';

-- 설정 즉시 반영
FLUSH PRIVILEGES;