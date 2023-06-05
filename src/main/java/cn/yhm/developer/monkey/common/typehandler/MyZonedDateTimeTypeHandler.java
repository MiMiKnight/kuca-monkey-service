package cn.yhm.developer.monkey.common.typehandler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * 自定义日期类型处理程序
 *
 * @author victor2015yhm@gmail.com
 * @since 2023-05-25 23:04:56
 */
@MappedJdbcTypes(JdbcType.VARCHAR)
@MappedTypes(ZonedDateTime.class)
public class MyZonedDateTimeTypeHandler extends BaseTypeHandler<ZonedDateTime> {


    private final static String DATE_TIME_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss.SSSSSSSSS";

    private final static String DATABASE_TIMEZONE = "UTC";

    @Override
    public void setNonNullParameter(PreparedStatement ps, int index, ZonedDateTime dateTime,
                                    JdbcType jdbcType) throws SQLException {
        String dateTimeStr = zonedDateTime2dateTimeStr(dateTime);
        ps.setString(index, dateTimeStr);
    }

    @Override
    public ZonedDateTime getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String dateTimeStr = rs.getString(columnName);
        return dateTimeStr2ZonedDateTime(dateTimeStr);
    }

    @Override
    public ZonedDateTime getNullableResult(ResultSet rs, int index) throws SQLException {
        String dateTimeStr = rs.getString(index);
        return dateTimeStr2ZonedDateTime(dateTimeStr);
    }

    @Override
    public ZonedDateTime getNullableResult(CallableStatement cs, int index) throws SQLException {
        String dateTimeStr = cs.getString(index);
        return dateTimeStr2ZonedDateTime(dateTimeStr);
    }

    /**
     * ZonedDateTime转日期字符串
     *
     * @param dateTime 日期时间
     * @return {@link String}
     */
    private String zonedDateTime2dateTimeStr(ZonedDateTime dateTime) {
        if (null == dateTime) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT_PATTERN, Locale.ENGLISH);
        ZonedDateTime utcDateTime = dateTime.withZoneSameInstant(ZoneId.of(DATABASE_TIMEZONE));
        return utcDateTime.format(formatter);
    }

    /**
     * 日期时间字符串转ZonedDateTime
     *
     * @param dateTimeStr 日期时间字符串
     * @return {@link ZonedDateTime}
     */
    private ZonedDateTime dateTimeStr2ZonedDateTime(String dateTimeStr) {
        if (null == dateTimeStr) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT_PATTERN, Locale.ENGLISH);
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr, formatter);
        return ZonedDateTime.of(dateTime, ZoneId.of(DATABASE_TIMEZONE));
    }
}
