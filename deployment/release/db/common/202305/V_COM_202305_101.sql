/**
 * 目的：新增表字段存储过程
 */
DROP PROCEDURE IF EXISTS `proc_add_column`;
COMMIT;

CREATE PROCEDURE `proc_add_column`(IN `var_table_name` VARCHAR(128),
                                   IN `var_column_name` VARCHAR(128),
                                   IN `var_sql_cmd` VARCHAR(512))
BEGIN
    SET @p_column_exist = 0;
    SET @p_table_name = var_table_name;
    SET @p_column_name = var_column_name;

    /*查询指定字段是否存在的SQL语句*/
    SET @p_sql_cmd = 'SELECT COUNT(*) > 0 INTO @p_column_exist
	FROM information_schema.COLUMNS
	WHERE TABLE_SCHEMA = database()
	AND TABLE_NAME = ?
	AND COLUMN_NAME = ?;';

    /*预编译SQL*/
    PREPARE stmt FROM @p_sql_cmd;
    /*执行SQL 查询指定字段是否存在*/
    EXECUTE stmt USING @p_table_name,@p_column_name;

    IF (@p_column_exist > 0) THEN
        SELECT 'the column has exist' AS tip;
    ELSE
        SET @p_sql_cmd = var_sql_cmd;
        PREPARE stmt FROM @p_sql_cmd;
        EXECUTE stmt;
        SELECT 'Add column success' AS tip;
    END IF;

    /*释放数据库连接*/
    DEALLOCATE PREPARE stmt;
END;
COMMIT;