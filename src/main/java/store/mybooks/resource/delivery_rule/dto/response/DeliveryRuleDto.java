package store.mybooks.resource.delivery_rule.dto.response;

import java.time.LocalDate;

/**
 * packageName    : store.mybooks.resource.delivery_rule.dto
 * fileName       : DeliveryRuleDto
 * author         : Fiat_lux
 * date           : 2/17/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/17/24        Fiat_lux       최초 생성
 */
public interface DeliveryRuleDto {
    Integer getId();

    String getDeliveryRuleName();

    String getCompanyName();

    Integer getCost();

    Integer getRuleCost();

    LocalDate getCreatedDate();

    Integer getIsAvailable();
}
