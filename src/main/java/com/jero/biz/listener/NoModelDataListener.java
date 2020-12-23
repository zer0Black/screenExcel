package com.jero.biz.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.google.gson.Gson;
import javafx.application.Platform;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description 无实体的读取，读取成map
 * @Author lixuetao
 * @Date 2020/12/23
 **/
public class NoModelDataListener extends AnalysisEventListener<Map<Integer, String>> {

    private File excelResult = null; // 符合条件的excel

    private String[] checkResultArr;

    private File excelFile;

    public NoModelDataListener(String searchStr, File excelFile){
        checkResultArr = searchStr.split(",");
        this.excelFile = excelFile;
    }

    public File getExcelList() {
        return excelResult;
    }

    @Override
    public void invoke(Map<Integer, String> data, AnalysisContext analysisContext) {
        String oneResult = new Gson().toJson(data);
        for (String str : checkResultArr){
            if (oneResult.contains(str)){
                excelResult = excelFile;
                return;
            }
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }


}
