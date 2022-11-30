import com.linkedplanet.ktorbase.routes.*
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import io.restassured.response.ValidatableResponse
import org.junit.BeforeClass
import kotlin.test.Test

/*
 * Meant to run against the fully built application, deployed e.g. as Docker Container.
 *
 * For testing backend logic, rather use: https://ktor.io/docs/testing.html
 */
class IntegrationTest {

    @Test
    fun loginSuccess() {
        login(BackendConfig.adminUsername, BackendConfig.adminPassword)
            .statusCode(200)
    }

    @Test
    fun loginFailure() {
        login("foo", "bar")
            .statusCode(401)
    }

    private fun login(username: String, password: String): ValidatableResponse {
        val jsonBody = LoginBody(username, password)
        return given()
            .contentType(ContentType.JSON)
            .body(jsonBody)
            .post(SESSION_ENDPOINT_PATH)
            .then()
    }

    companion object {
        @BeforeClass
        @JvmStatic
        fun configureRestAssured() {
            RestAssured.baseURI = BackendConfig.baseUrl
            RestAssured.port = BackendConfig.port
            RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
        }
    }

}
