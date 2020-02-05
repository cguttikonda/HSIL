package com.ezc.hsil.webapp.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.HealthIndicatorRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;


//import com.ezc.hsil.webapp.model
@Configuration
@EnableTransactionManagement
@PropertySource({ "classpath:persistence.properties" })
@ComponentScan({ "com.ezc.hsil.webapp.persistance" })
@EnableJpaRepositories(basePackages = "com.ezc.hsil.webapp.persistance.dao")
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class PersistenceJPAConfig {

	@Autowired
	private Environment env;
 
	public PersistenceJPAConfig() {
		super();
	}

	//

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(dataSource());
		em.setPackagesToScan(new String[] { "com.ezc.hsil.webapp.model" });
		final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		em.setJpaVendorAdapter(vendorAdapter);
		em.setJpaProperties(additionalProperties());
		return em;
	}

	@Bean
	public DataSource dataSource() {
		//final DriverManagerDataSource dataSource = new DriverManagerDataSource();
		
		
		
		HikariDataSource dataSource = new HikariDataSource();
		HikariConfig config = new HikariConfig();
	
		dataSource.setDriverClassName(env.getProperty("jdbc.driverClassName"));
		dataSource.setJdbcUrl(env.getProperty("jdbc.url"));
		dataSource.setUsername(env.getProperty("jdbc.user"));
		dataSource.setPassword(env.getProperty("jdbc.pass"));
		
		dataSource.setPoolName("HSIL_POOL");
		dataSource.setMaximumPoolSize(10);
		dataSource.setAutoCommit(true);
		dataSource.setCatalog("HSIL");
		dataSource.setConnectionTimeout(Integer.parseInt(env.getProperty("hikari.connection-timeout")));
		dataSource.setMaximumPoolSize(2);
		dataSource.setMinimumIdle(Integer.parseInt(env.getProperty("hikari.minimum-idle")));
		dataSource.setIdleTimeout(Integer.parseInt(env.getProperty("hikari.idle-timeout")));
		dataSource.setMaxLifetime(Integer.parseInt(env.getProperty("hikari.max-lifetime")));
		
		dataSource.addDataSourceProperty("dataSource.cachePrepStmts", true);
		dataSource.addDataSourceProperty("dataSource.prepStmtCacheSize", 250);
		dataSource.addDataSourceProperty("dataSource.prepStmtCacheSqlLimit", 2048);
		dataSource.addDataSourceProperty("dataSource.useServerPrepStmts", true);
		dataSource.addDataSourceProperty("dataSource.useLocalSessionState", true);
		dataSource.addDataSourceProperty("dataSource.rewriteBatchedStatements", true);
		dataSource.addDataSourceProperty("dataSource.cacheResultSetMetadata", true);
		dataSource.addDataSourceProperty("dataSource.cacheServerConfiguration", true);
		dataSource.addDataSourceProperty("dataSource.elideSetAutoCommits", true);
		dataSource.addDataSourceProperty("dataSource.maintainTimeStats", false);
		
		//config.setDataSource(dataSource);
		//config.setMetricRegistry(new MetricRegistry());
	
		return dataSource;
	}

//	@Bean
//	public JpaTransactionManager transactionManager() {
//		final JpaTransactionManager transactionManager = new JpaTransactionManager();
//		transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
//		return transactionManager;
//	}
//
//	@Bean
//	public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
//		return new PersistenceExceptionTranslationPostProcessor();
//	}

	protected Properties additionalProperties() {
		final Properties hibernateProperties = new Properties();
		hibernateProperties.setProperty("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
		hibernateProperties.setProperty("hibernate.dialect", env.getProperty("hibernate.dialect"));
		hibernateProperties.setProperty("hibernate.current_session_context_class",
				env.getProperty("hibernate.current_session_context_class"));
		hibernateProperties.setProperty("hibernate.format_sql", env.getProperty("hibernate.format_sql"));
		hibernateProperties.setProperty("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
		return hibernateProperties;
	}
	
	@Bean
    public AuditorAware<String> auditorAware() {
        return new AuditorAwareImpl(); 
    }

}
