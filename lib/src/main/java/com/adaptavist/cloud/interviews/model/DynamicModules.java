package com.adaptavist.cloud.interviews.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Value.Immutable
@JsonDeserialize(builder = ImmutableDynamicModules.Builder.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public interface DynamicModules {

    @NotEmpty
    List<DynamicModule> jiraEntityProperties();

}
