package com.jero.ui.dialog;

import javafx.scene.image.Image;
import javafx.stage.Stage;

public class DialogHelper {

	public static void alert(String title, String message){
		new AlertDialog.Builder()
			.view("dialog_alert")
			.title(title)
			.setText("#text_info", message)
			.click("#btn_confirm", new OnClickListener() {

				@Override
				public void onClick(Stage stage) {
					stage.close();
				}
			})
			.build()
			.show();
	}

	public static void alert(String title, String message,Image image){
		new AlertDialog.Builder()
			.view("dialog_alert")
			.title(title)
			.setText("#text_info", message)
			.setImage("#imageview_tip", image)
			.click("#btn_confirm", new OnClickListener() {

				@Override
				public void onClick(Stage stage) {
					stage.close();
				}
			})
			.build()
			.show();
	}

//	public static void about(){
//		new AlertDialog.Builder()
//			.view("dialog_about")
//			.title("����")
//			.build()
//			.show();
//	}

	public static AlertDialog progressbar() {
		AlertDialog progressAlert = new AlertDialog.Builder()
				.view("dialog_progressbar")
				.title("���ݼ�����")
				.build();
		progressAlert.show();
		return progressAlert;
	}

	public static void confim(String title, String msg, OnClickListener okClickListener, OnClickListener cancelClickListener){
		new AlertDialog.Builder()
			.view("dialog_confirm")
			.title(title)
			.setText("#label_info", msg)
			.click("#btn_confirm", okClickListener)
			.click("#btn_cancel", cancelClickListener)
			.build()
			.show();
	}

//	public static void showArticle(String content) {
//		AlertDialog alertDialog = new AlertDialog.Builder()
//			.view("dialog_article_detail")
//			.title("��������")
//			.build();
//		HTMLEditor htmlEditor = alertDialog.findView("#et_html", HTMLEditor.class);
//		htmlEditor.setHtmlText(content);
//		alertDialog.show();
//	}

//	public static void info(String title, String info) {
//		new AlertDialog.Builder()
//		.view("dialog_info")
//		.title(title)
//		.setText("#textarea_info", info)
//		.build()
//		.show();
//	}
}
