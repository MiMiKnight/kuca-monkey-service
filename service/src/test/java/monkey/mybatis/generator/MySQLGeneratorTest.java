package monkey.mybatis.generator;

import com.github.mimiknight.monkey.Application;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.querys.MySqlQuery;
import com.baomidou.mybatisplus.generator.keywords.MySqlKeyWordsHandler;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest(classes = {Application.class})
public class MySQLGeneratorTest extends BaseGeneratorTest {

    /**
     * 数据库配置
     *
     * @return {@link DataSourceConfig}
     */
    private static DataSourceConfig.Builder buildDataSourceConfig() {
        String url = "jdbc:mysql://127.0.0.1:3306/db_monkey";
        String username = "root";
        String password = "123456";
        String schema = "db_monkey";
        return new DataSourceConfig.Builder(url, username, password)
                .dbQuery(new MySqlQuery())
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
//                .templateConfig(BUILD_TEMPLATE_CONFIG)
//                .injectionConfig(BUILD_INJECTION_CONFIG)
                .execute();
    }
}
