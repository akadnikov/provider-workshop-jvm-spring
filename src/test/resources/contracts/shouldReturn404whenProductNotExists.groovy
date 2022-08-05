package contracts

import org.springframework.cloud.contract.spec.Contract
import org.springframework.http.MediaType

import java.net.http.HttpHeaders

Contract.make {

    description("should return 404 when product not exists")
    request {
        url("/product/11")
        headers {
            header(org.springframework.http.HttpHeaders.AUTHORIZATION, $(regex("Bearer (19|20)\\d\\d-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])T([01][1-9]|2[0123]):[0-5][0-9]")))
        }
        method GET()
    }
    response {
        status(404)
    }
}



