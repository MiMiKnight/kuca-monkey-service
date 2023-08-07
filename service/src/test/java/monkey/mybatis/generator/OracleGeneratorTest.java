package monkey.mybatis.generator;

import com.github.mimiknight.monkey.Application;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.converts.OracleTypeConvert;
import com.baomidou.mybatisplus.generator.config.querys.OracleQuery;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest(classes = {Application.class})
public class OracleGeneratorTest extends BaseGeneratorTest {

    /**
     * 数据库配置
     *
     * @return {@link DataSourceConfig}
     */
    private static DataSourceConfig.Builder buildDataSourceConfig() {
        String url = "jdbc:oracle:thin:@xxxx:1521:helowin";
        String username = "system";
        String password = "system";
        String schema = "db_monkey";
        return new DataSourceConfig.Builder(url, username, password)
                .dbQuery(new OracleQuery())
                .schema(schema)
                .typeConvert(new OracleTypeConvert());
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
