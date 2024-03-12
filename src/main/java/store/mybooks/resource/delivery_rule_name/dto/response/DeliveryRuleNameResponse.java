package store.mybooks.resource.delivery_rule_name.dto.response;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * packageName    : store.mybooks.resource.delivery_name_rule.dto
 * fileName       : DeliveryNameRuleReadResponse
 * author         : Fiat_lux
 * date           : 2/16/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/16/24        Fiat_lux       최초 생성
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryRuleNameResponse {
    private String id;
    private LocalDate createdDate;
}