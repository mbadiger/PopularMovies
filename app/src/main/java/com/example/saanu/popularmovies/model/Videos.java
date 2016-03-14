package com.example.saanu.popularmovies.model;

import java.util.List;

/**
 * Created by Saanu_mac on 2/10/16.
 */
public class Videos {


    /**
     * id : 131631
     * results : [{"id":"54300b340e0a2646400007e6","iso_639_1":"en","key":"C_Tsj_wTJkQ","name":"The Hunger Games: Mockingjay Trailer \u2013 \u201cThe Mockingjay Lives\u201d","site":"YouTube","size":720,"type":"Trailer"},{"id":"54300b890e0a2646400007fa","iso_639_1":"en","key":"JzcYyzCZdiM","name":"The Hunger Games: Mockingjay Part 1 - Teaser Trailer","site":"YouTube","size":720,"type":"Trailer"},{"id":"545126710e0a2639fe004a3c","iso_639_1":"en","key":"IXshQ5mv1K8","name":"The Hunger Games: Mockingjay Part 1 Final Trailer \u2013 \u201cBurn\u201d","site":"YouTube","size":720,"type":"Trailer"}]
     */

    private int id;
    /**
     * id : 54300b340e0a2646400007e6
     * iso_639_1 : en
     * key : C_Tsj_wTJkQ
     * name : The Hunger Games: Mockingjay Trailer – “The Mockingjay Lives”
     * site : YouTube
     * size : 720
     * type : Trailer
     */

    private List<ResultsEntity> results;

    public void setId(int id) {
        this.id = id;
    }

    public void setResults(List<ResultsEntity> results) {
        this.results = results;
    }

    public int getId() {
        return id;
    }

    public List<ResultsEntity> getResults() {
        return results;
    }

    public static class ResultsEntity {
        private String id;
        private String iso_639_1;
        private String key;
        private String name;
        private String site;
        private int size;
        private String type;

        public void setId(String id) {
            this.id = id;
        }

        public void setIso_639_1(String iso_639_1) {
            this.iso_639_1 = iso_639_1;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setSite(String site) {
            this.site = site;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getId() {
            return id;
        }

        public String getIso_639_1() {
            return iso_639_1;
        }

        public String getKey() {
            return key;
        }

        public String getName() {
            return name;
        }

        public String getSite() {
            return site;
        }

        public int getSize() {
            return size;
        }

        public String getType() {
            return type;
        }
    }
}
