/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.trung.g24hcode.ota;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 *
 * @author nhatn
 */
public class Taskn {

    private static final String USER = "********";
    private static final String PWD = "*********";

    public static void main(String[] args) throws IOException {
        NaviClient client = new NaviClient();
        Map<String, String> cookies = client.doLogin(USER, PWD);
        List<NaviModel> list = client.crawData(cookies);
        client.doUpdates(list, cookies).forEach(e -> {
            System.out.println("e: " + e.toString());
        });

    }
}
