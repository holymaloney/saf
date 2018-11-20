package no.nav.dok.saf.domain.secmodel.abstraction;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import no.nav.dok.saf.domain.secmodel.Bruker;
import no.nav.dok.saf.domain.secmodel.Journalpost;
import no.nav.dok.saf.domain.secmodel.Sak;
import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.util.CollectionUtils;


import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class StandalonePepEvaluatorTest {

    private class BrukerPepEvaluator extends StandalonePepEvaluator<Bruker> {

        public BrukerPepEvaluator(StandalonePepEvaluator parent, SecModelDataFetcher dataFetcher, Pep pep, SecModelParameterAdapter parameterAdapter) {
            super(parent, dataFetcher, pep, parameterAdapter);
        }
    }

    private class BrukerDataFetcher implements SecModelDataFetcher<Bruker> {

        @Override
        public List<Bruker> fetchAndFilter(ParameterContext parameterContext) {
            List<Bruker> brukers = Lists.newArrayList(Bruker.builder().aktoerId("123456789").build());
            return brukers.stream().filter(bruker -> parameterContext.getListParameter("aktoerIds").contains(bruker.getAktoerId())).collect(Collectors.toList());
        }
    }

    private class SakPepEvaluator extends StandalonePepEvaluator<Sak> {

        public SakPepEvaluator(StandalonePepEvaluator parent, SecModelDataFetcher<Sak> dataFetcher, Pep<Sak> pep, SecModelParameterAdapter parameterAdapter) {
            super(parent, dataFetcher, pep, parameterAdapter);
        }
    }

    private class SakParameterAdapter implements SecModelParameterAdapter<Sak> {

        @Override
        public Map<String, ?> extractSearchParameter(List<Sak> secModelResult) {
            List<String> aktoerIds = Lists.newArrayList();
            secModelResult.forEach( sak -> aktoerIds.add(sak.getAktoerId()));
            Map<String, List<String>> parameterMap = Maps.newHashMap();
            parameterMap.put("aktoerIds", aktoerIds);
            return parameterMap;
        }
    }

    private class SakDataFetcher implements SecModelDataFetcher<Sak>{

        @Override
        public List<Sak> fetchAndFilter(ParameterContext parameterContext) {
            List<Sak> saker = Lists.newArrayList(
                    Sak.builder().aktoerId("123456789").arkivsakRef("234").arkivsakSystem("PEN").build(),
                    Sak.builder().aktoerId("123456789").arkivsakRef("123").arkivsakSystem("FS22").build()
            );
            return saker.stream().filter( sak ->
                    (sak.getArkivsakSystem().equals("FS22") && parameterContext.getListParameter("gsakSaker").contains(sak.getArkivsakRef()))
                    || (sak.getArkivsakSystem().equals("PEN") && parameterContext.getListParameter("psakSaker").contains(sak.getArkivsakRef()))
            ).collect(Collectors.toList());
        }
    }

    private class JPPepEvaluator extends StandalonePepEvaluator<Journalpost> {

        public JPPepEvaluator(StandalonePepEvaluator parent, SecModelDataFetcher<Journalpost> dataFetcher, Pep<Journalpost> pep, SecModelParameterAdapter parameterAdapter) {
            super(parent, dataFetcher, pep, parameterAdapter);
        }

    }

    private class JPParameterAdapter implements SecModelParameterAdapter<Journalpost> {
        @Override
        public Map<String, ?> extractSearchParameter(List<Journalpost> secModelResult) {
            List<String> psakSaker = secModelResult.stream().filter( jp -> jp.getArkivSakSystem().equals("PEN")).map(jp -> jp.getArkivSakRef()).collect(Collectors.toList());
            List<String> gsakSaker = secModelResult.stream().filter( jp -> jp.getArkivSakSystem().equals("FS22")).map(jp -> jp.getArkivSakRef()).collect(Collectors.toList());
            Map<String, List<String>> parameterMap = Maps.newHashMap();
            parameterMap.put("gsakSaker", gsakSaker);
            parameterMap.put("psakSaker", psakSaker);
            return parameterMap;
        }
    }

    private class JPDataFetcher implements SecModelDataFetcher<Journalpost> {
        @Override
        public List<Journalpost> fetchAndFilter(ParameterContext parameterContext) {
            List<Journalpost> jps = Lists.newArrayList(
                    Journalpost.builder().journalpostId("1234").arkivSakRef("123").arkivSakSystem("FS22").build(),
                    Journalpost.builder().journalpostId("2345").arkivSakRef("234").arkivSakSystem("PEN").build()
            );
            return jps.stream().filter(jp -> jp.getJournalpostId().equals(parameterContext.getStringParameter("journalpostId"))).collect(Collectors.toList());
        }
    }

    JPPepEvaluator jpPepEvaluator;
    SakPepEvaluator sakPepEvaluator;
    BrukerPepEvaluator brukerPepEvaluator;

    @Mock
    Pep<Bruker> pep1;
    @Mock
    Pep<Sak> pep2;
    @Mock
    Pep<Journalpost> pep3;

    AccessDecisionContext accessDecisionContext;
    ParameterContext parameterContext;


    @Before
    public void setUp() {
        accessDecisionContext = new AccessDecisionContext();
        parameterContext = new ParameterContext();

        when(pep1.hasAccesOn(any(Bruker.class), any(AccessDecisionContext.class))).thenReturn(true);
        when(pep2.hasAccesOn(any(Sak.class), any(AccessDecisionContext.class))).thenReturn(true);
        when(pep3.hasAccesOn(any(Journalpost.class), any(AccessDecisionContext.class))).thenReturn(true);

        brukerPepEvaluator = new BrukerPepEvaluator(null, new BrukerDataFetcher(), pep1, null);
        sakPepEvaluator = new SakPepEvaluator(brukerPepEvaluator, new SakDataFetcher(), pep2, new SakParameterAdapter());
        jpPepEvaluator = new JPPepEvaluator(sakPepEvaluator, new JPDataFetcher(), pep3, new JPParameterAdapter());
    }

    @Test
    public void shouldReturnJournalpostInHappyPath() {
        parameterContext.addStringSearchParameter("journalpostId", "1234");
        List<Journalpost> journalposts = jpPepEvaluator.fetchAndFilterAndEnforce(parameterContext, accessDecisionContext).collect(Collectors.toList());
        assertTrue(journalposts.size() == 1);
        assertThat(journalposts.get(0).getJournalpostId(), Is.is("1234"));
    }

    @Test
    public void shouldReturnEmptyListIfAccessDenyOnBruker() {
        when(pep1.hasAccesOn(any(Bruker.class), any(AccessDecisionContext.class))).thenReturn(false);

        parameterContext.addStringSearchParameter("journalpostId", "1234");
        List<Journalpost> journalposts = jpPepEvaluator.fetchAndFilterAndEnforce(parameterContext, accessDecisionContext).collect(Collectors.toList());
        assertTrue(CollectionUtils.isEmpty(journalposts));
    }


}