package no.nav.dok.saf.domain.secmodel;

import lombok.Builder;
import lombok.Data;
import no.nav.dok.saf.domain.secmodel.abstraction.SecModel;

import java.util.List;

@Data
@Builder
public class Bruker implements SecModel {
    private String aktoerId;
    private List<Ident> historiskeIdenter;
}
