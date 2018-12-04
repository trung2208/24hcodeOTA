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
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 *
 * @author nhatn
 */
public class NaviClient {

    private Map<String, String> cookies;
    private List<NaviModel> models;
    private final NaviManager manager;
    private String username;
    private String passwd;

    public NaviClient() {
        manager = new NaviManagerImpl();
    }

    public NaviClient(String username, String passwd) {
        this.username = username;
        this.passwd = passwd;
        manager = new NaviManagerImpl();
        this.startup();
    }

    private void startup() {
        cookies = manager.doLogin(username, passwd);
        if (!isLogged()) {
            System.out.println("can't login to navi.tenten!");
        } else {
            models = manager.crawData(cookies);
        }
    }

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
        System.out.println("-- IPv4: " + ip + " --");
        return ip;
    }

    public List<Boolean> doUpdateIpRecords() {
        System.out.println("Starting Update all record!");
        List<Boolean> list = new ArrayList<>();
        String ip = this.getMyGlobalIP();
        if (ip.isEmpty()) {
            System.out.println("Cant get my ipv4! Check my network! ");
            return null;
        }
        for (NaviModel model : models) {
            if (!model.getRecodeType().equals("A") || model.getRecValue().equals(ip)) {
                continue;
            }
            boolean result = manager.updateRecord(model, cookies);
            list.add(result);
        }
        System.out.println(list.size() + " record need to be updated and " + list.stream().filter(result -> result).count() + " record update successfully!");
        return list;

    }

    public boolean addRecord(NaviModel model) {
        return manager.addRecord(model, cookies);
    }

    public boolean updateRecord(NaviModel model) {
        return manager.updateRecord(model, cookies);
    }

    public boolean removeRecord(NaviModel model) {
        return manager.removeRecord(model, cookies);
    }

    public boolean removeAllRecord() {
        return manager.removeAll(cookies);
    }

    public boolean createRecordsBy(NaviManager.Options options, String data) {
        return manager.createRecordBy(options, data, cookies);
    }

    public boolean isLogged() {
        return !cookies.isEmpty();
    }
}
