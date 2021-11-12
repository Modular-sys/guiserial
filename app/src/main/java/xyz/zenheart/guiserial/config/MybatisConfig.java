package xyz.zenheart.guiserial.config;

import com.zaxxer.hikari.HikariDataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;

/**
 * <p>项目名称: cgenerator </p>
 * <p>描述: mybatis配置 </p>
 * <p>创建时间: 2021/9/2 </p>
 * <p>公司信息: 维之星研发部</p>
 *
 * @author CKM
 * @version v1.0
 */
@Configuration
public class MybatisConfig {

    @Qualifier("pgsqlDataSource")
    @Bean(name = "pgsqlDataSource")
    public HikariDataSource pgsqlDataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setJdbcUrl("jdbc:postgresql://192.168.1.138:5432/postgres?currentSchema=elevator&useUnicode=true&characterEncoding=UTF-8&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false");
        dataSource.setUsername("postgres");
        dataSource.setPassword("123456");
        return dataSource;
    }

    @Qualifier("pgsqlSqlSessionFactoryBean")
    @Bean(name = "pgsqlSqlSessionFactoryBean")
    public SqlSessionFactoryBean pgsqlSqlSessionFactoryBean() throws IOException {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        /*Mybatis的参数配置*/
//        sqlSessionFactoryBean.setConfigLocation(new ClassPathResource("config/mybatis-config.xml"));
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        /*启用Mybatis的全部xml文件，就不需要一个个去打开*/
        String packageSearchPath = PathMatchingResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + "/mapper/pgsql/**.xml";
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources(packageSearchPath));
        sqlSessionFactoryBean.setDataSource(pgsqlDataSource());
        /*实体类所在的包*/
        sqlSessionFactoryBean.setTypeAliasesPackage("com.robot.generator.app.pojo.*.entity");
        return sqlSessionFactoryBean;
    }

    @Qualifier("pgsqlMapperScannerConfigurer")
    @Bean(name = "pgsqlMapperScannerConfigurer")
    public static MapperScannerConfigurer pgsqlMapperScannerConfigurer(){
        MapperScannerConfigurer configurer = new MapperScannerConfigurer();
        configurer.setBasePackage("com.robot.generator.app.modules.pgsql.mapper");
        configurer.setSqlSessionFactoryBeanName("pgsqlSqlSessionFactoryBean");
        return configurer;
    }

    @Qualifier("mysqlDataSource")
    @Bean(name = "mysqlDataSource")
    public HikariDataSource mysqlDataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setJdbcUrl("jdbc:mysql://192.168.1.138:3306/mysql?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai");
        dataSource.setUsername("root");
        dataSource.setPassword("123456");
        return dataSource;
    }

    @Qualifier("mysqlSqlSessionFactoryBean")
    @Bean(name = "mysqlSqlSessionFactoryBean")
    public SqlSessionFactoryBean mysqlSqlSessionFactoryBean() throws IOException {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        /*Mybatis的参数配置*/
//        sqlSessionFactoryBean.setConfigLocation(new ClassPathResource("config/mybatis-config.xml"));
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        /*启用Mybatis的全部xml文件，就不需要一个个去打开*/
        String packageSearchPath = PathMatchingResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + "/mapper/mysql/**.xml";
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources(packageSearchPath));
        sqlSessionFactoryBean.setDataSource(mysqlDataSource());
        /*实体类所在的包*/
        sqlSessionFactoryBean.setTypeAliasesPackage("com.robot.generator.app.pojo.*.entity");
        return sqlSessionFactoryBean;
    }

    @Qualifier("mysqlMapperScannerConfigurer")
    @Bean(name = "mysqlMapperScannerConfigurer")
    public static MapperScannerConfigurer mysqlMapperScannerConfigurer(){
        MapperScannerConfigurer configurer = new MapperScannerConfigurer();
        configurer.setBasePackage("com.robot.generator.app.modules.mysql.mapper");
        configurer.setSqlSessionFactoryBeanName("mysqlSqlSessionFactoryBean");
        return configurer;
    }

    @Primary
    @Qualifier("localeDataSource")
    @Bean(name = "localeDataSource")
    public HikariDataSource localeDataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName("org.sqlite.JDBC");
        dataSource.setJdbcUrl("jdbc:sqlite:D:\\BackUps\\database\\locale.db");
        return dataSource;
    }

    @Primary
    @Qualifier("localeSqlSessionFactoryBean")
    @Bean(name = "localeSqlSessionFactoryBean")
    public SqlSessionFactoryBean localeSqlSessionFactoryBean() throws IOException {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        /*Mybatis的参数配置*/
//        sqlSessionFactoryBean.setConfigLocation(new ClassPathResource("config/mybatis-config.xml"));
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        /*启用Mybatis的全部xml文件，就不需要一个个去打开*/
        String packageSearchPath = PathMatchingResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + "/mapper/locale/**.xml";
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources(packageSearchPath));
        sqlSessionFactoryBean.setDataSource(localeDataSource());
        /*实体类所在的包*/
        sqlSessionFactoryBean.setTypeAliasesPackage("com.robot.generator.app.pojo.*.entity");
        return sqlSessionFactoryBean;
    }

    @Primary
    @Qualifier("localeMapperScannerConfigurer")
    @Bean(name = "localeMapperScannerConfigurer")
    public static MapperScannerConfigurer localeMapperScannerConfigurer(){
        MapperScannerConfigurer configurer = new MapperScannerConfigurer();
        configurer.setBasePackage("com.robot.generator.app.modules.locale.mapper");
        configurer.setSqlSessionFactoryBeanName("localeSqlSessionFactoryBean");
        return configurer;
    }
}
