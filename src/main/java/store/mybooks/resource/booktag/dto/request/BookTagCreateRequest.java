package store.mybooks.resource.booktag.dto.request;

import java.util.List;
import javax.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * packageName    : store.mybooks.resource.book_tag.dto.request
 * fileName       : BookTagCreateRequest
 * author         : damho-lee
 * date           : 2/28/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/28/24          damho-lee          최초 생성
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BookTagCreateRequest {
    @Positive
    private Long bookId;
    private List<Integer> tagIdList;
}