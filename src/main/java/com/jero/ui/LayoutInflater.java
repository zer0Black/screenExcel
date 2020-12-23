package com.jero.ui;

import javafx.fxml.FXMLLoader;

import java.net.URL;

public class LayoutInflater {

	private final static String urlPrefix = "static/fxml/";
	private final static String urlSuffix = ".fxml";

	public static <T>T inflate(String viewName,Class<T> clazz){
		try {
			String filePath = urlPrefix + viewName + urlSuffix;
			String url = LayoutInflater.class.getClassLoader().getResource(filePath).toExternalForm();
			T t = FXMLLoader.load(new URL(url));
			return (T) t;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static FXMLLoader getFxmlLoad(String viewName){
		String filePath = urlPrefix + viewName + urlSuffix;
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(LayoutInflater.class.getClassLoader().getResource(filePath));
		return fxmlLoader;
	}
}
