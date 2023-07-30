package com.github.mimiknight.monkey.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.mimiknight.kuca.ecology.model.response.EcologyResponse;
import com.github.mimiknight.kuca.utils.constant.DateTimeFormatStandard;
import com.github.mimiknight.kuca.utils.constant.TimeZoneGMT;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.ZonedDateTime;

/**
 * 查询文章响应参数
 *
 * @author victor2015yhm@gmail.com
 * @since 2023-05-07 16:22:42
 */
@Accessors(chain = true)
@Data
public class QueryArticleByIdResponse implements EcologyResponse {

    /**
     * 主键
     */
    @JsonProperty(value = "id", index = 1)
    private String id;

    /**
     * 文章标题
     */
    @JsonProperty(value = "title", index = 2)
    private String title;

    /**
     * 文章内容
     */
    @JsonProperty(value = "article", index = 3)
    private String article;

    /**
     * 逻辑删除
     */
    @JsonProperty(value = "deleted", index = 5)
    private Integer deleted;

    /**
     * 创建时间
     * <p>
     * 格式：
     * <p>
     * 24小时制 "年-月-日 时:分:秒.毫秒 时区"
     * <p>
     * 案例：
     * <p>
     * 2022-09-04 10:06:39.123 +08:00 【表示 东八区 2022年9月4日10点6分39秒123毫秒】
     */
    @JsonFormat(pattern = DateTimeFormatStandard.STANDARD_6, timezone = TimeZoneGMT.GMT)
    @JsonProperty(value = "created_time", index = 6)
    private ZonedDateTime createdTime;

    /**
     * 更新时间
     * <p>
     * 格式：
     * <p>
     * 24小时制 "年-月-日 时:分:秒.毫秒 时区"
     * <p>
     * 案例：
     * <p>
     * 2022-09-04 10:06:39.123 +08:00 【表示 东八区 2022年9月4日10点6分39秒123毫秒】
     */
    @JsonFormat(pattern = DateTimeFormatStandard.STANDARD_6, timezone = TimeZoneGMT.GMT)
    @JsonProperty(value = "updated_time", index = 7)
    private ZonedDateTime updatedTime;

}
