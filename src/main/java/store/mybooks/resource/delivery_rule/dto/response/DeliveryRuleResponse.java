package store.mybooks.resource.delivery_rule.dto.response;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * packageName    : store.mybooks.resource.delivery_rule.dto
 * fileName       : DeliveryRuleResponse
 * author         : Fiat_lux
 * date           : 2/16/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/16/24        Fiat_lux       최초 생성
 */

@Getter
@AllArgsConstructor
public class DeliveryRuleResponse {

    private Integer id;

    private String deliveryRuleNameId;

    private String companyName;

    private Integer cost;

    private Integer ruleCost;

    private LocalDate createdDate;

    private Integer isAvailable;
}