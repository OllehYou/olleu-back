package com.example.olleuback.config.restdocs.config;

import static org.springframework.restdocs.payload.PayloadDocumentation.beneathPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.snippet.Attributes.attributes;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.PayloadSubsectionExtractor;
import org.springframework.restdocs.snippet.Snippet;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.OncePerRequestFilter;

@ActiveProfiles("test")
@Import(RestDocsConfig.class)
@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = EnumController.class)
public class EnumControllerTest {

    @Autowired
    MockMvc mvc;
    @Autowired
    protected RestDocumentationResultHandler restDocs;
    @Autowired
    ObjectMapper objectMapper;

    Logger log = LoggerFactory.getLogger(Logger.class);

    @BeforeEach
    void setUp(final WebApplicationContext context, final RestDocumentationContextProvider provider) {
        this.mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(MockMvcRestDocumentation.documentationConfiguration(provider))
                .alwaysDo(print())
                .alwaysDo(restDocs)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .build();
    }

    @Test
    void enumDocs() throws Exception {
        ResultActions result = mvc.perform(get("/enums")
                                                   .contentType(MediaType.APPLICATION_JSON));

        MockHttpServletResponse response = result.andExpect(status().isOk()).andReturn()
                .getResponse();

        Map<String, Map<String, String>> map = objectMapper.readValue(response.getContentAsString(), Map.class);

        List<CustomResponseFieldsSnippet> customResponseFieldsSnippets = new ArrayList<>();
        for(String key : map.keySet()) {
            Map<String, String> enumType = map.get(key);
            List<FieldDescriptor> descriptorList = new ArrayList<>();
            for(var enumVal: enumType.keySet()) {
                descriptorList.add(fieldWithPath(enumVal).description(enumType.get(enumVal)));
            }
            customResponseFieldsSnippets.add(
                    customResponseFields("custom-response", beneathPath(key).withSubsectionId(key),
                                 attributes(key("title").value(key)), descriptorList.toArray(new FieldDescriptor[0])));
        }
        result.andDo(restDocs.document(customResponseFieldsSnippets.toArray(new Snippet[0])));
    }

    public static CustomResponseFieldsSnippet customResponseFields(String type,
                                                                   PayloadSubsectionExtractor<?> subsectionExtractor,
                                                                   Map<String, Object> attributes, FieldDescriptor... descriptors) {
        return new CustomResponseFieldsSnippet(type, subsectionExtractor, Arrays.asList(descriptors), attributes, true);
    }
}
