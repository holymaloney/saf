package no.nav.dok.saf.domain;

import com.google.common.collect.Lists;
import graphql.execution.batched.BatchedDataFetcher;
import graphql.schema.DataFetchingEnvironment;
import no.nav.dok.saf.domain.secmodel.Bruker;
import no.nav.dok.saf.domain.secmodel.Journalpost;
import no.nav.dok.saf.domain.secmodel.Sak;
import no.nav.dok.saf.domain.secmodel.pep.PepEvaluator;
import no.nav.dok.saf.domain.secmodel.repo.SecModelRepo;
import no.nav.dok.saf.domain.viewmodel.repo.ViewModelRepo;


import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DomainCoordinator implements BatchedDataFetcher {
    SecModelRepo secModelRepo;
    ViewModelRepo viewModelRepo;

    PepEvaluator<Bruker> pep1;
    PepEvaluator<Sak> pep2;
    PepEvaluator<Journalpost> pep3;

    public List<no.nav.dok.saf.domain.viewmodel.Journalpost> getJournalposterForBruker(String aktørId, List<String> temaer, List<String> journalStatuser, DataFetchingEnvironment dataFetchingEnvironment) {
        Bruker bruker = secModelRepo.getBrukerForAktør(aktørId);
        if (!pep1.hasAccesOn(bruker, dataFetchingEnvironment)) return Lists.newArrayList();

        List<Sak> sakerForBruker = secModelRepo.getSakerForBruker(bruker, temaer);
        List<Sak> filteredSakerForBruker = sakerForBruker.stream().filter(sak -> pep2.hasAccesOn(sak, dataFetchingEnvironment)).collect(Collectors.toList());

        List<Journalpost> journalposter = secModelRepo.getJournalposterForSaker(filteredSakerForBruker, null, null, null, -1, -1);
        Stream<Journalpost> filteredJournalposter = journalposter.stream().filter(journalpost -> pep3.hasAccesOn(journalpost, dataFetchingEnvironment));

        List<Long> journalpostIder = filteredJournalposter.map( jp -> jp.journalpostId).collect(Collectors.toList());
        List<no.nav.dok.saf.domain.viewmodel.Journalpost> viewModelJournalposter = viewModelRepo.getJournalposter(journalpostIder);

        // Ide1: mest effektivt å filtrere bort dokumenter etter fetch?
        // Ide2: secmodel.repo.JournalpostRepo holder ferdig filtrert liste? Da må filtrert liste fores tilbake etter filtrering.
        return viewModelJournalposter;
    }

    @Override
    public List<no.nav.dok.saf.domain.viewmodel.Journalpost> get(DataFetchingEnvironment environment) {
        String aktoerId = environment.getArgument("aktoerId");
        List<String> temaer = environment.getArgument("temaer");
        List<String> journalStatuser = environment.getArgument("journalStatuser");
        Date opprettetFør = environment.getArgument("opprettet");
        return getJournalposterForBruker(aktoerId, temaer, journalStatuser, environment);
    }
}
