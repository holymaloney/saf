package no.nav.dok.saf.domain.secmodel;

import lombok.Builder;
import lombok.Data;
import no.nav.dok.saf.domain.secmodel.abstraction.SecModel;

@Data
@Builder
public class Sak implements SecModel {
    private String aktoerId;
    private String tema;
    private String arkivsakRef;
    private String arkivsakSystem;
}
