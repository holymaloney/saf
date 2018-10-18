package no.nav.dok.saf.domain.secmodel;

import java.util.Date;
import java.util.List;

public class Journalpost {
    Long journalpostId;
    String mottaksKanal;
    String journalStatus;
    boolean feilRegistrert;
    List<Dokument> dokumenter;
    Date opprettet;
}
