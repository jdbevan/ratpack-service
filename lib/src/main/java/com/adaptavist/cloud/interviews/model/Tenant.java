package com.adaptavist.cloud.interviews.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

@JsonIgnoreProperties(ignoreUnknown = true)
@Value.Immutable
@JsonSerialize
@JsonDeserialize(builder = ImmutableTenant.Builder.class)
public interface Tenant {
    @Value.Parameter
    String identifier();
}
