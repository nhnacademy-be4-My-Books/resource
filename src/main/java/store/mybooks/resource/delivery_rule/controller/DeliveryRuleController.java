package store.mybooks.resource.delivery_rule.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import store.mybooks.resource.delivery_rule.dto.DeliveryRuleModifyRequest;
import store.mybooks.resource.delivery_rule.dto.DeliveryRuleRegisterRequest;
import store.mybooks.resource.delivery_rule.dto.DeliveryRuleResponse;
import store.mybooks.resource.delivery_rule.service.DeliveryRuleService;

/**
 * packageName    : store.mybooks.resource.delivery_rule.controller
 * fileName       : DeliveryRuleController
 * author         : Fiat_lux
 * date           : 2/16/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/16/24        Fiat_lux       최초 생성
 */
@RestController
@RequestMapping("/api/deliveryRules")
public class DeliveryRuleController {
    private final DeliveryRuleService deliveryRuleService;

    public DeliveryRuleController(DeliveryRuleService deliveryRuleService) {
        this.deliveryRuleService = deliveryRuleService;
    }

    @GetMapping("/{deliveryRuleId}")
    public ResponseEntity<DeliveryRuleResponse> getDeliveryRule(
            @PathVariable("deliveryRuleId") Integer deliveryRuleId) {
        DeliveryRuleResponse deliveryRule = deliveryRuleService.getDeliveryRule(deliveryRuleId);

        return ResponseEntity.ok().body(deliveryRule);
    }

    @PostMapping
    public ResponseEntity<Void> createDeliveryRule(
            @RequestBody DeliveryRuleRegisterRequest deliveryRuleRegisterRequest) {
        deliveryRuleService.registerDeliveryRule(deliveryRuleRegisterRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{deliveryRuleId}")
    public ResponseEntity<Void> modifyDeliveryRule(@PathVariable("deliveryRuleId") Integer deliveryRuleId, @RequestBody
    DeliveryRuleModifyRequest deliveryRuleModifyRequest) {
        deliveryRuleService.modifyDeliveryRule(deliveryRuleId, deliveryRuleModifyRequest);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{deliveryRuleId}")
    public ResponseEntity<Void> deleteDeliveryRule(@PathVariable("deliveryRuleId") Integer deliveryRuleId) {
        deliveryRuleService.deleteDeliveryRule(deliveryRuleId);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
