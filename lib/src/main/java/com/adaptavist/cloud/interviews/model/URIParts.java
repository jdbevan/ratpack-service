package com.adaptavist.cloud.interviews.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.collect.Multimap;
import org.immutables.value.Value;

import javax.annotation.Nullable;
import java.util.Map;

@Value.Immutable
@JsonDeserialize(builder = ImmutableURIParts.Builder.class)
public interface URIParts {
    String path();
    @Nullable
    Map<String, String> queryParams();

    @Nullable
    Multimap<String, String> multiQueryParams();
}
