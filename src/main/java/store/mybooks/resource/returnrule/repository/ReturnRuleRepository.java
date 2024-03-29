package store.mybooks.resource.returnrule.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import store.mybooks.resource.returnrule.entity.ReturnRule;

/**
 * packageName    : store.mybooks.resource.return_rule.repository<br>
 * fileName       : ReturnRuleRespository<br>
 * author         : minsu11<br>
 * date           : 2/21/24<br>
 * description    :
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 2/21/24        minsu11       최초 생성<br>
 */
public interface ReturnRuleRepository extends JpaRepository<ReturnRule, Integer>, ReturnRuleRepositoryCustom {
}
