package store.mybooks.resource.category.dto.response;

import lombok.Getter;
import lombok.Setter;

/**
 * packageName    : store.mybooks.resource.category.dto.response
 * fileName       : CategoryModifyResponse
 * author         : damho-lee
 * date           : 2/16/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/16/24          damho-lee          최초 생성
 */
@Getter
@Setter
public class CategoryModifyResponse {
    private Integer parentCategoryId;
    private String parentCategoryName;
    private String name;
}
