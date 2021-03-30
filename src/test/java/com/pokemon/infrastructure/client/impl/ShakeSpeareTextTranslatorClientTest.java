package com.pokemon.infrastructure.client.impl;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.Options;
import com.pokemon.infrastructure.client.TextTranslatorClient;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.net.URISyntaxException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static java.lang.String.format;
import static java.net.URLEncoder.encode;
import static java.nio.charset.Charset.defaultCharset;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.OK;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class ShakeSpeareTextTranslatorClientTest {

    private static final String TEST_HOST = "http://localhost";
    private static final String SAMPLE_TEXT = "Spits fire that is hot enough to melt boulders.\fKnown to cause forest fires unintentionally.";
    private static final String SAMPLE_TOO_MANY_REQUEST_TEXT = "Spits fire that is hot enough to melt boulders";
    private static final String TRANSLATED_TEXT = "Spits fire yond is hot enow to melt boulders. Known to cause forest fires unintentionally.";

    TextTranslatorClient textTranslatorClient;
    static WireMockServer wireMockServer = new WireMockServer(Options.DYNAMIC_PORT);

    @Before
    public void setup() {
        wireMockServer.start();
        textTranslatorClient = new ShakeSpeareTextTranslatorClientImpl(getTestHost());
    }

    @Test
    public void translate_ValidText_ReturnShakeSpeareTranslation() throws URISyntaxException {
        // given
        wireMockServer.stubFor(get(urlEqualTo(getPath(SAMPLE_TEXT)))
                .willReturn(aResponse().withStatus(OK.value()).withBodyFile("translation_200.json")));

        // when
        var translation = textTranslatorClient.translate(SAMPLE_TEXT).get();

        // then
        assertTrue(translation.isSuccess());
        assertNotNull(translation.getContents());
        assertEquals(TRANSLATED_TEXT, translation.getContents().getTranslated());
    }

    @SneakyThrows
    @Test
    public void translate_ValidTextTooMany_ReturnOriginalTranslation() {
        // given
        wireMockServer.stubFor(get(urlEqualTo(getPath(SAMPLE_TOO_MANY_REQUEST_TEXT)))
                .willReturn(aResponse().withStatus(429).withBodyFile("translation_429.json")));

        // when
        var translation = textTranslatorClient.translate(SAMPLE_TOO_MANY_REQUEST_TEXT).get();

        // then
        assertTrue(translation.isSuccess());
        assertNotNull(translation.getContents());
        assertEquals(SAMPLE_TOO_MANY_REQUEST_TEXT, translation.getContents().getText());
    }

    private String getTestHost() {
        return format("%s:%d", TEST_HOST, wireMockServer.port());
    }

    private String getPath(String text) throws URISyntaxException {
        return new URI( "/translate/shakespeare.json?text=" + encode(text.replaceAll("\\r|\\n", " "), defaultCharset())).toString();
    }
}