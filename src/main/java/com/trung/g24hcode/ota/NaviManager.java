/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.trung.g24hcode.ota;

import java.util.List;
import java.util.Map;

/**
 *
 * @author nhatn
 */
public interface NaviManager {

    public static final String URL_SITE = "https://domain.tenten.vn/";

    public static enum Options {
        Google, GoogleMail, IP
    }

    public Map<String, String> doLogin(String user, String pwd);

    public List<NaviModel> crawData(Map<String, String> cookies);

    public boolean updateRecord(NaviModel model, Map<String, String> cookies);

    public boolean addRecord(NaviModel model, Map<String, String> cookies);

    public boolean removeRecord(NaviModel model, Map<String, String> cookies);

    public boolean removeAll(Map<String, String> cookies);

    public boolean createRecordBy(Options options, String data, Map<String, String> cookies);

}
