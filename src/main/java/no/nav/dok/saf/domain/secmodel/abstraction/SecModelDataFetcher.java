package no.nav.dok.saf.domain.secmodel.abstraction;


import java.util.List;

public interface SecModelDataFetcher<T> {

    List<T> fetchAndFilter(ParameterContext parameterContext);
}
