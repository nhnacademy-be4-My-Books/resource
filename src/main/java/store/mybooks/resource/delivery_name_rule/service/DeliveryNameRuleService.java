package store.mybooks.resource.delivery_name_rule.service;

import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.mybooks.resource.delivery_name_rule.dto.DeliveryNameRuleDto;
import store.mybooks.resource.delivery_name_rule.dto.DeliveryNameRuleModifyRequest;
import store.mybooks.resource.delivery_name_rule.dto.DeliveryNameRuleRegisterRequest;
import store.mybooks.resource.delivery_name_rule.entity.DeliveryNameRule;
import store.mybooks.resource.delivery_name_rule.exception.DeliveryNameRuleNotFoundException;
import store.mybooks.resource.delivery_name_rule.repository.DeliveryNameRuleRepository;

/**
 * packageName    : store.mybooks.resource.delivery_name_rule.service
 * fileName       : DeliveryNameRuleService
 * author         : Fiat_lux
 * date           : 2/15/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/15/24        Fiat_lux       최초 생성
 */
@Service
public class DeliveryNameRuleService {
    private final DeliveryNameRuleRepository deliveryNameRuleRepository;

    public DeliveryNameRuleService(DeliveryNameRuleRepository deliveryNameRuleRepository) {
        this.deliveryNameRuleRepository = deliveryNameRuleRepository;
    }

    @Transactional
    public void registerDeliveryNameRule(
            DeliveryNameRuleRegisterRequest deliveryNameRuleRegisterRequest) {
        DeliveryNameRule deliveryNameRule = new DeliveryNameRule(deliveryNameRuleRegisterRequest);
        this.deliveryNameRuleRepository.save(deliveryNameRule);
    }

    @Transactional(readOnly = true)
    public DeliveryNameRuleDto getDeliveryNameRule(Integer id) {
        Optional<DeliveryNameRuleDto> optionalDeliveryNameRule =
                this.deliveryNameRuleRepository.findDeliveryNameRuleById(id);

        if (optionalDeliveryNameRule.isPresent()) {
            return optionalDeliveryNameRule.get();
        } else {
            throw new DeliveryNameRuleNotFoundException("배송 이름 규칙이 존재하지 않습니다.");
        }
    }

    @Transactional
    public void modifyDeliveryNameRule(Integer deliveryNameRuleId,
                                       DeliveryNameRuleModifyRequest deliveryNameRuleModifyRequest) {
        Optional<DeliveryNameRule> optionalDeliveryNameRule =
                this.deliveryNameRuleRepository.findById(deliveryNameRuleId);

        if (optionalDeliveryNameRule.isPresent()) {
            DeliveryNameRule deliveryNameRule = optionalDeliveryNameRule.get();
            deliveryNameRule.setDeliveryNameRule(deliveryNameRuleModifyRequest);
        } else {
            throw new DeliveryNameRuleNotFoundException("배송 이름 규칙이 존재하지 않습니다.");
        }
    }

    @Transactional
    public void deleteDeliveryNameRule(Integer id) {
        if (this.deliveryNameRuleRepository.existsById(id)) {
            this.deliveryNameRuleRepository.deleteById(id);
        } else {
            throw new DeliveryNameRuleNotFoundException("배송 이름 규칙이 존재하지 않습니다.");
        }
    }
}
