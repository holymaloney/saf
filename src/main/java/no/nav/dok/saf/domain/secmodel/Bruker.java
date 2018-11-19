package no.nav.dok.saf.domain.secmodel;

import no.nav.dok.saf.domain.secmodel.abstraction.SecModel;

import java.util.List;

public class Bruker implements SecModel {
    String aktoerId;
    List<Sak> temaer;
    List<Ident> historiskeIdenter;
}
