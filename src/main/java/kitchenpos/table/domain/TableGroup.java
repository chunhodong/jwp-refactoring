package kitchenpos.table.domain;

import kitchenpos.common.BaseEntity;
import kitchenpos.order.domain.Order;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "table_group")
public class TableGroup extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private OrderTables orderTables = new OrderTables();

    protected TableGroup() {
    }

    private TableGroup(TableGroupBuilder builder) {
        this.id = builder.id;
        this.orderTables = builder.orderTables;
    }

    public void addOrderTables(List<OrderTable> orderTables) {
        this.orderTables.addOrderTables(this,orderTables);
    }

    public void ungroup(List<Order> orders) {
        orders.forEach(Order::validateBeforeCompleteStatus);
        orderTables.ungroup();
    }

    public Long getId() {
        return id;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables.getOrderTables();
    }

    public static class TableGroupBuilder {
        private Long id;
        private OrderTables orderTables = new OrderTables();

        public TableGroupBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public TableGroupBuilder orderTables(OrderTables orderTables) {
            this.orderTables = orderTables;
            return this;
        }

        public TableGroup build() {
            return new TableGroup(this);
        }
    }

    public static TableGroupBuilder builder() {
        return new TableGroupBuilder();
    }
}
