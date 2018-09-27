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
	// // General //
	/* //////////////////////////////////////////////////////////////////////////////// */
	final String FILE_CHOOSER_XFCRYPT_FILE_DESC;

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

	final String MENU_TAB_NAME;
	final String MENU_TAB_RENAME_TAB;
	final String MENU_TAB_RENAME_DIALOG_MESSAGE;
	final String MENU_TAB_MOVE_LEFT_TAB;
	final String MENU_TAB_MOVE_RIGHT_TAB;
	final String MENU_TAB_REMOVE_TAB;
	final String MENU_TAB_REMOVE_CONFIRM_MESSAGE;

	final String ADD_DATA_MENU_NAME;
	final String NEW_DATA_DEFAULT_TITLE;

	final String EXTRAS_MENU_NAME;
	final String EXTRAS_EXPORT_TO_TXT_NAME;
	final String EXTRAS_IMPORT_FROM_TXT_NAME;
	final String EXTRAS_TXT_FILE_DESCRIPTION;
	final String EXTRAS_IMPORT_FILE_DIALOG_NAME;
	final String EXTRAS_EXPORT_FILE_DIALOG_NAME;
	final String EXTRAS_IMPORT_DEFAULT_TAB_NAME;
	final String EXTRAS_EXPORT_COMPLETE_INFO;
	final String EXTRAS_EXPORT_FAILED_INFO;
	final String EXTRAS_IMPORT_COMPLETE_INFO;
	final String EXTRAS_IMPORT_FAILED_INFO;

	final String MENU_EXTRAS_ENCRYPT_FILE_LABEL;
	final String MENU_EXTRAS_ENCRYPT_FILE_DIALOG;
	final String MENU_EXTRAS_ENCRYPT_FAILED;
	final String MENU_EXTRAS_ENCRYPT_SUCCESSFUL;

	final String MENU_EXTRAS_DECRYPT_FILE_LABEL;
	final String MENU_EXTRAS_DECRYPT_FILE_DIALOG;
	final String MENU_EXTRAS_DECRYPT_FAILED;
	final String MENU_EXTRAS_DECRYPT_SUCCESSFUL;

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
	final String ADD_TAB_INFO_LABEL;
	final String ADD_TAB_TAB_NAME_LABEL;
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
	final String ERROR_INVALID_TAB_INDEX;

	static final int ENG = 0;
	static final int DE = 1;

	Language(int lang) {
		switch (lang) {
		case DE:
			// General
			this.FILE_CHOOSER_XFCRYPT_FILE_DESC     = "XFCRYPT Datein";

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

			this.MENU_TAB_NAME                   = "Tab";
			this.MENU_TAB_RENAME_TAB             = "Tab umbennen";
			this.MENU_TAB_RENAME_DIALOG_MESSAGE  = "Bitte geben Sie den Tab einen neuen Namen:";
			this.MENU_TAB_MOVE_LEFT_TAB          = "<== Nach links schieben";
			this.MENU_TAB_MOVE_RIGHT_TAB         = "==> Nach rechts schieben";
			this.MENU_TAB_REMOVE_TAB             = "Tab löschen";
			this.MENU_TAB_REMOVE_CONFIRM_MESSAGE = "Sind Sie sich sicher, dass der Tab gelöscht werden soll?";

			this.EXTRAS_MENU_NAME                = "Extras";
			this.ADD_DATA_MENU_NAME              = "Infos hinzufügen";
			this.NEW_DATA_DEFAULT_TITLE          = "Titel";

			this.EXTRAS_EXPORT_TO_TXT_NAME       = "Exportiere zu Text";
			this.EXTRAS_IMPORT_FROM_TXT_NAME     = "Importiere von Text";
			this.EXTRAS_TXT_FILE_DESCRIPTION     = "Text Datein";
			this.EXTRAS_IMPORT_FILE_DIALOG_NAME  = "Wähle die Datei die geladen werden soll";
			this.EXTRAS_EXPORT_FILE_DIALOG_NAME  = "Wähle die Output datei";
			this.EXTRAS_IMPORT_DEFAULT_TAB_NAME  = "Importiert";
			this.EXTRAS_EXPORT_COMPLETE_INFO     = "Die Datei wurde erfolgreich exportiert";
			this.EXTRAS_EXPORT_FAILED_INFO       = "Leider ist beim Importieren ein Fehler aufgetreten";
			this.EXTRAS_IMPORT_COMPLETE_INFO     = "Die Datei wurde erfolgreich importiert";
			this.EXTRAS_IMPORT_FAILED_INFO       = "Leider konnte die Datei nicht richtig importiert werden";

			this.MENU_EXTRAS_ENCRYPT_FILE_LABEL  = "Datei verschlüsseln";
			this.MENU_EXTRAS_ENCRYPT_FILE_DIALOG = "Wähle eine Datei zum verschlüsseln aus";
			this.MENU_EXTRAS_ENCRYPT_FAILED      = "Wärend der verschlüsselung ist etwas fehlgeschlagen";
			this.MENU_EXTRAS_ENCRYPT_SUCCESSFUL = "Die verschlüsselung war erfolgreich!";
			this.MENU_EXTRAS_DECRYPT_FILE_LABEL  = "Datei entschlüsseln";
			this.MENU_EXTRAS_DECRYPT_FILE_DIALOG = "Wähle eine Datei zum entschlüsseln aus";
			this.MENU_EXTRAS_DECRYPT_FAILED      = "Wärend der entschlüsselung ist etwas fehlgeschlagen";
			this.MENU_EXTRAS_DECRYPT_SUCCESSFUL  = "Die entschlüsselung war erfolgreich!";

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
			this.ADD_TAB_INFO_LABEL      = "Hier kannst du einen neuen Tab hinzufügen";
			this.ADD_TAB_TAB_NAME_LABEL  = "Tab Name";
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
			this.ERROR_INVALID_TAB_INDEX        = "Der gewählte tab index ist leider ungültig";

			break;

		default:
		case ENG:
			// General
			this.FILE_CHOOSER_XFCRYPT_FILE_DESC     = "XFCRYPT Files";

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

			this.MENU_TAB_NAME                   = "Tab";
			this.MENU_TAB_RENAME_TAB             = "Rename tab";
			this.MENU_TAB_RENAME_DIALOG_MESSAGE  = "Please enter the new tab name:";
			this.MENU_TAB_MOVE_LEFT_TAB          = "<== Move left";
			this.MENU_TAB_MOVE_RIGHT_TAB         = "==> Move right";
			this.MENU_TAB_REMOVE_TAB             = "Remove tab";
			this.MENU_TAB_REMOVE_CONFIRM_MESSAGE = "Are you sure you want to delete this tab?";

			this.EXTRAS_MENU_NAME                = "Extras";
			this.ADD_DATA_MENU_NAME              = "Add data";
			this.NEW_DATA_DEFAULT_TITLE          = "Title";

			this.EXTRAS_EXPORT_TO_TXT_NAME       = "Export to txt file";
			this.EXTRAS_IMPORT_FROM_TXT_NAME     = "Import from txt file";
			this.EXTRAS_TXT_FILE_DESCRIPTION     = "Text files";
			this.EXTRAS_IMPORT_FILE_DIALOG_NAME  = "Choose the file to import";
			this.EXTRAS_EXPORT_FILE_DIALOG_NAME  = "Choose the output file";
			this.EXTRAS_IMPORT_DEFAULT_TAB_NAME  = "Imported";
			this.EXTRAS_EXPORT_COMPLETE_INFO     = "The file was successfully exported";
			this.EXTRAS_EXPORT_FAILED_INFO       = "Something failed during the export process";
			this.EXTRAS_IMPORT_COMPLETE_INFO     = "The file was successfully imported";
			this.EXTRAS_IMPORT_FAILED_INFO       = "Something failed during the import process";

			this.MENU_EXTRAS_ENCRYPT_FILE_LABEL  = "Encrypt file";
			this.MENU_EXTRAS_ENCRYPT_FILE_DIALOG = "Select a file to encrypt";
			this.MENU_EXTRAS_ENCRYPT_FAILED      = "Something failed during the encryption";
			this.MENU_EXTRAS_ENCRYPT_SUCCESSFUL = "The encryption was successful";
			this.MENU_EXTRAS_DECRYPT_FILE_LABEL  = "Decrypt file";
			this.MENU_EXTRAS_DECRYPT_FILE_DIALOG = "Select a file to decrypt";
			this.MENU_EXTRAS_DECRYPT_FAILED      = "Something failed during the decryption";
			this.MENU_EXTRAS_DECRYPT_SUCCESSFUL = "The decryption was successful";

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
			this.ADD_TAB_INFO_LABEL      = "You can add a new tab here.";
			this.ADD_TAB_TAB_NAME_LABEL  = "Tab name:";
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
			this.ERROR_INVALID_TAB_INDEX        = "The chosen tab index is sadly invalid!";

			break;

		}
	}
}
