package store.mybooks.resource.tag.service;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.mybooks.resource.tag.dto.request.TagCreateRequest;
import store.mybooks.resource.tag.dto.request.TagModifyRequest;
import store.mybooks.resource.tag.dto.response.TagCreateResponse;
import store.mybooks.resource.tag.dto.response.TagDeleteResponse;
import store.mybooks.resource.tag.dto.response.TagGetResponse;
import store.mybooks.resource.tag.dto.response.TagModifyResponse;
import store.mybooks.resource.tag.entity.Tag;
import store.mybooks.resource.tag.exception.TagNameAlreadyExistsException;
import store.mybooks.resource.tag.exception.TagNotExistsException;
import store.mybooks.resource.tag.repository.TagRepository;

/**
 * packageName    : store.mybooks.resource.tag.service
 * fileName       : TagService
 * author         : damho-lee
 * date           : 2/17/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/17/24          damho-lee          최초 생성
 */
@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;

    @Transactional(readOnly = true)
    public List<TagGetResponse> getTags() {
        return tagRepository.findAllBy();
    }

    /**
     * methodName : createTag
     * author : damho-lee
     * description : tag 생성하는 메서드.
     *
     * @param tagCreateRequest name 을 포함. TagName 이 이미 존재하는 경우 TagNameAlreadyExistsException.
     * @return TagCreateResponse
     */
    @Transactional
    public TagCreateResponse createTag(TagCreateRequest tagCreateRequest) {
        if (tagRepository.existsByName(tagCreateRequest.getName())) {
            throw new TagNameAlreadyExistsException();
        }

        return tagRepository.save(new Tag(tagCreateRequest)).convertToCreateResponse();
    }

    /**
     * methodName : modifyTag
     * author : damho-lee
     * description : 태그 수정하는 메서드.
     *
     * @param id               수정하고자 하는 tag 의 Id.
     *                         존재하지 않는 Id 인 경우 TagNotExistsException.
     * @param tagModifyRequest name 을 포함.
     *                         이미 존재하는 name 인 경우 TagNameAlreadyExistsException.
     * @return TagModifyResponse
     */
    @Transactional
    public TagModifyResponse modifyTag(int id, TagModifyRequest tagModifyRequest) {
        Optional<Tag> optionalTag = tagRepository.findById(id);
        if (optionalTag.isEmpty()) {
            throw new TagNotExistsException();
        }

        if (tagRepository.existsByName(tagModifyRequest.getName())) {
            throw new TagNameAlreadyExistsException();
        }

        Tag tag = optionalTag.get();
        tag.setByTagModifyRequest(tagModifyRequest);

        return TagModifyResponse.builder()
                .name(tag.getName())
                .build();
    }

    /**
     * methodName : deleteTag
     * author : damho-lee
     * description : 태그 삭제 메서드.
     *
     * @param id 삭제하려는 Tag 의 id.
     * @return TagDeleteResponse
     */
    @Transactional
    public TagDeleteResponse deleteTag(int id) {
        Tag tag = tagRepository.findById(id).orElseThrow(TagNotExistsException::new);

        tagRepository.deleteById(id);

        return TagDeleteResponse.builder()
                .name(tag.getName())
                .build();
    }
}