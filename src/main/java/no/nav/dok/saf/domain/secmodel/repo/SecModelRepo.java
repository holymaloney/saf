package no.nav.dok.saf.domain.secmodel.repo;

import no.nav.dok.saf.domain.secmodel.Bruker;
import no.nav.dok.saf.domain.secmodel.Journalpost;
import no.nav.dok.saf.domain.secmodel.Sak;

import java.util.Date;
import java.util.List;

public interface SecModelRepo {
    Bruker getBrukerForAktør(String aktørId);
    List<Sak> getSakerForBruker(Bruker bruker, List<String> temaer);
    List<Journalpost> getJournalposterForSaker(List<Sak> saker, Date opprettetEtter, Date opprettetFør, List<String> journalStatuser, int offset, int pagesize );
}
