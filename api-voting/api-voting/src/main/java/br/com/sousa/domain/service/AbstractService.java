package br.com.sousa.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

public abstract class AbstractService {

    @Autowired
    private MessageSource messageSource;

    protected String getMessage(String messageId){
        return messageSource.getMessage(messageId, null, LocaleContextHolder.getLocale());
    }

    protected String getMessage(String messageId, Number value){
        return String.format(messageSource.getMessage(messageId, null, LocaleContextHolder.getLocale()), value);
    }

    protected String getMessage(String messageId, String value){
        return String.format(messageSource.getMessage(messageId, null, LocaleContextHolder.getLocale()), value);
    }

    protected String getMessage(String messageId, String valueText, Long valueLong){
        return String.format(messageSource.getMessage(messageId, null, LocaleContextHolder.getLocale()), valueText, valueLong);
    }
}
