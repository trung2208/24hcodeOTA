/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.trung.g24hcode.ota;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author nhatn
 */
public class Taskn {

    public enum Command {
        LOGIN, LOGOUT, ADD_RECORD, UPDATE_RECORD, REMOVE_RECORD, REMOVE_ALL, CREATE_BY, UPDATE_THIS_IP, ERROR;

        public static Command Parse(String cmd) {
            return NaviUtil.Lookup(Command.class, cmd);
        }
    }
    public static final Gson gson = new Gson();

    public static void main(String[] args) throws IOException {
        NaviClient client = null;
        boolean result = false;
        boolean eof = false;
        Scanner sc = new Scanner(System.in);
        while (!eof) {
            /**
             * data format is JSON like: { "command":"login", "user":{
             * "username":"24hcode.info", "password":"******" } }
             */
            String data = sc.nextLine();
            CommandField cmdF = gson.fromJson(data, CommandField.class);
            switch (Command.Parse(cmdF.getCommand())) {
                case LOGIN:
                    client = new NaviClient(cmdF.getUser().getUsername(), cmdF.getUser().getPassword());
                    result = client.isLogged();
                    if (result) {
                        System.out.println("successful!");
                    } else {
                        System.out.println("failed!");
                    }
                    break;
                case ADD_RECORD:
                    if (client != null || client.isLogged()) {
                        result = client.addRecord(cmdF.getModel());
                        if (result) {
                            System.out.println("successful!");
                        } else {
                            System.out.println("failed!");
                        }
                    } else {
                        System.out.println("Client is not logged in! ");
                    }
                    break;
                case UPDATE_RECORD:
                    if (client != null || client.isLogged()) {
                        result = client.updateRecord(cmdF.getModel());
                        if (result) {
                            System.out.println("successful!");
                        } else {
                            System.out.println("failed!");
                        }
                    } else {
                        System.out.println("Client is not logged in! ");
                    }
                    break;
                case REMOVE_RECORD:
                    if (client != null || client.isLogged()) {
                        result = client.removeRecord(cmdF.getModel());
                        if (result) {
                            System.out.println("successful!");
                        } else {
                            System.out.println("failed!");
                        }
                    } else {
                        System.out.println("Client is not logged in! ");
                    }
                    break;
                case REMOVE_ALL:
                    if (client != null || client.isLogged()) {
                        result = client.removeAllRecord();
                        if (result) {
                            System.out.println("successful!");
                        } else {
                            System.out.println("failed!");
                        }
                    } else {
                        System.out.println("Client is not logged in! ");
                    }
                    break;
                case CREATE_BY:
                    if (client != null || client.isLogged()) {
                        result = client.createRecordsBy(cmdF.getOptions(), cmdF.getData());
                        if (result) {
                            System.out.println("successful!");
                        } else {
                            System.out.println("failed!");
                        }
                    } else {
                        System.out.println("Client is not logged in! ");
                    }
                    break;
                case UPDATE_THIS_IP:
                    if (client != null || client.isLogged()) {
                        List<Boolean> results = client.doUpdateIpRecords();
                    } else {
                        System.out.println("Client is not logged in! ");
                    }
                    break;
                case LOGOUT:
                    return;
                default:
                    System.out.println("unknow command!");
                    break;

            }
        }
        //<editor-fold defaultstate="collapsed" desc="comment">
//        NaviClient client = new NaviClient();
//        Map<String, String> cookies = client.doLogin(USER, PWD);
//        List<NaviModel> list = client.crawData(cookies);
//        List<Boolean> results = client.doUpdates(list, cookies);
//        if (results != null) {
//            for (int i = 0; i < results.size(); i++) {
//                System.out.println("record " + i + ": " + String.valueOf(results.get(i)));
//            }
//        } else {
//            System.out.println("Something went wrong! please try again!");
//        }
//</editor-fold>
    }
}
