package kitchenpos;

import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

public class ParentContainer {

    private static MySQLContainer mysqlContainer = null;

    public static MySQLContainer getMysqlContainer() {

        return mysqlContainer == null ?
                new MySQLContainer(DockerImageName.parse("mysql"))
                        .withDatabaseName("test")
                : mysqlContainer;

    }


}
