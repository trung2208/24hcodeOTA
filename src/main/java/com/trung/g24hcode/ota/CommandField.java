/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.trung.g24hcode.ota;

/**
 *
 * @author nhatn
 */
public class CommandField {
    private String command;
    private User user;
    private NaviModel model;
    private String data;
    private NaviManager.Options options;

    public CommandField() {
    }

    public CommandField(String command, User user, NaviModel model, String data, NaviManager.Options options) {
        this.command = command;
        this.user = user;
        this.model = model;
        this.data = data;
        this.options = options;
    }


    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public NaviModel getModel() {
        return model;
    }

    public void setModel(NaviModel model) {
        this.model = model;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public NaviManager.Options getOptions() {
        return options;
    }

    public void setOptions(NaviManager.Options options) {
        this.options = options;
    }
    
}
