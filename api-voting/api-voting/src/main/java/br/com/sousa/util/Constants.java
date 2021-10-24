package br.com.sousa.util;

public class Constants {
    private Constants(){}

    //Schedule messages
    public static final String MESSAGE_SUCCESS_SCHEDULE_REGISTER = "message.created.schedule";
    public static final String MESSAGE_SUCCESS_OPEN_SCHEDULE = "message.open.schedule";
    public static final String MESSAGE_EXCEPTION_NOT_OPEN_SCHEDULE = "exception.not.open.schedule";
    public static final String MESSAGE_EXCEPTION_NOT_FOUND_SCHEDULE = "exception.not.found.schedule";
    public static final String MESSAGE_EXCEPTION_ERROR_OPEN_SCHEDULE= "exception.error.open.schedule";
    public static final String MESSAGE_WARNING_VOTES_NOT_FOUND_FOR_SCHEDULE= "warning.not.found.votes.for.schedule";
    public static final String MESSAGE_SUCCESS_RESULT_LOADED_FOR_SCHEDULE="message.result.shedule";

    //Vote messages
    public static final String MESSAGE_SUCCESS_VOTE_REGISTER ="message.register.vote";
    public static final String MESSAGE_EXCEPTION_NOT_ALLOWED_DELETE_VOTE = "exception.not.allowed.delete.vote";
    public static final String MESSAGE_EXCEPTION_NOT_FOUND_VOTE = "exception.not.found.vote";
    public static final String MESSAGE_EXCEPTION_NOT_ALLOWED_UPDATE_VOTE = "exception.not.allowed.update.vote";
    public static final String MESSAGE_WARNING_MULTIPLE_VOTES = "warning.not.allowed.multiple.vote";

    //Consumer API messages
    public static final String MESSAGE_EXCEPTION_INVALID_DOCUMENT = "consumer.exception.invalid.document";

    //Formats
    public static final String DATE_TIME_FORMAT = "date.time.format";

}
