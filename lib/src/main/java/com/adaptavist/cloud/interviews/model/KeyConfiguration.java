package com.adaptavist.cloud.interviews.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;

import javax.validation.constraints.NotNull;
import java.util.List;

@Value.Immutable
@JsonDeserialize(builder = ImmutableKeyConfiguration.Builder.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public interface KeyConfiguration {

    @NotNull
    List<Extraction> extractions();

    @NotNull
    String propertyKey();

}
