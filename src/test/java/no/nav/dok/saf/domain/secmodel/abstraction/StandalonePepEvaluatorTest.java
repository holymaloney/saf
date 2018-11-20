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
import static org.mockito.Matchers.eq;
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
            return brukers.stream().filter(bruker -> parameterContext.getStringParameter("aktoerId").equals(bruker.getAktoerId())).collect(Collectors.toList());
        }
    }

    private class SakPepEvaluator extends StandalonePepEvaluator<Sak> {

        public SakPepEvaluator(StandalonePepEvaluator parent, SecModelDataFetcher<Sak> dataFetcher, Pep<Sak> pep, SecModelParameterAdapter parameterAdapter) {
            super(parent, dataFetcher, pep, parameterAdapter);
        }
    }

    private class SakParameterAdapter implements SecModelParameterAdapter<Sak> {

        @Override
        public ParameterContext extractSearchParameter(Sak sak) {
            Map<String, String> parameterMap = Maps.newHashMap();
            parameterMap.put("aktoerId", sak.getAktoerId());
            return new ParameterContext(parameterMap);
        }
    }

    private class SakDataFetcher implements SecModelDataFetcher<Sak>{

        @Override
        public List<Sak> fetchAndFilter(ParameterContext parameterContext) {
            List<Sak> saker = Lists.newArrayList( sak1, sak2);
            return saker.stream().filter( sak ->
                    sak.getArkivsakSystem().equals(parameterContext.getStringParameter("arkivSakSystem"))
                            && sak.getArkivsakRef().equals(parameterContext.getStringParameter("arkivSakRef"))
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
        public ParameterContext extractSearchParameter(Journalpost journalpost) {
            Map<String, String> parameterMap = Maps.newHashMap();
            parameterMap.put("arkivSakRef", journalpost.getArkivSakRef());
            parameterMap.put("arkivSakSystem", journalpost.getArkivSakSystem());
            return new ParameterContext(parameterMap);
        }
    }

    private class JPDataFetcher implements SecModelDataFetcher<Journalpost> {
        @Override
        public List<Journalpost> fetchAndFilter(ParameterContext parameterContext) {
            List<Journalpost> jps = Lists.newArrayList(
                    Journalpost.builder().journalpostId("1234").arkivSakRef("123").arkivSakSystem("FS22").build(),
                    Journalpost.builder().journalpostId("2345").arkivSakRef("234").arkivSakSystem("PEN").build()
            );
            if (parameterContext.getListParameter("journalpostIds") != null) {
                return jps.stream().filter(jp -> parameterContext.getListParameter("journalpostIds").contains(jp.getJournalpostId())).collect(Collectors.toList());
            } else if (parameterContext.getStringParameter("journalpostId") != null){
                return jps.stream().filter(jp -> jp.getJournalpostId().equals(parameterContext.getStringParameter("journalpostId"))).collect(Collectors.toList());
            } else if (parameterContext.getListParameter("psakSaker") != null || parameterContext.getListParameter("gsakSaker") != null) {
                return jps.stream().filter(jp ->
                        parameterContext.getListParameter("gsakSaker").contains(jp.getArkivSakRef()) && jp.getArkivSakSystem().equals("FS22")
                        || parameterContext.getListParameter("psakSaker").contains(jp.getArkivSakRef()) && jp.getArkivSakSystem().equals("PEN")
                        ).collect(Collectors.toList());
            } else {
                return Lists.newArrayList();
            }
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

    Sak sak1 = Sak.builder().aktoerId("123456789").arkivsakRef("123").arkivsakSystem("FS22").build();
    Sak sak2 = Sak.builder().aktoerId("123456789").arkivsakRef("234").arkivsakSystem("PEN").build();


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
        List<Journalpost> journalposts = jpPepEvaluator.fetchAndFilterAndEnforce(parameterContext, accessDecisionContext);
        assertTrue(journalposts.size() == 1);
        assertThat(journalposts.get(0).getJournalpostId(), Is.is("1234"));
    }

    @Test
    public void shouldReturnEmptyListIfAccessDenyOnBruker() {
        when(pep1.hasAccesOn(any(Bruker.class), any(AccessDecisionContext.class))).thenReturn(false);

        parameterContext.addStringSearchParameter("journalpostId", "1234");
        List<Journalpost> journalposts = jpPepEvaluator.fetchAndFilterAndEnforce(parameterContext, accessDecisionContext);
        assertTrue(CollectionUtils.isEmpty(journalposts));
    }

    @Test
    public void shouldSupportJournalpostListParameter() {
        List<String> journalpostIds = Lists.newArrayList("1234", "2345");
        parameterContext.addListSearchParameter("journalpostIds", journalpostIds);
        List<Journalpost> journalposts = jpPepEvaluator.fetchAndFilterAndEnforce(parameterContext, accessDecisionContext);
        assertTrue(journalposts.size() == 2);
    }

    @Test
    public void shouldLimitAccessToOneSak() {
        when(pep2.hasAccesOn(eq(sak1), any(AccessDecisionContext.class))).thenReturn(false);

        List<String> journalpostIds = Lists.newArrayList("1234", "2345");
        parameterContext.addListSearchParameter("journalpostIds", journalpostIds);
        List<Journalpost> journalposts = jpPepEvaluator.fetchAndFilterAndEnforce(parameterContext, accessDecisionContext);
        assertTrue(journalposts.size() == 1);
        assertThat(journalposts.get(0).getJournalpostId(), Is.is("2345"));
    }

    @Test
    public void shouldSupporSakListParameter() {
        List<String> gsakSaker = Lists.newArrayList("123");
        parameterContext.addListSearchParameter("gsakSaker", gsakSaker);
        List<String> psakSaker = Lists.newArrayList("234");
        parameterContext.addListSearchParameter("psakSaker", psakSaker);

        List<Journalpost> journalposts = jpPepEvaluator.fetchAndFilterAndEnforce(parameterContext, accessDecisionContext);
        assertTrue(journalposts.size() == 2);
    }



}