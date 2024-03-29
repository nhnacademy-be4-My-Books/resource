package store.mybooks.resource.author.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import store.mybooks.resource.author.entity.Author;

/**
 * packageName    : store.mybooks.resource.author.repository
 * fileName       : AuthorRepository
 * author         : newjaehun
 * date           : 2/20/24
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2/20/24        newjaehun       최초 생성
 */
public interface AuthorRepository extends JpaRepository<Author, Integer>, AuthorRepositoryCustom {
    Boolean existsByName(String name);
}
