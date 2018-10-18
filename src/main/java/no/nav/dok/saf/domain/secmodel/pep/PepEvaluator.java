package no.nav.dok.saf.domain.secmodel.pep;

import graphql.schema.DataFetchingEnvironment;

public interface PepEvaluator<T> {
    boolean hasAccesOn(T resource, DataFetchingEnvironment context);
}
