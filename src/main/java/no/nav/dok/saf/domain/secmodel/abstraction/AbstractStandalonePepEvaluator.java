package no.nav.dok.saf.domain.secmodel.abstraction;

public class AbstractStandalonePepEvaluator<T extends SecModel> implements StandalonePepEvaluator<T> {
    StandalonePepEvaluator parent;
    private boolean evaluated;


    public AbstractStandalonePepEvaluator(StandalonePepEvaluator parent) {
        this.parent = parent;
    }

    /*
     - populate current entity, e.g. sak  - key saksref parameter
            - populate aktoerId parameter from sak
         - evaluate pep2 - tematilgang?
            - store/cache saksbehandler, tema, accessDecision (in pep2)?
         - has sibline? - if bidragssak
                        - populate saksparter (sibling may be multi)
                        - evaluate pep3 - kode6/7/egenansatt/§19 on each part
     - parent is bruker - is bruker evaluated?
                        - if no enter parent bruker
                        - populate parent entity, bruker - key aktoerId parameter
                        - evaluate pep1 kode6/7/egenansatt


      - populate current entity, e.g. dokumentInfo - key dokumentInfo and variantformat parameters
        - if begrensning on dokumentInfo/variantformat evaluate pep5
      - parent is Journalpost - is journalpost evaluated?
            -if no enter parent Journalpost
            - populate parent entity, Journalpost - key journalpostId parameter
                 - populate saksref parameter from JP
            - if begrensning and/or journalstatus protected, evaluate pep3
      - parent is Sak - is sak evaluated?
                      if no enter parent Sak
                      ... etc

       List<T> fetchAndFilterAndEnforce(parameterContext, accessDecisionContext, secModelContext)


     */

    @Override
    public void evaluate(SecurityModelWorld securityModelWorld, AccessDecisionContext accessDecisionContext) {
        if (evaluated) {
            return;
        } else {
            parent.evaluate(securityModelWorld, accessDecisionContext);
            
        }
    }

    @Override
    public boolean isEvaluated() {
        return evaluated;
    }


}
