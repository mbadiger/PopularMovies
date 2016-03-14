package com.example.saanu.popularmovies.model;

import java.util.List;

/**
 * Created by Saanu_mac on 2/24/16.
 */
public class MovieReviews {

    /**
     * id : 131631
     * page : 1
     * results : [{"id":"547e6075c3a368256200022f","author":"anthonypagan1975","content":"It was good. Although I wish it had more action scenes. It's worth watching ago don't miss out!","url":"http://j.mp/1vjM0BW"},{"id":"54d9b29c9251410a4500100a","author":"Andres Gomez","content":"Yet more of the same extended in an inexcusable way. Let's hope the last movie of the saga can get a proper end.","url":"http://j.mp/16Ki1te"}]
     * total_pages : 1
     * total_results : 2
     */

    private int id;
    private int page;
    private int total_pages;
    private int total_results;
    /**
     * id : 547e6075c3a368256200022f
     * author : anthonypagan1975
     * content : It was good. Although I wish it had more action scenes. It's worth watching ago don't miss out!
     * url : http://j.mp/1vjM0BW
     */

    private List<ResultsEntity> results;

    public void setId(int id) {
        this.id = id;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    public void setTotal_results(int total_results) {
        this.total_results = total_results;
    }

    public void setResults(List<ResultsEntity> results) {
        this.results = results;
    }

    public int getId() {
        return id;
    }

    public int getPage() {
        return page;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public int getTotal_results() {
        return total_results;
    }

    public List<ResultsEntity> getResults() {
        return results;
    }

    public static class ResultsEntity {
        private String id;
        private String author;
        private String content;
        private String url;

        public void setId(String id) {
            this.id = id;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getId() {
            return id;
        }

        public String getAuthor() {
            return author;
        }

        public String getContent() {
            return content;
        }

        public String getUrl() {
            return url;
        }
    }
}
