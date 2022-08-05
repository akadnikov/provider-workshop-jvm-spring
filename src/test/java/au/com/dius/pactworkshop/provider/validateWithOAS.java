package au.com.dius.pactworkshop.provider;

import com.atlassian.oai.validator.pact.PactProviderValidationResults;
import com.atlassian.oai.validator.pact.PactProviderValidator;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

public class validateWithOAS {

    @Test
    public void validateWithOASTest() {

        final String API_SPEC_URL = new File("oas/schema.json").getAbsolutePath();
        final String BROKER_URL = "http://localhost:8000";
        final String USERNAME = "pact_workshop";
        final String PASSWORD = "pact_workshop";
        final String PROVIDER_ID = "ProductService";

        final PactProviderValidator validator =
                PactProviderValidator
                        .createFor(API_SPEC_URL)
                        .withPactsFrom(BROKER_URL, USERNAME, PASSWORD, PROVIDER_ID)
                        .build();

        final PactProviderValidationResults results = validator.validate();

        assertThat(results.hasErrors()).withFailMessage(results.getValidationFailureReport()).isFalse();
    }
}
