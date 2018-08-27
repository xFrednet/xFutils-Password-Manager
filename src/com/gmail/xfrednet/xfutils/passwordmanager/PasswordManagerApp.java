package com.gmail.xfrednet.xfutils.passwordmanager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import static javafx.application.Platform.exit;

public class PasswordManagerApp {

	private static final boolean JUMP_ASK  = true;

	private static final String TITLE              = "xFutils Password Manager";
	private static final String TIME_STAMP_FORMAT  = "yyyy.MM.dd.HH.mm.ss";
	private static final String SALT_FILE_NAME     = "salt.and.pepper";
	private static final int    SALT_SIZE          = 256;
	private static final String SETTINGS_FILE_NAME = "settings.txt";

	private static final Color[] GUI_MODE_INFO_COLORS = {Color.green, Color.blue, Color.red};
	private static final Color   GUI_BORDER_COLOR     = new Color(0xacacac);
	private static final int     RETRIEVE_MODE       = 0;
	private static final int     CHANGE_MODE         = 1;
	private static final int     DELETE_MODE         = 2;
	private static final int     MODE_COUNT          = 3;

	byte[] key;
	private Settings settings;
	private final Language language = new Language();
	private int currentMode;

	//
	// Base GUI
	//
	private JFrame window;
	private JPanel guiDataInfoPanel;
	private JLabel guiModeInfoLabel;
	private JPanel guiModeInfoColor;
	private JButton[] guiModeButtons;

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

		if (!initSettings()) {
			System.err.println("initSettings failed!");
			exit();
		}
		initBaseGUI();
		System.out.println("Let'se got");

		//TODO load data
		//TODO create JFRAME
		//TODO create data GUI
		//TODO update data GUI
		setMode(RETRIEVE_MODE);
	}

	private boolean initSettings() {
		this.settings = Settings.LoadSettings(SETTINGS_FILE_NAME);
		if (this.settings == null) {

			System.err.println("Settings.LoadSettings has failed a new settings instance will be initialized and saved!");

			this.settings = Settings.InitDefault();
			if (!this.settings.saveOptions(SETTINGS_FILE_NAME)) {
				System.err.println("Saving the nwe  Settings instance has failed! bye bye :/");
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
		//window.setLayout(new BoxLayout(window, BoxLayout.Y_AXIS));

		//
		// Info/Option panel
		//
		if (true) {
			JPanel infoPanel = new JPanel();
			infoPanel.setMinimumSize(new Dimension(0, 100) );
			infoPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100) );
			infoPanel.setLayout(new GridLayout(1, 2));

			// Data info 1/2
			this.guiDataInfoPanel = new JPanel();
			this.guiDataInfoPanel.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createLineBorder(GUI_BORDER_COLOR),
					BorderFactory.createEmptyBorder(5, 5, 5, 5)));
			infoPanel.add(this.guiDataInfoPanel);

			//
			// Mode 1/2
			//
			JPanel selectModePanel = new JPanel();
			selectModePanel.setLayout(new GridLayout(3, 1, 5, 5));
			selectModePanel.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createLineBorder(GUI_BORDER_COLOR),
					BorderFactory.createEmptyBorder(5, 5, 5, 5)));

			// guiModeInfoLabel
			this.guiModeInfoLabel = new JLabel(this.language.NO_MODE_SELECTED, JLabel.LEFT);
			selectModePanel.add(this.guiModeInfoLabel);

			// guiModeInfoColor
			this.guiModeInfoColor = new JPanel();
			selectModePanel.add(this.guiModeInfoColor);

			// mode select buttons
			JPanel modeButtonPanel = new JPanel(new GridLayout(1, 3, 5, 5));
			this.guiModeButtons = new JButton[3];
			for (int modeButtonNo = 0; modeButtonNo < MODE_COUNT; modeButtonNo++) {
				this.guiModeButtons[modeButtonNo] = new JButton(this.language.MODE_BUTTON_LABELS[modeButtonNo]);
				this.guiModeButtons[modeButtonNo].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
						JButton button = (JButton)e.getSource();

						for (int modeNo = 0; modeNo < MODE_COUNT; modeNo++) {
							String testLabel = PasswordManagerApp.this.language.MODE_BUTTON_LABELS[modeNo];
							if (button.getText().equals(testLabel)) {
								PasswordManagerApp.this.setMode(modeNo);
								break;
							}
						}
					}
				});
				modeButtonPanel.add(this.guiModeButtons[modeButtonNo]);
			}

			selectModePanel.add(modeButtonPanel);

			infoPanel.add(selectModePanel);
			mainPanel.add(infoPanel);
		}

		//
		// Content panel
		//
		JPanel contentPanel = new JPanel();
		contentPanel.setPreferredSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
		contentPanel.setBackground(Color.PINK);
		mainPanel.add(contentPanel);

		//
		// Finish
		//
		this.window.add(mainPanel);
		this.window.setVisible(true);
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
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return false; // The writing failed
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
}




