package com.graphaware.neo4j.triggers.config;

import com.graphaware.common.policy.inclusion.InclusionPolicies;
import com.graphaware.common.policy.role.InstanceRole;
import com.graphaware.common.policy.role.InstanceRolePolicy;
import com.graphaware.runtime.config.BaseTxAndTimerDrivenModuleConfiguration;

public class TriggersConfiguration extends BaseTxAndTimerDrivenModuleConfiguration<TriggersConfiguration> {

    private final ModuleState state;

    private final String file;

    private final String queries;

    public enum ModuleState {
        ACTIVE,
        DISABLED
    }

    public static TriggersConfiguration defaultConfiguration() {
        return new TriggersConfiguration(InclusionPolicies.all(), Long.MAX_VALUE, InstanceRole::isWritable, ModuleState.DISABLED, null, null);
    }

    public TriggersConfiguration(InclusionPolicies inclusionPolicies, long initializeUntil, InstanceRolePolicy instanceRolePolicy, ModuleState state, String file, String queries) {
        super(inclusionPolicies, initializeUntil, instanceRolePolicy);
        this.state = state;
        this.file = file;
        this.queries = queries;
    }

    @Override
    protected TriggersConfiguration newInstance(InclusionPolicies inclusionPolicies, long initializeUntil, InstanceRolePolicy instanceRolePolicy) {
        return new TriggersConfiguration(inclusionPolicies, initializeUntil, instanceRolePolicy, getState(), getFile(), getQueries());
    }

    public TriggersConfiguration withFile(String file) {
        return new TriggersConfiguration(getInclusionPolicies(), initializeUntil(), getInstanceRolePolicy(), ModuleState.ACTIVE, file, getQueries());
    }

    public TriggersConfiguration withQueries(String queries) {
        return new TriggersConfiguration(getInclusionPolicies(), initializeUntil(), getInstanceRolePolicy(), ModuleState.ACTIVE, getFile(), queries);
    }

    public ModuleState getState() {
        return state;
    }

    public String getFile() {
        return file;
    }

    public String getQueries() {
        return queries;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        TriggersConfiguration that = (TriggersConfiguration) o;

        if (file != null ? !file.equals(that.file) : that.file != null) {
            return false;
        }

        if (queries != null ? !queries.equals(that.queries) : that.queries != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (file != null ? file.hashCode() : 0);
        result = 31 * result + (state != null ? state.hashCode() : 0);
        result = 31 * result + (queries != null ? queries.hashCode() : 0);

        return result;
    }
}
