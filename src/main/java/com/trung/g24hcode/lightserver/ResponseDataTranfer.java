/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.trung.g24hcode.lightserver;

import com.trung.g24hcode.ota.NaviModel;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nhatn
 */
public class ResponseDataTranfer {
    private String log;
    private List<NaviModel> models;
    private boolean result;

    public ResponseDataTranfer() {
        models=new ArrayList<>();
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public List<NaviModel> getModels() {
        return models;
    }

    public void setModels(List<NaviModel> models) {
        this.models = models;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
    
    
}
