package no.nav.dok.saf.domain.viewmodel.repo;

import no.nav.dok.saf.domain.viewmodel.Journalpost;

import java.util.List;

public interface ViewModelRepo {
    List<Journalpost> getJournalposter(List<Long> journalpostIder, String ... søkeparametre);
}
