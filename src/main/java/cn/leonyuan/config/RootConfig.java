package cn.leonyuan.config;

import java.util.Properties;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;
import redis.clients.jedis.JedisPoolConfig;

@Configuration

@ComponentScan(value= "cn.*", includeFilters= {@Filter(type = FilterType.ANNOTATION, value ={Service.class})})

@EnableTransactionManagement

public class RootConfig implements TransactionManagementConfigurer {
	
	private DataSource dataSource = null;
	

	@Bean(name = "dataSource")
	public DataSource initDataSource() {
		if (dataSource != null) {
			return dataSource;
		}
		Properties props = new Properties();
		props.setProperty("driverClassName", "com.mysql.jdbc.Driver");
		props.setProperty("url", "jdbc:mysql://localhost:3306/RedPacket");
		props.setProperty("username", "root");
		props.setProperty("password", "root");
       props.setProperty("maxActive", "200");
		props.setProperty("maxIdle", "20");
		props.setProperty("maxWait", "30000");
		try {
			dataSource = BasicDataSourceFactory.createDataSource(props);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dataSource;
	}

	@Bean(name="sqlSessionFactory")
	public SqlSessionFactoryBean initSqlSessionFactory() {
		SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
		sqlSessionFactory.setDataSource(initDataSource());

		Resource resource = new ClassPathResource("mybatis/mybatis-cfg.xml");
		sqlSessionFactory.setConfigLocation(resource);
		return sqlSessionFactory;
	}

	@Bean 
	public MapperScannerConfigurer initMapperScannerConfigurer() {
		MapperScannerConfigurer msc = new MapperScannerConfigurer();
		msc.setBasePackage("cn.*");
		msc.setSqlSessionFactoryBeanName("sqlSessionFactory");
		msc.setAnnotationClass(Repository.class);
		return msc;
	}

	@Override
	@Bean(name="annotationDrivenTransactionManager")
	public PlatformTransactionManager annotationDrivenTransactionManager() {
		DataSourceTransactionManager transactionManager = 
           new DataSourceTransactionManager();
		transactionManager.setDataSource(initDataSource());
		return transactionManager;
	}
	@Bean(name = "redisTemplate")
	public RedisTemplate initRedisTemplate() {
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		//最大空闲数
		poolConfig.setMaxIdle(50);
		//最大连接数
		poolConfig.setMaxTotal(100);
		//最大等待毫秒数
		poolConfig.setMaxWaitMillis(20000);
		//创建Jedis链接工厂
		JedisConnectionFactory connectionFactory = new JedisConnectionFactory(poolConfig);
		connectionFactory.setHostName("localhost");
		connectionFactory.setPort(6379);
		//调用后初始化方法，没有它将抛出异常
		connectionFactory.afterPropertiesSet();
		//自定Redis序列化器
		RedisSerializer jdkSerializationRedisSerializer = new JdkSerializationRedisSerializer();
		RedisSerializer stringRedisSerializer = new StringRedisSerializer();
		//定义RedisTemplate，并设置连接工程[修改为：工厂]
		RedisTemplate redisTemplate = new RedisTemplate();
		redisTemplate.setConnectionFactory(connectionFactory);
		//设置序列化器
		redisTemplate.setDefaultSerializer(stringRedisSerializer);
		redisTemplate.setKeySerializer(stringRedisSerializer);
		redisTemplate.setValueSerializer(stringRedisSerializer);
		redisTemplate.setHashKeySerializer(stringRedisSerializer);
		redisTemplate.setHashValueSerializer(stringRedisSerializer);
		return redisTemplate;
	}


}