package br.com.sousa.domain.service;

import br.com.sousa.util.exeption.ConsumerAPIException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static br.com.sousa.util.Constants.MESSAGE_EXCEPTION_INVALID_DOCUMENT;

@Service
public class ServiceConsumer extends AbstractService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceConsumer.class);

    private static final String ABLE_TO_VOTE = "ABLE_TO_VOTE";
    private static final String API_USERS = "https://user-info.herokuapp.com/users/{document}";

    private final RestTemplate restTemplate;

    public ServiceConsumer() {
        restTemplate = new RestTemplate();
    }

    /**
     * Verify if is a valid document from a external API
     * @param document associate document
     * @return true if document exists and service returned ABLE_TO_VOTE
     * @throws ConsumerAPIException document not found in service
     */
    public boolean isValidDocument(String document) {

        try {
            LOGGER.debug("Consuming API {}", API_USERS);
            var returned = restTemplate.getForEntity( API_USERS, String.class, document);

            LOGGER.debug("Validating return {}", returned.getStatusCodeValue());
            var mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(returned.getBody());
            JsonNode status = root.path("status");

            LOGGER.debug("Return validation ABLE_TO_VOTE.equals {}", status);
            return ABLE_TO_VOTE.equals(status.textValue());
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw  new ConsumerAPIException(getMessage(MESSAGE_EXCEPTION_INVALID_DOCUMENT, document));
        }
    }
}
