package store.mybooks.resource.user_address.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * packageName    : store.mybooks.resource.user_address.dto.response
 * fileName       : UserAddressGetResponse
 * author         : masiljangajji
 * date           : 2/19/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/19/24        masiljangajji       최초 생성
 */
public interface UserAddressGetResponse {

    String getAlias();

    String getRoadName();

    String getDetail();

    Integer getNumber();

    String getReference();

}
