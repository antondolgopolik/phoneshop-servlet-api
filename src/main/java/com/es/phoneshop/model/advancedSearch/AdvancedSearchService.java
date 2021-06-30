package com.es.phoneshop.model.advancedSearch;

public class AdvancedSearchService {

    public static AdvancedSearchService getInstance() {
        return AdvancedSearchService.SingletonInstanceHolder.instance;
    }

    private AdvancedSearchService() {

    }

    public SearchMode[] getSearchModes() {
        return SearchMode.values();
    }

    private static class SingletonInstanceHolder {
        private static final AdvancedSearchService instance = new AdvancedSearchService();
    }
}
