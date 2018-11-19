package no.nav.dok.saf.domain.secmodel.abstraction;

import java.util.stream.Stream;

public interface StandalonePepEvaluator<T extends SecModel> {

    Stream<T> fetchAndFilterAndEnforce(ParameterContext parameterContext, AccessDecisionContext accessDecicionContext);

}
