package no.nav.dok.saf.domain.secmodel.abstraction;

public interface StandalonePepEvaluator<T extends SecModel> {

    void fetchAndFilterAndEnforce(boolean evaluateParent, ParameterContext parameterContext, AccessDecisionContext accessDecicionContext, SecurityModelWorld securityModelWorld);

}
