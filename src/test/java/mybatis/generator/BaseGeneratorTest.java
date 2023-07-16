package mybatis.generator;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.config.TemplateType;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.fill.Property;

import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Consumer;

/**
 * 基础生成器测试类
 *
 * @author victor2015yhm@gmail.com
 * @since 2023-06-04 10:24:50
 */
public class BaseGeneratorTest {


    /**
     * 全局配置
     */
    protected static final Consumer<GlobalConfig.Builder> BUILD_GLOBAL_CONFIG = (builder) -> {
        // 当前项目文件夹
        String projectPath = System.getProperty("user.dir");
        String outputDir = projectPath + "/.code-generate/";
        String author = "victor2015yhm@gmail.com";
        String commentDateFormat = "yyyy-MM-dd HH:mm:ss";
        builder.outputDir(outputDir)
                .disableOpenDir()
                .author(author)
//              .enableKotlin()
//              .enableSwagger()
                .dateType(DateType.TIME_PACK)
                .commentDate(commentDateFormat);
    };

    /**
     * 包配置
     */
    protected static final Consumer<PackageConfig.Builder> BUILD_PACKAGE_CONFIG = (builder) -> {
        // 当前项目文件夹
        String projectPath = System.getProperty("user.dir");
        String xmlOutputDir = projectPath + "/.code-generate/mapperXml";

        String parent = "cn.yhm.developer";
        String moduleName = "monkey";
        String entityPackageName = "model.entity";
        String servicePackageName = "service.standard";
        String serviceImplPackageName = "service.impl";
        String mapperPackageName = "mapper";
        String mapperXmlPackageName = "mapper";
        String controllerPackageName = "controller";

        builder.parent(parent)
                .moduleName(moduleName)
                .entity(entityPackageName)
                .service(servicePackageName)
                .serviceImpl(serviceImplPackageName)
                .mapper(mapperPackageName)
                .xml(mapperXmlPackageName)
                .controller(controllerPackageName)
                .pathInfo(Collections.singletonMap(OutputFile.xml, xmlOutputDir));
    };

    /**
     * 策略配置
     * <p>
     * Entity 策略配置
     * <p>
     * Mapper 策略配置
     * <p>
     * Service 策略配置
     * <p>
     * Controller 策略配置
     */
    protected static final Consumer<StrategyConfig.Builder> BUILD_STRATEGY_CONFIG = (builder) -> {
        // 数据库表集合
        ArrayList<String> dbTableList = new ArrayList<>();
        dbTableList.add("t_monkey_article");

        builder.enableCapitalMode()
                .enableSkipView() // 跳过视图
                .disableSqlFilter() // 禁用过滤
                .addInclude(dbTableList) // 数据表匹配
                .addTablePrefix("t_monkey_"); // 表前缀
        // 构建Entity
        builder.entityBuilder()
                .formatFileName("%sEntity")
                .idType(IdType.ASSIGN_ID)
                .enableLombok() // 开启Lombok注解
                .enableRemoveIsPrefix() // 删除is前缀
                .enableTableFieldAnnotation() // 开启@TableField注解
                .enableActiveRecord()
                .enableColumnConstant() // 开启字段名称常量
                .versionPropertyName("version") // 乐观锁字段属性名称
                .logicDeletePropertyName("deleted") // 逻辑删除字段属性名称
                .columnNaming(NamingStrategy.underline_to_camel)
                .addTableFills(new Property("createdTime", FieldFill.INSERT))
                .addTableFills(new Property("updatedTime", FieldFill.INSERT_UPDATE));
        // 构建 Mapper
        builder.mapperBuilder()
                .formatMapperFileName("%sMapper")
                .formatXmlFileName("%sMapper")
                .superClass(BaseMapper.class)
                .enableMapperAnnotation()
                .enableBaseResultMap()
                .enableBaseColumnList();
        // 构建 Service
        builder.serviceBuilder()
                .formatServiceFileName("%sService")
                .formatServiceImplFileName("%sServiceImpl");
        // 构建 Controller
        builder.controllerBuilder()
                .formatFileName("%sController")
                .enableHyphenStyle() // 开启驼峰命名
                .enableRestStyle()
                .build();
    };

    /**
     * 模板配置
     */
    protected static final Consumer<TemplateConfig.Builder> BUILD_TEMPLATE_CONFIG = (builder) -> {
        builder.disable(TemplateType.ENTITY)
                .entity("templates/entity.java.vm")
                .service("templates/service.java.vm")
                .serviceImpl("templates/serviceImpl.java.vm")
                .mapper("templates/mapper.java.vm")
                .xml("templates/mapper.xml.vm")
                .controller("templates/controller.java.vm")
                .build();
    };


    /**
     * 注入配置
     */
    protected static final Consumer<InjectionConfig.Builder> BUILD_INJECTION_CONFIG = (builder) -> {
        builder.beforeOutputFile((tableInfo, objectMap) -> {
                    System.out.println("tableInfo: " + tableInfo.getEntityName() + " objectMap: " + objectMap.size());
                })
                .customMap(Collections.singletonMap("test", "baomidou"))
                .customFile(Collections.singletonMap("test.txt", "/templates/test.vm"))
                .build();
    };
}