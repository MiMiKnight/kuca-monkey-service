package cn.yhm.developer.monkey.model.entity;

import cn.yhm.developer.monkey.common.mybatis.typehandler.MyZonedDateTimeTypeHandler;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * <p>
 * 文章表
 * </p>
 *
 * @author victor2015yhm@gmail.com
 * @since 2023-06-05 06:42:47
 */
@Getter
@Setter
@TableName("t_monkey_article")
public class ArticleEntity extends Model<ArticleEntity> {

    private static final long serialVersionUID = 123080239902732129L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 标题
     */
    @TableField("title")
    private String title;

    /**
     * 文章内容
     */
    @TableField("article")
    private String article;

    /**
     * 乐观锁
     */
    @TableField("version")
    @Version
    private Integer version;

    /**
     * 逻辑删除
     */
    @TableField("deleted")
    @TableLogic
    private Integer deleted;

    /**
     * 创建时间
     */
    @TableField(value = "created_time", fill = FieldFill.INSERT, typeHandler = MyZonedDateTimeTypeHandler.class)
    private ZonedDateTime createdTime;

    /**
     * 更新时间
     */
    @TableField(value = "updated_time", fill = FieldFill.INSERT_UPDATE, typeHandler = MyZonedDateTimeTypeHandler.class)
    private ZonedDateTime updatedTime;

    public static final String ID = "id";

    public static final String TITLE = "title";

    public static final String ARTICLE = "article";

    public static final String VERSION = "version";

    public static final String DELETED = "deleted";

    public static final String CREATED_TIME = "created_time";

    public static final String UPDATED_TIME = "updated_time";

    @Override
    public Serializable pkVal() {
        return this.id;
    }


}
