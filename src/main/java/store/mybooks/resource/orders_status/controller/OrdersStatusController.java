package store.mybooks.resource.orders_status.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import store.mybooks.resource.orders_status.dto.request.OrdersStatusCreateRequest;
import store.mybooks.resource.orders_status.dto.response.OrdersStatusCreateResponse;
import store.mybooks.resource.orders_status.dto.response.OrdersStatusResponse;
import store.mybooks.resource.orders_status.service.OrdersStatusService;

/**
 * packageName    : store.mybooks.resource.orders_status.controller
 * fileName       : OrdersStatusController
 * author         : minsu11
 * date           : 2/16/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/16/24        minsu11       최초 생성
 */
@RestController
@RequestMapping("/api/orders-statuses")
@RequiredArgsConstructor
public class OrdersStatusController {
    private final OrdersStatusService ordersStatusService;

    @GetMapping("/{ordersStatusId}")
    public ResponseEntity<OrdersStatusResponse> getOrdersStatus(
            @PathVariable String ordersStatusId
    ) {
        OrdersStatusResponse response = ordersStatusService.getOrdersStatusById(ordersStatusId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<OrdersStatusResponse>> getOrdersStatusList(
    ) {
        List<OrdersStatusResponse> ordersStatusResponseList = ordersStatusService.getOrdersStatusList();
        return new ResponseEntity<>(ordersStatusResponseList, HttpStatus.OK);
    }
    

    @PostMapping
    public ResponseEntity<OrdersStatusCreateResponse> createOrdersStatus(
            @RequestBody OrdersStatusCreateRequest request
    ) {
        OrdersStatusCreateResponse response = ordersStatusService.createOrdersStatus(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
