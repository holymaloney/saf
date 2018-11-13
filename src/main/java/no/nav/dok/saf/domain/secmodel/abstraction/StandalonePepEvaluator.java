package no.nav.dok.saf.domain.secmodel.abstraction;

public interface StandalonePepEvaluator<T extends SecModel> {

    void evaluate(SecurityModelWorld securityModelWorld, AccessDecicionContext accessDecicionContext);

    boolean isEvaluated();
}
