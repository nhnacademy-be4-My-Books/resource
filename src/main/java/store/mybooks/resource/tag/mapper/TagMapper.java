package store.mybooks.resource.tag.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import store.mybooks.resource.tag.dto.response.TagCreateResponse;
import store.mybooks.resource.tag.dto.response.TagDeleteResponse;
import store.mybooks.resource.tag.dto.response.TagModifyResponse;
import store.mybooks.resource.tag.entity.Tag;

/**
 * packageName    : store.mybooks.resource.tag.mapper
 * fileName       : TagMapper
 * author         : damho-lee
 * date           : 2/19/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/19/24          damho-lee          최초 생성
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface TagMapper {
    TagMapper INSTANCE = Mappers.getMapper(TagMapper.class);

    TagCreateResponse createResponse(Tag tag);

    TagModifyResponse modifyResponse(Tag tag);

    TagDeleteResponse deleteResponse(Tag tag);
}
