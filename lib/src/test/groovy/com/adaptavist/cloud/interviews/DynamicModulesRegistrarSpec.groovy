package com.adaptavist.cloud.interviews

import com.adaptavist.cloud.interviews.model.DynamicModule
import com.adaptavist.cloud.interviews.model.DynamicModules
import com.adaptavist.cloud.interviews.model.ExecutionUser
import com.adaptavist.cloud.interviews.model.Extraction
import com.adaptavist.cloud.interviews.model.ImmutableDynamicModule
import com.adaptavist.cloud.interviews.model.ImmutableDynamicModules
import com.adaptavist.cloud.interviews.model.ImmutableExtraction
import com.adaptavist.cloud.interviews.model.ImmutableKeyConfiguration
import com.adaptavist.cloud.interviews.model.ImmutableTenant
import com.adaptavist.cloud.interviews.model.KeyConfiguration
import com.adaptavist.cloud.interviews.model.Tenant
import com.adaptavist.cloud.interviews.model.URIParts
import com.adaptavist.cloud.interviews.services.ProxyClient
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.guava.GuavaModule
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import io.netty.buffer.Unpooled
import ratpack.exec.Promise
import ratpack.http.Headers
import ratpack.http.HttpMethod
import ratpack.http.MediaType
import ratpack.http.Status
import ratpack.http.client.ReceivedResponse
import ratpack.http.client.internal.DefaultReceivedResponse
import ratpack.http.internal.ByteBufBackedTypedData
import ratpack.http.internal.DefaultMediaType
import ratpack.test.exec.ExecHarness
import spock.lang.Specification

class DynamicModulesRegistrarSpec extends Specification {

    private ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new Jdk8Module())
            .registerModule(new GuavaModule())
    Tenant TENANT = ImmutableTenant.of("test-tenant")
    ProxyClient proxyClient = Mock(ProxyClient)
    DynamicModulesRegistrar service = new DynamicModulesRegistrar(proxyClient, objectMapper)
    DynamicModule dynamicModule = createDynamicModule(
            "Scripted Fields with Name",
            "value",
            "number",
            "scriptedField1",
            "scripted-field-d1edf562-8cb7-4bb4-9f03-f628e2c1c348",
            "issue",
            "scripted-field-d1edf562-8cb7-4bb4-9f03-1111111"
    )
    List<DynamicModule> jiraEntityProperties = [dynamicModule]
    DynamicModules dynamicModules = ImmutableDynamicModules.builder()
            .jiraEntityProperties(jiraEntityProperties)
            .build()

    def "should return a promise of true if a dynamic module is registered successfully"() {
        given:
        1 * proxyClient.proxyWithPayload(TENANT,
                HttpMethod.POST,
                _ as URIParts,
                dynamicModules,
                ExecutionUser.ADD_ON) >> Promise.value(receivedResponse(Status.OK, objectMapper.writeValueAsString([]))
        )

        when:
        def response = ExecHarness.yieldSingle {
            service.registerModules(jiraEntityProperties, TENANT)
        }

        then:
        response.value
    }

    def "should return a promise of false if dynamic module registration response is failure"() {
        given:
        1 * proxyClient.proxyWithPayload(TENANT,
                HttpMethod.POST,
                _ as URIParts,
                dynamicModules,
                ExecutionUser.ADD_ON) >> Promise.value(receivedResponse(Status.INTERNAL_SERVER_ERROR, objectMapper.writeValueAsString([])))

        when:
        def response = ExecHarness.yieldSingle {
            service.registerModules(dynamicModules, TENANT)
        }

        then:
        response.value == false
        !response.throwable
    }

    def "should return a promise of false if dynamic module registration request experiences a socket timeout"() {
        given:
        1 * proxyClient.proxyWithPayload(TENANT,
                HttpMethod.POST,
                _ as URIParts,
                dynamicModules,
                ExecutionUser.ADD_ON) >> Promise.error(new SocketTimeoutException("Socket timed out after 10 seconds"))

        when:
        def response = ExecHarness.yieldSingle {
            service.registerModules(dynamicModules, TENANT)
        }

        then:
        response.value == false
        !response.throwable
    }

    def "should return a promise of true when a dynamic module is de-registered successfully"() {
        given:
        def alias = "thing"
        1 * proxyClient.proxy(TENANT,
                HttpMethod.DELETE,
                _ as URIParts,
        ) >> Promise.value(receivedResponse(Status.NO_CONTENT, objectMapper.writeValueAsString(null)))

        when:
        def response = ExecHarness.yieldSingle {
            service.deregisterModule(alias, TENANT)
        }

        then:
        response.value
    }

    def "should return a promise of false when a dynamic module is not de-registered successfully"() {
        given:
        def alias = "thing"
        1 * proxyClient.proxy(TENANT,
                HttpMethod.DELETE,
                _ as URIParts,
        ) >> Promise.value(receivedResponse(Status.INTERNAL_SERVER_ERROR, objectMapper.writeValueAsString(null)))

        when:
        def response = ExecHarness.yieldSingle {
            service.deregisterModule(alias, TENANT)
        }

        then:
        !response.value
        !response.throwable
    }

    def "should get dynamic modules when retrieved successfully"() {
        given:
        1 * proxyClient.proxy(TENANT,
                HttpMethod.GET,
                _ as URIParts,
        ) >> Promise.value(receivedResponse(Status.OK, objectMapper.writeValueAsString(dynamicModules)))

        when:
        def response = ExecHarness.yieldSingle {
            service.getModules(TENANT)
        }

        then:
        response.valueOrThrow == jiraEntityProperties

    }

    def "should throw Runtime exception when dynamic modules are not retrieved successfully"() {
        given:
        1 * proxyClient.proxy(TENANT,
                HttpMethod.GET,
                _ as URIParts,
        ) >> Promise.value(receivedResponse(Status.INTERNAL_SERVER_ERROR, objectMapper.writeValueAsString(dynamicModules)))

        when:
        def response = ExecHarness.yieldSingle {
            service.getModules(TENANT)
        }

        then:
        response.error
        response.throwable instanceof RuntimeException
        response.throwable.message == "Could not get dynamic modules"

    }

    def "should ignore properties other than JiraEntityProperties when getting dynamic modules"() {
        given:
        String json = "{\n" +
                "  \"jiraEntityProperties\": [\n" +
                "    {\n" +
                "      \"keyConfigurations\": [\n" +
                "        {\n" +
                "          \"extractions\": [\n" +
                "            {\n" +
                "              \"objectName\": \"extension\",\n" +
                "              \"type\": \"text\",\n" +
                "              \"alias\": \"attachmentExtension\"\n" +
                "            }\n" +
                "          ],\n" +
                "          \"propertyKey\": \"attachment\"\n" +
                "        }\n" +
                "      ],\n" +
                "      \"entityType\": \"issue\",\n" +
                "      \"name\": {\n" +
                "        \"value\": \"Attachment Index Document\"\n" +
                "      },\n" +
                "      \"key\": \"dynamic-attachment-entity-property\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"jiraIssueFields\": [\n" +
                "    {\n" +
                "      \"description\": {\n" +
                "        \"value\": \"A dynamically added single-select field\"\n" +
                "      },\n" +
                "      \"type\": \"single_select\",\n" +
                "      \"extractions\": [\n" +
                "        {\n" +
                "          \"path\": \"category\",\n" +
                "          \"type\": \"text\",\n" +
                "          \"name\": \"categoryName\"\n" +
                "        }\n" +
                "      ],\n" +
                "      \"name\": {\n" +
                "        \"value\": \"Dynamic single select\"\n" +
                "      },\n" +
                "      \"key\": \"dynamic-select-field\"\n" +
                "    }\n" +
                "  ]\n" +
                "}"
        1 * proxyClient.proxy(TENANT,
                HttpMethod.GET,
                _ as URIParts,
        ) >> Promise.value(receivedResponse(Status.OK, json))

        when:
        def response = ExecHarness.yieldSingle {
            service.getModules(TENANT)
        }

        then:
        response.value
    }

    private static createDynamicModule(String value, String objectName, String type, String alias, String key, String entityType, String propertyKey) {
        Map<String, String> name = ["value": value]
        Extraction extraction = ImmutableExtraction.builder()
                .objectName(objectName)
                .type(type)
                .alias(alias)
                .build()
        KeyConfiguration keyConfiguration = ImmutableKeyConfiguration.builder()
                .extractions([extraction])
                .propertyKey(propertyKey)
                .build()
        DynamicModule dynamicModule = ImmutableDynamicModule.builder()
                .name(name)
                .key(key)
                .entityType(entityType)
                .keyConfigurations([keyConfiguration])
                .build()
        return dynamicModule
    }

    static ReceivedResponse receivedResponse(Status status, String payload = "", String mediaType = MediaType.APPLICATION_JSON) {
        receivedResponse(status, null, payload, mediaType)
    }

    static ReceivedResponse receivedResponse(Status status, Headers headers, String payload, String mediaType = MediaType.APPLICATION_JSON) {
        new DefaultReceivedResponse(status, headers, new ByteBufBackedTypedData(
                Unpooled.wrappedBuffer(payload.bytes),
                DefaultMediaType.get(mediaType)
        ))
    }

}
