package kitchenpos.menu.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.ParentContainer;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
import kitchenpos.utils.DatabaseCleanup;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Testcontainers
public class MenuGroupAcceptanceTest  {

    @LocalServerPort
    int port;

    @Container
    static final MySQLContainer postgreSQLContainer = new MySQLContainer("mysql:8")
            .withDatabaseName("test1");

    static {
        postgreSQLContainer.start();
    }

    @Autowired
    private DatabaseCleanup databaseCleanup;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        databaseCleanup.execute();
    }

    @DisplayName("메뉴 그룹을 생성한다")
    @Test
    void createMenuGroup() {
        ExtractableResponse<Response> response = 메뉴그룹_생성을_요청("치킨");

        assertEquals(HttpStatus.CREATED.value(), response.statusCode());
    }

    @DisplayName("메뉴 그룹 목록을 조회한다")
    @Test
    void getMenuGroupList() {
        메뉴그룹_생성을_요청("치킨");
        메뉴그룹_생성을_요청("피자");

        ExtractableResponse<Response> response = 메뉴그룹_목록을_요청();

        assertThat(response.jsonPath().getList(".", MenuGroupResponse.class)).hasSize(2);
    }

    public static ExtractableResponse<Response> 메뉴그룹_생성을_요청(String name) {
        MenuGroupRequest request = new MenuGroupRequest(name);
        return RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/menu-groups")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 메뉴그룹_목록을_요청() {
        return RestAssured.given().log().all()
                .when().get("/api/menu-groups")
                .then().log().all()
                .extract();
    }
}
