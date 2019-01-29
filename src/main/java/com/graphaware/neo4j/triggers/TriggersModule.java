package com.graphaware.neo4j.triggers;

import com.graphaware.neo4j.triggers.config.TriggersConfiguration;
import com.graphaware.neo4j.triggers.definition.DefinitionFileReader;
import com.graphaware.neo4j.triggers.definition.TriggersDefinition;
import com.graphaware.neo4j.triggers.domain.TriggersRegistry;
import com.graphaware.neo4j.triggers.executor.TriggersExecutor;
import com.graphaware.runtime.config.BaseTxAndTimerDrivenModuleConfiguration;
import com.graphaware.runtime.metadata.TimerDrivenModuleContext;
import com.graphaware.runtime.module.BaseTxDrivenModule;
import com.graphaware.runtime.module.DeliberateTransactionRollbackException;
import com.graphaware.runtime.module.TimerDrivenModule;
import com.graphaware.tx.event.improved.api.ImprovedTransactionData;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.kernel.configuration.Config;
import org.neo4j.kernel.internal.GraphDatabaseAPI;

import java.util.stream.Collectors;

public class TriggersModule extends BaseTxDrivenModule<Void> implements TimerDrivenModule {

    private final TriggersConfiguration configuration;
    private final GraphDatabaseService database;
    private final TriggersExecutor triggersExecutor;

    public TriggersModule(String moduleId, GraphDatabaseService graphDatabaseService, TriggersConfiguration configuration) {
        super(moduleId);
        this.configuration = configuration;
        this.database = graphDatabaseService;
        Config config = ((GraphDatabaseAPI) database).getDependencyResolver().resolveDependency(Config.class);
        TriggersDefinition triggersDefinition = DefinitionFileReader.loadTriggersDefinition(configuration.getFile(), config.getRaw());
        triggersExecutor = new TriggersExecutor(database, TriggersRegistry.fromDefinition(triggersDefinition));
    }

    @Override
    public Void beforeCommit(ImprovedTransactionData transactionData) throws DeliberateTransactionRollbackException {
        triggersExecutor.handleCreatedNodes(transactionData.getAllCreatedNodes());
        triggersExecutor.handleUpdatedNodes(transactionData.getAllChangedNodes().stream().map(c -> {return c.getCurrent();}).collect(Collectors.toList()));
        triggersExecutor.handleDeletedNodes(transactionData.getAllDeletedNodes());

        return null;
    }

    @Override
    public TimerDrivenModuleContext createInitialContext(GraphDatabaseService database) {
        return null;
    }

    @Override
    public TimerDrivenModuleContext doSomeWork(TimerDrivenModuleContext lastContext, GraphDatabaseService database) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BaseTxAndTimerDrivenModuleConfiguration getConfiguration() {
        return configuration;
    }

}