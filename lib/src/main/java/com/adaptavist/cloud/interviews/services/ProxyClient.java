package com.adaptavist.cloud.interviews.services;

import com.adaptavist.cloud.interviews.model.DynamicModules;
import com.adaptavist.cloud.interviews.model.ExecutionUser;
import com.adaptavist.cloud.interviews.model.Tenant;
import com.adaptavist.cloud.interviews.model.URIParts;
import ratpack.core.http.HttpMethod;
import ratpack.core.http.client.ReceivedResponse;
import ratpack.exec.Promise;

public interface ProxyClient {
    Promise<ReceivedResponse> proxyWithPayload(Tenant tenant, HttpMethod method, URIParts uriParts, DynamicModules dynamicModules, ExecutionUser addOn);

    Promise<ReceivedResponse> proxy(Tenant tenant, HttpMethod delete, URIParts uri);
}
