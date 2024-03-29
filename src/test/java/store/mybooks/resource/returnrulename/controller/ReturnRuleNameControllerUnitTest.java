package store.mybooks.resource.returnrulename.controller;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import store.mybooks.resource.returnrulename.dto.request.ReturnRuleNameCreateRequest;
import store.mybooks.resource.returnrulename.dto.response.ReturnRuleNameCreateResponse;
import store.mybooks.resource.returnrulename.dto.response.ReturnRuleNameResponse;
import store.mybooks.resource.returnrulename.service.ReturnRuleNameService;

/**
 * packageName    : store.mybooks.resource.return_name_rule.controller<br>
 * fileName       : ReturnNameRuleControllerTest<br>
 * author         : minsu11<br>
 * date           : 2/20/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2/20/24        minsu11       최초 생성<br>
 */
@ExtendWith({MockitoExtension.class, RestDocumentationExtension.class})
@WebMvcTest(value = ReturnRuleNameRestController.class)
class ReturnRuleNameControllerUnitTest {

    @MockBean
    ReturnRuleNameService returnRuleNameService;

    MockMvc mockMvc;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext,
               RestDocumentationContextProvider restDocumentation) {

        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    @DisplayName("id값에 대한 반품 규정 명 조회 데이터를 response 응답")
    void givenStringName_whenCallGetReturnNameRule_thenReturnReturnNameRuleResponse() throws Exception {
        ReturnRuleNameResponse response = new ReturnRuleNameResponse("test123", LocalDate.of(1212, 12, 12));
        when(returnRuleNameService.getReturnRuleName(anyString())).thenReturn(response);
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/return-rule-names/{id}", "test123"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(response.getId()))
                .andExpect(jsonPath("$.createdDate").value("1212-12-12"))
                .andDo(document("return-rule-name-get-id",
                        pathParameters(
                                parameterWithName("id").description("조회 아이디")
                        ),
                        responseFields(
                                fieldWithPath("id").description("반품 규정 명 아이디"),
                                fieldWithPath("createdDate").description("반품 규정 명 생성 날짜")

                        )));

        verify(returnRuleNameService, times(1)).getReturnRuleName(anyString());

    }

    @Test
    @DisplayName("모든 반품 규정 명에 대한 조회 데이터를 response list 응답")
    void given_whenGetReturnRuleNameList_thenReturnReturnRuleNameResponseList() throws Exception {
        LocalDate date = LocalDate.of(1212, 12, 12);
        List<ReturnRuleNameResponse> returnRuleNameResponseList = List.of(new ReturnRuleNameResponse("test", date));
        when(returnRuleNameService.getReturnRuleNameList()).thenReturn(returnRuleNameResponseList);
        mockMvc.perform(get("/api/return-rule-names"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()", equalTo(returnRuleNameResponseList.size())))
                .andExpect(jsonPath("$[0].id", equalTo(returnRuleNameResponseList.get(0).getId())))
                .andExpect(jsonPath("$[0].createdDate").value("1212-12-12"))
                .andDo(document("return-rule-name-get-list",
                        responseFields(
                                fieldWithPath("[].id").description("반품 규정 명 아이디"),
                                fieldWithPath("[].createdDate").description("반품 규정 명 생성 날짜")
                        )));
        verify(returnRuleNameService, times(1)).getReturnRuleNameList();
    }

    @Test
    @DisplayName("DB에 아무런 값이 없을 경우 빈 리스트를 응답")
    void given_whenGetReturnRuleNameList_thenReturnEmptyList() throws Exception {
        when(returnRuleNameService.getReturnRuleNameList()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/return-rule-names"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(0));
        verify(returnRuleNameService, times(1)).getReturnRuleNameList();
    }

    @Test
    @DisplayName("요청 데이터를 저장한 뒤 response dto 반환")
    void givenReturnRuleNameCreateRequest_whenCreateReturnRuleName_thenReturnReturnRulenameCreateResponse()
            throws Exception {
        String testData = "test";
        ReturnRuleNameCreateRequest request = new ReturnRuleNameCreateRequest();
        ReflectionTestUtils.setField(request, "id", testData);
        LocalDate date = LocalDate.of(1212, 12, 12);
        ReturnRuleNameCreateResponse response = new ReturnRuleNameCreateResponse(testData, date);
        ObjectMapper objectMapper = new ObjectMapper();

        when(returnRuleNameService.createReturnRuleName(any())).thenReturn(response);

        mockMvc.perform(post("/api/return-rule-names")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(testData))
                .andExpect(jsonPath("$.createdDate").value("1212-12-12"))
                .andDo(document("return-rule-name-created",
                        requestFields(
                                fieldWithPath("id").description("반품 규정 명 아이디"))
                        , responseFields(fieldWithPath("id").description("반품 규정 명 아이디"),
                                fieldWithPath("createdDate").description("반품 규정 명 생성 날짜"))
                ));
        verify(returnRuleNameService, times(1)).createReturnRuleName(any());
    }

    @Test
    @DisplayName("요청으로 들어온 데이터가 유효성을 지키지 않은 경우")
    void givenReturnNameCreateRequest_whenCreateReturnRuleName_thenReturnReturnRuleNameCreateResponse()
            throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ReturnRuleNameCreateRequest request = new ReturnRuleNameCreateRequest();
        ReflectionTestUtils.setField(request, "id", "t");
        mockMvc.perform(post("/api/return-rule-names")
                        .content(mapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

}