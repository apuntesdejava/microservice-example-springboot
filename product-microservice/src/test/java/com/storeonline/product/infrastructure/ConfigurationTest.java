/*
 * Copyright 2024 Diego Silva (diego.silva at apuntesdejava.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.storeonline.product.infrastructure;

import io.r2dbc.spi.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.r2dbc.connection.init.CompositeDatabasePopulator;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;

/**
 *
 * @author Diego Silva (diego.silva at apuntesdejava.com)
 */
@Configuration
public class ConfigurationTest {

    @Bean
    @Profile("h2")
    public ConnectionFactoryInitializer initializerH2(ConnectionFactory connectionFactory) {
        return initializer(connectionFactory, "schema-h2.sql");
    }

    @Bean
    @Profile("mysql")
    public ConnectionFactoryInitializer initializerMySql(ConnectionFactory connectionFactory) {
        return initializer(connectionFactory, "schema-mysql.sql");
    }

    ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory, String schemaFile) {

        var initializer = new ConnectionFactoryInitializer();
        initializer.setConnectionFactory(connectionFactory);

        var populator = new CompositeDatabasePopulator();
        populator.addPopulators(new ResourceDatabasePopulator(new ClassPathResource(schemaFile)));
        initializer.setDatabasePopulator(populator);

        return initializer;
    }

}
