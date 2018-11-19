package no.nav.dok.saf.domain.secmodel.abstraction;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import no.nav.dok.saf.domain.secmodel.Bruker;
import no.nav.dok.saf.domain.secmodel.Journalpost;
import no.nav.dok.saf.domain.secmodel.Sak;
import org.junit.Before;
import org.junit.Test;
import sun.security.krb5.internal.PAEncTSEnc;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AbstractStandalonePepEvaluatorTest {

    private class BrukerPepEvaluator extends AbstractStandalonePepEvaluator<Bruker> {

        public BrukerPepEvaluator(StandalonePepEvaluator parent, SecModelDataFetcher dataFetcher, Pep pep) {
            super(parent, dataFetcher, pep);
        }

        @Override
        Map<String, Object> extractParentSearchParameter(List secModelResult) {
            return Maps.newHashMap();
        }
    }

    private class BrukerDataFetcher implements SecModelDataFetcher<Bruker> {

        @Override
        public List<Bruker> fetchAndFilter(ParameterContext parameterContext) {
            return Lists.newArrayList();
        }
    }

    private class Pep1 implements Pep<Bruker> {

        @Override
        public boolean hasAccesOn(Bruker resource, AccessDecisionContext accessDecisionContext) {
            return true;
        }
    }

    private class SakPepEvaluator extends AbstractStandalonePepEvaluator<Sak> {

        public SakPepEvaluator(StandalonePepEvaluator parent, SecModelDataFetcher<Sak> dataFetcher, Pep<Sak> pep) {
            super(parent, dataFetcher, pep);
        }

        @Override
        Map<String, List<String>> extractParentSearchParameter(List<Sak> secModelResult) {
            List<String> aktoerIds = Lists.newArrayList();
            secModelResult.forEach( sak -> aktoerIds.add(sak.aktoerId));
            Map<String, List<String>> parameterMap = Maps.newHashMap();
            parameterMap.put("aktoerIds", aktoerIds);
            return parameterMap;
        }
    }

    private class SakDataFetcher implements SecModelDataFetcher<Sak>{

        @Override
        public List<Sak> fetchAndFilter(ParameterContext parameterContext) {
            return Lists.newArrayList();
        }
    }

    private class Pep2 implements Pep<Sak> {

        @Override
        public boolean hasAccesOn(Sak resource, AccessDecisionContext accessDecisionContext) {
            return true;
        }
    }

    private class JPPepEvaluator extends AbstractStandalonePepEvaluator<Journalpost> {

        public JPPepEvaluator(StandalonePepEvaluator parent, SecModelDataFetcher<Journalpost> dataFetcher, Pep<Journalpost> pep) {
            super(parent, dataFetcher, pep);
        }

        @Override
        Map<String, ?> extractParentSearchParameter(List<Journalpost> secModelResult) {
            List<String> psakSaker = secModelResult.stream().filter( jp -> jp.arkivSakSystem.equals("PEN")).map(jp -> jp.arkivSakRef).collect(Collectors.toList());
            List<String> gsakSaker = secModelResult.stream().filter( jp -> jp.arkivSakSystem.equals("FS22")).map(jp -> jp.arkivSakRef).collect(Collectors.toList());
            Map<String, List<String>> parameterMap = Maps.newHashMap();
            parameterMap.put("gsakSaker", gsakSaker);
            parameterMap.put("psakSaker", psakSaker);
            return parameterMap;
        }
    }

    private class JPDataFetcher implements SecModelDataFetcher<Journalpost> {
        @Override
        public List<Journalpost> fetchAndFilter(ParameterContext parameterContext) {
            return Lists.newArrayList();
        }
    }

    private class Pep3 implements Pep<Journalpost> {

        @Override
        public boolean hasAccesOn(Journalpost resource, AccessDecisionContext accessDecisionContext) {
            return true;
        }
    }

    JPPepEvaluator jpPepEvaluator;
    SakPepEvaluator sakPepEvaluator;
    BrukerPepEvaluator brukerPepEvaluator;

    @Before
    public void setUp() {
        brukerPepEvaluator = new BrukerPepEvaluator(null, new BrukerDataFetcher(), new Pep1());
        sakPepEvaluator = new SakPepEvaluator(brukerPepEvaluator, new SakDataFetcher(), new Pep2());
        jpPepEvaluator = new JPPepEvaluator(sakPepEvaluator, new JPDataFetcher(), new Pep3());
    }

    @Test
    public void canaryTest() {
        AccessDecisionContext accessDecisionContext = new AccessDecisionContext();
        ParameterContext parameterContext = new ParameterContext();
        parameterContext.addStringSearchParameter("journalpostId", "1234");
        Stream<Journalpost> journalpostStream = jpPepEvaluator.fetchAndFilterAndEnforce(parameterContext, accessDecisionContext);

    }
}