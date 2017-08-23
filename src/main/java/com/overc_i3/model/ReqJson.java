package com.overc_i3.model;

import java.util.List;

public class ReqJson {
    private String id;
    private List<ListBean> list;

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static class ListBean {

        private String maxname;

        public String getMaxname() {
            return maxname;
        }

        public void setMaxname(String maxname) {
            this.maxname = maxname;
        }
    }
}
