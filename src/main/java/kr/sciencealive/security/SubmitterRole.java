package kr.sciencealive.security;

import io.jmix.security.model.EntityAttributePolicyAction;
import io.jmix.security.model.EntityPolicyAction;
import io.jmix.security.model.SecurityScope;
import io.jmix.security.role.annotation.EntityAttributePolicy;
import io.jmix.security.role.annotation.EntityPolicy;
import io.jmix.security.role.annotation.ResourceRole;
import kr.sciencealive.entity.Edition;
import kr.sciencealive.entity.Speaker;
import kr.sciencealive.entity.Submission;

/** Research-institution submitter: can browse the site and create submissions. */
@ResourceRole(name = "PR Award · Submitter", code = SubmitterRole.CODE, scope = SecurityScope.UI)
public interface SubmitterRole {
    String CODE = "submitter-role";

    @EntityPolicy(entityClass = Submission.class, actions = {EntityPolicyAction.CREATE, EntityPolicyAction.READ})
    @EntityPolicy(entityClass = Edition.class, actions = EntityPolicyAction.READ)
    @EntityPolicy(entityClass = Speaker.class, actions = EntityPolicyAction.READ)
    @EntityAttributePolicy(entityClass = Submission.class, attributes = "*", action = EntityAttributePolicyAction.MODIFY)
    @EntityAttributePolicy(entityClass = Edition.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityAttributePolicy(entityClass = Speaker.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    void content();
}
