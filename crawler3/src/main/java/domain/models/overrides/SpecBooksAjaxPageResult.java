package domain.models.overrides;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.fetcher.PageFetchResult;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;

import java.net.SocketTimeoutException;

public class SpecBooksAjaxPageResult extends PageFetchResult {
    @Override
    public boolean fetchContent(Page page, int maxBytes) throws SocketTimeoutException {
        return super.fetchContent(page, maxBytes);
    }
}
