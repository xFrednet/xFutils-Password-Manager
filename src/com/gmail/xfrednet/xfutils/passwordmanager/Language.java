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

class Language {

	/* //////////////////////////////////////////////////////////////////////////////// */
	// // Enter password dialog //
	/* //////////////////////////////////////////////////////////////////////////////// */
	final String ENTER_PASSWORD_INFO_LABEL;
	final String ENTER_PASSWORD_CANCEL_BUTTON_LABEL;
	final String ENTER_PASSWORD_OKAY_BUTTON_LABEL;

	/* //////////////////////////////////////////////////////////////////////////////// */
	// // Menu //
	/* //////////////////////////////////////////////////////////////////////////////// */
	final String FILE_MENU_NAME;
	final String CHANGE_PASSWORD_MENU_NAME;
	final String BACKUP_MENU_NAME;
	final String SETTINGS_MENU_NAME;
	final String SETTINGS_FRAME_MENU_NAME;
	final String SETTINGS_SAVE_POS_MENU_NAME;
	final String SETTINGS_SAVE_SIZE_MENU_NAME;
	final String SETTINGS_LAYOUT_MENU_NAME;
	final String SETTINGS_BUTTON_COUNT_MENU_NAME;
	final String SETTINGS_LANGUAGE_MENU_NAME;
	final String SETTINGS_LANGUAGE_ENG_MENU_NAME = "English";
	final String SETTINGS_LANGUAGE_DE_MENU_NAME = "Deutsch";
	final String SETTINGS_RESET_MENU_NAME;
	final String EXTRAS_MENU_NAME;
	final String ADD_DATA_MENU_NAME;
	final String NEW_DATA_DEFAULT_TITLE;

	/* //////////////////////////////////////////////////////////////////////////////// */
	// // Info panel //
	/* //////////////////////////////////////////////////////////////////////////////// */
	final String   INFO_PANEL_TITLE_LABEL;
	final String   INFO_PANEL_DATA_LABEL;
	final String   NO_MODE_SELECTED;
	final String[] MODE_BUTTON_LABELS;
	final String[] MODE_INFO_LABEL_TEXT;

	/* //////////////////////////////////////////////////////////////////////////////// */
	// // Add tab panel //
	/* //////////////////////////////////////////////////////////////////////////////// */
	final String ADD_TAB_NAME_FIELD_TEXT;
	final String ADD_TAB_INDEX_LABEL;
	final String ADD_TAB_BUTTON_LABEL;

	/* //////////////////////////////////////////////////////////////////////////////// */
	// // Change data panel //
	/* //////////////////////////////////////////////////////////////////////////////// */
	final String CHANGE_DATA_ADD_BUTTON_LABEL;
	final String CHANGE_DATA_SAVE_BUTTON_LABEL;
	final String CHANGE_DATA_CANCEL_BUTTON_LABEL;
	final String CHANGE_DATA_COPY_INDEX_SELECT_RADIO_TOOLTIP;
	final String CHANGE_DATA_EDIT_BUTTON_TOOLTIP;
	final String CHANGE_DATA_COPY_BUTTON_TOOLTIP;
	final String CHANGE_DATA_REMOVE_BUTTON_TOOLTIP;
	final String CHANGE_DATA_UNDO_BUTTON_TOOLTIP;

	/* //////////////////////////////////////////////////////////////////////////////// */
	// // Change password panel //
	/* //////////////////////////////////////////////////////////////////////////////// */
	final String CHANGE_PASS_OLD_PASS_LABEL;
	final String CHANGE_PASS_NEW_PASS_LABEL;
	final String CHANGE_PASS_NEW_PASS_AGAIN_LABEL;
	final String CHANGE_PASS_CANCEL_BUTTON_LABEL;
	final String CHANGE_PASS_SAVE_BUTTON_LABEL;
	final String CHANGE_PASS_INVALID_INPUT_LABEL;
	final String CHANGE_PASS_CHANGE_PASS_FAILED;
	final String CHANGE_PASS_ALL_GOOD;

	/* //////////////////////////////////////////////////////////////////////////////// */
	// // Error/Warning/Info popup messages //
	/* //////////////////////////////////////////////////////////////////////////////// */
	final String ERROR_STRING_NOT_SUPPORTED;
	final String ERROR_DATA_0_ENTRIES;
	final String ERROR_PLEASE_SELECT_A_DATA_TAB;
	final String ERROR_SAVE_TO_FILE_FAILED;
	final String ERROR_SAVE_BACKUP_FAILED;
	final String INFO_SAVE_BACKUP_OKAY;
	final String INFO_RESTART_TO_LOAD_CHANGE;

	static final int ENG = 0;
	static final int DE = 1;

	Language(int lang) {
		switch (lang) {
		case DE:
			// Enter password dialog
			this.ENTER_PASSWORD_INFO_LABEL          = "Gebe bitte dein Passwort ein: ";
			this.ENTER_PASSWORD_CANCEL_BUTTON_LABEL = "Abbrechen";
			this.ENTER_PASSWORD_OKAY_BUTTON_LABEL   = "Fertig";

			// Menu
			this.FILE_MENU_NAME                  = "Datei";
			this.CHANGE_PASSWORD_MENU_NAME       = "Passwort ändern";
			this.BACKUP_MENU_NAME                = "Erstelle ein Backup";
			this.SETTINGS_MENU_NAME              = "Einstellungen";
			this.SETTINGS_FRAME_MENU_NAME        = "Fenster";
			this.SETTINGS_SAVE_POS_MENU_NAME     = "Position speichern";
			this.SETTINGS_SAVE_SIZE_MENU_NAME    = "Größe speichern";
			this.SETTINGS_LAYOUT_MENU_NAME       = "Layout auswählen";
			this.SETTINGS_BUTTON_COUNT_MENU_NAME = "%d Knöpfe pro Reihe";
			this.SETTINGS_LANGUAGE_MENU_NAME     = "Sprache";
			this.SETTINGS_RESET_MENU_NAME        = "Einstellungen zurücksetzen";
			this.EXTRAS_MENU_NAME                = "Extras";
			this.ADD_DATA_MENU_NAME              = "Infos hinzufügen";
			this.NEW_DATA_DEFAULT_TITLE          = "Titel";

			// Info panel
			this.INFO_PANEL_TITLE_LABEL = "Name: ";
			this.INFO_PANEL_DATA_LABEL  = "Info %d: ";
			this.NO_MODE_SELECTED       = "Kein Modus wurde ausgewählt";
			this.MODE_BUTTON_LABELS     = new String[]{"Lese Modus", "Änderungs Modus", "Lösch Modus"};
			this.MODE_INFO_LABEL_TEXT   = new String[]{
					"Durch klicken auf ein Knopf werden die gespeicherten Daten angezeigt",
					"Durch klicken auf ein Knopf können die Daten geändert werden",
					"Durch klicken auf ein Knopf werden die gespeicherten Daten gelöscht",
			};

			// Add tab info panel
			this.ADD_TAB_NAME_FIELD_TEXT = "Gebe den neuen Tab einen Namen";
			this.ADD_TAB_INDEX_LABEL     = "Index:";
			this.ADD_TAB_BUTTON_LABEL    = "Tab hinzufügen";

			// Change data panel
			this.CHANGE_DATA_ADD_BUTTON_LABEL                = "Neue Info hinzufügen";
			this.CHANGE_DATA_SAVE_BUTTON_LABEL               = "Speichern";
			this.CHANGE_DATA_CANCEL_BUTTON_LABEL             = "Abbrechen";
			this.CHANGE_DATA_COPY_INDEX_SELECT_RADIO_TOOLTIP = "Hierdurch können sie entscheiden was automatisch kopiert werden soll";
			this.CHANGE_DATA_EDIT_BUTTON_TOOLTIP             = "Drücken Sie, um diese Info zu bearbeiten";
			this.CHANGE_DATA_COPY_BUTTON_TOOLTIP             = "Kopiert die Info in die Zwischenablage";
			this.CHANGE_DATA_REMOVE_BUTTON_TOOLTIP           = "Drücken Sie, um diese Info zu löschen";
			this.CHANGE_DATA_UNDO_BUTTON_TOOLTIP             = "Drücken Sie, um die Änderung rückgängig zu machen";

			// Change password panel
			this.CHANGE_PASS_OLD_PASS_LABEL       = "Altes Passwort:";
			this.CHANGE_PASS_NEW_PASS_LABEL       = "Neues Passwort:";
			this.CHANGE_PASS_NEW_PASS_AGAIN_LABEL = "Neues Passwort:";
			this.CHANGE_PASS_CANCEL_BUTTON_LABEL  = "Abbrechen";
			this.CHANGE_PASS_SAVE_BUTTON_LABEL    = "Password ändern";
			this.CHANGE_PASS_INVALID_INPUT_LABEL  = "Etwas stimmt nicht, bitte überprüfe deine Eingaben!";
			this.CHANGE_PASS_CHANGE_PASS_FAILED   = "Leider konnte das Passwort nicht geändert werden.";
			this.CHANGE_PASS_ALL_GOOD             = "Das Passwort wurde erfolgreich geändert";

			// Error/Warning/Info popup messages
			this.ERROR_STRING_NOT_SUPPORTED     = "Der eingegebene Text wird leider nicht unterstützt";
			this.ERROR_DATA_0_ENTRIES           = "Es muss zumindest eine Info zum Titel gespeichert werden";
			this.ERROR_PLEASE_SELECT_A_DATA_TAB = "Bitte wähle einen anderen Tab aus um eine Info hinzuzufügen";
			this.ERROR_SAVE_TO_FILE_FAILED      = "Die Daten konnten leider nicht gespeichert werden";
			this.ERROR_SAVE_BACKUP_FAILED       = "Das Backup konnte leider nicht gespeichert werden";
			this.INFO_SAVE_BACKUP_OKAY          = "Das Backup wurde erfolgreich erstellt";
			this.INFO_RESTART_TO_LOAD_CHANGE    = "Restarte bitte die Anwendung um die Änderungen zu laden";

			break;

		default:
		case ENG:
			// Enter password dialog
			this.ENTER_PASSWORD_INFO_LABEL          = "Please enter your Password: ";
			this.ENTER_PASSWORD_CANCEL_BUTTON_LABEL = "Cancel";
			this.ENTER_PASSWORD_OKAY_BUTTON_LABEL   = "Done";

			// Menu
			this.FILE_MENU_NAME                  = "File";
			this.CHANGE_PASSWORD_MENU_NAME       = "Change password";
			this.BACKUP_MENU_NAME                = "Make Backup";
			this.SETTINGS_MENU_NAME              = "Settings";
			this.SETTINGS_FRAME_MENU_NAME        = "Window";
			this.SETTINGS_SAVE_POS_MENU_NAME     = "Save position";
			this.SETTINGS_SAVE_SIZE_MENU_NAME    = "Save size";
			this.SETTINGS_LAYOUT_MENU_NAME       = "Layout";
			this.SETTINGS_BUTTON_COUNT_MENU_NAME = "%d buttons per row";
			this.SETTINGS_LANGUAGE_MENU_NAME     = "Language";
			this.SETTINGS_RESET_MENU_NAME        = "Reset settings";
			this.EXTRAS_MENU_NAME                = "Extras";
			this.ADD_DATA_MENU_NAME              = "Add data";
			this.NEW_DATA_DEFAULT_TITLE          = "Title";

			// Info panel
			this.INFO_PANEL_TITLE_LABEL = "Name: ";
			this.INFO_PANEL_DATA_LABEL  = "Data %d: ";
			this.NO_MODE_SELECTED       = "No mode is selected";
			this.MODE_BUTTON_LABELS     = new String[]{"Retrieve mode", "Change mode", "Delete mode"};
			this.MODE_INFO_LABEL_TEXT   = new String[]{
					"Clicking on a button will show the stored data",
					"Clicking on a button will open a dialog to change the data",
					"Clicking on a button will delete the stored data",
			};

			// Add tab info panel
			this.ADD_TAB_NAME_FIELD_TEXT = "Please enter the name of the new Tab";
			this.ADD_TAB_INDEX_LABEL     = "Index:";
			this.ADD_TAB_BUTTON_LABEL    = "Add tab";

			// Change data panel
			this.CHANGE_DATA_ADD_BUTTON_LABEL                = "Add data";
			this.CHANGE_DATA_SAVE_BUTTON_LABEL               = "Save";
			this.CHANGE_DATA_CANCEL_BUTTON_LABEL             = "Cancel";
			this.CHANGE_DATA_COPY_INDEX_SELECT_RADIO_TOOLTIP = "This decides which data will be copied to clipboard using the shortcut";
			this.CHANGE_DATA_EDIT_BUTTON_TOOLTIP             = "Press to edit this data";
			this.CHANGE_DATA_COPY_BUTTON_TOOLTIP             = "Copies the data to the clipboard";
			this.CHANGE_DATA_REMOVE_BUTTON_TOOLTIP           = "Press to remove this data";
			this.CHANGE_DATA_UNDO_BUTTON_TOOLTIP             = "Press to undo the change";

			// Change password panel
			this.CHANGE_PASS_OLD_PASS_LABEL       = "Old password:";
			this.CHANGE_PASS_NEW_PASS_LABEL       = "New Password:";
			this.CHANGE_PASS_NEW_PASS_AGAIN_LABEL = "New Password again:";
			this.CHANGE_PASS_CANCEL_BUTTON_LABEL  = "Cancel";
			this.CHANGE_PASS_SAVE_BUTTON_LABEL    = "Change Password";
			this.CHANGE_PASS_INVALID_INPUT_LABEL  = "Something does not add up please check your input!";
			this.CHANGE_PASS_CHANGE_PASS_FAILED   = "Something went wrong while trying to change to the new password";
			this.CHANGE_PASS_ALL_GOOD             = "The password was changed successfully";

			// Error/Warning/Info popup messages
			this.ERROR_STRING_NOT_SUPPORTED     = "The entered string contains unsupported characters";
			this.ERROR_DATA_0_ENTRIES           = "This title has to have at least 1 string associated with it";
			this.ERROR_PLEASE_SELECT_A_DATA_TAB = "Please select a data tab to add data to";
			this.ERROR_SAVE_TO_FILE_FAILED      = "The data couldn't be saved.";
			this.ERROR_SAVE_BACKUP_FAILED       = "The backup couldn't be saved.";
			this.INFO_SAVE_BACKUP_OKAY          = "The backup was saved!";
			this.INFO_RESTART_TO_LOAD_CHANGE    = "Please restart the application to load the changes";

			break;

		}
	}
}
