package kitchenpos.utils;

import com.google.common.base.CaseFormat;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.stream.Collectors;

@Service
@ActiveProfiles("test")
public class DatabaseCleanup implements InitializingBean {
    @PersistenceContext
    private EntityManager entityManager;

    private List<String> tableNames;

    @Override
    public void afterPropertiesSet() {
        tableNames = entityManager.getMetamodel().getEntities().stream()
                .filter(e -> e.getJavaType().getAnnotation(Entity.class) != null)
                .map(e -> CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, e.getName()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void execute() {
        entityManager.flush();

        entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0;").executeUpdate();

        entityManager.createNativeQuery("TRUNCATE TABLE menu;").executeUpdate();
        entityManager.createNativeQuery("ALTER TABLE menu ALTER COLUMN id RESTART WITH 1;").executeUpdate();

        entityManager.createNativeQuery("TRUNCATE TABLE menu_group;").executeUpdate();
        entityManager.createNativeQuery("ALTER TABLE menu_group ALTER COLUMN id RESTART WITH 1;").executeUpdate();

        entityManager.createNativeQuery("TRUNCATE TABLE menu_product;").executeUpdate();
        entityManager.createNativeQuery("ALTER TABLE menu_product ALTER COLUMN seq RESTART WITH 1;").executeUpdate();

        entityManager.createNativeQuery("TRUNCATE TABLE product;").executeUpdate();
        entityManager.createNativeQuery("ALTER TABLE product ALTER COLUMN id RESTART WITH 1;").executeUpdate();

        entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1;").executeUpdate();


        //entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
    }

    private void initAutoIncrement(String tableName) {

        if(tableName.equals("order_line_item") || tableName.equals("menu_product")){
            entityManager.createNativeQuery("ALTER TABLE " + tableName + " ALTER COLUMN SEQ RESTART WITH 1").executeUpdate();
            return;
        }
        entityManager.createNativeQuery("ALTER TABLE " + tableName + " ALTER COLUMN ID RESTART WITH 1").executeUpdate();
    }
}
