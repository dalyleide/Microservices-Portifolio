CREATE TABLE `schedule` (
                            `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'unique identifier',
                            `title` VARCHAR(50) NOT NULL COMMENT 'briefy purpose',
                            `description` VARCHAR(200) NOT NULL COMMENT 'details the agenda',
                            `status` INT(1) NOT NULL COMMENT '1 - OPEN, 2 - CLOSED, 3 - CANCELED',
                            `open_date` TIMESTAMP NULL COMMENT 'poll opening date',
                            `expiration_date` TIMESTAMP NULL COMMENT 'end date poll',
                            PRIMARY KEY (`id`)
)
    COMMENT='this table maintains a schedule submitted for voting at the assembly';