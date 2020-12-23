package com.jero.biz.controller;

import com.alibaba.excel.EasyExcel;
import com.google.gson.Gson;
import com.jero.biz.listener.NoModelDataListener;
import com.jero.ui.dialog.AlertDialog;
import com.jero.ui.dialog.DialogHelper;
import com.jero.utils.FileUtils;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextArea;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * @Description 主体控制器
 * @Author lixuetao
 * @Date 2020/12/23
 **/
public class MainController implements Initializable {

    public static MainController controller; //通过初始化传入自身对象，从而可以访问到 fxml 中的控件

    public static Stage stage; //主窗口

    @FXML
    public JFXButton selectFileBtn;
    @FXML
    public TextArea selectFileDir;
    @FXML
    public JFXTextField searchText;
    @FXML
    public JFXButton startScreen;
    @FXML
    public TextArea resultDIrTextArea;
    @FXML
    public JFXButton openFileDir;

    private String selectDirectory; //所选择的文件夹

    private String destDir;
//    private String searchStr; //检索的字符串

    public void initialize(URL location, ResourceBundle resources) {

    }

    /**
     * 初始化界面
     *
     * @param stage      界面
     * @param controller 自身
     */
    public void init(Stage stage, MainController controller) {
        MainController.controller = controller;
        MainController.stage = stage;
    }

    @FXML
    public void selectFileEvent(){
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("选择需要筛选的excel文件夹");
        File directory = directoryChooser.showDialog(new Stage());
        if (directory != null){
            this.selectDirectory = directory.getAbsolutePath();
            this.selectFileDir.setText(selectDirectory);
        }
    }

    @FXML
    public void startScreenEvent() throws IOException {
        String searchStr = this.searchText.getText();
        // 判断是否有excel文件，如果有全部找出文件
        if (this.selectDirectory == null || this.selectDirectory.equals("")
            || searchStr == null || searchStr.equals("")){
            DialogHelper.alert("系统错误", "请选择文件夹并填写检索文字后,再进行筛选");
            return;
        }

        File rootDirectory = new File(this.selectDirectory);
        if (rootDirectory.exists()){
            LinkedList<File> excelList = new LinkedList<File>();
            File[] fileArr = rootDirectory.listFiles();

            for (File file : fileArr){
                if (FileUtils.getExtend(file.getName()).equals("xlsx")
                || FileUtils.getExtend(file.getName()).equals("xls")){
                    excelList.add(file);
                } else {
                    continue;
                }
            }

            // 判断是否有文件
            if (excelList.isEmpty()){
                DialogHelper.alert("系统错误", "该路径下无excel文件，请检查");
            } else {
                this.destDir = null;
                handleExcel(searchStr, excelList);
            }

        } else {
            DialogHelper.alert("系统错误", "该路径不存在，请检查");
        }

    }

    private void handleExcel(String searchStr, LinkedList<File> excelList) throws IOException {
        Service<String> service = new Service<String>() {
            @Override
            protected Task<String> createTask() {
                return new Task<String>() {
                    @Override
                    protected String call() throws Exception {
                        // 挨个读取excel检查，检查出来以后就在同级目录下建立子文件夹，把excel复制到其中
                        List<File> matchExcel = new ArrayList<>();
                        for (File excelFile : excelList){
                            try{
                                System.out.printf(excelFile.getAbsolutePath());
                                NoModelDataListener noModelDataListener = new NoModelDataListener(searchStr, excelFile);
                                EasyExcel.read(excelFile.getAbsolutePath(), noModelDataListener).sheet().doReadSync();
                                File resultExcel = noModelDataListener.getExcelList();

                                if (resultExcel != null) {
                                    matchExcel.add(resultExcel);
                                }
                            } catch (Exception e){
                                e.printStackTrace();
                                continue;
                            }
                        }

                        if (!matchExcel.isEmpty()){
                            destDir = selectDirectory + File.separator + "temp_excel";

                            File saveFile = new File(destDir);
                            saveFile.mkdirs();
                            FileUtils.cleanDirectory(saveFile);

                            for (File resultfile: matchExcel){
                                String destFile = destDir + File.separator + resultfile.getName();
                                FileUtils.copyFile(resultfile, new File(destFile));
                            }
                        } else {
                            destDir = "此检索条件未筛选出excel";
                        }

                        return "success";
                    }
                };
            }
        };

        AlertDialog progressAlert = new AlertDialog.Builder()
                .view("dialog_progressbar")
                .title("数据加载中")
                .build();

        service.setOnSucceeded((WorkerStateEvent event) -> {
            System.out.println("任务处理完成！");
            this.resultDIrTextArea.setText(this.destDir);
            progressAlert.close();
        });

        //启动任务start()一定是最后才调用的
        service.start();
        progressAlert.show();
    }

    @FXML
    private void openResultFileDir(){
        try {
            if (this.destDir == null || this.destDir.equals("")) {
                DialogHelper.alert("系统错误", "请先筛选excel");
            } else if (destDir.equals("此检索条件未筛选出excel")){
//                this.resultDIrTextArea.setText(this.destDir);
                DialogHelper.alert("系统错误", "此检索条件未筛选出excel，无法打开文件夹");
            } else {
//                Runtime.getRuntime().exec("cmd /c start explorer "+ this.destDir);
                java.awt.Desktop.getDesktop().open(new File(this.destDir));
            }
        } catch (Exception e) {
            e.printStackTrace();
            DialogHelper.alert("系统错误", "操作顺序有误，文件夹可能损坏");
        }
    }

}
