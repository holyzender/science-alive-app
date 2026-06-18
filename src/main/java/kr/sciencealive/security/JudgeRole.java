package kr.sciencealive.security;

import io.jmix.security.model.EntityAttributePolicyAction;
import io.jmix.security.model.EntityPolicyAction;
import io.jmix.security.model.SecurityScope;
import io.jmix.security.role.annotation.EntityAttributePolicy;
import io.jmix.security.role.annotation.EntityPolicy;
import io.jmix.security.role.annotation.ResourceRole;
import kr.sciencealive.entity.Edition;
import kr.sciencealive.entity.Submission;

/**
 * PR Award judge: reads and evaluates assigned submissions. The rule that a
 * judge cannot view/evaluate submissions from their own institution is
 * enforced in the Judge console using the judge's {@code institution}.
 */
@ResourceRole(name = "PR Award · Judge", code = JudgeRole.CODE, scope = SecurityScope.UI)
public interface JudgeRole {
    String CODE = "judge-role";

    @EntityPolicy(entityClass = Submission.class, actions = {EntityPolicyAction.READ, EntityPolicyAction.UPDATE})
    @EntityPolicy(entityClass = Edition.class, actions = EntityPolicyAction.READ)
    @EntityAttributePolicy(entityClass = Submission.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityAttributePolicy(entityClass = Edition.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    void content();
}
