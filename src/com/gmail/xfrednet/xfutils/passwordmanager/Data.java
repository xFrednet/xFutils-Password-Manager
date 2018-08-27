package com.gmail.xfrednet.xfutils.passwordmanager;

import java.util.ArrayList;

public class Data {

	public static final char FORMAT_UNIT_SEPARATOR = 0x1F;

	String title;
	ArrayList<String> data;
	int copyDataIndex;

	private Data() {
		this.data = new ArrayList<String>();
	}
	public Data(String title, String data)
	{
		this();
		this.title = title;
		this.data.add(data);
		this.copyDataIndex = 0;
	}
	public static Data LoadData(String saveString) {
		Data data = new Data();

		if (!data.loadSaveString(saveString)) {
			return null;
		}

		return data;
	}

	// [FORMAT_UNIT_SEPARATOR] => US
	// Save format [title] [US] [copyDataIndex] ([US] [data]) x data.size()
	public String getSaveString() {
		StringBuilder sb = new StringBuilder();

		// Title
		sb.append(title);

		// copy Data Index
		sb.append(FORMAT_UNIT_SEPARATOR);
		sb.append(Integer.toString(copyDataIndex));

		// Data
		for (int dataIndex = 0; dataIndex < data.size(); dataIndex++) {
			sb.append(FORMAT_UNIT_SEPARATOR);
			sb.append(data.get(dataIndex));
		}

		return sb.toString();
	}
	public boolean loadSaveString(String saveString) {
		if (saveString.isEmpty()) {
			return false;
		}

		String[] components = saveString.split("" + FORMAT_UNIT_SEPARATOR);

		if (components.length < 3) {
			return false; //a save string has at least 3 components
		}

		this.title = components[0];
		try {
			this.copyDataIndex = Integer.parseInt(components[1]);
		} catch (NumberFormatException e) {
			return false;
		}

		for (int componentNo = 2; componentNo < components.length; componentNo++) {
			this.data.add(components[componentNo]);
		}

		return true;
	}

}
