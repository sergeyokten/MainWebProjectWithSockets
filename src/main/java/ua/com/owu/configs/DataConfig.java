package ua.com.owu.configs;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableJpaRepositories("ua.com.owu.dao")
@EnableTransactionManagement
@PropertySource("classpath:db.properties")
public class DataConfig {
    @Autowired
    private Environment env;

    @Bean
    public DataSource dataSource() {
      HikariConfig hikariConfig = new HikariConfig();
           hikariConfig.setDriverClassName("com.mysql.jdbc.Driver");
           hikariConfig.setJdbcUrl("jdbc:mysql://localhost:3306/main");
           hikariConfig.setUsername("root");
           hikariConfig.setPassword("root");

           hikariConfig.setMaximumPoolSize(5);
           hikariConfig.setConnectionTestQuery("SELECT 1");
           hikariConfig.setPoolName("springHikariCP");

           hikariConfig.addDataSourceProperty("dataSource.cachePrepStmts", "true");
           hikariConfig.addDataSourceProperty("dataSource.prepStmtCacheSize", "250");
           hikariConfig.addDataSourceProperty("dataSource.prepStmtCacheSqlLimit", "2048");
           hikariConfig.addDataSourceProperty("dataSource.useServerPrepStmts", "true");

           HikariDataSource dataSource = new HikariDataSource(hikariConfig);
        return dataSource;
    }

    @Bean
    public JpaVendorAdapter vendorAdapter() {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setDatabase(Database.MYSQL);
        vendorAdapter.setShowSql(true);
        return vendorAdapter;
    }

    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean factoryBean() {
        LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();
        bean.setDataSource(dataSource());
        bean.setPackagesToScan("ua.com.owu.entity");
        bean.setJpaVendorAdapter(vendorAdapter());
        Properties properties = new Properties();
        properties.setProperty(env.getProperty("db.hbm2ddl"), env.getProperty("db.hbm2ddl.val"));
        bean.setJpaProperties(properties);
        return bean;

    }

    @Bean
    public JpaTransactionManager transactionManager(EntityManagerFactory factory) {
        JpaTransactionManager manager = new JpaTransactionManager(factory);
        return manager;
    }


}
