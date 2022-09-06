package io.github.zam0k.simplifiedpsp.containers;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.testcontainers.containers.MySQLContainer;

public class ContainerInit implements
            ApplicationContextInitializer<ConfigurableApplicationContext> {

        public static MySQL mysql;

        static {
            mysql = MySQL.getInstance();
            mysql.start();
        }

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
                    applicationContext,
                    "spring.datasource.url=" + mysql.getJdbcUrl(),
                    "spring.datasource.username=" + mysql.getUsername(),
                    "spring.datasource.password=" + mysql.getPassword(),
                    "db.host=" + mysql.getHost(),
                    "db.port=" + mysql.getMappedPort(MySQLContainer.MYSQL_PORT),
                    "db.name=" + mysql.getDatabaseName(),
                    "db.username=" + mysql.getUsername(),
                    "db.password=" + mysql.getPassword()
            );
        }
}
