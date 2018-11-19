package no.nav.dok.saf.domain.secmodel.abstraction;

import no.nav.dok.saf.domain.secmodel.pep.PepEvaluator;

import java.util.List;
import java.util.Map;

public abstract class AbstractStandalonePepEvaluator<T extends SecModel> implements StandalonePepEvaluator<T> {
    StandalonePepEvaluator parent;
    SecModelDataFetcher<T> dataFetcher;
    PepEvaluator<T> pepX;


    public AbstractStandalonePepEvaluator(StandalonePepEvaluator parent) {
        this.parent = parent;
    }

    /*
     - populate current entity, e.g. sak  - key saksref parameter
            - populate aktoerId parameter from sak
         - fetchAndFilterAndEnforce pep2 - tematilgang?
            - store/cache saksbehandler, tema, accessDecision (in pep2)?
         - has sibline? - if bidragssak
                        - populate saksparter (sibling may be multi)
                        - fetchAndFilterAndEnforce pep3 - kode6/7/egenansatt/§19 on each part
     - parent is bruker - is bruker evaluated?
                        - if no enter parent bruker
                        - populate parent entity, bruker - key aktoerId parameter
                        - fetchAndFilterAndEnforce pep1 kode6/7/egenansatt


      - populate current entity, e.g. dokumentInfo - key dokumentInfo and variantformat parameters
        - if begrensning on dokumentInfo/variantformat fetchAndFilterAndEnforce pep5
      - parent is Journalpost - is journalpost evaluated?
            -if no enter parent Journalpost
            - populate parent entity, Journalpost - key journalpostId parameter
                 - populate saksref parameter from JP
            - if begrensning and/or journalstatus protected, fetchAndFilterAndEnforce pep3
      - parent is Sak - is sak evaluated?
                      if no enter parent Sak
                      ... etc

       List<T> fetchAndFilterAndEnforce(parameterContext, accessDecisionContext, secModelContext)


     */

    @Override
    public void fetchAndFilterAndEnforce(boolean evaluateParent, ParameterContext parameterContext, AccessDecisionContext accessDecicionContext, SecurityModelWorld securityModelWorld) {
        List<T> secModelResult = dataFetcher.fetchAndFilter(parameterContext);

        if (evaluateParent) {
            Map<String, Object> parentSearchParameters = extractParentSearchParameter(secModelResult);
            parameterContext.addSearchParameters(parentSearchParameters);
            parent.fetchAndFilterAndEnforce(evaluateParent, parameterContext, accessDecicionContext, securityModelWorld );
        }

        secModelResult.stream().filter( e -> pepX.hasAccesOn(e, accessDecicionContext, securityModelWorld));

    }

    abstract Map<String, Object> extractParentSearchParameter(List<T> secModelResult);


}
