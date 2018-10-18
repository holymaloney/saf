package no.nav.dok.saf.domain.viewmodel;

import java.util.List;

public class Journalpost {
    Long journalpostId;
    String journalpostStatus;
    String tema;
    Sak sak;
    String beskrivelse;
    List<Dokument> dokumenter;
}
