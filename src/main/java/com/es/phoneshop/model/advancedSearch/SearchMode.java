package com.es.phoneshop.model.advancedSearch;

public enum SearchMode {
    ALL_WORDS("All words"), ANY_WORD("Any word");

    private final String searchMode;

    SearchMode(String searchMode) {
        this.searchMode = searchMode;
    }

    public String getSearchMode() {
        return searchMode;
    }

    @Override
    public String toString() {
        return searchMode;
    }
}
