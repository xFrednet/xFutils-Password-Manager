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


import java.awt.event.InputEvent;
import java.nio.charset.StandardCharsets;
import java.security.*;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.*;
import java.security.spec.InvalidKeySpecException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.*;

import static javafx.application.Platform.exit;


public class PasswordManagerApp {

	private static final boolean JUMP_ASK = true;

	private static final String TITLE                = "xFutils Password Manager";
	private static final String TIME_STAMP_FORMAT    = "yyyy.MM.dd.HH.mm.ss";

	//
	// cipher & saving
	//
	private static final char FORMAT_TAB_SEPARATOR  = 0x1D;
	private static final char FORMAT_DATA_SEPARATOR = 0x1E;
	private static final char FORMAT_UNIT_SEPARATOR = 0x1F;
	private static final char FORMAT_PADDING_START  = 0x03;

	private static final String CRYPT_FILE_EXTENSION = ".XFCRYPT";
	private static final String SAFE_FILE_NAME       = "save" + CRYPT_FILE_EXTENSION;
	private static final String BACKUP_FILE_NAME     = "backup\\backup%s.XFCRYPT";
	private static final String SETTINGS_FILE_NAME   = "settings.txt";
	private static final String TXT_EXPORT_FILE_NAME = "Password Manager Export.txt";

	private static final String CIPHER_PWHASH_ALGORITHM   = "PBKDF2WithHmacSHA1";
	private static final int    CIPHER_PWHASH_SIZE        = 128 / 8;
	private static final int    CIPHER_PWHASH_ITERATIONS  = 200000;
	private static final String CIPHER_FILE_ALGORITHM     = "AES/CBC/PKCS5Padding";
	private static final String CIPHER_DATA_ALGORITHM     = "AES/CBC/PKCS5Padding";
	private static final int    CIPHER_INIT_VEC_SIZE      = 16;
	private static final int    CIPHER_SALT_SIZE          = 256 / 8;
	private static final int    CIPHER_FILE_BUFFER_SIZE   = 1024;
	private static final int CIPHER_CONTENT_BLOCK_SIZE    = 256;

	//
	// GUI
	//
	private static final int       GUI_ENTER_PASSWORD_GUI_WIDTH      = 400;
	private static final Color     GUI_DELETE_DATA_BACKGROUND        = Color.red;
	private static final int       GUI_INFO_AREA_HEIGHT              = 100;
	private static final Color[]   GUI_MODE_INFO_COLORS              = new Color[]{Color.green, Color.blue, Color.red};
	private static final Color     GUI_BORDER_COLOR                  = new Color(0xacacac);
	private static final int       GUI_DEFAULT_GAP                   = 5;
	private static final Border    GUI_DEFAULT_GAP_BORDER            = BorderFactory.createEmptyBorder(GUI_DEFAULT_GAP, GUI_DEFAULT_GAP, GUI_DEFAULT_GAP, GUI_DEFAULT_GAP);
	private static final int       GUI_DATA_INFO_PANE_COUNT          = 5;
	private static final int       GUI_DATA_INFO_LABEL_WIDTH         = 50;
	private static final Dimension GUI_ICON_BUTTON_DIMENSIONS        = new Dimension(24, 24);
	private static final int       GUI_CHANGE_DATA_GUI_DEFAULT_WIDTH = 500;
	private static final int[]     GUI_SETTINGS_BUTTONS_PER_ROW_OPTIONS = new int[]{1, 3, 5};
	private static final Dimension GUI_WINDOW_MIN_SIZE               = new Dimension(900, 500);

	private static final ImageIcon GUI_EDIT_ICON   = new ImageIcon(Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResource("edit_icon.png")));
	private static final ImageIcon GUI_COPY_ICON   = new ImageIcon(Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResource("copy_icon.png")));
	private static final ImageIcon GUI_DELETE_ICON = new ImageIcon(Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResource("delete_icon.png")));
	private static final ImageIcon GUI_CHECK_ICON  = new ImageIcon(Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResource("check_icon.png")));
	private static final ImageIcon GUI_UNDO_ICON   = new ImageIcon(Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResource("undo_icon.png")));
	private static final ImageIcon GUI_WINDOW_ICON = new ImageIcon(Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResource("main_icon.png")));

	//
	// Modes
	//
	private static final int     RETRIEVE_MODE       = 0;
	private static final int     CHANGE_MODE         = 1;
	private static final int     DELETE_MODE         = 2;
	private static final int     MODE_COUNT          = 3;

	/* //////////////////////////////////////////////////////////////////////////////// */
	// // THE CLASS //
	/* //////////////////////////////////////////////////////////////////////////////// */
	private Settings settings;
	private final Language language;

	private byte[] salt;
	private String password;
	private byte[] cipherInitVector;

	private int currentMode;
	private ArrayList<DataTab> dataTabs;

	//
	// Base GUI
	//
	private JFrame window;

	private JMenuItem guiAddDataMItem;
	private JMenu guiTabUtilMenu;
	private JMenuItem guiMenuMoveTabLeft;
	private JMenuItem guiMenuMoveTabRight;

	private JTextPane[] guiDataInfoPanes;

	private JLabel guiModeInfoLabel;
	private JPanel guiModeInfoColor;
	private JButton[] guiModeButtons;
	//contentList tabs
	private JTabbedPane guiDataTabs;
	private JSpinner guiNewTabIndexSelector;

	public static void main(String[] args){
		new PasswordManagerApp();
	}

	private PasswordManagerApp() {
		this.window = null;
		this.dataTabs = new ArrayList<DataTab>();

		//
		// load settings
		//
		if (!initSettings()) {
			System.err.println("initSettings failed!");
			exit();
		}

		this.language = new Language(this.settings.languageID);

		//
		// get password
		//
		this.password = askForPassword();
		if (this.password == null) {
			System.out.println("GetDecryptionKey failed or was terminated. The program will also terminate!");
			return;
		}

		//
		// Load data
		//
		if (!loadData(SAFE_FILE_NAME)) {
			reinitCipherValues();
		}

		initWindow();
		initMenu();
		initBaseGUI();
		initTabGUI();

		setMode(RETRIEVE_MODE);

		//
		// make backup if last backup was last month
		//
		Date thirtyDaysAgo = Date.from(ZonedDateTime.now().plusDays(-30).toInstant());
		Date lastBackup = new Date(this.settings.lastBackup);
		if (thirtyDaysAgo.after(lastBackup)) {
			createBackup();
		}
	}

	private boolean initSettings() {
		this.settings = Settings.LoadSettings(SETTINGS_FILE_NAME);
		if (this.settings == null) {

			System.err.println("Settings.LoadSettings has failed a new settings instance will be initialized and saved!");

			this.settings = Settings.InitDefault();
			if (!this.settings.save(SETTINGS_FILE_NAME)) {
				System.err.println("Saving the new Settings instance has failed! bye bye :/");
				return false;
			}
		}

		return true;
	}
	private void initWindow() {
		//
		// JFrame
		//
		this.window = new JFrame(TITLE);
		this.window.setIconImage(GUI_WINDOW_ICON.getImage());
		this.window.setMinimumSize(new Dimension(GUI_WINDOW_MIN_SIZE));
		this.window.setBounds(this.settings.frameX, this.settings.frameY, this.settings.frameWidth, this.settings.frameHeight);
		this.window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.window.setResizable(true);
	}
	private void initMenu() {
		JMenuBar menuBar = new JMenuBar();

		// #######################
		// # File menu #
		// #######################
		menuBar.add(createFileMenu());

		// ##########################
		// # Tab util Menu #
		// ##########################
		menuBar.add(createTabUtilMenu());

		// #######################
		// # add Data #
		// #######################
		this.guiAddDataMItem = new JMenuItem(this.language.ADD_DATA_MENU_NAME);
		this.guiAddDataMItem.setMnemonic(KeyEvent.VK_A);
		this.guiAddDataMItem.addActionListener(e -> {
			int tabIndex = this.guiDataTabs.getSelectedIndex();

			// make sure that the current tab is not the "+" tab
			if (tabIndex == this.guiDataTabs.getTabCount() - 1) {
				showInfoDialog(this.language.ERROR_PLEASE_SELECT_A_DATA_TAB);
				return;
			}

			this.dataTabs.get(tabIndex).createData();

		});
		menuBar.add(this.guiAddDataMItem);

		// #######################
		// # Extras menu #
		// #######################
		menuBar.add(createExtrasMenu());

		this.window.setJMenuBar(menuBar);
	}
	private JMenu createFileMenu(){
		// file
		JMenu fileMenu = new JMenu(this.language.FILE_MENU_NAME);
		fileMenu.setMnemonic(KeyEvent.VK_F);

		JMenuItem changePassItem = new JMenuItem(this.language.CHANGE_PASSWORD_MENU_NAME);
		changePassItem.addActionListener(e -> {
			showChangePasswordDialog();
		});
		fileMenu.add(changePassItem);
		fileMenu.add(new JPopupMenu.Separator());

		JMenuItem backupItem = new JMenuItem(this.language.BACKUP_MENU_NAME);
		backupItem.addActionListener(e -> {
			if (createBackup()) {
				showInfoDialog(this.language.INFO_SAVE_BACKUP_OKAY);
			}
		});
		fileMenu.add(backupItem);
		fileMenu.add(new JPopupMenu.Separator());

		// #######################
		// # settings #
		// #######################
		JMenu settingsMenu = new JMenu(this.language.SETTINGS_MENU_NAME);

		//
		// frameMenu
		//
		JMenu frameMenu = new JMenu(this.language.SETTINGS_FRAME_MENU_NAME);
		JMenuItem savePosMenu = new JMenuItem(this.language.SETTINGS_SAVE_POS_MENU_NAME);
		savePosMenu.addActionListener(e -> {
			settings.frameX = this.window.getX();
			settings.frameY = this.window.getY();
			settings.save(SETTINGS_FILE_NAME);
		});
		frameMenu.add(savePosMenu);
		JMenuItem saveSizeMenu = new JMenuItem(this.language.SETTINGS_SAVE_SIZE_MENU_NAME);
		saveSizeMenu.addActionListener(e -> {
			settings.frameWidth  = this.window.getWidth();
			settings.frameHeight = this.window.getHeight();
			settings.save(SETTINGS_FILE_NAME);
		});
		frameMenu.add(saveSizeMenu);
		settingsMenu.add(frameMenu);

		//
		// layout menu
		//
		JMenu layoutMenu = new JMenu(this.language.SETTINGS_LAYOUT_MENU_NAME);
		ButtonGroup radioStation = new ButtonGroup(); // yes this joke is getting very old
		JRadioButtonMenuItem[] layoutRadios = new JRadioButtonMenuItem[GUI_SETTINGS_BUTTONS_PER_ROW_OPTIONS.length];
		int index = 0;
		for (int buttonsPerRow : GUI_SETTINGS_BUTTONS_PER_ROW_OPTIONS) {

			// create radio
			String radioName = String.format(this.language.SETTINGS_BUTTON_COUNT_MENU_NAME, buttonsPerRow);
			JRadioButtonMenuItem radio = new JRadioButtonMenuItem(radioName);
			radio.setSelected(buttonsPerRow == this.settings.buttonsPerRow); // select if it is the current "layout"
			radioStation.add(radio);


			// action listener
			radio.addActionListener(e -> {
				JRadioButtonMenuItem clickedRadio = (JRadioButtonMenuItem)e.getSource();

				int clickedIndex = 0;

				for (JRadioButtonMenuItem stationRadio : layoutRadios) {
					if (stationRadio.equals(clickedRadio)) {
						break;
					}
					clickedIndex++;
				}

				// save settings
				this.settings.buttonsPerRow = GUI_SETTINGS_BUTTONS_PER_ROW_OPTIONS[clickedIndex];
				this.settings.save(SETTINGS_FILE_NAME);

				updateTabGUIs();
			});

			// save the radio
			layoutMenu.add(radio);
			layoutRadios[index] = radio;
			index++;
		}

		settingsMenu.add(layoutMenu);

		//
		// languageMenu
		//
		JMenu languageMenu = new JMenu(this.language.SETTINGS_LANGUAGE_MENU_NAME);
		JCheckBoxMenuItem langDe = new JCheckBoxMenuItem(this.language.SETTINGS_LANGUAGE_DE_MENU_NAME);
		JCheckBoxMenuItem langEng = new JCheckBoxMenuItem(this.language.SETTINGS_LANGUAGE_ENG_MENU_NAME);
		langDe.setState(this.settings.languageID == Language.DE);
		langEng.setState(this.settings.languageID == Language.ENG);
		langDe.addActionListener(e -> {
			if (this.settings.languageID == Language.DE)
				return;

			this.settings.languageID = Language.DE;
			this.settings.save(SETTINGS_FILE_NAME);
			langEng.setState(false);

			showInfoDialog(this.language.INFO_RESTART_TO_LOAD_CHANGE);
		});
		langEng.addActionListener(e -> {
			if (this.settings.languageID == Language.ENG)
				return;

			this.settings.languageID = Language.ENG;
			this.settings.save(SETTINGS_FILE_NAME);
			langDe.setState(false);

			showInfoDialog(this.language.INFO_RESTART_TO_LOAD_CHANGE);
		});
		languageMenu.add(langDe);
		languageMenu.add(langEng);
		settingsMenu.add(languageMenu);
		settingsMenu.add(new JPopupMenu.Separator());

		//
		// reset
		//
		JMenuItem resetMenu = new JMenuItem(this.language.SETTINGS_RESET_MENU_NAME);
		resetMenu.addActionListener(e -> {
			this.settings = Settings.InitDefault();
			this.settings.save(SETTINGS_FILE_NAME);
			updateTabGUIs();

			showInfoDialog(this.language.INFO_RESTART_TO_LOAD_CHANGE);
		});
		settingsMenu.add(resetMenu);

		fileMenu.add(settingsMenu);

		return fileMenu;
	}
	private JMenu createTabUtilMenu() {
		this.guiTabUtilMenu = new JMenu(this.language.MENU_TAB_NAME);

		//
		// rename tab
		//
		JMenuItem renameItem = new JMenuItem(this.language.MENU_TAB_RENAME_TAB);
		renameItem.addActionListener(e -> {

			String newName = JOptionPane.showInputDialog(this.window, this.language.MENU_TAB_RENAME_DIALOG_MESSAGE);
			if (newName != null) {
				int tabIndex = this.guiDataTabs.getSelectedIndex();
				if (tabIndex >= this.dataTabs.size())
					return; // invalid tab index

				this.dataTabs.get(tabIndex).title = newName;
				this.guiDataTabs.setTitleAt(tabIndex, newName);

				if (!saveData(SAFE_FILE_NAME)) {
					showInfoDialog(this.language.ERROR_SAVE_TO_FILE_FAILED);
				}
			}

		});
		this.guiTabUtilMenu.add(renameItem);
		this.guiTabUtilMenu.addSeparator();

		//
		// move tab left
		//
		this.guiMenuMoveTabLeft = new JMenuItem(this.language.MENU_TAB_MOVE_LEFT_TAB);
		this.guiMenuMoveTabLeft.addActionListener(e -> {
			int tabIndex = this.guiDataTabs.getSelectedIndex();
			int newTabIndex = tabIndex - 1;

			changeTabIndex(tabIndex, newTabIndex);
		});
		this.guiTabUtilMenu.add(this.guiMenuMoveTabLeft);

		//
		// move right
		//
		this.guiMenuMoveTabRight = new JMenuItem(this.language.MENU_TAB_MOVE_RIGHT_TAB);
		this.guiMenuMoveTabRight.addActionListener(e -> {
			int tabIndex = this.guiDataTabs.getSelectedIndex();
			int newTabIndex = tabIndex + 1;

			changeTabIndex(tabIndex, newTabIndex);
		});
		this.guiTabUtilMenu.add(this.guiMenuMoveTabRight);
		this.guiTabUtilMenu.addSeparator();

		//
		// remove tab
		//
		JMenuItem removeTabMenu = new JMenuItem(this.language.MENU_TAB_REMOVE_TAB);
		removeTabMenu.addActionListener(e -> {
			int tabIndex = this.guiDataTabs.getSelectedIndex();
			int decision = JOptionPane.showConfirmDialog(this.window, this.language.MENU_TAB_REMOVE_CONFIRM_MESSAGE,"", JOptionPane.YES_NO_OPTION);

			if (decision == JOptionPane.OK_OPTION) {
				removeTab(tabIndex);
			}
		});
		this.guiTabUtilMenu.add(removeTabMenu);

		return this.guiTabUtilMenu;
	}
	private JMenu createExtrasMenu(){
		JMenu extrasMenu = new JMenu(this.language.EXTRAS_MENU_NAME);

		JMenuItem exportToTXTMenu = new JMenuItem(this.language.EXTRAS_EXPORT_TO_TXT_NAME);
		exportToTXTMenu.addActionListener(e -> {

			String input = askForPassword();
			if (comparePasswords(input, this.password, this.salt) && exportToTXT()) {
				showInfoDialog(this.language.EXTRAS_EXPORT_COMPLETE_INFO);
			} else {
				showInfoDialog(this.language.EXTRAS_EXPORT_FAILED_INFO);
			}

		});
		extrasMenu.add(exportToTXTMenu);
		JMenuItem importFromTextMenu = new JMenuItem(this.language.EXTRAS_IMPORT_FROM_TXT_NAME);
		importFromTextMenu.addActionListener(e -> {

			if (importFromTXT()) {
				showInfoDialog(this.language.EXTRAS_IMPORT_COMPLETE_INFO);
			} else {
				showInfoDialog(this.language.EXTRAS_IMPORT_FAILED_INFO);
			}
		});
		extrasMenu.add(importFromTextMenu);
		extrasMenu.add(new JPopupMenu.Separator());

		JMenuItem encryptFileMenu = new JMenuItem(this.language.MENU_EXTRAS_ENCRYPT_FILE_LABEL);
		encryptFileMenu.addActionListener(e -> {
			boolean result = encryptFile();
			if (result) {
				showInfoDialog(this.language.MENU_EXTRAS_ENCRYPT_SUCCESSFUL);
			} else {
				showInfoDialog(this.language.MENU_EXTRAS_ENCRYPT_FAILED);
			}
		});
		extrasMenu.add(encryptFileMenu);
		JMenuItem decryptFileMenu = new JMenuItem(this.language.MENU_EXTRAS_DECRYPT_FILE_LABEL);
		decryptFileMenu.addActionListener(e -> {
			boolean result = decryptFile();

			if (result) {
				showInfoDialog(this.language.MENU_EXTRAS_DECRYPT_SUCCESSFUL);
			} else {
				showInfoDialog(this.language.MENU_EXTRAS_DECRYPT_FAILED);
			}
		});
		extrasMenu.add(decryptFileMenu);

		return extrasMenu;
	}

	private void initBaseGUI() {
		//
		// Layout
		//
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.setLayout(new BorderLayout());

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
			// info
			JPanel guiDataInfoPanel = new JPanel(new GridBagLayout());
			guiDataInfoPanel.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createLineBorder(GUI_BORDER_COLOR),
					GUI_DEFAULT_GAP_BORDER));
			infoPanel.add(guiDataInfoPanel);

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
				guiDataInfoPanel.add(labels[infoPaneIndex], labelConstrains);

				// text
				this.guiDataInfoPanes[infoPaneIndex] = CreateSelectableText();
				this.guiDataInfoPanes[infoPaneIndex].setText("<html>-</html>");
				guiDataInfoPanel.add(this.guiDataInfoPanes[infoPaneIndex], paneConstrains);
			}

			//
			// Mode 1/2
			//
			JPanel selectModePanel = new JPanel();
			selectModePanel.setLayout(new GridLayout(3, 1, GUI_DEFAULT_GAP, GUI_DEFAULT_GAP));
			selectModePanel.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createLineBorder(GUI_BORDER_COLOR),
					GUI_DEFAULT_GAP_BORDER));

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

			initAddTabGUI();
			this.guiDataTabs.setSelectedIndex(0);
			this.guiDataTabs.addChangeListener(l -> {

				// ie. if the "+" tab is selected
				int selectedIndex = this.guiDataTabs.getSelectedIndex();
				if (selectedIndex == this.guiDataTabs.getTabCount() - 1) {
					this.guiAddDataMItem.setEnabled(false);
					this.guiTabUtilMenu.setEnabled(false);
				} else {
					this.guiAddDataMItem.setEnabled(true);
					this.guiTabUtilMenu.setEnabled(true);

					this.guiMenuMoveTabLeft.setEnabled(selectedIndex != 0);
					this.guiMenuMoveTabRight.setEnabled(selectedIndex != this.dataTabs.size() - 1);
				}
			});

			contentPanel.add(BorderLayout.CENTER, this.guiDataTabs);
			mainPanel.add(BorderLayout.CENTER, contentPanel);
		}

		//
		// Finish
		//
		this.window.add(mainPanel);
		this.window.setVisible(true);
	}
	private void initAddTabGUI() {
		// add tab tab
		JPanel addTabPanel = new JPanel();
		addTabPanel.setBorder(GUI_DEFAULT_GAP_BORDER);
		addTabPanel.setLayout(new GridBagLayout());

		// constrains
		GridBagConstraints endlConstrains = new GridBagConstraints();
		endlConstrains.insets    = new Insets(0, 0, GUI_DEFAULT_GAP, 0);
		endlConstrains.weightx   = 1.0;
		endlConstrains.weighty   = 1;
		endlConstrains.fill      = GridBagConstraints.HORIZONTAL;
		endlConstrains.gridwidth = GridBagConstraints.REMAINDER;
		GridBagConstraints labelConstrains = new GridBagConstraints();
		labelConstrains.insets    = new Insets(0, 0, GUI_DEFAULT_GAP, 0);
		labelConstrains.weightx   = 0.5;
		labelConstrains.weighty   = 1;
		labelConstrains.fill      = GridBagConstraints.HORIZONTAL;
		labelConstrains.gridwidth = 1;

		// info Text
		addTabPanel.add(new JLabel(this.language.ADD_TAB_INFO_LABEL), endlConstrains);

		// name filed
		addTabPanel.add(new JLabel(this.language.ADD_TAB_TAB_NAME_LABEL), labelConstrains);
		JTextField newTabName = new JTextField();
		addTabPanel.add(newTabName, endlConstrains);

		// index selector
		addTabPanel.add(new JLabel(this.language.ADD_TAB_INDEX_LABEL), labelConstrains);
		this.guiNewTabIndexSelector = new JSpinner(
				new SpinnerNumberModel(this.dataTabs.size(), 0, this.dataTabs.size(), 1));
		addTabPanel.add(this.guiNewTabIndexSelector, endlConstrains);

		//add button
		JButton addTabButton = new JButton(this.language.ADD_TAB_BUTTON_LABEL);
		addTabButton.addActionListener(e -> {
			String name = newTabName.getText();
			int index = (int) this.guiNewTabIndexSelector.getValue();

			createTab(name, index);

			newTabName.setText("");
			this.guiNewTabIndexSelector.setValue(this.dataTabs.size());
		});
		endlConstrains.gridwidth = 2;
		addTabPanel.add(addTabButton, endlConstrains);
		addTabPanel.setMaximumSize(new Dimension(250, 88 + 20));
		addTabPanel.setPreferredSize(new Dimension(250, 88 + 20));

		//tab panel to center addTabPanel
		JPanel tabPanel = new JPanel();
		tabPanel.setLayout(new BoxLayout(tabPanel, BoxLayout.Y_AXIS));
		tabPanel.add(addTabPanel);

		this.guiDataTabs.addTab("+", tabPanel);
	}
	private void initTabGUI() {

		int iteration = 0;
		for (DataTab tab : this.dataTabs) {

			JPanel panel = tab.initGUI();
			JScrollPane scrollPane = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

			this.guiDataTabs.insertTab(tab.title, null, scrollPane, null, iteration);

			iteration++;
		}

		this.guiDataTabs.setSelectedIndex(0);
	}
	private void updateTabGUIs() {
		for (DataTab tab : this.dataTabs) {
			tab.updateGUI();
		}
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
	private DataTab createTab(String name, int index) {
		if (isStringInvalid(name)) {
			showInfoDialog(this.language.ERROR_STRING_NOT_SUPPORTED);
			return null;
		}

		DataTab tab = new DataTab(name);
		this.dataTabs.add(index, tab);

		if (!saveData(SAFE_FILE_NAME)) {
			showInfoDialog(this.language.ERROR_SAVE_TO_FILE_FAILED);
			this.dataTabs.remove(index);
			return null;
		}

		//gui
		JPanel panel = tab.initGUI();
		JScrollPane scrollPane = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		this.guiDataTabs.insertTab(tab.title, null, scrollPane, null, index);
		this.guiDataTabs.setSelectedIndex(index);

		// update the addTab panel
		((SpinnerNumberModel)this.guiNewTabIndexSelector.getModel()).setMaximum(this.dataTabs.size()); // one line wonder or horror
		this.guiNewTabIndexSelector.setValue(this.dataTabs.size());

		return tab;
	}
	private void changeTabIndex(int index, int newIndex) {

		if (index < 0 || index >= this.dataTabs.size() ||
			newIndex < 0 || newIndex >= this.dataTabs.size())
			return; // invalid tab index

		DataTab tab = this.dataTabs.get(index);
		this.dataTabs.remove(index);
		this.dataTabs.add(newIndex, tab);

		Component tabGUI = this.guiDataTabs.getComponentAt(index);
		this.guiDataTabs.remove(index);
		this.guiDataTabs.add(tabGUI, newIndex);
		this.guiDataTabs.setTitleAt(newIndex, tab.title);
		this.guiDataTabs.setSelectedIndex(newIndex);

		if (!saveData(SAFE_FILE_NAME)) {
			showInfoDialog(this.language.ERROR_SAVE_TO_FILE_FAILED);
		}
	}
	private void removeTab(int index) {
		if (index < 0 || index >= this.dataTabs.size()) {
			showInfoDialog(this.language.ERROR_INVALID_TAB_INDEX);
			return;
		}

		this.dataTabs.remove(index);
		this.guiDataTabs.remove(index);

		if (!saveData(SAFE_FILE_NAME)) {
			showInfoDialog(this.language.ERROR_SAVE_TO_FILE_FAILED);
		}

		// update the addTab panel
		((SpinnerNumberModel)this.guiNewTabIndexSelector.getModel()).setMaximum(this.dataTabs.size()); // one line wonder or horror
		this.guiNewTabIndexSelector.setValue(this.dataTabs.size());
	}

	/* //////////////////////////////////////////////////////////////////////////////// */
	// // Cipher utility //
	/* //////////////////////////////////////////////////////////////////////////////// */
	private String askForPassword() {

		if (JUMP_ASK)
			return "HELLO";
		/*
		 * Configuration
		 */
		JDialog pwDialog = new JDialog();
		JPanel contentPanel = new JPanel();
		contentPanel.setBorder(GUI_DEFAULT_GAP_BORDER);
		contentPanel.setLayout(new GridLayout(3, 1, 5, 5));
		contentPanel.setFocusable(false);

		// text
		JLabel textLabel = new JLabel(this.language.ENTER_PASSWORD_INFO_LABEL);
		textLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		textLabel.setFocusable(false);
		contentPanel.add(textLabel);

		// password field
		JPasswordField passwordField = new JPasswordField();
		contentPanel.add(passwordField);

		// buttons
		JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 5, 5));

		JButton cancelButton = new JButton(this.language.ENTER_PASSWORD_CANCEL_BUTTON_LABEL);
		cancelButton.addActionListener(e ->{
			pwDialog.dispose();
		});
		buttonPanel.add(cancelButton);

		JButton submitButton = new JButton(this.language.ENTER_PASSWORD_OKAY_BUTTON_LABEL);
		pwDialog.getRootPane().setDefaultButton(submitButton); // make it the submit button
		submitButton.addActionListener(e -> {
			pwDialog.setVisible(false);
		});
		buttonPanel.add(submitButton);
		contentPanel.add(buttonPanel);

		/*
		 * show the input dialog
		 */
		// size & content
		pwDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		pwDialog.setContentPane(contentPanel);
		pwDialog.pack();
		pwDialog.setSize(GUI_ENTER_PASSWORD_GUI_WIDTH, pwDialog.getHeight());
		pwDialog.setResizable(false);
		pwDialog.setLocationRelativeTo(null);
		pwDialog.setTitle(TITLE);
		//focus
		pwDialog.setModal(true);
		pwDialog.setAutoRequestFocus(true);
		pwDialog.setVisible(true);

		// the okay button only hides the dialog. which also means that it is still is displayable at this point
		if (pwDialog.isDisplayable()) {
			pwDialog.dispose();

			char[] password = passwordField.getPassword();

			if (password.length == 0) {
				return null;
			}

			return new String(password);
		} else {
			return null;
		}
	}
	private void showChangePasswordDialog() {
		JDialog cpDialog = new JDialog();
		JPanel contentPanel = new JPanel(new GridBagLayout());
		contentPanel.setBorder(BorderFactory.createEmptyBorder(GUI_DEFAULT_GAP, GUI_DEFAULT_GAP, 0, GUI_DEFAULT_GAP));

		// constrains
		GridBagConstraints labelConstrains = new GridBagConstraints();
		labelConstrains.weighty = 1.0;
		labelConstrains.fill = GridBagConstraints.BOTH;
		labelConstrains.insets = new Insets(GUI_DEFAULT_GAP, 0, GUI_DEFAULT_GAP, GUI_DEFAULT_GAP);
		GridBagConstraints inputConstrains = new GridBagConstraints();
		inputConstrains.weighty = 1.0;
		inputConstrains.weightx = 1.0;
		inputConstrains.fill = GridBagConstraints.BOTH;
		inputConstrains.gridwidth = GridBagConstraints.REMAINDER;
		inputConstrains.insets = new Insets(GUI_DEFAULT_GAP, 0, GUI_DEFAULT_GAP, 0);

		// old password
		contentPanel.add(new JLabel(this.language.CHANGE_PASS_OLD_PASS_LABEL), labelConstrains);
		JPasswordField oldPassField = new JPasswordField();
		contentPanel.add(oldPassField, inputConstrains);

		// new password input
		contentPanel.add(new JLabel(this.language.CHANGE_PASS_NEW_PASS_LABEL), labelConstrains);
		JPasswordField newPassField = new JPasswordField();
		contentPanel.add(newPassField, inputConstrains);

		// new password again
		contentPanel.add(new JLabel(this.language.CHANGE_PASS_NEW_PASS_AGAIN_LABEL), labelConstrains);
		JPasswordField againNewPassField = new JPasswordField();
		contentPanel.add(againNewPassField, inputConstrains);

		// input button
		JPanel buttonPanel = new JPanel(new GridLayout(1, 2, GUI_DEFAULT_GAP, GUI_DEFAULT_GAP));
		JButton cancelButton = new JButton(this.language.CHANGE_PASS_CANCEL_BUTTON_LABEL);
		cancelButton.addActionListener(e -> {
			cpDialog.dispose();
		});
		buttonPanel.add(cancelButton);

		JButton changePWButton = new JButton(this.language.CHANGE_PASS_SAVE_BUTTON_LABEL);
		changePWButton.addActionListener(e -> {
			boolean isInputCorrect = true;

			// check old pass
			String oldPass = new String(oldPassField.getPassword());
			if (oldPass.isEmpty() ||
				!comparePasswords(this.password, oldPass, this.salt)) {
				isInputCorrect = false;
			}

			// check new pass
			String newPass1 = new String(newPassField.getPassword());
			String newPass2 = new String(againNewPassField.getPassword());
			byte[] newSalt = createRandomArray(CIPHER_SALT_SIZE);
			if (newPass1.isEmpty() || newPass2.isEmpty() ||
				!comparePasswords(newPass1, newPass2, newSalt)) {
				isInputCorrect = false;
			}

			// invalid input
			if (!isInputCorrect) {
				showInfoDialog(this.language.CHANGE_PASS_INVALID_INPUT_LABEL, cpDialog);
				return;
			}

			// just to be save
			createBackup();

			// save data with new password or reverse changes if something failed
			byte[] oldSalt    = this.salt;
			byte[] oldInitVec = this.cipherInitVector;
			this.password         = newPass1;
			this.salt             = newSalt;
			this.cipherInitVector = createRandomArray(CIPHER_INIT_VEC_SIZE);
			if (!saveData(SAFE_FILE_NAME)) {
				this.password         = oldPass;
				this.salt             = oldSalt;
				this.cipherInitVector = oldInitVec;
				showInfoDialog(this.language.CHANGE_PASS_CHANGE_PASS_FAILED, cpDialog);
				return;
			}

			// dispose is everything worked
			showInfoDialog(this.language.CHANGE_PASS_ALL_GOOD, cpDialog);
			cpDialog.dispose();
		});
		cpDialog.getRootPane().setDefaultButton(changePWButton);
		buttonPanel.add(changePWButton);

		contentPanel.add(buttonPanel, inputConstrains);

		// dialog
		cpDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		cpDialog.setContentPane(contentPanel);
		cpDialog.pack();
		cpDialog.setSize(GUI_ENTER_PASSWORD_GUI_WIDTH, cpDialog.getHeight());
		cpDialog.setResizable(false);
		cpDialog.setLocationRelativeTo(null);
		cpDialog.setTitle(TITLE);
		cpDialog.setModal(true);
		cpDialog.setAutoRequestFocus(true);
		cpDialog.setVisible(true);
	}
	private boolean isStringInvalid(String testString) {
		if (testString.isEmpty())
			return true;

		if (testString.contains(FORMAT_TAB_SEPARATOR + ""))
			return true;

		if (testString.contains(FORMAT_DATA_SEPARATOR + ""))
			return true;

		if (testString.contains(FORMAT_UNIT_SEPARATOR + ""))
			return true;

		if (testString.contains(FORMAT_PADDING_START + ""))
			return true;

		if (testString.contains("<html>") || testString.contains("</html>"))
			return true;

		return false;
	}

	private String createRandomString(int length) {
		if (length <= 0)
			return "";

		byte[] stringBytes = new byte[length];
		SecureRandom rand = new SecureRandom();

		for (int index = 0; index < length; index++) {
			stringBytes[index] = (byte) (rand.nextInt(127 - 33) + 33);
		}

		return new String(stringBytes);
	}
	private String getPaddedString(String srcString) {
		if (srcString.isEmpty())
			return null;

		int baseLength = srcString.length();
		if (baseLength % CIPHER_CONTENT_BLOCK_SIZE == CIPHER_CONTENT_BLOCK_SIZE - 1)
			return srcString + FORMAT_PADDING_START;

		/*
		* Start the construction
		* */
		int paddingSize = CIPHER_CONTENT_BLOCK_SIZE - ((baseLength + 1 /* for the \0 */) % CIPHER_CONTENT_BLOCK_SIZE);
		return srcString + FORMAT_PADDING_START + createRandomString(paddingSize);
	}
	private String getUnpaddedString(String srcString) {
		if (srcString.isEmpty())
			return null;

		int strEnd = srcString.indexOf(FORMAT_PADDING_START);
		if (strEnd == -1)
			return null;

		return srcString.substring(0, strEnd);
	}
	private byte[] createRandomArray(int size) {
		byte[] bytes = new byte[size];

		new SecureRandom().nextBytes(bytes);

		return bytes;
	}
	private Cipher createCipher(String algorithm, int cipherMode, byte[] keyBytes, byte[] initVector) {
		try {
			Cipher cipher = Cipher.getInstance(algorithm);
			cipher.init(cipherMode, new SecretKeySpec(keyBytes, "AES"), new IvParameterSpec(initVector));

			return cipher;

		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
				InvalidAlgorithmParameterException e) {
			showInfoDialog(this.language.ERROR_GENERAL_ERROR_INFO, e);
		}

		return null;
	}
	private byte[] hashPassword(String password, byte[] salt) {

		PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, CIPHER_PWHASH_ITERATIONS, CIPHER_PWHASH_SIZE * 8);

		try {
			SecretKeyFactory skf = SecretKeyFactory.getInstance(CIPHER_PWHASH_ALGORITHM);
			return skf.generateSecret(spec).getEncoded();
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			showInfoDialog(this.language.ERROR_GENERAL_ERROR_INFO, e);
		}

		return null;
	}

	/* //////////////////////////////////////////////////////////////////////////////// */
	// // Utility //
	/* //////////////////////////////////////////////////////////////////////////////// */
	private static String GetTimeStamp() {
		return new SimpleDateFormat(TIME_STAMP_FORMAT).format(new Date());
	}
	private void showInfoDialog(String infoString, Component parent, Exception err) {
		Object[] options;
		String title;
		if (err != null) {
			options = new Object[]{this.language.ERROR_DIALOG_COPY_ERROR, "OK"};
			title = TITLE + "(" + err.toString() + ")";
		} else {
			options = new Object[]{"OK"};
			title = TITLE;
		}

		int result = JOptionPane.showOptionDialog(parent,
				infoString, title,
				JOptionPane.DEFAULT_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null,
				options,
				options[0]);

		if (err != null && result == 0) {
			StringWriter writer = new StringWriter();
			PrintWriter printWriter= new PrintWriter(writer);
			err.printStackTrace(printWriter);

			String errString = writer.toString();

			int lastXFTracePos = errString.lastIndexOf("xfrednet.xfutils");
			String trimmedStack = errString.substring(0, errString.indexOf(")", lastXFTracePos) + 1);

			CopyToClipboard(trimmedStack);
		}
	}
	private void showInfoDialog(String infoString, Exception e) {
		showInfoDialog(infoString, this.window, e);
	}
	private void showInfoDialog(String infoString, Component parent) {
		showInfoDialog(infoString, parent, null);
	}
	private void showInfoDialog(String infoString) {
		showInfoDialog(infoString, this.window);
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
	private boolean comparePasswords(String password1, String password2, byte[] salt) {

		// Why do I hash the passwords, well just to be more correct and not have
		// different timings if the password have different lengths. Is this necessary
		// absolutely not because the old actual password is stored in ram and the password
		// was entered before so a keylogger would be a way better attack.

		// hash
		byte[] pass1Hash = hashPassword(password1, salt);
		byte[] pass2Hash = hashPassword(password2, salt);
		if (pass1Hash == null || pass2Hash == null)
			return false; // well nothing to compare

		// compare hashes
		byte diff = 0x00;
		for (int index = 0; index < pass1Hash.length; index++) {
			diff |= pass1Hash[index] ^ pass2Hash[index];
		}

		// evaluate the result
		return diff == 0;
	}

	private void reinitCipherValues() {
		this.salt = createRandomArray(CIPHER_SALT_SIZE);
		this.cipherInitVector = createRandomArray(CIPHER_INIT_VEC_SIZE);
	}
	private boolean saveData(String fileName) {
		File saveFile = new File(fileName);

		// create the containing directory
		if (fileName.contains("\\")) {
			String pathStr = fileName.substring(0, fileName.lastIndexOf("\\"));
			File path = new File(pathStr);
			//noinspection ResultOfMethodCallIgnored
			path.mkdirs();
		}

		if (this.salt.length > Byte.MAX_VALUE || this.cipherInitVector.length > Byte.MAX_VALUE)
			return false; // salt or init vector to long

		String saveString = getDataTabGroupSaveString(this.dataTabs);
		Cipher cipher = createCipher(CIPHER_DATA_ALGORITHM, Cipher.ENCRYPT_MODE,
				hashPassword(this.password, this.salt), this.cipherInitVector);
		if (cipher == null)
			return false;

		byte[] encryptedData;
		try {
			encryptedData = cipher.doFinal(saveString.getBytes());
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			showInfoDialog(this.language.ERROR_GENERAL_ERROR_INFO, e);
			return false; // no data to save
		}

		/*
		 * Save the salt
		 */
		try {
			FileOutputStream fileStream = new FileOutputStream(saveFile);

			//salt
			fileStream.write((byte)this.salt.length);
			fileStream.write(this.salt);

			// init vector
			fileStream.write((byte)this.cipherInitVector.length);
			fileStream.write(this.cipherInitVector);

			fileStream.write(encryptedData);

			fileStream.close();

			return true;
		} catch (IOException e) {
			showInfoDialog(this.language.ERROR_GENERAL_ERROR_INFO, e);
		}

		return false;
	}
	private boolean loadData(String fileName) {
		File file = new File(fileName);

		if (!file.exists()) {
			return false; // no file => no data
		}

		try {
			FileInputStream fileStream = new FileInputStream(file);

			//noinspection LoopStatementThatDoesntLoop,ConstantConditions
			do {

				//
				// load salt
				//
				byte[] saltSize = new byte[1];
				if (fileStream.read(saltSize) != 1) {
					break;
				}
				if (saltSize[0] <= 0) {
					break; // try to salt something with antimatter, does it work, NO!!
				}
				this.salt = new byte[saltSize[0]];
				if (fileStream.read(this.salt) != this.salt.length) {
					break; // salt reading failed
				}

				//
				// load cipher init vector
				//
				byte[] ivSize = new byte[1];
				if (fileStream.read(ivSize) != 1) {
					break;
				}
				if (ivSize[0] <= 0) {
					break; // invalid size
				}
				this.cipherInitVector = new byte[ivSize[0]];
				if (fileStream.read(this.cipherInitVector) != this.cipherInitVector.length) {
					break; // vector reading failed
				}

				//
				// Load data
				//
				long fileDataSize = file.length() - 1 - this.salt.length - 1 - this.cipherInitVector.length;
				byte[] encryptedDataBuffer = new byte[(int)fileDataSize];
				if (fileStream.read(encryptedDataBuffer) != encryptedDataBuffer.length) {
					break; // read data failed
				}
				fileStream.close();

				//
				// decrypt
				//
				Cipher cipher = createCipher(CIPHER_DATA_ALGORITHM,
						Cipher.DECRYPT_MODE, hashPassword(this.password, this.salt), this.cipherInitVector);
				if (cipher == null) {
					break;
				}
				String saveString;
				try {
					saveString = new String(cipher.doFinal(encryptedDataBuffer));
				} catch (IllegalBlockSizeException e) {
					showInfoDialog(this.language.ERROR_GENERAL_ERROR_INFO, e);
					break;
				} catch (BadPaddingException e) {
					showInfoDialog(this.language.ERROR_WRONG_PASSWORD);
					break;
				}

				//
				// load save String
				//
				ArrayList<DataTab> loadedTabs = loadDataTabGroupFromSaveString(saveString);
				if (loadedTabs == null) {
					break;
				}
				this.dataTabs = loadedTabs;

				return true;
			} while (false);
			fileStream.close();
			return false;
		} catch (IOException e) {
			showInfoDialog(this.language.ERROR_GENERAL_ERROR_INFO, e);
		}

		return false;
	}
	private boolean createBackup() {

		String fileName = String.format(BACKUP_FILE_NAME, GetTimeStamp());
		if (saveData(fileName)) {
			System.out.println("Made a backup, backup name: " + fileName);
			this.settings.lastBackup = new Date().getTime();
			this.settings.save(SETTINGS_FILE_NAME);

			return true;
		} else {
			showInfoDialog(this.language.ERROR_SAVE_BACKUP_FAILED);
			return false;
		}

	}
	private String getDataTabGroupSaveString(ArrayList<DataTab> tabs) {
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
	private ArrayList<DataTab> loadDataTabGroupFromSaveString(String saveString) {
		if (saveString == null || saveString.isEmpty()) {
			return null;
		}

		String[] components = saveString.split(FORMAT_TAB_SEPARATOR + "");

		if (components.length == 0) {
			return null; // well there is no contentList to load
		}

		ArrayList<DataTab> tabs = new ArrayList<DataTab>();
		for (String component : components) {
			DataTab tab = new DataTab("Loading");

			if (!tab.loadSaveString(component)) {
				return null; // the tab failed to load
			}

			tabs.add(tab);
		}

		return tabs;
	}

	/* //////////////////////////////////////////////////////////////////////////////// */
	// // Extras //
	/* //////////////////////////////////////////////////////////////////////////////// */
	private boolean exportToTXT() {

		// #################
		// # select file #
		// #################
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle(this.language.EXTRAS_EXPORT_TO_TXT_NAME);
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int result = chooser.showSaveDialog(this.window);
		if (result != JFileChooser.APPROVE_OPTION) {
			return false; // well nothing really failed
		}
		File outputFile = new File(chooser.getSelectedFile().getAbsolutePath() + "\\" + TXT_EXPORT_FILE_NAME);
		System.out.println("exportToTXT: The output file will be: " + outputFile.getAbsolutePath());

		// #################
		// # write to file #
		// #################
		try {
			PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(outputFile), StandardCharsets.UTF_8));

			// write tab
			for (DataTab tab : this.dataTabs) {
				writer.println("[" + tab.title + "]");

				// write data
				for (Data data : tab.dataList) {
					writer.print(data.title + ": ");

					int contentIndex = 0;
					for (String content : data.contentList) {
						writer.print(content);

						if (contentIndex != data.contentList.size() - 1) {
							writer.print((char)FORMAT_UNIT_SEPARATOR);
						}
						contentIndex++;
					}

					writer.println();
				}
			}

			writer.flush();
			writer.close();

			return true;
		} catch (IOException e) {
			showInfoDialog(this.language.ERROR_GENERAL_ERROR_INFO, e);
		}

		return false;
	}
	private boolean importFromTXT() {
		// #################
		// # select file #
		// #################
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle(this.language.EXTRAS_IMPORT_FROM_TXT_NAME);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setFileFilter(new FileNameExtensionFilter("Text files", "txt"));
		int result = chooser.showSaveDialog(this.window);
		if (result != JFileChooser.APPROVE_OPTION) {
			return false; // well nothing really failed
		}
		File inputFile = new File(chooser.getSelectedFile().getAbsolutePath());
		System.out.println("importFromTXT: The input file is: " + inputFile.getAbsolutePath());

		// #################
		// # Create Backup #
		// #################
		createBackup();

		// #############
		// # Read file #
		// #############
		try {
			Scanner input = new Scanner(inputFile);

			DataTab tab = null;
			while (input.hasNextLine()) {
				String line = input.nextLine().trim(); // trim remove spaces and tabs on start and end

				if (line.isEmpty()) {
					continue;
				}

				// =============
				// Tab name
				// =============
				if (line.startsWith("[")) {
					int endIndex = line.lastIndexOf("]");
					if (endIndex == -1) {
						continue; // no tab name end => next line
					}

					String name = line.substring(1, endIndex);
					if (isStringInvalid(name)) {
						continue;
					}
					// test if there is already a tab with this name and if so use that tab
					tab = null;
					for (DataTab testTab : this.dataTabs) {
						if (testTab.title.equals(name)) {
							tab = testTab;
							break;
						}
					}
					// if tab was not found
					if (tab == null) {
						tab = createTab(name, this.dataTabs.size());
						if (tab == null) {
							input.close();
							return false;
						}
					}
					continue; // next line
				}

				// ============
				// data
				// ============
				// The data has to have a Tab that it gets saved to so I create a tab called "imported"
				int nameEnd = line.indexOf(": ");
				if (nameEnd == -1)  {
					// ie not found
					continue;
				}
				if (tab == null) {
					tab = createTab(this.language.EXTRAS_IMPORT_DEFAULT_TAB_NAME, this.dataTabs.size());
					if (tab == null) {
						input.close();
						return false;
					}
				}

				// get name and validate it
				String dataName = line.substring(0, nameEnd);
				if (isStringInvalid(dataName)) {
					continue;
				}
				Data data = new Data(dataName, tab);

				// read components
				String[] components = line.substring(nameEnd + 2).split(FORMAT_UNIT_SEPARATOR + "");
				for (String component : components) {
					if (isStringInvalid(component)) {
						continue;
					}

					data.contentList.add(component);
				}

				// add if valid
				if (data.contentList.size() != 0) {
					data.createGUI();
					tab.add(data);
				}
			}
			updateTabGUIs();

			input.close();

			if (!saveData(SAFE_FILE_NAME)) {
				return false;
			}

			return true;
		} catch (FileNotFoundException e) {
			showInfoDialog(this.language.ERROR_GENERAL_ERROR_INFO, e);
		}

		return false;
	}
	private boolean cipherFile(Cipher cipher, OutputStream writer, InputStream reader) {
		try {

			/*
			* Validation
			* */
			if (cipher == null || reader == null || writer == null) {
				return false;
			}

			/*
			* ciphering
			* */
			byte[] buffer = new byte[CIPHER_FILE_BUFFER_SIZE];
			while (true) {

				// read
				for (int index = 0; index < CIPHER_FILE_BUFFER_SIZE; index++) {
					buffer[index] = 0;
				}
				int bytesRead = reader.read(buffer);
				if (bytesRead < 0) {
					break;
				}

				// cipher
				writer.write(cipher.update(buffer, 0, bytesRead));
			}

			writer.write(cipher.doFinal());

			return true;
		} catch (IOException | IllegalBlockSizeException e) {
			showInfoDialog(this.language.ERROR_GENERAL_ERROR_INFO, e);
		} catch (BadPaddingException e) {
			showInfoDialog(this.language.ERROR_WRONG_PASSWORD);
		}
		return false;
	}
	private boolean encryptFile() {

		//
		// selected file
		//
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File("E:\\Temp\\Temp"));
		fileChooser.setDialogTitle(this.language.MENU_EXTRAS_ENCRYPT_FILE_DIALOG);
		int fileSelectorRes = fileChooser.showOpenDialog(this.window);
		if (fileSelectorRes != JFileChooser.APPROVE_OPTION)
			return false;

		File fileIn = fileChooser.getSelectedFile();
		File fileOut = new File(fileIn.getPath() + CRYPT_FILE_EXTENSION);
		System.out.println("encryptFile: The following file was selected: " + fileIn.getAbsolutePath());

		//
		// get password
		//
		String pass = askForPassword();
		if (pass == null || pass.isEmpty())
			return false;
		byte[] salt = createRandomArray(CIPHER_SALT_SIZE);
		byte[] initVec = createRandomArray(CIPHER_INIT_VEC_SIZE);
		byte[] passHash = hashPassword(pass, salt);

		if (salt.length > Byte.MAX_VALUE || initVec.length > Byte.MAX_VALUE)
			return false; // salt or init vector to long

		//
		// encrypt
		//
		Cipher cipher = createCipher(CIPHER_FILE_ALGORITHM, Cipher.ENCRYPT_MODE, passHash, initVec);
		if (cipher == null)
			return false;

		try {
			OutputStream writer = new FileOutputStream(fileOut);
			InputStream reader  = new FileInputStream(fileIn);

			//salt
			writer.write((byte)salt.length);
			writer.write(salt);

			// init vector
			writer.write((byte)initVec.length);
			writer.write(initVec);

			boolean result = true;
			if (!cipherFile(cipher, writer, reader)) {
				result = false;
			}

			writer.flush();
			writer.close();
			reader.close();

			return result;
		} catch (IOException e) {
			showInfoDialog(this.language.ERROR_GENERAL_ERROR_INFO, e);

			return false;
		}

	}
	private boolean decryptFile() {

		//
		// selected file
		//
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File("E:\\Temp\\Temp"));
		fileChooser.setFileFilter(new FileNameExtensionFilter(this.language.FILE_CHOOSER_XFCRYPT_FILE_DESC, CRYPT_FILE_EXTENSION.substring(1)));
		fileChooser.setDialogTitle(this.language.MENU_EXTRAS_ENCRYPT_FILE_DIALOG);
		int fileSelectorRes = fileChooser.showOpenDialog(this.window);
		if (fileSelectorRes != JFileChooser.APPROVE_OPTION)
			return false;

		//
		// output file
		//
		File fileIn = fileChooser.getSelectedFile();
		if (!fileIn.getName().endsWith(CRYPT_FILE_EXTENSION))
			return false; // wrong file format
		String fileInPath = fileIn.getPath();
		File fileOut = new File(fileInPath.substring(0, fileInPath.lastIndexOf(CRYPT_FILE_EXTENSION)));
		System.out.println("decryptFile: The following file was selected: " + fileIn.getAbsolutePath());

		//
		// ask for password
		//
		String pass = askForPassword();
		if (pass == null || pass.isEmpty())
			return false;

		//
		// read file
		//
		try {
			boolean result = false;
			InputStream reader = new FileInputStream(fileIn);
			OutputStream writer = new FileOutputStream(fileOut);
			//noinspection ConstantConditions
			do {

				// salt
				int saltSize = reader.read();
				if (saltSize < 0)
					break;
				byte[] salt = new byte[saltSize];
				if (reader.read(salt) != saltSize) {
					break;
				}

				// init vec
				int initVecSize = reader.read();
				if (initVecSize < 0)
					break;
				byte[] initVec = new byte[initVecSize];
				if (reader.read(initVec) != initVecSize) {
					break;
				}

				// create cipher
				byte[] passHash = hashPassword(pass, salt);
				Cipher cipher = createCipher(CIPHER_FILE_ALGORITHM, Cipher.DECRYPT_MODE, passHash, initVec);
				if (!cipherFile(cipher, writer, reader)){
					break;
				}

				result = true;
			} while (false);

			writer.flush();
			writer.close();
			reader.close();

			return result;

		} catch (IOException e) {
			showInfoDialog(this.language.ERROR_GENERAL_ERROR_INFO, e);
		}

		return false;
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
		void createData() {
			Data data = new Data(PasswordManagerApp.this.language.NEW_DATA_DEFAULT_TITLE, this);
			data.openChangeDataGUI(true);

			if (data.contentList.size() == 0) {
				return; // the data is invalid
			}

			this.dataList.add(data);

			if (!saveData(SAFE_FILE_NAME)) {
				showInfoDialog(PasswordManagerApp.this.language.ERROR_SAVE_TO_FILE_FAILED);
				this.dataList.remove(data);
				return; // saving failed
			}
			data.createGUI();
			updateGUI();
		}
		JPanel initGUI() {

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
			updateGUI();

			return this.guiPanel;
		}
		void updateGUI() {

			int buttonsPerRow = PasswordManagerApp.this.settings.buttonsPerRow;
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
			// keep the layout if there are less data than buttonsPerRow
			for (; iterations < placements.length; iterations++) {
				this.guiPanel.add(new JPanel(), placements[iterations % placements.length]);
			}

			// The buttons have a padding on the right and bottom
			// this adds a padding to the top and left
			this.guiPanel.setBorder(BorderFactory.createEmptyBorder(GUI_DEFAULT_GAP, GUI_DEFAULT_GAP, 0, 0));

			this.guiPanel.updateUI();
		}

		/* ********************************************************* */
		// * saving and loading *
		/* ********************************************************* */
		// FORMAT_DATA_SEPARATOR => DS
		// [title] ([DS] [contentList.getSaveString()]) x contentList.size()
		String getSaveString() {
			StringBuilder sb = new StringBuilder();

			//title
			sb.append(getPaddedString(this.title));

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
			this.title = getUnpaddedString(components[0]);

			// load contentList
			for (int dataNo = 1; dataNo < components.length; dataNo++) {
				Data data = new Data(this);

				if (!data.loadSaveString(components[dataNo])) {
					return false;// loading the contentList has failed. well done me!
				}

				this.add(data);
			}

			// well done
			return true;
		}

		void killData(Data data) {

			int index = this.dataList.indexOf(data);
			if (index == -1) {
				return; // the data was not found in this tab
			}
			this.dataList.remove(index);

			if (!saveData(SAFE_FILE_NAME)) {
				this.dataList.add(index, data); // readd it since it wasn't really killed
				showInfoDialog(PasswordManagerApp.this.language.ERROR_SAVE_TO_FILE_FAILED);
			} else {
				updateGUI();
			}
		}
	}
	public class Data {

		String title;
		ArrayList<String> contentList;
		int copyDataIndex;

		DataTab parent;

		JButton guiButton;

		private Data(DataTab parent) {
			this.parent = parent;
			this.contentList = new ArrayList<String>();
		}
		Data(String title, DataTab parent) {
			this(parent);
			this.title = title;
			this.copyDataIndex = 0;
		}

		void createGUI() {

			this.guiButton = new JButton("<html>" + this.title + "</html>"); // this text is also updated by the change gui save button
			this.guiButton.setPreferredSize(new Dimension(0, DataTab.GUI_ROW_HEIGHT));

			this.guiButton.addActionListener(e -> {

				switch (PasswordManagerApp.this.currentMode) {
					case RETRIEVE_MODE:
						// copy to clipboard if shift is pressed
						if ((e.getModifiers() & InputEvent.SHIFT_MASK) != 0) {

							if (this.copyDataIndex >= this.contentList.size()) {
								this.copyDataIndex = 0;
							}
							String data = this.contentList.get(this.copyDataIndex);
							CopyToClipboard(data);

							PasswordManagerApp.this.window.dispose();

						} else {
							writeToInfoLabel();
						}
						break;
					case CHANGE_MODE:
						openChangeDataGUI(false);
						break;
					case DELETE_MODE:
						parent.killData(this);
						break;
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
		void openChangeDataGUI(boolean newlyCreated) {
			final int GUI_EDIT_DATA_COMPONENT_INDEX = 3;
			Language language = PasswordManagerApp.this.language;

			//
			// Create dialog
			//
			JDialog dialog = new JDialog(PasswordManagerApp.this.window);
			dialog.setAutoRequestFocus(true);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setResizable(false);

			//
			// Panel & Layout
			//
			GridLayout mainLayout = new GridLayout(1 + this.contentList.size() + 1, 1);
			JPanel mainPanel = new JPanel(mainLayout);
			ButtonGroup radioGroup = new ButtonGroup();

			//
			// Add title
			//
			JPanel titlePanel = createChangeDataGUITitleRow();
			JTextPane titleField = (JTextPane) titlePanel.getComponent(2);
			mainPanel.add(titlePanel);
			if (newlyCreated) {
				((JButton)titlePanel.getComponent(GUI_EDIT_DATA_COMPONENT_INDEX)).doClick();
			}

			//
			// Add content rows
			//
			ArrayList<JRadioButton> radioList = new ArrayList<JRadioButton>();
			ArrayList<JTextPane> dataFields = new ArrayList<JTextPane>();
			for (int contentIndex = 0; contentIndex < this.contentList.size(); contentIndex++) {
				mainPanel.add(createChangeDataGUIDataRow(contentIndex, radioGroup, radioList, dataFields));
			}

			//
			// action button row
			//
			JPanel actionRow = new JPanel(new GridLayout(1, 3, GUI_DEFAULT_GAP, GUI_DEFAULT_GAP));
			actionRow.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createLineBorder(GUI_BORDER_COLOR),
					GUI_DEFAULT_GAP_BORDER));

			JButton addDataButton = new JButton(language.CHANGE_DATA_ADD_BUTTON_LABEL);
			addDataButton.addActionListener(e -> {
				int index = dataFields.size();

				// add panel
				mainLayout.setRows(mainLayout.getRows() + 1);
				JPanel dataRowPanel = createChangeDataGUIDataRow(index, radioGroup, radioList, dataFields);
				mainPanel.add(dataRowPanel, index + 1);
				((JButton)dataRowPanel.getComponent(GUI_EDIT_DATA_COMPONENT_INDEX)).doClick();

				mainPanel.updateUI();

				// resize the dialog
				dialog.setSize(dialog.getWidth(), dialog.getHeight() + titlePanel.getHeight());
			});
			actionRow.add(addDataButton);
			if (newlyCreated) {
				addDataButton.doClick();
			}

			JButton saveDataButton = new JButton(language.CHANGE_DATA_SAVE_BUTTON_LABEL);
			saveDataButton.addActionListener(e -> {

				//
				// Validation
				//
				if (isStringInvalid(titleField.getText())) {
					showInfoDialog(language.ERROR_STRING_NOT_SUPPORTED, dialog);
					return; // couldn't save give the user the option to change the input
				}
				int dataCount = 0;
				for (JTextPane dataField : dataFields) {

					// test if this data was deleted
					if (dataField.getBackground() == GUI_DELETE_DATA_BACKGROUND) {
						continue; // jup deleted
					}

					if (isStringInvalid(dataField.getText())) {
						showInfoDialog(language.ERROR_STRING_NOT_SUPPORTED, dialog);
						return; // couldn't save give the user the option to change the input
					}
					dataCount++;
				}
				if (dataCount == 0) {
					showInfoDialog(language.ERROR_DATA_0_ENTRIES, dialog);
					return; // couldn't save give the user the option to change the input
				}

				//
				// Save to this class
				//
				this.title = titleField.getText();
				this.contentList.clear();
				this.copyDataIndex = 0;
				int rowIndex = -1; // the index of the current loop
				int dataIndex = 0;
				for (JTextPane dataField : dataFields) {

					rowIndex++;

					// test if this data was deleted
					if (dataField.getBackground() == GUI_DELETE_DATA_BACKGROUND) {
						continue; // jup deleted
					}

					this.contentList.add(dataField.getText());

					if (radioList.get(rowIndex).isSelected())
						this.copyDataIndex = dataIndex;

					dataIndex++;
				}

				if (this.guiButton != null)
					this.guiButton.setText("<html>" + this.title + "</html>");

				//
				// Save to file
				//
				if (!newlyCreated){
					if (!saveData(SAFE_FILE_NAME)) {
						showInfoDialog(PasswordManagerApp.this.language.ERROR_SAVE_TO_FILE_FAILED);
						return;
					}
				}

				//
				// Finish
				//
				dialog.dispose();
			});
			actionRow.add(saveDataButton);

			JButton cancelEditButton = new JButton(language.CHANGE_DATA_CANCEL_BUTTON_LABEL);
			cancelEditButton.addActionListener(e -> {
				dialog.dispose();
			});
			actionRow.add(cancelEditButton);

			mainPanel.add(actionRow);

			//
			// Show dialog
			//
			dialog.setContentPane(mainPanel);
			dialog.pack();
			dialog.setSize(GUI_CHANGE_DATA_GUI_DEFAULT_WIDTH, dialog.getHeight());
			dialog.setLocationRelativeTo(PasswordManagerApp.this.window);
			dialog.setModal(true); // makes it a real pop up that pauses the program
			dialog.setVisible(true);
		}
		private JPanel createChangeDataGUITitleRow() {
			JPanel titlePanel = new JPanel(new GridBagLayout());
			titlePanel.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createLineBorder(GUI_BORDER_COLOR, 1),
					BorderFactory.createEmptyBorder(GUI_DEFAULT_GAP / 2, GUI_DEFAULT_GAP, GUI_DEFAULT_GAP / 2, GUI_DEFAULT_GAP)));

			//
			// Radio button fill panel
			//
			GridBagConstraints fillPanelConstraints1 = new GridBagConstraints();
			fillPanelConstraints1.fill           = GridBagConstraints.HORIZONTAL;
			fillPanelConstraints1.insets         = new Insets(0, 0, 0, GUI_DEFAULT_GAP);

			JPanel fillPanel1 = new JPanel();
			fillPanel1.setPreferredSize(GUI_ICON_BUTTON_DIMENSIONS);
			titlePanel.add(fillPanel1, fillPanelConstraints1); // no radio button

			//
			// titleLabel
			//
			GridBagConstraints labelConstrains = new GridBagConstraints();
			labelConstrains.fill           = GridBagConstraints.HORIZONTAL;
			labelConstrains.insets         = new Insets(0, 0, 0, GUI_DEFAULT_GAP);
			titlePanel.add(new JLabel(language.INFO_PANEL_TITLE_LABEL), labelConstrains);

			// titleField
			GridBagConstraints dataConstrains = new GridBagConstraints();
			dataConstrains.fill            = GridBagConstraints.HORIZONTAL;
			dataConstrains.weightx         = 1.0;
			dataConstrains.insets          = new Insets(0, 0, 0, GUI_DEFAULT_GAP);

			JTextPane titleField = CreateSelectableText();
			titleField.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
			titleField.setName("titleField");
			titleField.setContentType("text/plain");
			titleField.setText(this.title);
			titlePanel.add(titleField, dataConstrains);

			// editTitleButton
			GridBagConstraints editIconConstrains = new GridBagConstraints();
			editIconConstrains.fill        = GridBagConstraints.HORIZONTAL;
			editIconConstrains.insets      = new Insets(0, 0, 0, GUI_DEFAULT_GAP);

			JButton editTitleButton = new JButton(GUI_EDIT_ICON);
			editTitleButton.setPreferredSize(GUI_ICON_BUTTON_DIMENSIONS);
			editTitleButton.setToolTipText(language.CHANGE_DATA_EDIT_BUTTON_TOOLTIP);
			editTitleButton.addActionListener(e -> {
				if (editTitleButton.getIcon().equals(GUI_EDIT_ICON)) {

					//
					// make the data field eatable(no this is not a typo)
					//
					titleField.setEditable(true);
					titleField.setBorder(BorderFactory.createLineBorder(GUI_BORDER_COLOR));
					titleField.setCaretPosition(titleField.getText().length());
					titleField.requestFocus();

					editTitleButton.setIcon(GUI_CHECK_ICON);
				} else {

					//
					// make uneatable(again not a typo) nad remove visual border
					//
					titleField.setEditable(false);
					titleField.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));

					editTitleButton.setIcon(GUI_EDIT_ICON);
				}
			});
			titlePanel.add(editTitleButton, editIconConstrains);

			//
			// copyTitleButton
			//
			GridBagConstraints copyIconConstrains = new GridBagConstraints();
			copyIconConstrains.fill        = GridBagConstraints.HORIZONTAL;
			copyIconConstrains.insets      = new Insets(0, 0, 0, GUI_DEFAULT_GAP);

			JButton copyTitleButton = new JButton(GUI_COPY_ICON);
			copyTitleButton.setToolTipText(language.CHANGE_DATA_COPY_BUTTON_TOOLTIP);
			copyTitleButton.setPreferredSize(GUI_ICON_BUTTON_DIMENSIONS);
			copyTitleButton.addActionListener(e -> {
				CopyToClipboard(this.title);
			});
			titlePanel.add(copyTitleButton, copyIconConstrains);

			//
			// Delete icon fill panel
			//
			GridBagConstraints fillPanelConstraints2 = new GridBagConstraints();
			fillPanelConstraints2.fill      = GridBagConstraints.HORIZONTAL;
			fillPanelConstraints2.gridwidth = GridBagConstraints.REMAINDER; // end row
			fillPanelConstraints2.insets    = new Insets(0, 0, 0, 0); // no insets, done by the panel

			JPanel fillPanel2 = new JPanel();
			fillPanel2.setPreferredSize(GUI_ICON_BUTTON_DIMENSIONS);
			titlePanel.add(fillPanel2, fillPanelConstraints2); // no delete Icon for the title

			return titlePanel;
		}
		private JPanel createChangeDataGUIDataRow(int index, ButtonGroup radioGroup, ArrayList<JRadioButton> radioList, ArrayList<JTextPane> dataFields) {
			//
			// Create Panel
			//
			JPanel titlePanel = new JPanel(new GridBagLayout());
			titlePanel.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createLineBorder(GUI_BORDER_COLOR, 1),
					BorderFactory.createEmptyBorder(GUI_DEFAULT_GAP / 2, GUI_DEFAULT_GAP, GUI_DEFAULT_GAP / 2, GUI_DEFAULT_GAP)));

			//
			// Radio
			//
			GridBagConstraints radioConstrains = new GridBagConstraints();
			radioConstrains.fill           = GridBagConstraints.HORIZONTAL;
			radioConstrains.insets         = new Insets(0, 0, 0, GUI_DEFAULT_GAP);

			JRadioButton copyIndexRadio = new JRadioButton();  // the best radio on the world
			copyIndexRadio.setSelected(index == this.copyDataIndex);
			copyIndexRadio.setToolTipText(language.CHANGE_DATA_COPY_INDEX_SELECT_RADIO_TOOLTIP);
			JPanel radioHome = new JPanel(); // I need a home to keep the space if copyIndexRadio is invisible
			radioHome.setPreferredSize(GUI_ICON_BUTTON_DIMENSIONS);
			radioHome.add(copyIndexRadio); // move into the home
			radioGroup.add(copyIndexRadio); // Join a radio station
			radioList.add(copyIndexRadio);
			titlePanel.add(radioHome, radioConstrains); // Advertisement for the new Radio (I need a life)

			// title label
			GridBagConstraints labelConstrains = new GridBagConstraints();
			labelConstrains.fill           = GridBagConstraints.HORIZONTAL;
			labelConstrains.insets         = new Insets(0, 0, 0, GUI_DEFAULT_GAP);
			titlePanel.add(new JLabel(String.format(language.INFO_PANEL_DATA_LABEL, index)), labelConstrains);

			//
			// text field
			//
			GridBagConstraints dataConstrains = new GridBagConstraints();
			dataConstrains.fill            = GridBagConstraints.HORIZONTAL;
			dataConstrains.weightx         = 1.0;
			dataConstrains.insets          = new Insets(0, 0, 0, GUI_DEFAULT_GAP);

			JTextPane dataField = CreateSelectableText();
			dataField.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
			dataField.setContentType("text/plain");
			if (index < this.contentList.size()) {
				dataField.setText(this.contentList.get(index));
			} else {
				dataField.setText("");
			}
			titlePanel.add(dataField, dataConstrains);
			dataFields.add(dataField);

			//
			// editDataButton
			//
			GridBagConstraints editIconConstrains = new GridBagConstraints();
			editIconConstrains.fill        = GridBagConstraints.HORIZONTAL;
			editIconConstrains.insets      = new Insets(0, 0, 0, GUI_DEFAULT_GAP);

			JButton editDataButton = new JButton(GUI_EDIT_ICON);
			editDataButton.setToolTipText(language.CHANGE_DATA_EDIT_BUTTON_TOOLTIP);
			editDataButton.setPreferredSize(GUI_ICON_BUTTON_DIMENSIONS);
			editDataButton.addActionListener(e -> {
				if (editDataButton.getIcon().equals(GUI_EDIT_ICON)) {

					//
					// make the data field eatable(no this is not a typo)
					//
					dataField.setEditable(true);
					dataField.setBorder(BorderFactory.createLineBorder(GUI_BORDER_COLOR));
					dataField.setCaretPosition(dataField.getText().length());
					dataField.requestFocus();

					editDataButton.setIcon(GUI_CHECK_ICON);
				} else {

					//
					// make uneatable(again not a typo) nad remove visual border
					//
					dataField.setEditable(false);
					dataField.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));

					editDataButton.setIcon(GUI_EDIT_ICON);
				}
			});
			titlePanel.add(editDataButton, editIconConstrains);

			//
			// copyDataButton
			//
			GridBagConstraints copyIconConstrains = new GridBagConstraints();
			copyIconConstrains.fill        = GridBagConstraints.HORIZONTAL;
			copyIconConstrains.insets      = new Insets(0, 0, 0, GUI_DEFAULT_GAP);

			JButton copyDataButton = new JButton(GUI_COPY_ICON);
			copyDataButton.setToolTipText(language.CHANGE_DATA_COPY_BUTTON_TOOLTIP);
			copyDataButton.setPreferredSize(GUI_ICON_BUTTON_DIMENSIONS);
			copyDataButton.addActionListener(e -> {
				CopyToClipboard(dataField.getText());
			});
			titlePanel.add(copyDataButton, copyIconConstrains);

			//
			// deleteIconButton
			//
			GridBagConstraints deleteIconConstrains = new GridBagConstraints(); // deleteIconConstrains
			deleteIconConstrains.fill      = GridBagConstraints.HORIZONTAL;
			deleteIconConstrains.gridwidth = GridBagConstraints.REMAINDER; // end row
			deleteIconConstrains.insets    = new Insets(0, 0, 0, 0); // no insets, done by the panel

			JButton deleteIconButton = new JButton(GUI_DELETE_ICON);
			deleteIconButton.setToolTipText(language.CHANGE_DATA_REMOVE_BUTTON_TOOLTIP);
			deleteIconButton.setPreferredSize(GUI_ICON_BUTTON_DIMENSIONS);
			deleteIconButton.addActionListener(e -> {

				boolean newComponentVisibility;
				if (deleteIconButton.getIcon().equals(GUI_DELETE_ICON)) {
					//
					// delete
					//
					newComponentVisibility = false;

					copyIndexRadio.setSelected(false); // unselect it just in case

					deleteIconButton.setIcon(GUI_UNDO_ICON);
					deleteIconButton.setToolTipText(language.CHANGE_DATA_UNDO_BUTTON_TOOLTIP);

					dataField.setBackground(GUI_DELETE_DATA_BACKGROUND);
					dataField.setEditable(false);
					dataField.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
				} else {
					//
					// undo
					//
					newComponentVisibility = true;

					deleteIconButton.setIcon(GUI_DELETE_ICON);
					deleteIconButton.setToolTipText(language.CHANGE_DATA_REMOVE_BUTTON_TOOLTIP);

					dataField.setBackground(null);
				}

				//hide or show components
				copyIndexRadio.setVisible(newComponentVisibility);
				copyDataButton.setVisible(newComponentVisibility);
				editDataButton.setVisible(newComponentVisibility);

				titlePanel.updateUI();
			});
			titlePanel.add(deleteIconButton, deleteIconConstrains);

			return titlePanel;
		}

		/* ********************************************************* */
		// * saving and loading *
		/* ********************************************************* */
		// [FORMAT_UNIT_SEPARATOR] => US
		// Save format [title] [US] [copyDataIndex] ([US] [contentList]) x contentList.size()
		String getSaveString() {
			StringBuilder sb = new StringBuilder();

			// Title
			sb.append(getPaddedString(this.title));

			// copy Data Index
			sb.append(FORMAT_UNIT_SEPARATOR);
			sb.append(Integer.toString(this.copyDataIndex));

			// Data
			for (String data : this.contentList) {
				sb.append(FORMAT_UNIT_SEPARATOR);
				sb.append(getPaddedString(data));
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

			this.title = getUnpaddedString(components[0]);
			try {
				this.copyDataIndex = Integer.parseInt(components[1]);
			} catch (NumberFormatException e) {
				return false;
			}

			for (int componentNo = 2; componentNo < components.length; componentNo++) {
				this.contentList.add(getUnpaddedString(components[componentNo]));
			}

			return true;
		}

	}
}
