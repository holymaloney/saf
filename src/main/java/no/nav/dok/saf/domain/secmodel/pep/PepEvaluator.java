package no.nav.dok.saf.domain.secmodel.pep;

import graphql.schema.DataFetchingEnvironment;

/**
 * Evaluerer tilgang til ressurs. Implementasjonen bør være request-scoped, og ta vare på tidligere tilgangsbeslutninger,
 * slik at pdp ikke kalles unødig. DataFetchingEnvironment kan brukes til å overføre sikkerhets-context og annen context,
 * slik som søkeparametre og tidligere tilgangsbeslutninger.
 * @param <T> Ressursetypen som evalueres - action er alltid READ
 */
public interface PepEvaluator<T> {
    boolean hasAccesOn(T resource, DataFetchingEnvironment context);
}
