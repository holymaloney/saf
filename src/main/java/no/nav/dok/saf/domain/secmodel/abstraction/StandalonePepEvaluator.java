package no.nav.dok.saf.domain.secmodel.abstraction;

public interface StandalonePepEvaluator<T extends SecModel> {

    void evaluate(SecurityModelWorld securityModelWorld, AccessDecisionContext accessDecicionContext);

    boolean isEvaluated();
}
