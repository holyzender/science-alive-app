package kr.sciencealive.security;

import io.jmix.security.model.EntityAttributePolicyAction;
import io.jmix.security.model.EntityPolicyAction;
import io.jmix.security.role.annotation.EntityAttributePolicy;
import io.jmix.security.role.annotation.EntityPolicy;
import io.jmix.security.role.annotation.ResourceRole;
import kr.sciencealive.entity.Article;
import kr.sciencealive.entity.Edition;
import kr.sciencealive.entity.Speaker;
import kr.sciencealive.entity.Submission;
import kr.sciencealive.entity.Talk;

/**
 * Granted to the anonymous (not-logged-in) visitor. The public surfaces
 * (Home, Archive, PR Award gallery, Submit, Judge) are plain Vaadin routes
 * annotated {@code @AnonymousAllowed}; this role grants the read access their
 * DataManager queries need so the whole design is viewable without login.
 */
@ResourceRole(name = "Anonymous", code = AnonymousRole.CODE, scope = "UI")
public interface AnonymousRole {
    String CODE = "anonymous-role";

    @EntityPolicy(entityClass = Edition.class, actions = EntityPolicyAction.READ)
    @EntityPolicy(entityClass = Speaker.class, actions = EntityPolicyAction.READ)
    @EntityPolicy(entityClass = Submission.class, actions = EntityPolicyAction.READ)
    @EntityPolicy(entityClass = Talk.class, actions = EntityPolicyAction.READ)
    @EntityPolicy(entityClass = Article.class, actions = EntityPolicyAction.READ)
    @EntityAttributePolicy(entityClass = Edition.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityAttributePolicy(entityClass = Speaker.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityAttributePolicy(entityClass = Submission.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityAttributePolicy(entityClass = Talk.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    @EntityAttributePolicy(entityClass = Article.class, attributes = "*", action = EntityAttributePolicyAction.VIEW)
    void content();
}
