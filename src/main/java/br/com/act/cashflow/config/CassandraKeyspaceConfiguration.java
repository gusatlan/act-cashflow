package br.com.act.cashflow.config;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.CqlSessionBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.cassandra.CassandraAutoConfiguration;
import org.springframework.boot.autoconfigure.cassandra.CassandraProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.core.cql.generator.CreateKeyspaceCqlGenerator;
import org.springframework.data.cassandra.core.cql.keyspace.CreateKeyspaceSpecification;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@Configuration
@ConditionalOnClass(CqlSession.class)
@ConditionalOnProperty(name = "spring.data.cassandra.create-keyspace", havingValue = "true")
@AutoConfigureBefore(CassandraAutoConfiguration.class)
public class CassandraKeyspaceConfiguration implements CommandLineRunner {
    private CqlSessionBuilder cqlSessionBuilder;
    private CassandraProperties properties;
    private Logger logger;
    private final String cql_keyspace;

    public CassandraKeyspaceConfiguration(
            final CqlSessionBuilder cqlSessionBuilder,
            final CassandraProperties properties,
            @Value("${cql.keyspace}") final String cql_keyspace
    ) {
        this.cqlSessionBuilder = cqlSessionBuilder;
        this.properties = properties;
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.cql_keyspace = cql_keyspace;
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

    @Override
    public void run(String... args) throws Exception {
        logger.info("Initialize keyspace forced");

        try {
            final String host = properties.getContactPoints().stream().findAny().orElse("db");
            final String cmd = String.format("/cassandra/bin/cqlsh %s -e \"%s\"", host, cql_keyspace);
            if (!Runtime.getRuntime().exec(cmd).waitFor(10L, TimeUnit.SECONDS)) {
                throw new Exception("Timeout");
            }
        } catch (Exception e) {
            logger.error("Initialize keyspace forced", e);
        }
    }
}
