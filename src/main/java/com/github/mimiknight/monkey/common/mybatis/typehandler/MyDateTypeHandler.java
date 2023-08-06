package com.github.mimiknight.monkey.common.mybatis.typehandler;

import com.github.mimiknight.kuca.utils.constant.DateTimeFormatStandard;
import com.github.mimiknight.kuca.utils.constant.TimeZoneGMT;
import com.github.mimiknight.kuca.utils.service.utils.DateTimeUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 我日期类型处理程序
 * 自定义日期类型处理程序
 * <p>
 * jdbcType=VARCHAR或jdbcType=CHAR 才生效此TypeHandler
 * <p>
 * #{updatedTime,jdbcType=VARCHAR}
 *
 * @author MiMiKnight victor2015yhm@gmail.com
 * @date 2023-08-06 08:11:23
 * @since 0.0.1-SNAPSHOT
 */
@MappedTypes(value = {Date.class})
@MappedJdbcTypes(value = {JdbcType.VARCHAR, JdbcType.CHAR})
public class MyDateTypeHandler extends BaseTypeHandler<Date> {


    /**
     * 数据库日期格式
     * <p>
     * yyyy-MM-dd HH:mm:ss.SSS          此处时间精确到毫秒，对应数据库 datetime时间精确到3位
     * yyyy-MM-dd HH:mm:ss.SSSSSS       此处时间精确到微秒，对应数据库 datetime时间精确到6位
     * yyyy-MM-dd HH:mm:ss.SSSSSSSSS    此处时间精确到纳秒，对应数据库 datetime时间精确到9位
     */
    private static final String DATE_TIME_FORMAT_PATTERN = DateTimeFormatStandard.STANDARD_3;

    /**
     * 入数据库的日期默认时区
     */
    private static final String DATABASE_TIMEZONE = TimeZoneGMT.GMT;

    @Override
    public void setNonNullParameter(PreparedStatement ps, int index, Date dateTime,
                                    JdbcType jdbcType) throws SQLException {
        String dateTimeStr = date2dateTimeStr(dateTime);
        ps.setString(index, dateTimeStr);
    }

    @Override
    public Date getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String dateTimeStr = rs.getString(columnName);
        return dateTimeStr2Date(dateTimeStr);
    }

    @Override
    public Date getNullableResult(ResultSet rs, int index) throws SQLException {
        String dateTimeStr = rs.getString(index);
        return dateTimeStr2Date(dateTimeStr);
    }

    @Override
    public Date getNullableResult(CallableStatement cs, int index) throws SQLException {
        String dateTimeStr = cs.getString(index);
        return dateTimeStr2Date(dateTimeStr);
    }

    /**
     * Date转日期字符串
     *
     * @param dateTime 日期时间
     * @return {@link String}
     */
    private String date2dateTimeStr(Date dateTime) {
        if (null == dateTime) {
            return null;
        }
        Instant instant = dateTime.toInstant();
        LocalDateTime utcDateTime = LocalDateTime.ofInstant(instant, ZoneId.of(DATABASE_TIMEZONE));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT_PATTERN, Locale.ENGLISH);
        return utcDateTime.format(formatter);
    }

    /**
     * 日期时间字符串转Date
     *
     * @param dateTimeStr 日期时间字符串
     * @return {@link ZonedDateTime}
     */
    private Date dateTimeStr2Date(String dateTimeStr) {
        if (null == dateTimeStr) {
            return null;
        }
        TimeZone zone = TimeZone.getTimeZone(DATABASE_TIMEZONE);
        return DateTimeUtils.toDate(dateTimeStr, DATE_TIME_FORMAT_PATTERN, zone, true, Locale.ENGLISH);
    }
}
