/**
 * 目的：新增索引存储过程
 */
DROP PROCEDURE IF EXISTS `proc_add_index`;
COMMIT;

CREATE PROCEDURE `proc_add_index`(IN `var_table_name` VARCHAR(128),
                                  IN `var_index_name` VARCHAR(128),
                                  IN `var_sql_cmd` VARCHAR(512))
BEGIN
    SET @p_index_exist = 0;
    SET @p_table_name = var_table_name;
    SET @p_index_name = var_index_name;

    /*查询指定索引是否存在的SQL语句*/
    SET @p_sql_cmd = 'SELECT COUNT(*) > 0 INTO @p_index_exist
	FROM information_schema.STATISTICS
	WHERE table_schema = database()
	AND TABLE_NAME = ?
	AND INDEX_NAME = ?;';

    /*预编译SQL*/
    PREPARE stmt FROM @p_sql_cmd;
    /*执行SQL 查询指定索引是否存在*/
    EXECUTE stmt USING @p_table_name,@p_index_name;

    IF (@p_index_exist > 0) THEN
        SELECT 'the index has exist' AS tip;
    ELSE
        SET @p_sql_cmd = var_sql_cmd;
        PREPARE stmt FROM @p_sql_cmd;
        EXECUTE stmt;
        SELECT 'Add index success' AS tip;
    END IF;

    /*释放数据库连接*/
    DEALLOCATE PREPARE stmt;
END;
COMMIT;