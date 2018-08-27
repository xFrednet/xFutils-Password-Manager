package com.gmail.xfrednet.xfutils.passwordmanager;

import java.util.ArrayList;

public class DataTab {

	public static final char FORMAT_TAB_SEPARATOR  = 0x1D;
	public static final char FORMAT_DATA_SEPARATOR = 0x1E;

	/* //////////////////////////////////////////////////////////////////////////////// */
	// // Static group load and save functions //
	/* //////////////////////////////////////////////////////////////////////////////// */
	public static String GetGroupSaveString(ArrayList<DataTab> tabs) {
		StringBuilder sb = new StringBuilder();

		for (int tabIndex = 0; tabIndex < tabs.size(); tabIndex++) {
			sb.append(tabs.get(tabIndex).getSaveString());

			// add divider
			if (tabIndex != tabs.size() - 1) { // if not last
				sb.append(FORMAT_TAB_SEPARATOR);
			}
		}

		return sb.toString();
	}
	public static ArrayList<DataTab> LoadGroupFromSaveString(String saveString) {
		if (saveString.isEmpty()) {
			return null;
		}

		String[] components = saveString.split(FORMAT_TAB_SEPARATOR + "");

		if (components.length == 0) {
			return null; // well there is no dataList to load
		}

		ArrayList<DataTab> tabs = new ArrayList<DataTab>();
		for (int componentIndex = 0; componentIndex < components.length; componentIndex++) {
			DataTab tab = DataTab.LoadDataTab(components[componentIndex]);

			if (tab == null) {
				return null; // the tab failed to load
			}

			tabs.add(tab);
		}

		return tabs;
	}

	/* //////////////////////////////////////////////////////////////////////////////// */
	// // Class //
	/* //////////////////////////////////////////////////////////////////////////////// */
	private String title;
	private ArrayList<Data> dataList;

	private DataTab() {
		this.dataList = new ArrayList<Data>();
	}
	public DataTab(String title) {
		this();
		this.title = title;
	}
	private static DataTab LoadDataTab(String saveString) {
		DataTab tab = new DataTab();

		if (!tab.loadSaveString(saveString)) {
			return null;
		}

		return tab;
	}

	/* ********************************************************* */
	// * utils *
	/* ********************************************************* */
	public void add(Data data) {
		this.dataList.add(data);
	}

	/* ********************************************************* */
	// * saving and loading *
	/* ********************************************************* */
	// FORMAT_DATA_SEPARATOR => DS
	// [title] ([DS] [dataList.getSaveString()]) x dataList.size()
	private String getSaveString() {
		StringBuilder sb = new StringBuilder();

		//title
		sb.append(this.title);

		for (int dataIndex = 0; dataIndex < dataList.size(); dataIndex++) {
			sb.append(FORMAT_DATA_SEPARATOR);
			sb.append(dataList.get(dataIndex).getSaveString());
		}

		return sb.toString();
	}
	private boolean loadSaveString(String saveString) {
		if (saveString.isEmpty()) {
			return false; // something went seriously wrong
		}
		String[] components = saveString.split(FORMAT_DATA_SEPARATOR + "");

		// load title
		this.title = components[0];

		// load dataList
		for (int dataNo = 1; dataNo < components.length; dataNo++) {
			Data data = Data.LoadData(components[dataNo]);

			if (data == null)
				return false; // loading the dataList has failed. well done me!

			this.add(data);
		}

		// well done
		return true;
	}
}
