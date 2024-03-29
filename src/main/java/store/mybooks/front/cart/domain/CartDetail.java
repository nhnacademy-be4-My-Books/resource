package store.mybooks.front.cart.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * packageName    : store.mybooks.front.cart.domain <br/>
 * fileName       : CartDetail<br/>
 * author         : Fiat_lux <br/>
 * date           : 3/13/24<br/>
 * description    :<br/>
 * ===========================================================<br/>
 * DATE              AUTHOR             NOTE<br/>
 * -----------------------------------------------------------<br/>
 * 3/13/24        Fiat_lux       최초 생성<br/>
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CartDetail {
    private Long bookId;
    private int cartDetailAmount;
    private String name;
    private String bookImage;
    private Integer cost;
    private Integer saleCost;
    private Integer stock;
    private String sellingStatus;
}
