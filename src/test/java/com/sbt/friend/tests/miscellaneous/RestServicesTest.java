package com.sbt.friend.tests.miscellaneous;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.specification.RequestSpecification;
import com.sbt.friend.Application;
import com.sbt.friend.RestUtils;
import com.sbt.friend.configuration.props.PropertySourceLoader;
import com.sbt.friend.server.dto.NotificationDTO;
import com.sbt.friend.server.dto.TemplateDTO;
import com.sbt.friend.server.dto.sm.ProtocolDTO;
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
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class RestServicesTest {

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
    public void getTemplateTest() throws IOException {
        final String body = "{\"arg1\":\"FRIEND\",\"arg0\":\"Калькулятор грейдов\"}";
        String jsonObject = RestUtils.getJsonObject(body, psl, REST_GET_TEMPLATE, objectMapper, requestSpecification);

        TemplateDTO templateDTO = objectMapper.readValue(jsonObject, TemplateDTO.class);
        assertThat(templateDTO.getName()).isEqualTo("Калькулятор грейдов");
        assertThat(templateDTO.getExtSystem()).isEqualTo("FRIEND");
        assertThat(templateDTO.getServiceName()).isEqualTo("Телефон");
    }

    @Test
    public void getProtocolsTest() throws IOException {
        final String body = "{\n" +
                "  \n" +
                "  \"arg0\":{\n" +
                "    \"extArgs\":{\n" +
                "    \"page\":{\n" +
                "      \"lastPage\":false\n" +
                "      ,\"count\":12\n" +
                "      ,\"read\":0\n" +
                "      ,\"start\":1}\n" +
                "  \t}\n" +
                "  \t,\n" +
                "    \"params\":{\n" +
                "      \"asString\":[\n" +
                "        {\"value\":\"FRIEND\",\"property\":\"extSystem\",\"type\":\"EQ\"}\n" +
                "        ,{\"value\":\"RQ1000000307\",\"property\":\"parentId\",\"type\":\"EQ\"}\n" +
                "      ]\n" +
                "    }\n" +
                "  }\n" +
                "}";
        String jsonObject = RestUtils.getJsonObject(body, psl, REST_GET_PROTOCOLS, objectMapper, requestSpecification);

        List<ProtocolDTO> protocolDTOList = objectMapper.readValue(jsonObject, new TypeReference<List<ProtocolDTO>>() {
        });
        assertTrue(protocolDTOList.size() > 0);
        for (ProtocolDTO protocol : protocolDTOList) {
            if (protocol.getId().equals("1011441609DQh3")) {
                assertThat(protocol.getCreatedByFio()).isEqualTo("Топ Иван Иванович");
                assertThat(protocol.getParentId()).isEqualTo("RQ1000000307");
            }
        }
    }

    @Test
    public void getNotificationsTest() throws IOException {
        final String body = "{\n" +
                "  \"arg0\": false\n" +
                "}";
        String jsonObject = RestUtils.getJsonObject(body, psl, REST_GET_NOTIFICATIONS, objectMapper, requestSpecification);

        List<NotificationDTO> notificationDTOs = objectMapper.readValue(jsonObject, new TypeReference<List<NotificationDTO>>() {
        });
        assertTrue(notificationDTOs.size() > 0);
        for (NotificationDTO notification : notificationDTOs) {
            if (notification.getReferenceObjectID().equals("SD115911609")) {
                assertThat(notification.getCategory()).isEqualTo("Компенсация за использование легкового автомобиля");
                assertThat(notification.getType()).isEqualTo(NotificationDTO.NotifyType.NEED_ZNO_APPROVE);
                assertThat(notification.getTitle()).isEqualTo("Компенсация за использование легкового автомобиля");
            }
        }
    }
}
