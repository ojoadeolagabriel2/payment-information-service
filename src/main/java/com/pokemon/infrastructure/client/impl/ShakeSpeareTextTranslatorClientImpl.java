package com.pokemon.infrastructure.client.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pokemon.infrastructure.client.TextTranslatorClient;
import com.pokemon.infrastructure.client.data.TranslationResult;
import com.pokemon.infrastructure.client.data.TranslationResultContent;
import com.pokemon.infrastructure.client.data.TranslationResultSuccessInformation;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

import static java.net.URLEncoder.encode;
import static java.nio.charset.Charset.defaultCharset;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.TOO_MANY_REQUESTS;

@Slf4j
@Component
public class ShakeSpeareTextTranslatorClientImpl implements TextTranslatorClient {

    private static final Integer DEFAULT_SUCCESS_TOTAL = 1;
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final String shakespeareTranslatorBaseurl;

    public ShakeSpeareTextTranslatorClientImpl(@Value("${app.shakespeare_translator_host_url}") String shakespeareTranslatorBaseurl) {
        this.shakespeareTranslatorBaseurl = shakespeareTranslatorBaseurl;
    }

    @Override
    public Optional<TranslationResult> translate(String text) {
        try {
            URI uri = getUri(text);
            final HttpGet httpGet = new HttpGet(uri.toString());
            CloseableHttpResponse response = httpClient().execute(httpGet);

            if (response.getStatusLine().getStatusCode() == OK.value()) {
                var responseBody = EntityUtils.toString(response.getEntity());
                return of(objectMapper.readValue(responseBody, TranslationResult.class));
            } else if (response.getStatusLine().getStatusCode() == TOO_MANY_REQUESTS.value()) {

                log.info("too many requests detected (429) to translation api, defaulting {}", text);
                return of(TranslationResult
                        .builder()
                        .success(TranslationResultSuccessInformation.builder().total(DEFAULT_SUCCESS_TOTAL).build())
                        .contents(TranslationResultContent
                                .builder()
                                .translated(text)
                                .translation(text)
                                .text(text)
                                .build())
                        .build());
            }
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        return empty();
    }

    private URI getUri(String text) throws URISyntaxException {
        return new URI(shakespeareTranslatorBaseurl + "/translate/shakespeare.json?text=" + encode(text.replaceAll("\\r|\\n", " "), defaultCharset()));
    }

    private CloseableHttpClient httpClient() {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(5000)
                .setConnectTimeout(5000)
                .setSocketTimeout(5000)
                .build();

        return HttpClientBuilder.create()
                .setDefaultRequestConfig(requestConfig)
                .build();
    }
}