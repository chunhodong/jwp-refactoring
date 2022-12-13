package kitchenpos.table.application;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.persistence.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.persistence.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TableService {
    private final OrderRepository orderDao;
    private final OrderTableRepository orderTableDao;

    public TableService(final OrderRepository orderDao, final OrderTableRepository orderTableDao) {
        this.orderDao = orderDao;
        this.orderTableDao = orderTableDao;
    }

    @Transactional
    public OrderTableResponse create(final OrderTableRequest orderTableRequest) {
        return OrderTableResponse.of(orderTableDao.save(orderTableRequest.toOrderTable()));
    }

    public List<OrderTableResponse> list() {
        return orderTableDao.findAll()
                .stream()
                .map(OrderTableResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderTable changeEmpty(final Long orderTableId, final OrderTable orderTable) {
        OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        List<Order> savedOrders = orderDao.findAllByOrderTable(savedOrderTable);
        savedOrderTable.chnageEmpty(orderTable.isEmpty(),savedOrders);
        return orderTableDao.save(savedOrderTable);
    }

    @Transactional
    public OrderTable changeNumberOfGuests(final Long orderTableId, final OrderTable orderTable) {
        OrderTable savedOrderTable = orderTableDao.findById(orderTableId)
                .orElseThrow(IllegalArgumentException::new);
        savedOrderTable.changeNumberOfGuests(orderTable.getNumberOfGuests());
        return orderTableDao.save(savedOrderTable);
    }
}
