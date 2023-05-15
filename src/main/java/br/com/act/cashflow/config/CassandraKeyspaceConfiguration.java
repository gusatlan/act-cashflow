package br.com.act.cashflow.config;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.CqlSessionBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.cassandra.CassandraAutoConfiguration;
import org.springframework.boot.autoconfigure.cassandra.CassandraProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.core.cql.generator.CreateKeyspaceCqlGenerator;
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification;

import javax.annotation.PostConstruct;

@Configuration
@ConditionalOnClass(CqlSession.class)
@AutoConfigureBefore(CassandraAutoConfiguration.class)
public class CassandraKeyspaceConfiguration {
    private CqlSessionBuilder cqlSessionBuilder;
    private CassandraProperties properties;
    private Logger logger;

    public CassandraKeyspaceConfiguration(
            final CqlSessionBuilder cqlSessionBuilder,
            final CassandraProperties properties
    ) {
        this.cqlSessionBuilder = cqlSessionBuilder;
        this.properties = properties;
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    @PostConstruct
    public void generateKeyspaceName() {
        final String keyspaceName = properties.getKeyspaceName();

        try (CqlSession session = cqlSessionBuilder.withKeyspace((CqlIdentifier) null).build()) {
            session.execute(
                    CreateKeyspaceCqlGenerator.toCql(
                            CreateKeyspaceSpecification.createKeyspace(keyspaceName).ifNotExists()
                    )
            );
            logger.debug("Keyspace {} created", keyspaceName);
        } catch (Exception e) {
            logger.error("generateKeyspaceName: Error on generate keyspace: ", e);
        }
    }
}
