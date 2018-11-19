package no.nav.dok.saf.domain.secmodel.abstraction;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * BrukerStandalonePepEvaluator brukerEvaluator = new BrukerStandalonePepEvaluator(null, brukerDataFetcher, pep1);
 * SakStandalonePepEvaluator sakEvaluator = new SakStandalonePepEvaluator(brukerEvaluator, sakDataFetcher, pep2);
 * JournalpostStandalonePepEvaluator jpEvaluator= new JournalpostStandalonePepEvaluator(sakEvaluator, jpDataFetcher, pep3);
 *
 * @param <T>
 */
public abstract class AbstractStandalonePepEvaluator<T extends SecModel> implements StandalonePepEvaluator<T> {
    StandalonePepEvaluator parent;
    SecModelDataFetcher<T> dataFetcher;
    Pep<T> pep;


    public AbstractStandalonePepEvaluator(StandalonePepEvaluator parent, SecModelDataFetcher<T> dataFetcher, Pep<T> pep) {
        this.parent = parent;
        this.dataFetcher = dataFetcher;
        this.pep = pep;
    }


    /**
     * This method will fetch and filter data from the repo using a DataFetcher and the parameterContext,
     * and will further filter the result using the configured Pep.
     * If parent is not null, the method will call fetchAndFilterAndEnforce on parent objects all the way to
     * the root of the tree. The root parent will have a null parent.
     * If any of the parent searches results in an empty stream, this either means there are no search results or
     * that all results have been filtered away by access denied in ABAC.
     * @param parameterContext
     * @param accessDecicionContext
     * @return
     */

    @Override
    public Stream<T> fetchAndFilterAndEnforce(ParameterContext parameterContext, AccessDecisionContext accessDecicionContext) {
        List<T> secModelResult = dataFetcher.fetchAndFilter(parameterContext);

        if (parent != null) {
            Map<String, ? extends Object> parentSearchParameters = extractParentSearchParameter(secModelResult);
            parameterContext.addSearchParameters(parentSearchParameters);
            Stream parentStream = parent.fetchAndFilterAndEnforce(parameterContext, accessDecicionContext);
            if (((List)parentStream.collect(Collectors.toList())).isEmpty()) {
                return Stream.empty();
            }
        }

        return secModelResult.stream().filter( e -> pep.hasAccesOn(e, accessDecicionContext));

    }

    /**
     * Extract the neccesary search keys from the secModelResult to populate the parent SecModel entity.
     * For Journalpost, the necessary keys to populate the above level is saksref + arkivsaksystem,
     * For Sak, the necessary key to populate the above level is aktoerId.
     * @param secModelResult
     * @return
     */
    abstract Map<String, ? extends Object> extractParentSearchParameter(List<T> secModelResult);


}
