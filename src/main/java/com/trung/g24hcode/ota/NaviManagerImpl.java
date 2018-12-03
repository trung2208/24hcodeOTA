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

public class NaviManagerImpl implements NaviManager {

    @Override
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

    @Override
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

    @Override
    public boolean updateRecord(NaviModel model, Map<String, String> cookies) {
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

    @Override
    public boolean addRecord(NaviModel model, Map<String, String> cookies) {
        boolean result = false;
        System.out.println("record to add: \n" + model.toString());
        try {
            Connection.Response r = Jsoup.connect(URL_SITE + "DnsSettingNew/addDns/" + String.valueOf(NaviUtil.getRandom()))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .data(NaviUtil.Parse(model))
                    .cookies(cookies)
                    .method(Connection.Method.POST)
                    .execute();
            System.out.println("response: " + r.body() + "\n");
            //<editor-fold defaultstate="collapsed" desc="switch result">
//            switch(r.body().trim()){
//                case "1":{
//                    result="";
//                }
//                case "3":{
//                    result="Bản ghi đã tồn tại";
//                }
//                case "4":{
//                    result="";
//                }
//                case "5":{
//                    result="";
//                }
//                case "6":{
//                    result="";
//                }
//                case "7":{
//                    result="";
//                }
//                case "8":{
//                    result="";
//                }
//                case "9":{
//                    result="";
//                }
//                case "10":{
//                    result="";
//                }
//                case "11":{
//                    result="";
//                }
//                case "12":{
//                    result="";
//                }
//                case "13":{
//                    result="";
//                }
//                case "14":{
//                    result="";
//                }
//                case "15":{
//                    result="";
//                }
//                case "16":{
//                    result="";
//                }
//            }
//</editor-fold>
            result = r.body().trim().equals("1");
        } catch (IOException ex) {
            Logger.getLogger(NaviClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public boolean removeRecord(NaviModel model, Map<String, String> cookies) {
        //To change body of generated methods, choose Tools | Templates.
        boolean result = false;
        System.out.println("record to delete: \n" + model.toString());
        try {
            Connection.Response r = Jsoup.connect(URL_SITE + "DnsSettingNew/deteleDns/" + String.valueOf(NaviUtil.getRandom()))
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

    @Override
    public boolean removeAll(Map<String, String> cookies) {
        boolean result = false;
        //this function is missing
        return result;
    }

    @Override
    public boolean createRecordBy(Options options, String data, Map<String, String> cookies) {
        String pathName = "";
        switch (options) {
            case Google:
                pathName = "DnsDomainDefaul/issetDefaultGoogleDns/";
                break;
            case GoogleMail:
                pathName = "DnsDomainDefaul/issetDefaultGoogleMailDns/";
                break;
            case IP:
                pathName = "DnsDomainDefaul/addDnsHosting/";
                break;
        }
        boolean result = false;
        try {
            Connection.Response r = Jsoup.connect(URL_SITE + pathName + String.valueOf(NaviUtil.getRandom()))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .data("dnsdefaul", data)
                    .cookies(cookies)
                    .method(Connection.Method.POST)
                    .execute();
            System.out.println("response: " + r.body() + "\n");
            result = Integer.parseInt(r.body().trim()) > 0;
        } catch (IOException ex) {
            Logger.getLogger(NaviClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

}
