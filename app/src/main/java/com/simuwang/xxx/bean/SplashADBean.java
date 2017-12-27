package com.simuwang.xxx.bean;


import com.simuwang.xxx.base.BaseBean;

/**
 * function:广告页 广告信息数据对象
 *
 * <p>
 * Created by Leo on 2017/4/14.
 */
@SuppressWarnings("ALL")
public class SplashADBean extends BaseBean {

    /**
     * data : {"route_key":"1_1492081136","route_img_id":"1","route_img":"http://www.simuwang.com/apps/img/f1.jpg","route_port":"201","route_port_key":"","route_show_second":"1"}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * route_key : 1_1492081136
         * route_img_id : 1
         * route_img : http://www.simuwang.com/apps/img/f1.jpg
         * route_port : 201
         * route_port_key :
         * route_show_second : 1
         */

        private String route_key;
        private String route_img_id;
        private String route_img;
        private String route_port = "-1";
        private String route_port_key;
        private String route_show_second;

        public String getRoute_key() {
            return route_key;
        }

        public void setRoute_key(String route_key) {
            this.route_key = route_key;
        }

        public String getRoute_img_id() {
            return route_img_id;
        }

        public void setRoute_img_id(String route_img_id) {
            this.route_img_id = route_img_id;
        }

        public String getRoute_img() {
            return route_img;
        }

        public void setRoute_img(String route_img) {
            this.route_img = route_img;
        }

        public String getRoute_port() {
            return route_port;
        }

        public void setRoute_port(String route_port) {
            this.route_port = route_port;
        }

        public String getRoute_port_key() {
            return route_port_key;
        }

        public void setRoute_port_key(String route_port_key) {
            this.route_port_key = route_port_key;
        }

        public String getRoute_show_second() {
            return route_show_second;
        }

        public void setRoute_show_second(String route_show_second) {
            this.route_show_second = route_show_second;
        }
    }
}
