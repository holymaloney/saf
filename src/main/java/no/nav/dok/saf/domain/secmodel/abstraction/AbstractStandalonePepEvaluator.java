package no.nav.dok.saf.domain.secmodel.abstraction;

import java.util.List;

public class AbstractStandalonePepEvaluator<T extends SecModel> implements StandalonePepEvaluator<T> {
    StandalonePepEvaluator parent;
    List<T> evaluateAccessOn;
    private boolean evaluated;


    public AbstractStandalonePepEvaluator(StandalonePepEvaluator parent) {
        this.parent = parent;
    }

    @Override
    public void evaluate(SecurityModelWorld securityModelWorld, AccessDecicionContext accessDecicionContext) {
        if (evaluated) {
            return;
        } else {
            parent.evaluate(securityModelWorld, accessDecicionContext);
            
        }
    }

    @Override
    public boolean isEvaluated() {
        return evaluated;
    }
}
