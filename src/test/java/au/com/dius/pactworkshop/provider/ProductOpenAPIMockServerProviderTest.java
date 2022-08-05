package au.com.dius.pactworkshop.provider;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.atlassian.oai.validator.mockmvc.OpenApiValidationMatchers.openApi;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProductOpenAPIMockServerProviderTest {
    public static String SWAGGER_HUB_PRODUCTS_API_SPECS_URL; //"http://localhost:80/productsApiSpecs.json";

    static {
        try {
            SWAGGER_HUB_PRODUCTS_API_SPECS_URL = Files.readString(Path.of("oas/schema.json"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void isValidProductListRequest() throws Exception {
        final ProductController productsController = new ProductController(new ProductRepository());
        final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(productsController).build();
        mockMvc
                .perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(openApi().isValid(SWAGGER_HUB_PRODUCTS_API_SPECS_URL));
    }

    @Test
    public void isValidProductRequest() throws Exception {
        final ProductController productsController = new ProductController(new ProductRepository());
        final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(productsController).build();
        mockMvc
                .perform(get("/product/09"))
                .andExpect(status().isOk())
                .andExpect(openApi().isValid(SWAGGER_HUB_PRODUCTS_API_SPECS_URL));
    }
}
