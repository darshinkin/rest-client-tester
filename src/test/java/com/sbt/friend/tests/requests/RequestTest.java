package com.sbt.friend.tests.requests;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.specification.RequestSpecification;
import com.sbt.friend.Application;
import com.sbt.friend.RestUtils;
import com.sbt.friend.configuration.props.PropertySourceLoader;
import com.sbt.friend.server.dto.request.RequestDTO;
import com.sbt.friend.utils.Dates;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.List;

import static com.sbt.friend.RestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class RequestTest {
    @Autowired
    PropertySourceLoader psl;

    private RequestSpecification requestSpecification;
    private ObjectMapper objectMapper;

    @Before
    public void authAuthentication() {
        requestSpecification = getRequestSpecification(psl);
        objectMapper = getObjectMapper();
    }

    @Test
    public void requestTest() throws IOException {
        String requestId = saveRequest();
        getRequest(requestId);
        editCloseRequest(requestId);
    }

    private void editCloseRequest(String requestId) throws IOException {
        final String body = String.format("{\"arg0\":{\"id\":\"%s\",\"status\":\"%s\"}}", requestId, RequestDTO.RequestStatus.CLOSED.toString());
        String jsonObject = getJsonObject(body, psl, REST_SAVE_REQUEST, objectMapper, requestSpecification);

        RequestDTO request = objectMapper.readValue(jsonObject, RequestDTO.class);
        requestId = request.getId();
        assertNotNull(requestId);
        assertThat(request.getType()).isEqualTo(RequestDTO.RequestType.TASK);
        assertThat(request.getRealtyObjId()).isEqualTo("realtyObjId");
        assertThat(request.getStatus()).isEqualTo(RequestDTO.RequestStatus.CLOSED);
    }

    private void getRequest(String requestId) throws IOException {
        final String body = String.format("{\n" +
                "  \"arg0\":{\n" +
                "   \"asString\":[\n" +
                "    \t{\"value\":\"%s\",\"type\":\"EQ\",\"property\":\"id\"}\n" +
                "  \t]\n" +
                "  }\n" +
                "}", requestId);
        String jsonObject = getJsonObject(body, psl, REST_GET_REQUEST, objectMapper, requestSpecification);

        List<RequestDTO> requestDTOs = objectMapper.readValue(jsonObject, new TypeReference<List<RequestDTO>>() {
        });
        assertTrue(requestDTOs.size() > 0);
        for (RequestDTO request : requestDTOs) {
            assertThat(request.getType()).isEqualTo(RequestDTO.RequestType.TASK);
            assertThat(request.getRealtyObjId()).isEqualTo("realtyObjId");
        }
    }

    private String saveRequest() throws IOException {
        String requestId;
        String realtyObjId = "realtyObjId";
        DateTime dateTime = DateTime.now();
        dateTime = dateTime.plusHours(2);
        String deadlineTime = Dates.printDateToIso(dateTime);
        final String body = String.format("{\"arg0\":\n" +
                " {\n" +
                "   \"deadline\":\"%s\",\n" +
                "   \"status\":\"OPEN\",\n" +
                "   \"type\":\"TASK\",\n" +
                "   \"text\":\"11222\",\n" +
                "   \"title\":\"т4ст\",\n" +
                "   \"toEmail\":\"sbt-krivosheev-kb@mail.ca.sbrf.ru\",\n" +
                "   \"realtyObjId\":\"%s\"\n" +
                " }\n" +
                "}", deadlineTime, realtyObjId);
        String jsonObject = RestUtils.getJsonObject(body, psl, REST_SAVE_REQUEST, objectMapper, requestSpecification);

        RequestDTO request = objectMapper.readValue(jsonObject, RequestDTO.class);
        requestId = request.getId();
        assertNotNull(requestId);
        assertThat(request.getType()).isEqualTo(RequestDTO.RequestType.TASK);
        assertThat(request.getRealtyObjId()).isEqualTo("realtyObjId");
        return requestId;
    }
}
