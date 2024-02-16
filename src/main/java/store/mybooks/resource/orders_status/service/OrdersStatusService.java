package store.mybooks.resource.orders_status.service;

import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import store.mybooks.resource.orders_status.dto.request.OrdersStatusCreateRequest;
import store.mybooks.resource.orders_status.dto.request.OrdersStatusRequest;
import store.mybooks.resource.orders_status.dto.response.OrdersStatusCreateResponse;
import store.mybooks.resource.orders_status.dto.response.OrdersStatusResponse;
import store.mybooks.resource.orders_status.entity.OrdersStatus;
import store.mybooks.resource.orders_status.exception.OrdersStatusAlreadyExistException;
import store.mybooks.resource.orders_status.exception.OrdersStatusNotFoundException;
import store.mybooks.resource.orders_status.repository.OrdersStatusRepository;

/**
 * packageName    : store.mybooks.resource.orders_status.service
 * fileName       : OrdersStatusService
 * author         : minsu11
 * date           : 2/15/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/15/24        minsu11       최초 생성
 */
@Service
@RequiredArgsConstructor
public class OrdersStatusService {
    private final OrdersStatusRepository ordersStatusRepository;

    public OrdersStatusResponse getOrdersStatus(OrdersStatusRequest request) {

        OrdersStatus ordersStatus = ordersStatusRepository.findById(
                request.getId()).orElseThrow(() -> new OrdersStatusNotFoundException());
        return ordersStatus.convertToOrdersStatusResponse();
    }

    public List<OrdersStatusResponse> getOrdersStatusList() {
        List<OrdersStatusResponse> ordersStatusResponses = ordersStatusRepository.getOrdersStatusList();

        if (Objects.isNull(ordersStatusResponses)) {
            throw new NullPointerException("order status null");
        }
        return ordersStatusResponses;
    }

    public OrdersStatusCreateResponse createOrdersStatus(OrdersStatusCreateRequest request) {
        if (ordersStatusRepository.findById(request.getId()).isPresent()) {
            throw new OrdersStatusAlreadyExistException("order status 이미 존재");
        }
        OrdersStatus ordersStatus = new OrdersStatus(request);
        ordersStatusRepository.save(ordersStatus);
        return ordersStatus.convertToOrdersStatusCreateResponse();
    }


    public OrdersStatusResponse deleteOrderStatus(OrdersStatusRequest request) {
        if (!ordersStatusRepository.findById(request.getId()).isPresent()) {
            throw new OrdersStatusNotFoundException("삭제할 order status 없음");
        }
        OrdersStatus ordersStatus = new OrdersStatus(request);
        ordersStatusRepository.delete(ordersStatus);
        return ordersStatus.convertToOrdersStatusResponse();
    }
}
