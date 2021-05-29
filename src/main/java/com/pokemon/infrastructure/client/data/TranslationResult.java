package com.pokemon.infrastructure.client.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static org.springframework.util.ObjectUtils.isEmpty;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class TranslationResult {
    private static final Integer SUCCESS_COUNTER = 1;
    private TranslationResultSuccessInformation success;
    private TranslationResultContent contents;

    public Boolean isSuccess() {
        return !isEmpty(success) && success.getTotal().equals(SUCCESS_COUNTER);
    }
}