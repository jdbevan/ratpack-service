package com.adaptavist.cloud.interviews.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

@Value.Immutable
@JsonDeserialize(builder = ImmutableDynamicModule.Builder.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public interface DynamicModule {

    @NotNull
    Map<String, String> name();

    @NotNull
    String key();

    @NotNull
    String entityType();

    @NotNull
    List<KeyConfiguration> keyConfigurations();

}
