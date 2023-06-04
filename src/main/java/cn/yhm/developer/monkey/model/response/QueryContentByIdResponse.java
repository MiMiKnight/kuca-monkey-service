package cn.yhm.developer.monkey.model.response;

import cn.yhm.developer.kuca.ecology.model.response.EcologyResponse;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

/**
 * 保存内容响应参数
 *
 * @author victor2015yhm@gmail.com
 * @since 2023-05-07 16:22:42
 */
@Getter
@Setter
public class QueryContentByIdResponse implements EcologyResponse {

    /**
     * 主键
     */
    @JsonProperty(value = "id", index = 1)
    private String id;

    /**
     * 内容
     */
    @JsonProperty(value = "content", index = 2)
    private String content;

    /**
     * 乐观锁
     */
    @JsonProperty(value = "version", index = 3)
    private Integer version;

    /**
     * 逻辑删除
     */
    @JsonProperty(value = "deleted", index = 4)
    private Integer deleted;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS z", timezone = "UTC")
    @JsonProperty(value = "create_time", index = 5)
    private ZonedDateTime createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS z", timezone = "UTC")
    @JsonProperty(value = "update_time", index = 6)
    private ZonedDateTime updateTime;

}
