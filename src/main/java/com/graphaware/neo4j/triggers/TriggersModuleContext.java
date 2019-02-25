package com.graphaware.neo4j.triggers;

import com.graphaware.runtime.metadata.PositionNotFoundException;
import com.graphaware.runtime.metadata.TimerDrivenModuleContext;

import org.neo4j.graphdb.GraphDatabaseService;

public class TriggersModuleContext implements TimerDrivenModuleContext<TriggersModuleContext> {

    @Override
    public long earliestNextCall() {
        return 0;
    }

    @Override
    public TriggersModuleContext find(GraphDatabaseService db) throws PositionNotFoundException {
        return null;
    }

}