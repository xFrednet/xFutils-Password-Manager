/******************************************************************************
 * xFutils-Password-Manager - A simple password manager                        *
 *                 <https://github.com/xFrednet/xFutils-Password-Manager>      *
 *                                                                             *
 * =========================================================================== *
 * Copyright (C) 2018, xFrednet <xFrednet@gmail.com>                           *
 *                                                                             *
 * This software is provided 'as-is', without any express or implied warranty. *
 * In no event will the authors be held liable for any damages arising from    *
 * the use of this software.                                                   *
 *                                                                             *
 * Permission is hereby granted, free of charge, to anyone to use this         *
 * software for any purpose, including the rights to use, copy, modify,        *
 * merge, publish, distribute, sublicense, and/or sell copies of this          *
 * software, subject to the following conditions:                              *
 *                                                                             *
 *   1.  The origin of this software must not be misrepresented; you           *
 *       must not claim that you wrote the original software. If you           *
 *       use this software in a product, an acknowledgment in the              *
 *       product documentation would be greatly appreciated but is not         *
 *       required                                                              *
 *                                                                             *
 *   2.  Altered source versions should be plainly marked as such, and         *
 *       must not be misrepresented as being the original software.            *
 *                                                                             *
 *   3.  This code should not be used for any military or malicious            *
 *       purposes.                                                             *
 *                                                                             *
 *   4.  This notice may not be removed or altered from any source             *
 *       distribution.                                                         *
 *                                                                             *
 ******************************************************************************/
package com.gmail.xfrednet.xfutils.passwordmanager;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import static javafx.application.Platform.exit;


public class PasswordManagerApp {

	private static final boolean JUMP_ASK  = true;

	private static final String TITLE              = "xFutils Password Manager";
	private static final String TIME_STAMP_FORMAT  = "yyyy.MM.dd.HH.mm.ss";
	private static final String SALT_FILE_NAME     = "salt.and.pepper";
	private static final int    SALT_SIZE          = 256;
	private static final String SETTINGS_FILE_NAME = "settings.txt";

	private static final int       GUI_INFO_AREA_HEIGHT       = 100;
	private static final Color[]   GUI_MODE_INFO_COLORS       = {Color.green, Color.blue, Color.red};
	private static final Color     GUI_BORDER_COLOR           = new Color(0xacacac);
	private static final int       GUI_DEFAULT_GAP            = 5;
	private static final Border    GUI_DEFAULT_BORDER         = BorderFactory.createEmptyBorder(GUI_DEFAULT_GAP, GUI_DEFAULT_GAP, GUI_DEFAULT_GAP, GUI_DEFAULT_GAP);
	private static final int       GUI_DATA_INFO_PANE_COUNT   = 5;
	private static final int       GUI_DATA_INFO_LABEL_WIDTH  = 50;
	private static final Dimension GUI_ICON_BUTTON_DIMENSIONS = new Dimension(24, 24);

	private static final int     RETRIEVE_MODE       = 0;
	private static final int     CHANGE_MODE         = 1;
	private static final int     DELETE_MODE         = 2;
	private static final int     MODE_COUNT          = 3;

	private Settings settings;
	private final Language language = new Language();

	byte[] key;
	private int currentMode;
	private int buttonsPerRow;
	ArrayList<DataTab> dataTabs;

	//
	// Base GUI
	//
	private JFrame window;
	// info
	private JPanel guiDataInfoPanel;

	private JTextPane[] guiDataInfoPanes;

	private JLabel guiModeInfoLabel;
	private JPanel guiModeInfoColor;
	private JButton[] guiModeButtons;
	//contentList tabs
	private JTabbedPane guiDataTabs;
	private JTextField guiNewTabNameField;
	private JSpinner guiNewTabIndexSelector;

	public static void main(String[] args) {

		byte[] key = GetDecryptionKey();

		if (key == null) {
			System.out.println("GetDecryptionKey failed or was terminated. The program will also terminate!");
			return;
		} else {
			System.out.println("Key: " + ArrayToHex(key));
		}

		System.out.println("Key: " + ArrayToHex(HexToArray(ArrayToHex(key))));

		PasswordManagerApp app = new PasswordManagerApp(key);

	}

	public PasswordManagerApp(byte[] key) {
		this.key = key;
		this.dataTabs = new ArrayList<DataTab>();
		this.buttonsPerRow = 3;

		if (!initSettings()) {
			System.err.println("initSettings failed!");
			exit();
		}
		System.out.println("Let'se got");

		//TODO load contentList
		DataTab tab1 = new DataTab("tab 1");
		tab1.add(new Data("Test data 1", "hello 1"));
		tab1.dataList.get(0).contentList.add("This");
		tab1.dataList.get(0).contentList.add("is");
		tab1.dataList.get(0).contentList.add("xFrednet");
		tab1.dataList.get(0).contentList.add("The");
		tab1.dataList.get(0).contentList.add("One");
		tab1.dataList.get(0).contentList.add("And");
		tab1.dataList.get(0).contentList.add("Only");
		tab1.add(new Data("Test data 2<br>next Line", "hello 2"));
		tab1.add(new Data("Test data 3", "hello 3"));
		tab1.add(new Data("Test data 4", "hello 4"));
		tab1.add(new Data("Test data 5", "hello 5"));
		tab1.add(new Data("Test data 6", "hello 6"));
		tab1.add(new Data("Test data 7", "hello 7"));
		this.dataTabs.add(tab1);

		initBaseGUI();

		initTabGUI();
		//TODO update contentList GUI

		setMode(CHANGE_MODE);
	}

	private boolean initSettings() {
		this.settings = Settings.LoadSettings(SETTINGS_FILE_NAME);
		if (this.settings == null) {

			System.err.println("Settings.LoadSettings has failed a new settings instance will be initialized and saved!");

			this.settings = Settings.InitDefault();
			if (!this.settings.saveOptions(SETTINGS_FILE_NAME)) {
				System.err.println("Saving the new Settings instance has failed! bye bye :/");
				return false;
			}
		}

		return true;
	}
	private void initBaseGUI() {
		//
		// JFrame
		//
		this.window = new JFrame(TITLE);
		this.window.setBounds(this.settings.frameX, this.settings.frameY, this.settings.frameWidth, this.settings.frameHeight);
		this.window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.window.setResizable(true);

		//
		// Layout
		//
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.setLayout(new BorderLayout());

		//
		// Menu
		//
		if (true) { //for folding
			JMenuBar menuBar = new JMenuBar();

			// file
			JMenu fileMenu = new JMenu(this.language.FILE_MENU_NAME);
			fileMenu.setMnemonic(KeyEvent.VK_F);

			fileMenu.add(new JMenuItem("TODO Change Password"));
			fileMenu.add(new JPopupMenu.Separator());
			fileMenu.add(new JMenuItem("TODO Add Data"));
			fileMenu.add(new JPopupMenu.Separator());
			fileMenu.add(new JMenuItem("TODO Copy Salt"));
			fileMenu.add(new JMenuItem("TODO Make Backup"));
			fileMenu.add(new JPopupMenu.Separator());
			fileMenu.add(new JMenuItem("TODO Open Settings"));
			menuBar.add(fileMenu);

			// add Data
			JMenu addDataMenu = new JMenu("TODO Add Data");
			menuBar.add(addDataMenu);

			// extra
			JMenu extrasMenu = new JMenu(this.language.EXTRAS_MENU_NAME);
			extrasMenu.add(new JMenuItem("TODO export to unencrypted txt file"));
			extrasMenu.add(new JMenuItem("TODO import txt file"));
			extrasMenu.add(new JPopupMenu.Separator());
			extrasMenu.add(new JMenuItem("TODO encrypt file"));
			extrasMenu.add(new JMenuItem("TODO decrypt file"));
			menuBar.add(extrasMenu);

			this.window.setJMenuBar(menuBar);
		}

		//
		// Info/Option panel
		//
		if (true) { // for folding
			JPanel infoPanel = new JPanel();
			infoPanel.setMinimumSize(new Dimension(0, GUI_INFO_AREA_HEIGHT) );
			infoPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, GUI_INFO_AREA_HEIGHT) );
			infoPanel.setLayout(new GridLayout(1, 2));

			//
			// Data info 1/2
			//
			this.guiDataInfoPanel = new JPanel(new GridBagLayout());
			this.guiDataInfoPanel.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createLineBorder(GUI_BORDER_COLOR),
					GUI_DEFAULT_BORDER));
			infoPanel.add(this.guiDataInfoPanel);


			JLabel[] labels = new JLabel[GUI_DATA_INFO_PANE_COUNT];
			this.guiDataInfoPanes = new JTextPane[GUI_DATA_INFO_PANE_COUNT];

			// GridBagConstraints
			GridBagConstraints labelConstrains = new GridBagConstraints();
			labelConstrains.fill = GridBagConstraints.BOTH;
			GridBagConstraints paneConstrains = new GridBagConstraints();
			paneConstrains.fill = GridBagConstraints.BOTH;
			paneConstrains.weightx = 1.0f;
			paneConstrains.gridwidth = GridBagConstraints.REMAINDER;

			for (int infoPaneIndex = 0; infoPaneIndex < GUI_DATA_INFO_PANE_COUNT; infoPaneIndex++) {
				// label
				labels[infoPaneIndex] = new JLabel();
				labels[infoPaneIndex].setPreferredSize(new Dimension(GUI_DATA_INFO_LABEL_WIDTH, 0));

				if (infoPaneIndex == 0) {
					labels[infoPaneIndex].setText(this.language.INFO_PANEL_TITLE_LABEL);
				} else {
					String content = String.format(this.language.INFO_PANEL_DATA_LABEL, infoPaneIndex - 1);
					labels[infoPaneIndex].setText(content);
				}
				this.guiDataInfoPanel.add(labels[infoPaneIndex], labelConstrains);

				// text
				this.guiDataInfoPanes[infoPaneIndex] = CreateSelectableText();
				this.guiDataInfoPanes[infoPaneIndex].setText("<html>-</html>");
				this.guiDataInfoPanel.add(this.guiDataInfoPanes[infoPaneIndex], paneConstrains);
			}

			//
			// Mode 1/2
			//
			JPanel selectModePanel = new JPanel();
			selectModePanel.setLayout(new GridLayout(3, 1, GUI_DEFAULT_GAP, GUI_DEFAULT_GAP));
			selectModePanel.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createLineBorder(GUI_BORDER_COLOR),
					GUI_DEFAULT_BORDER));

			// guiModeInfoLabel
			this.guiModeInfoLabel = new JLabel(this.language.NO_MODE_SELECTED, JLabel.LEFT);
			selectModePanel.add(this.guiModeInfoLabel);

			// guiModeInfoColor
			this.guiModeInfoColor = new JPanel();
			selectModePanel.add(this.guiModeInfoColor);

			// mode select buttons
			JPanel modeButtonPanel = new JPanel(new GridLayout(1, 3, GUI_DEFAULT_GAP, GUI_DEFAULT_GAP));
			this.guiModeButtons = new JButton[3];
			for (int modeButtonNo = 0; modeButtonNo < MODE_COUNT; modeButtonNo++) {
				this.guiModeButtons[modeButtonNo] = new JButton(this.language.MODE_BUTTON_LABELS[modeButtonNo]);
				this.guiModeButtons[modeButtonNo].addActionListener(e -> {
						JButton button = (JButton)e.getSource();

						for (int modeNo = 0; modeNo < MODE_COUNT; modeNo++) {
							String testLabel = PasswordManagerApp.this.language.MODE_BUTTON_LABELS[modeNo];
							if (button.getText().equals(testLabel)) {
								PasswordManagerApp.this.setMode(modeNo);
								break;
							}
						}
					});
				modeButtonPanel.add(this.guiModeButtons[modeButtonNo]);
			}

			selectModePanel.add(modeButtonPanel);

			infoPanel.add(selectModePanel);
			mainPanel.add(BorderLayout.NORTH, infoPanel);
		}

		//
		// Content panel
		//
		if (true) { // this is a folding line
			JPanel contentPanel = new JPanel();

			contentPanel.setBorder(BorderFactory.createLineBorder(GUI_BORDER_COLOR));
			contentPanel.setLayout(new BorderLayout());
			this.guiDataTabs = new JTabbedPane();

			// add tab tab
			JPanel addTabPanel = new JPanel();
			addTabPanel.setLayout(new GridLayout(3, 1, 5, 5));

			// name filed
			this.guiNewTabNameField = new JTextField();
			this.guiNewTabNameField.setText(this.language.ADD_TAB_NAME_FIELD_TEXT);
			addTabPanel.add(this.guiNewTabNameField);

			// index selector
			JPanel indexSelectPanel = new JPanel(new GridLayout(1, 2));
			indexSelectPanel.add(new JLabel(this.language.ADD_TAB_INDEX_LABEL));
			this.guiNewTabIndexSelector = new JSpinner(
					new SpinnerNumberModel(this.dataTabs.size(), 0, this.dataTabs.size(), 1));
			indexSelectPanel.add(this.guiNewTabIndexSelector);
			addTabPanel.add(indexSelectPanel);

			//add button
			JButton addTabButton = new JButton(this.language.ADD_TAB_BUTTON_LABEL);
			addTabButton.addActionListener(e -> {
				String name = PasswordManagerApp.this.guiNewTabNameField.getText();
				int index = (int) PasswordManagerApp.this.guiNewTabIndexSelector.getValue();

				createTab(name, index);
			});
			addTabPanel.add(addTabButton);
			addTabPanel.setMaximumSize(new Dimension(250, 70));

			//tab panel to center addTabPanel
			JPanel tabPanel = new JPanel();
			tabPanel.setLayout(new BoxLayout(tabPanel, BoxLayout.Y_AXIS));
			tabPanel.add(addTabPanel);

			this.guiDataTabs.addTab("+", tabPanel);

			contentPanel.add(BorderLayout.CENTER, this.guiDataTabs);
			mainPanel.add(BorderLayout.CENTER, contentPanel);
		}

		//
		// Finish
		//
		this.window.add(mainPanel);
		this.window.setVisible(true);
	}
	private void initTabGUI() {

		int iteration = 0;
		for (DataTab tab : this.dataTabs) {

			JPanel panel = tab.initGUI(this.buttonsPerRow);
			JScrollPane scrollPane = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

			this.guiDataTabs.insertTab(tab.title, null, scrollPane, null, iteration);

			iteration++;
		}

		this.guiDataTabs.setSelectedIndex(0);
	}

	private void setMode(int modeID) {
		if (modeID < 0 || modeID >= MODE_COUNT) {
			System.err.println("setMode: An undefined modeID was requested, modeID:" + modeID);
			return;
		}

		this.currentMode = modeID;
		this.guiModeInfoLabel.setText(this.language.MODE_INFO_LABEL_TEXT[modeID]);
		this.guiModeInfoColor.setBackground(GUI_MODE_INFO_COLORS[modeID]);
		for (int modeNo = 0; modeNo < MODE_COUNT; modeNo++) {
			this.guiModeButtons[modeNo].setEnabled(true);
		}
		this.guiModeButtons[modeID].setEnabled(false);

		System.out.println("A new Mode was selected! Mode: " + this.language.MODE_BUTTON_LABELS[modeID]);
	}
	private void createTab(String name, int index) {
		if (!IsStringSupported(name)) {
			ShowInfoDialog(this.language.ERROR_STRING_NOT_SUPPORTED, this.window);
			return;
		}

		DataTab tab = new DataTab(name);
		this.dataTabs.add(index, tab);

		//gui
		JPanel panel = tab.initGUI(this.buttonsPerRow);
		JScrollPane scrollPane = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		this.guiDataTabs.insertTab(tab.title, null, scrollPane, null, index);
		this.guiDataTabs.setSelectedIndex(index);

		// update the addTab panel
		((SpinnerNumberModel)this.guiNewTabIndexSelector.getModel()).setMaximum(this.dataTabs.size()); // one line wonder or horror
		this.guiNewTabIndexSelector.setValue(this.dataTabs.size());

		//TODO save the new created tab
	}

	/* //////////////////////////////////////////////////////////////////////////////// */
	// // Cipher interfacing //
	/* //////////////////////////////////////////////////////////////////////////////// */
	private static byte[] GetDecryptionKey() {
		String userInput = AskForPassword();
		if (userInput == null) {
			return null;
		}

		/*
		 * Load or generate salt
		 */
		byte[] salt = LoadSalt();
		if (salt == null) {
			//Info dialog
			//TODO add option to set salt instead of creating a new one
			int option = JOptionPane.showConfirmDialog(null,
					"The salt could not be loaded! A new salt will be generated!",
					TITLE, JOptionPane.OK_CANCEL_OPTION);

			// create new if ok was selected
			if (option == JOptionPane.OK_OPTION) {
				System.out.println("A new salt will be generated!");
				salt = CreateNewSalt();
			}
		}
		if (salt == null) {
			System.err.println("The creation of the salt failed!!!");
			return null; // unable to load or create the salt
		}

		System.out.println("Salt: " +  ArrayToHex(salt));

		return Cipher.HashPassword(userInput.toCharArray(), salt);
	}
	private static String AskForPassword() {

		if (JUMP_ASK)
			return "HELLO";
		/*
		 * Configuration
		 */
		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		contentPanel.setFocusable(false);
		JLabel textLabel = new JLabel("Enter a password:");
		textLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
		textLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		textLabel.setFocusable(false);

		JPasswordField passwordField = new JPasswordField();
		contentPanel.add(textLabel);
		contentPanel.add(passwordField);

		/*
		 * show the input dialog
		 */
		int option = JOptionPane.showConfirmDialog(null, contentPanel, TITLE, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

		/*
		 * process the user input
		 */
		if (option == JOptionPane.OK_OPTION) {
			char[] password = passwordField.getPassword();

			if (password.length == 0) {
				return null;
			}

			return new String(password);
		} else {
			return null; //Cancel was selected
		}
	}
	private static byte[] LoadSalt() {
		File file = new File(SALT_FILE_NAME);

		// if file exists load it's content
		if (file.exists()) {
			try {
				FileInputStream fileStream = new FileInputStream(file);

				//read
				byte[] buffer = new byte[SALT_SIZE];
				int readBytes = fileStream.read(buffer, 0, SALT_SIZE);
				fileStream.close();
				if (readBytes == SALT_SIZE) {
					return buffer;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return null;
	}
	private static byte[] CreateNewSalt() {
		byte[] buffer = Cipher.GenerateSalt(SALT_SIZE);

		if (!SafeSalt(buffer)) {
			return null; // We shouldn't use a salt that is not saved
		}

		return buffer;
	}
	private static boolean SafeSalt(byte[] salt) {

		/*
		 * Validation
		 */
		if (salt.length < SALT_SIZE) {
			return false;
		}

		/*
		 * backup / rename old file
		 */
		File saltFile = new File(SALT_FILE_NAME);
		if (saltFile.exists()) {
			if (!saltFile.renameTo(new File(GetTimeStamp() + SALT_FILE_NAME))) {
				return false; //failed to rename old salt file!
			}
		}

		/*
		 * Save the salt
		 */
		try {
			FileOutputStream fileStream = new FileOutputStream(saltFile);
			fileStream.write(salt, 0, SALT_SIZE);
			fileStream.close();

			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return false; // The writing failed
	}
	private static boolean IsStringSupported(String testString) {
		if (testString.isEmpty())
			return false;

		if (testString.contains(DataTab.FORMAT_TAB_SEPARATOR + ""))
			return false;

		if (testString.contains(DataTab.FORMAT_DATA_SEPARATOR + ""))
			return false;

		if (testString.contains(Data.FORMAT_UNIT_SEPARATOR + ""))
			return false;

		if (testString.contains("<html>") || testString.contains("</html>"))
			return false;

		return true;
	}

	/* //////////////////////////////////////////////////////////////////////////////// */
	// // Utility //
	/* //////////////////////////////////////////////////////////////////////////////// */
	private static String GetTimeStamp() {
		return new SimpleDateFormat(TIME_STAMP_FORMAT).format(new Date());
	}
	private static String ArrayToHex(byte[] data) {
		StringBuilder sb = new StringBuilder();

		for (int index = 0; index < data.length; index++) {
			sb.append(String.format("%02X", data[index]));
		}

		return sb.toString();
	}
	private static byte[] HexToArray(String hex) {
		int hexLen = hex.length();
		byte[] buffer = new byte[hexLen / 2];

		for (int hexNo = 0; hexNo < hexLen; hexNo += 2) {
			buffer[hexNo / 2] =  (byte)(Character.digit(hex.charAt(hexNo), 16) << 4);
			buffer[hexNo / 2] += (byte)(Character.digit(hex.charAt(hexNo + 1), 16));
		}

		return buffer;
	}
	private static void ShowInfoDialog(String infoString, Component parent) {
		Object[] options = {"OK"};
		JOptionPane.showOptionDialog(parent,
				infoString,TITLE,
				JOptionPane.PLAIN_MESSAGE,
				JOptionPane.QUESTION_MESSAGE,
				null,
				options,
				options[0]);
	}
	private static JTextPane CreateSelectableText() {

		JTextPane testField = new JTextPane();

		testField.setContentType("text/html"); // let the text pane know that I want HTML support
		testField.setEditable(false);          // remove the edit option
		testField.setBackground(null);         // Remove cosmetics
		testField.setBorder(null);             // Remove cosmetics

		return testField;
	}
	private static void CopyToClipboard(String text) {
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(text), null);
	}

	private String getDataTabGroupSaveString(ArrayList<DataTab> tabs) {
		StringBuilder sb = new StringBuilder();

		for (int tabIndex = 0; tabIndex < tabs.size(); tabIndex++) {
			sb.append(tabs.get(tabIndex).getSaveString());

			// add divider
			if (tabIndex != tabs.size() - 1) { // if not last
				sb.append(DataTab.FORMAT_TAB_SEPARATOR);
			}
		}

		return sb.toString();
	}
	private ArrayList<DataTab> loadDataTabGroupFromSaveString(String saveString) {
		if (saveString.isEmpty()) {
			return null;
		}

		String[] components = saveString.split(DataTab.FORMAT_TAB_SEPARATOR + "");

		if (components.length == 0) {
			return null; // well there is no contentList to load
		}

		ArrayList<DataTab> tabs = new ArrayList<DataTab>();
		for (int componentIndex = 0; componentIndex < components.length; componentIndex++) {
			DataTab tab = loadDataTabFromSaveString(components[componentIndex]);

			if (tab == null) {
				return null; // the tab failed to load
			}

			tabs.add(tab);
		}

		return tabs;
	}
	private DataTab loadDataTabFromSaveString(String saveString) {
		DataTab tab = new DataTab("Loading");

		if (!tab.loadSaveString(saveString)) {
			return null;
		}

		return tab;
	}
	private Data loadDataFromSaveString(String saveString) {
		Data data = new Data();

		if (!data.loadSaveString(saveString)) {
			return null;
		}

		return data;
	}

	/* //////////////////////////////////////////////////////////////////////////////// */
	// // Nested classes //
	/* //////////////////////////////////////////////////////////////////////////////// */
	/*
	* I'm sorry this is definitely not ideal. Data needs to access objects from
	* this outer class there is no cleaner way that I can think of. The DataTab class
	* could be separate but it would be just weired if that would be a different class
	* while the contentList is here.
	*/
	public class DataTab {

		static final char FORMAT_TAB_SEPARATOR  = 0x1D;
		static final char FORMAT_DATA_SEPARATOR = 0x1E;
		static final int  GUI_ROW_HEIGHT = 50;

		/* //////////////////////////////////////////////////////////////////////////////// */
		// // Class //
		/* //////////////////////////////////////////////////////////////////////////////// */
		String title;
		ArrayList<Data> dataList;
		JPanel guiPanel;

		DataTab(String title) {
			this.dataList = new ArrayList<Data>();
			this.title = title;
		}

		/* ********************************************************* */
		// * utils *
		/* ********************************************************* */
		void add(Data data) {
			this.dataList.add(data);
		}
		JPanel initGUI(int buttonsPerRow) {

			/*
			* creating the GUI
			*/
			this.guiPanel = new JPanel();
			this.guiPanel.setLayout(new GridBagLayout());

			/*
			* Create button GUIs
			* */
			for (Data data : this.dataList) {
				data.createGUI();
			}

			/*
			* update GUI
			* */
			updateGUI(buttonsPerRow);

			return this.guiPanel;
		}
		void updateGUI(int buttonsPerRow) {
			/*
			 * Validation
			 */
			if (buttonsPerRow <= 0)
				return; // well there you have less than one button per row

			// The following is a roller coaster of emotions
			//
			// 1. this is gonna end terrible
			//    so basically I create a array of GridBagConstrains that
			//    should be used for everyone in a row. This means that if the length
			//    of this array is 5 the GUI has 5 buttons in one row
			//
			// 2. I even have a worse idea I'll see if I change this
			//
			// 3. So I removed the gui modes and replaced that with the value
			//    buttonsPerRow. Now I'll also create a array with a GridBagConstraints
			//    for every button in a row
			//
			GridBagConstraints[] placements = new GridBagConstraints[buttonsPerRow];
			for (int pIndex = 0; pIndex < placements.length; pIndex++) {
				placements[pIndex] = new GridBagConstraints();

				placements[pIndex].fill    = GridBagConstraints.HORIZONTAL;
				placements[pIndex].weightx = 1.0;
				placements[pIndex].insets  = new Insets(0, 0,
						GUI_DEFAULT_GAP, GUI_DEFAULT_GAP);

				if (pIndex == placements.length - 1)
					placements[pIndex].gridwidth = GridBagConstraints.REMAINDER;
			}

			/*
			* Remove the buttons readd the buttons
			* */
			this.guiPanel.removeAll();
			int iterations = 0;
			for (Data data : this.dataList) {

				this.guiPanel.add(data.guiButton, placements[iterations % placements.length]);

				iterations++;
			}

			// The buttons have a padding on the right and bottom
			// this adds a padding to the top and left
			this.guiPanel.setBorder(BorderFactory.createEmptyBorder(GUI_DEFAULT_GAP, GUI_DEFAULT_GAP, 0, 0));
		}

		/* ********************************************************* */
		// * saving and loading *
		/* ********************************************************* */
		// FORMAT_DATA_SEPARATOR => DS
		// [title] ([DS] [contentList.getSaveString()]) x contentList.size()
		String getSaveString() {
			StringBuilder sb = new StringBuilder();

			//title
			sb.append(this.title);

			for (Data data : this.dataList) {
				sb.append(FORMAT_DATA_SEPARATOR);
				sb.append(data.getSaveString());
			}

			return sb.toString();
		}
		boolean loadSaveString(String saveString) {
			if (saveString.isEmpty()) {
				return false; // something went seriously wrong
			}
			String[] components = saveString.split(FORMAT_DATA_SEPARATOR + "");

			// load title
			this.title = components[0];

			// load contentList
			for (int dataNo = 1; dataNo < components.length; dataNo++) {
				Data data = loadDataFromSaveString(components[dataNo]);

				if (data == null)
					return false; // loading the contentList has failed. well done me!

				this.add(data);
			}

			// well done
			return true;
		}
	}
	public class Data {

		static final char FORMAT_UNIT_SEPARATOR = 0x1F;

		String title;
		ArrayList<String> contentList;
		int copyDataIndex;

		JButton guiButton;

		private Data() {
			this.contentList = new ArrayList<String>();
		}
		Data(String title, String data) {
			this();
			this.title = title;
			this.contentList.add(data);
			this.copyDataIndex = 0;
		}

		void createGUI() {

			this.guiButton = new JButton("<html>" + this.title + "</html>");
			this.guiButton.setPreferredSize(new Dimension(0, DataTab.GUI_ROW_HEIGHT));

			this.guiButton.addActionListener(e -> {

				boolean saveChanges = false;

				switch (PasswordManagerApp.this.currentMode) {
					case RETRIEVE_MODE:
						writeToInfoLabel();
						break;
					case CHANGE_MODE:
						saveChanges = true;
						openChangeDataGUI();
						break;
					case DELETE_MODE:
						saveChanges = true;
						break;
				}

				if (saveChanges) {
					// TODO save changes
				}
			});
		}

		// actions
		void writeToInfoLabel() {

			//TODO maybe remove the line break for long titles or data
			JTextPane[] panes = PasswordManagerApp.this.guiDataInfoPanes;

			panes[0].setText("<html>" + this.title.replace("<br>", " ") + "</html>");

			for (int paneIndex = 1; paneIndex < panes.length; paneIndex++) {
				int contentIndex = paneIndex - 1;

				if (contentIndex < this.contentList.size()) {
					String content = this.contentList.get(contentIndex).replace("<br>", "");
					panes[paneIndex].setText("<html>" + content + "</html>");

				} else {
					panes[paneIndex].setText("<html>-</html>");
				}
			}
		}

		/* ********************************************************* */
		// * GUI stuff *
		/* ********************************************************* */
		void openChangeDataGUI() {

			Language language = PasswordManagerApp.this.language;

			//
			// Create dialog
			//
			JDialog dialog = new JDialog(PasswordManagerApp.this.window);
			dialog.setAutoRequestFocus(true);
			dialog.setLocationRelativeTo(PasswordManagerApp.this.window);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

			//
			// Panel & Layout
			//
			ImageIcon editIcon   = new ImageIcon(Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResource("edit_icon.png")));
			ImageIcon copyIcon   = new ImageIcon(Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResource("copy_icon.png")));
			ImageIcon deleteIcon = new ImageIcon(Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResource("delete_icon.png")));
			ImageIcon checkIcon  = new ImageIcon(Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResource("check_icon.png")));
			Border panelBorder = BorderFactory.createCompoundBorder(
					BorderFactory.createLineBorder(GUI_BORDER_COLOR, 1),
					BorderFactory.createEmptyBorder(GUI_DEFAULT_GAP / 2, GUI_DEFAULT_GAP, GUI_DEFAULT_GAP / 2, GUI_DEFAULT_GAP));

			GridLayout mainLayout = new GridLayout(this.contentList.size() + 1, 1);
			JPanel mainPanel = new JPanel(mainLayout);
			ButtonGroup radioGroup = new ButtonGroup();

			//
			// GridBagLayout constrains
			//
			// radioConstrains
			GridBagConstraints radioConstrains = new GridBagConstraints();
			radioConstrains.fill           = GridBagConstraints.HORIZONTAL;
			radioConstrains.insets         = new Insets(0, 0, 0, GUI_DEFAULT_GAP);
			// labelConstrains
			GridBagConstraints labelConstrains = new GridBagConstraints();
			labelConstrains.fill           = GridBagConstraints.HORIZONTAL;
			labelConstrains.insets         = new Insets(0, 0, 0, GUI_DEFAULT_GAP);
			// dataConstrains
			GridBagConstraints dataConstrains = new GridBagConstraints();
			dataConstrains.fill            = GridBagConstraints.HORIZONTAL;
			dataConstrains.weightx         = 1.0;
			dataConstrains.insets          = new Insets(0, 0, 0, GUI_DEFAULT_GAP);
			// editIconConstrains
			GridBagConstraints editIconConstrains = new GridBagConstraints();
			editIconConstrains.fill        = GridBagConstraints.HORIZONTAL;
			editIconConstrains.insets      = new Insets(0, 0, 0, GUI_DEFAULT_GAP);
			// copyIconConstrains
			GridBagConstraints copyIconConstrains = new GridBagConstraints();
			copyIconConstrains.fill        = GridBagConstraints.HORIZONTAL;
			copyIconConstrains.insets      = new Insets(0, 0, 0, GUI_DEFAULT_GAP);
			// deleteIconConstrains
			GridBagConstraints deleteIconConstrains = new GridBagConstraints();
			deleteIconConstrains.fill      = GridBagConstraints.HORIZONTAL;
			deleteIconConstrains.gridwidth = GridBagConstraints.REMAINDER; // end row
			deleteIconConstrains.insets    = new Insets(0, 0, 0, 0); // no insets, done by the panel

			//
			// Add title
			//
			if (true) { // for folding
				JPanel titlePanel = new JPanel(new GridBagLayout());
				titlePanel.setBorder(panelBorder);

				titlePanel.add(new JPanel(), radioConstrains); // no radio button

				// titleLabel
				JLabel titleLabel = new JLabel(language.INFO_PANEL_TITLE_LABEL);
				titlePanel.add(titleLabel, labelConstrains);

				// titleField
				JTextPane titleField = CreateSelectableText();
				titleField.setContentType("text/plain");
				titleField.setText(this.title);
				titlePanel.add(titleField, dataConstrains);

				// editTitleButton
				JButton editTitleButton = new JButton(editIcon);
				editTitleButton.setPreferredSize(GUI_ICON_BUTTON_DIMENSIONS);
				editTitleButton.addActionListener(e -> {
					System.out.println("editTitleButton: Edit le title");
				});
				titlePanel.add(editTitleButton, editIconConstrains);

				// copyTitleButton
				JButton copyTitleButton = new JButton(copyIcon);
				copyTitleButton.setPreferredSize(GUI_ICON_BUTTON_DIMENSIONS);
				copyTitleButton.addActionListener(e -> {
					CopyToClipboard(this.title);
				});
				titlePanel.add(copyTitleButton, copyIconConstrains);

				titlePanel.add(new JPanel(), deleteIconConstrains); // no delete Icon for the title

				// add to the main panel, not that I may have ever forgotten that
				mainPanel.add(titlePanel);
			}

			//
			// Add content rows
			//
			for (int contentIndex = 0; contentIndex < this.contentList.size(); contentIndex++) {
				JPanel panel = new JPanel();
				panel.setBorder(panelBorder);

				//TODO add content row

				mainPanel.add(panel);
			}

			//
			// Show dialog
			//
			dialog.setContentPane(mainPanel);
			dialog.pack();
			dialog.setVisible(true);
		}

		/* ********************************************************* */
		// * saving and loading *
		/* ********************************************************* */
		// [FORMAT_UNIT_SEPARATOR] => US
		// Save format [title] [US] [copyDataIndex] ([US] [contentList]) x contentList.size()
		String getSaveString() {
			StringBuilder sb = new StringBuilder();

			// Title
			sb.append(this.title);

			// copy Data Index
			sb.append(FORMAT_UNIT_SEPARATOR);
			sb.append(Integer.toString(this.copyDataIndex));

			// Data
			for (String data : this.contentList) {
				sb.append(FORMAT_UNIT_SEPARATOR);
				sb.append(data);
			}

			return sb.toString();
		}
		boolean loadSaveString(String saveString) {
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
				this.contentList.add(components[componentNo]);
			}

			return true;
		}

	}
}




