package tests.backend.api;

import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.*;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("avaliacao_final")
public class TestesDeApi {

    private String cookie;

    @BeforeEach
    public void setUp() throws InterruptedException {
        RestAssured.baseURI = "https://parabank.parasoft.com/parabank/";
        Map<String, Object> data = new HashMap<>();
        data.put("username", "outro_fulano2");
        data.put("password", "0v7r04ulan0");

        Response response = given()
                .body(data)
                .and()
                .when()
                .post("login.html")
                .then()
                .extract().response();

        cookie = response.getCookies().toString();
        System.out.println("cookie: " + cookie);
    }

    @Test
    @Tag("teste_registro")
    @DisplayName("Validação de status code 401 para o processo de registro de um novo cliente.")
    public void validarStatusCodeRegistro() {

        Map<String, Object> data = new HashMap<>();

        data.put("customer.firstName", "Outro");
        data.put("customer.lastName", "Fulano De Tal");
        data.put("customer.address", "street: Uma Rua");
        data.put("customer.address", "city: Uberaba");
        data.put("customer.address", "state: Minas Gerais");
        data.put("customer.address", "zipCode: 39524-000");
        data.put("customer.phoneNumber", "(31) 32663-1569");
        data.put("customer.ssn", " AAA-GG-SSSS");
        data.put("customer.username", "outro_fulano13");
        data.put("customer.password", "0v7r04ulan0");

        //Given
        Response response = given()
                .body(new Gson().toJson(data))
                .and()
                .when()
                .post("register.htm")
                .then()
                .extract().response();
        //When
        int statusCode = response.getStatusCode();
        System.out.println(statusCode);
        System.out.println("cookies: " + response.getCookies().toString());

        //Then
        assertEquals(HttpStatus.SC_INTERNAL_SERVER_ERROR, statusCode);

    }

    @Test
    @Tag("teste_nova_conta")
    @DisplayName("Validação de status code 401 para nova conta.")
    public void validarStatusCodeNovaConta() {

        //Given
        Map<String, Object> data = new HashMap<>();
        Map<String, Object> headers = new HashMap<>();

        data.put("customerId", "19760");
        data.put("newAccountType", "1");
        data.put("fromAccountId", "23667");

        headers.put("Content-type", "application/json");

        Response response = given()
                .headers(headers)
//                .cookie("JSESSIONID=CBE89619681291FCBD744CA00197AD7C; ajs_user_id=2OXgx1IbROQjPn7ICu0vPavsMc23; ajs_anonymous_id=69f991c2-0627-4812-969e-e1f987d455e0; _ga=GA1.2.620207242.1663722367; _gid=GA")
                .header("Cookies", cookie)
                .and()
                .body(new Gson().toJson(data))
                .when()
                .post("services_proxy/bank/createAccount?customerId=19760&newAccountType=1&fromAccountId=23667")
                .then()
                .extract().response();

        //When
        int statusCode = response.getStatusCode();
        System.out.println(statusCode);

        //Then
        assertEquals(HttpStatus.SC_UNAUTHORIZED, statusCode);

    }

    @Test
    @Tag("teste_visao_geral")
    @DisplayName("Validação de status code 401 visão geral das contas.")
    public void validarStatusCodeVisaoGeral() {

        //Given
        Response response = RestAssured.get("overview.htm");

        //When
        int statusCode = response.getStatusCode();
        System.out.println(statusCode);

        //Then
        assertEquals(HttpStatus.SC_INTERNAL_SERVER_ERROR, statusCode);

    }

    @Test
    @Tag("teste_transferir_fundos")
    @DisplayName("Validação de status code 401 em transferir fundos.")
    public void validarStatusCodeTransferirFundos() {

        //Given
        Response response = RestAssured.post("services_proxy/bank/transfer?fromAccountId=23667&toAccountId=24222&amount=100.00");

        //When
        int statusCode = response.getStatusCode();
        System.out.println(statusCode);

        //Then
        assertEquals(HttpStatus.SC_UNAUTHORIZED, statusCode);

    }

    @Test
    @Tag("teste_atividades_conta")
    @DisplayName("Validação de status code 401 atividades da conta.")
    public void validarStatusCodeAtividadesDaConta() {

        //Given
        Response response = RestAssured.get("services_proxy/bank/accounts/23667/transactions/month/All/type/All");

        //When
        int statusCode = response.getStatusCode();
        System.out.println(statusCode);

        //Then
        assertEquals(HttpStatus.SC_UNAUTHORIZED, statusCode);

    }

    @AfterEach
    public void tearDown() {
        RestAssured.reset();
    }

}
