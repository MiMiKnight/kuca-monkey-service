package mybatis.generator;

import cn.yhm.developer.monkey.Application;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.querys.MariadbQuery;
import com.baomidou.mybatisplus.generator.keywords.MySqlKeyWordsHandler;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


@Slf4j
@SpringBootTest(classes = {Application.class})
public class MariaDBGeneratorTest extends BaseGeneratorTest {

    /**
     * 数据库配置
     *
     * @return {@link DataSourceConfig}
     */
    private static DataSourceConfig.Builder buildDataSourceConfig() {
        String schema = "db_monkey";
        String url = "jdbc:mariadb://127.0.0.1:3306/";
        String username = "root";
        String password = "123456";
        return new DataSourceConfig.Builder(url + schema, username, password)
                .dbQuery(new MariadbQuery())
                .schema(schema)
                .typeConvert(new MySqlTypeConvert())
                .keyWordsHandler(new MySqlKeyWordsHandler());
    }


    @Test
    public void execute() {
        FastAutoGenerator.create(buildDataSourceConfig())
                .globalConfig(BUILD_GLOBAL_CONFIG)
                .packageConfig(BUILD_PACKAGE_CONFIG)
                .strategyConfig(BUILD_STRATEGY_CONFIG)
                .templateConfig(BUILD_TEMPLATE_CONFIG)
//                .injectionConfig(BUILD_INJECTION_CONFIG)
                .execute();
    }
}
