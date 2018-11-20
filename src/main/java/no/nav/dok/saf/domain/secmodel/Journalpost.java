package no.nav.dok.saf.domain.secmodel;

import lombok.Builder;
import lombok.Data;
import no.nav.dok.saf.domain.secmodel.abstraction.SecModel;

import java.util.Date;

@Data
@Builder
public class Journalpost implements SecModel {
    private String arkivSakSystem;
    private String arkivSakRef;
    private String journalpostId;
    private String mottaksKanal;
    private String journalStatus;
    private boolean feilRegistrert;
    private Date opprettet;
}
