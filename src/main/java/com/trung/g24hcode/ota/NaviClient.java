/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.trung.g24hcode.ota;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author nhatn
 */
public class NaviClient {

    private static final String URL_SITE = "https://domain.tenten.vn/";

    public String getMyGlobalIP() {
        String[] servers = {"http://ipecho.net/plain/", "http://ipinfo.io/ip", "http://ipconf.cf/"};
        String ip = "";
        int index = 0;
        int max = servers.length;
        do {
            System.out.println("server: " + index);
            Document document;
            try {
                document = Jsoup.connect(servers[index]).get();
                ip = document.text();
            } catch (IOException ex) {
                Logger.getLogger(NaviClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        } while (!NaviUtil.validate(ip) && index++ < max);
        System.out.println("-- IPv4: "+ip+" --");
        return ip;
    }

    public Map<String, String> doLogin(String user, String pwd) {
        System.out.println("login to " + URL_SITE);
        try {
            Connection.Response response = Jsoup.connect(URL_SITE)
                    .timeout(10 * 1000)
                    .method(Connection.Method.GET)
                    .execute();
            Connection.Response r = Jsoup.connect(URL_SITE + "login")
                    .data("username", user)
                    .data("password", pwd)
                    .cookies(response.cookies())
                    .method(Connection.Method.POST)
                    .execute();
            System.out.println("login success!");
            return r.cookies();
        } catch (IOException ex) {
            Logger.getLogger(NaviClient.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public List<NaviModel> crawData(Map<String, String> cookies) {
        System.out.println("Starting crawl all record!");
        List<NaviModel> list = new ArrayList<>();
        int index = 0;
        try {
            Document document = Jsoup.connect(URL_SITE).cookies(cookies).get();
            Elements elements = document.select("tr.updatebinh");
            for (Element e : elements) {
                NaviModel model = NaviUtil.convertRaw(e);
                if (model != null) {
                    list.add(model);
                    System.out.println("model: " + index++ + "\n " + model.toString());
                }
            }
            System.out.println("Crawl successfully!");
        } catch (IOException ex) {
            Logger.getLogger(NaviClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    public boolean UpdateRecord(NaviModel model, Map<String, String> cookies) {
        boolean result = false;
        System.out.println("record to update: \n" + model.toString());
        try {
            Connection.Response r = Jsoup.connect(URL_SITE + "DnsSettingNew/editDns/" + String.valueOf(NaviUtil.getRandom()))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .data(NaviUtil.Parse(model))
                    .cookies(cookies)
                    .method(Connection.Method.POST)
                    .execute();
            System.out.println("response: " + r.body() + "\n");
            result = r.body().trim().equals("1");
        } catch (IOException ex) {
            Logger.getLogger(NaviClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public List<Boolean> doUpdates(List<NaviModel> models, Map<String, String> cookies) {
        System.out.println("Starting Update all record!");
        List<Boolean> list = new ArrayList<>();
        String ip = this.getMyGlobalIP();
        if (ip.isEmpty()) {
            System.out.println("Cant get my ipv4! Check my network! ");
            return null;
        }
        int count = 0;
        count = models.stream().filter((model) -> (NaviUtil.validate(model.getRecValue()) && !model.getRecValue().equals(ip))).map((model) -> {
            model.setRecValue(ip);
            //update record
            return model;
        }).map((model) -> this.UpdateRecord(model, cookies)).map((result) -> {
            list.add(result);
            return result;
        }).map((_item) -> 1).reduce(count, Integer::sum);
        System.out.println(count+" record need to be updated!");
        return list;

    }

}
