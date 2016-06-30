package api;

import com.google.gson.annotations.SerializedName;

public class PagingInfo {
    @SerializedName("current-page")
    private String currentPage;

    @SerializedName("next-page")
    private String nextPage;

    public String getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
    }

    public String getNextPage() {
        return nextPage;
    }

    public void setNextPage(String nextPage) {
        this.nextPage = nextPage;
    }
}
