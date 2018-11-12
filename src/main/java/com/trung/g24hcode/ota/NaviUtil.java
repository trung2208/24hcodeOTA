/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.trung.g24hcode.ota;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.jsoup.nodes.Element;

/**
 *
 * @author nhatn
 */
public class NaviUtil {

    public static NaviModel convertRaw(Element element) {
        NaviModel model = new NaviModel();
        try {
            model.setId(element.select("input#cid").val());
            model.setHostName(element.select("input.host_name").val());
            model.setRecodeType(element.child(2).child(0).text());
            model.setRecValue(element.child(3).child(0).text());
            model.setMxPreference(Integer.parseInt(element.child(4).text()));
            System.out.println(model.toString());
        } catch (NumberFormatException e) {
            System.out.println("error in convertRaw: " + e.getMessage());
            return null;
        }
        return model;
    }

    public static Map<String, String> Parse(NaviModel model) {
        Map<String, String> result = new HashMap<>();
        result.put("id", model.getId());
        result.put("hostName", model.getHostName());
        result.put("recodeType", model.getRecodeType());
        result.put("recValue", model.getRecValue());
        result.put("mxPreference", String.valueOf(model.getMxPreference()));
        return result;
    }

    public static double getRandom() {
        Random random = new Random();
        return random.nextDouble();
    }

    public static boolean validate(final String ip) {
        String PATTERN = "^((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.){3}(0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)$";

        return ip.matches(PATTERN);
    }
}
