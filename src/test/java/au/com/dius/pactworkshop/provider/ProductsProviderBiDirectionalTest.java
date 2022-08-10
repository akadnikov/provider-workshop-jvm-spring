package au.com.dius.pactworkshop.provider;

import com.atlassian.oai.validator.OpenApiInteractionValidator;
import com.atlassian.oai.validator.report.LevelResolver;
import com.atlassian.oai.validator.report.ValidationReport.Level;
import com.atlassian.oai.validator.restassured.OpenApiValidationFilter;
import com.atlassian.oai.validator.whitelist.ValidationErrorsWhitelist;
import com.atlassian.oai.validator.whitelist.rule.WhitelistRules;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import static io.restassured.RestAssured.given;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ProductsProviderBiDirectionalTest {
    @LocalServerPort
    int port;

    @Autowired
    ProductRepository repository;

    File spec = new File("oas/schema.json");

    private String token = "Bearer " + new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").format(new Date());

    // Use this for "happy path" testing
    private final OpenApiValidationFilter validationFilter = new OpenApiValidationFilter(spec.getAbsolutePath());

    // Use this for "negative scenario" testing
    private final OpenApiInteractionValidator responseOnlyValidator = OpenApiInteractionValidator
            .createForSpecificationUrl(spec.getAbsolutePath())
            .withLevelResolver(LevelResolver.create().withLevel("validation.request", Level.WARN).build())
            .withWhitelist(ValidationErrorsWhitelist.create().withRule("Ignore request entities", WhitelistRules.isRequest()))
            .build();
    private OpenApiValidationFilter responseOnlyValidation = new OpenApiValidationFilter(responseOnlyValidator);

    @Test
    public void testListProducts() {
        given().port(port).filter(validationFilter).when()
                .header("Authorization", token)
                .get("/products").then().assertThat().statusCode(200);
    }

    @Test
    public void testGetProduct200() {
        given().port(port).filter(validationFilter).when()
                .header("Authorization", token)
                .get("/product/09").then().assertThat().statusCode(200);
    }

    @Test
    public void testGetProduct404() {
        given().port(port).filter(validationFilter).when()
                .header("Authorization", token)
                .get("/product/999").then().assertThat().statusCode(404);
    }
}