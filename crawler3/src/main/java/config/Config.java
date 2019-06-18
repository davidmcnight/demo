package config;

public class Config {

    public static String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36";
    public static String connString = "jdbc:mysql://127.0.0.1/specbook_products?" +"user=root&password=root";
    public static String HEADERS =

            "name," +
                    "category1," +
                    "category2," +
                    "category3," +
                    "description," +
                    "model_number," +
                    "upc," +
                    "list_price," +
                    "image_link," +
                    "specification_document," +
                    "care_cleaning_document," +
                    "installation_document," +
                    "homeowners_document," +
                    "warranty_document," +
                    "parts_breakdown_document," +
                    "features," +
                    "url," +
                    "discontinued," +
                    "width," +
                    "length," +
                    "depth," +
                    "height," +
                    "additional_images," +  //|| seperated
                    "additional_documents," + //|| seperated
                    "finishes," +
                    "collection," +
                    "note,";



}
