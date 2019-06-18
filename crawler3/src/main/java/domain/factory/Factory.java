package domain.factory;

import domain.models.ICrawler;

public class Factory {

        private static Factory instance = null;

        private Factory() {
            // Exists only to defeat instantiation.
        }
        public static Factory getInstance() {
            if(instance == null) {
                instance = new Factory();
            }
            return instance;
        }

        public ICrawler getManufacturerCrawler(String name)
                throws ClassNotFoundException, InstantiationException, IllegalAccessException{
            Class aClass = Class.forName(name);
            return (ICrawler) aClass.newInstance();
        }

    public Class getClass(ICrawler name)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException{
        return name.getClass();
    }


    }