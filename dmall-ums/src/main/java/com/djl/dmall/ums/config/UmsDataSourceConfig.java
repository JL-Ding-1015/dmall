package com.djl.dmall.ums.config;

import io.shardingjdbc.core.api.MasterSlaveDataSourceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

@Configuration
public class UmsDataSourceConfig {

    @Bean
    public DataSource dataSource() throws IOException, SQLException {
        File file = ResourceUtils.getFile("classpath:sharding-jdbc.yaml");
        return MasterSlaveDataSourceFactory.createDataSource(file);
    }
}
