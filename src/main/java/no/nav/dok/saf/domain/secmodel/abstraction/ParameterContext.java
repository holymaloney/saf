package no.nav.dok.saf.domain.secmodel.abstraction;


import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ParameterContext {
    private final ConcurrentHashMap<String, Object> parameterMap = new ConcurrentHashMap<>();

    public void addSearchParameters(Map<String, ? extends Object> parentSearchParameters) {
        parameterMap.putAll(parentSearchParameters);
    }

    public void addStringSearchParameter(String parameterName, String parameterValue) {
        parameterMap.put(parameterName, parameterValue);
    }

    public void addListSearchParameter(String parameterName, List<String> valueList) {
        parameterMap.put(parameterName, valueList);
    }

    public String getStringParameter(String parameterName) {
        return parameterMap.get(parameterName).toString();
    }

    public List<String> getListParameter(String parameterName) {
        return (List<String>)parameterMap.get(parameterName);
    }

}
