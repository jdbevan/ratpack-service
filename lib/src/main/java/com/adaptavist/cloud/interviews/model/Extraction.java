package com.adaptavist.cloud.interviews.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;

import javax.validation.constraints.NotNull;

@Value.Immutable
@JsonDeserialize(builder = ImmutableExtraction.Builder.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public interface Extraction {

    @NotNull
    String objectName();

    @NotNull
    String type();

    @NotNull
    String alias();

}
