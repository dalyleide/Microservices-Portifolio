CREATE TABLE `votes` (
                            `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'unique identifier',
                            `document` VARCHAR(11) NOT NULL COMMENT 'cooperate document',
                            `vote` INT(1) NOT NULL COMMENT '1 - YES, 2 - NO',
                            `register_date` TIMESTAMP NULL COMMENT 'date register on poll',
                            `id_schedule` BIGINT NOT NULL COMMENT 'identifier a shedule',
                            PRIMARY KEY (`id`),
                            CONSTRAINT `fk_schedule` FOREIGN KEY (`id_schedule`) REFERENCES `schedule` (`id`),
                            CONSTRAINT `uc_schedule_document` UNIQUE (`id_schedule`, `document` )
)
COMMENT='this table maintains the votes of a schedule';
