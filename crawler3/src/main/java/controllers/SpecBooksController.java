package controllers;

import crawlers.SpecBooksCrawler;
import domain.factory.Factory;
import domain.models.ICrawler;
import domain.models.manufacturers.*;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

import java.util.ArrayList;

public class SpecBooksController {


    public static int PRODUCT_COUNTER = 1;
    public static int PAGE_COUNTER = 1;


    public static void main(String[] args) throws Exception {

        String crawlerName = "InfinityDrain";

        String crawlStorageFolder = "storage";
        int numberOfCrawlers = 3;

        CrawlConfig config = new CrawlConfig();
        config.setCrawlStorageFolder(crawlStorageFolder);

        /*
         * Instantiate the controller for this crawl.
         */
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        robotstxtConfig.setEnabled(false);
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);


        /*
         * For each crawl, you need to add some seed urls. These are the first
         * URLs that are fetched and then the crawler starts following links
         * which are found in these pages
         */



        Factory factory  = Factory.getInstance();
        ICrawler myCrawlerSpec = factory.getManufacturerCrawler("domain.models.manufacturers."+crawlerName);
        ArrayList<String> seeds = myCrawlerSpec.getSeeds();
        for (String seed: seeds){
            controller.addSeed(seed);
        }

        /*
         * Start the crawl. This is a blocking operation, meaning that your code
         * will reach the line after this only when crawling is finished.
         */
        controller.start(factory.getClass(myCrawlerSpec), numberOfCrawlers);

    }


}