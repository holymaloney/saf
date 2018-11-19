package no.nav.dok.saf.domain.secmodel;

import no.nav.dok.saf.domain.secmodel.abstraction.SecModel;

import java.util.Date;
import java.util.List;

public class Journalpost implements SecModel {
    public String arkivSakSystem;
    public String arkivSakRef;
    public Long journalpostId;
    String mottaksKanal;
    String journalStatus;
    boolean feilRegistrert;
    Date opprettet;
}
