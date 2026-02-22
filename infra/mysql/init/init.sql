-- [Project Nova] 서비스별 데이터베이스 생성 스크립트
CREATE DATABASE IF NOT EXISTS `nova_auth_db`;
CREATE DATABASE IF NOT EXISTS `nova_system_db`;
CREATE DATABASE IF NOT EXISTS `nova_ad_db`;

-- 권한 부여 (필요 시)
GRANT ALL PRIVILEGES ON *.* TO 'root'@'%';
FLUSH PRIVILEGES;