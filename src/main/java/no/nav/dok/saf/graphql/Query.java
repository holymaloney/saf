package no.nav.dok.saf.graphql;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import no.nav.dok.saf.domain.DomainCoordinator;
import no.nav.dok.saf.domain.viewmodel.Journalpost;

import java.util.List;

public class Query implements GraphQLQueryResolver {
    DomainCoordinator coordinator;
    public List<Journalpost> getJournalposterForBruker(String aktør, List<String> temaer, List<String> journalStatuser, DataFetchingEnvironment environment) {
        return coordinator.getJournalposterForBruker(aktør, temaer, journalStatuser, environment);
    }
}