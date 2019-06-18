package domain.models;

import java.lang.reflect.Field;

public class Product {


    private String name;
    private String category1;
    private String category2;
    private String category3;
    private String description;
    private String modelNumber;
    private String upc;
    private String listPrice;
    private String imageLink;
    private String specificationDocument;
    private String careCleaningDocument;
    private String installationDocument;
    private String homeownersDocument;
    private String warrantyDocument;
    private String partsBreakdownDocument;
    private String features;
    private String url;
    private String discontinued;
    private String width;
    private String length;
    private String depth;
    private String height;
    private String additionalImages; //|| sperated
    private String additionalDocuments;
    private String finishes;
    private String collection;
    private String note;


    public Product() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory1() {
        return category1;
    }

    public void setCategory1(String category1) {
        this.category1 = category1;
    }

    public String getCategory2() {
        return category2;
    }

    public void setCategory2(String category2) {
        this.category2 = category2;
    }

    public String getCategory3() {
        return category3;
    }

    public void setCategory3(String category3) {
        this.category3 = category3;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getModelNumber() {
        return modelNumber;
    }

    public void setModelNumber(String modelNumber) {
        this.modelNumber = modelNumber;
    }

    public String getUpc() {
        return upc;
    }

    public void setUpc(String upc) {
        this.upc = upc;
    }

    public String getListPrice() {
        return listPrice;
    }

    public void setListPrice(String listPrice) {
        this.listPrice = listPrice;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getSpecificationDocument() {
        return specificationDocument;
    }

    public void setSpecificationDocument(String specificationDocument) {
        this.specificationDocument = specificationDocument;
    }

    public String getCareCleaningDocument() {
        return careCleaningDocument;
    }

    public void setCareCleaningDocument(String careCleaningDocument) {
        this.careCleaningDocument = careCleaningDocument;
    }

    public String getInstallationDocument() {
        return installationDocument;
    }

    public void setInstallationDocument(String installationDocument) {
        this.installationDocument = installationDocument;
    }

    public String getHomeownersDocument() {
        return homeownersDocument;
    }

    public void setHomeownersDocument(String homeownersDocument) {
        this.homeownersDocument = homeownersDocument;
    }

    public String getWarrantyDocument() {
        return warrantyDocument;
    }

    public void setWarrantyDocument(String warrantyDocument) {
        this.warrantyDocument = warrantyDocument;
    }

    public String getPartsBreakdownDocument() {
        return partsBreakdownDocument;
    }

    public void setPartsBreakdownDocument(String partsBreakdownDocument) {
        this.partsBreakdownDocument = partsBreakdownDocument;
    }

    public String getFeatures() {
        return features;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDiscontinued() {
        return discontinued;
    }

    public void setDiscontinued(String discontinued) {
        this.discontinued = discontinued;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getDepth() {
        return depth;
    }

    public void setDepth(String depth) {
        this.depth = depth;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getAdditionalImages() {
        return additionalImages;
    }

    public void setAdditionalImages(String additionalImages) {
        this.additionalImages = additionalImages;
    }

    public String getAdditionalDocuments() {
        return additionalDocuments;
    }

    public void setAdditionalDocuments(String additionalDocuments) {
        this.additionalDocuments = additionalDocuments;
    }

    public String getFinishes() {
        return finishes;
    }

    public void setFinishes(String finishes) {
        this.finishes = finishes;
    }

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getField(String property) {

        if (property.trim().equals("name")) {
            return getName();
        }


        if (property.trim().equals("category1")) {
            return getCategory1();
        }


        if (property.trim().equals("category2")) {
            return getCategory2();
        }


        if (property.trim().equals("category3")) {
            return getCategory3();
        }

        if (property.trim().equals("description")) {
            return getDescription();
        }

        if (property.trim().equals("modelNumber")) {
            return getModelNumber();
        }

        if (property.trim().equals("upc")) {
            return getUpc();
        }

        if (property.trim().equals("listPrice")) {
            return getListPrice();
        }

        if (property.trim().equals("imageLink")) {
            return getImageLink();
        }

        if (property.trim().equals("specificationDocument")) {
            return getSpecificationDocument();
        }

        if (property.trim().equals("careCleaningDocument")) {
            return getCareCleaningDocument();
        }

        if (property.trim().equals("installationDocument")) {
            return getInstallationDocument();
        }
        if (property.trim().equals("homeownersDocument")) {
            return getHomeownersDocument();
        }
        if (property.trim().equals("warrantyDocument")) {
            return getWarrantyDocument();
        }

        if (property.trim().equals("partsBreakdownDocument")) {
            return getPartsBreakdownDocument();
        }

        if (property.trim().equals("features")) {
            return getFeatures();
        }
        if (property.trim().equals("url")) {
            return getUrl();
        }
        if (property.trim().equals("discontinued")) {
            return getDiscontinued();
        }
        if (property.trim().equals("width")) {
            return getWidth();
        }

        if (property.trim().equals("length")) {
            return getLength();
        }


        if (property.trim().equals("depth")) {
            return getDepth();
        }

        if (property.trim().equals("height")) {
            return getHeight();
        }

        if (property.trim().equals("additionalImages")) {
            return getAdditionalImages();
        }

        if (property.trim().equals("additionalDocuments")) {
            return getAdditionalDocuments();
        }

        if (property.trim().equals("finishes")) {
            return getFinishes();
        }


        if (property.trim().equals("collection")) {
            return getCollection();
        }


        if (property.trim().equals("note")) {
            return getNote();
        }

        return null;

    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", category1='" + category1 + '\'' +
                ", category2='" + category2 + '\'' +
                ", category3='" + category3 + '\'' +
                ", description='" + description + '\'' +
                ", modelNumber='" + modelNumber + '\'' +
                ", upc='" + upc + '\'' +
                ", listPrice='" + listPrice + '\'' +
                ", imageLink='" + imageLink + '\'' +
                ", specificationDocument='" + specificationDocument + '\'' +
                ", careCleaningDocument='" + careCleaningDocument + '\'' +
                ", installationDocument='" + installationDocument + '\'' +
                ", homeownersDocument='" + homeownersDocument + '\'' +
                ", warrantyDocument='" + warrantyDocument + '\'' +
                ", partsBreakdownDocument='" + partsBreakdownDocument + '\'' +
                ", features='" + features + '\'' +
                ", url='" + url + '\'' +
                ", discontinued='" + discontinued + '\'' +
                ", width='" + width + '\'' +
                ", length='" + length + '\'' +
                ", depth='" + depth + '\'' +
                ", height='" + height + '\'' +
                ", additionalImages='" + additionalImages + '\'' +
                ", additionalDocuments='" + additionalDocuments + '\'' +
                ", finishes='" + finishes + '\'' +
                ", collection='" + collection + '\'' +
                ", note='" + note + '\'' +
                '}';
    }


    public Product(Product product, String name, String modelNumber) {

        try {

            Field[] fields = Product.class.getDeclaredFields();
            for (Field field : fields) {
                field.set(this, product.getField(field.getName()));
            }
            this.finishes = name.trim();
            this.name = this.getName().trim() + " " + name.trim();
            this.modelNumber = this.getModelNumber()+modelNumber;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public Product(Product product) {
        try {

            Field[] fields = Product.class.getDeclaredFields();
            for (Field field : fields) {
                field.set(this, product.getField(field.getName()));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
