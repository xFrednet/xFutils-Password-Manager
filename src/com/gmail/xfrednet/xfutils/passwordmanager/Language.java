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

public class Language {

	/* //////////////////////////////////////////////////////////////////////////////// */
	// // Enter password dialog //
	/* //////////////////////////////////////////////////////////////////////////////// */
	public final String ENTER_PASSWORD_INFO_LABEL   = "Please enter your Password: ";
	public final String ENTER_PASSWORD_CANCEL_BUTTON_LABEL = "Cancel";
	public final String ENTER_PASSWORD_OKAY_BUTTON_LABEL = "Done";

	/* //////////////////////////////////////////////////////////////////////////////// */
	// // Menu //
	/* //////////////////////////////////////////////////////////////////////////////// */
	public final String FILE_MENU_NAME = "File";
	public final String BACKUP_MENU_NAME = "Make Backup";
	public final String EXTRAS_MENU_NAME = "Extras";
	public final String ADD_DATA_MENU_NAME = "Add data";
	public final String NEW_DATA_DEFAULT_TITLE = "Title";

	/* //////////////////////////////////////////////////////////////////////////////// */
	// // Info panel //
	/* //////////////////////////////////////////////////////////////////////////////// */
	public final String INFO_PANEL_TITLE_LABEL = "Name: ";
	public final String INFO_PANEL_DATA_LABEL = "Data %d: ";
	public final String NO_MODE_SELECTED = "No mode is selected";
	public final String[] MODE_BUTTON_LABELS = {"Retrieve mode", "Change mode", "Delete mode"};
	public final String[] MODE_INFO_LABEL_TEXT = {
			"Clicking on a label will show the stored contentList",
			"Clicking on a label will open a dialog to change the contentList",
			"Clicking on a label will delete the stored contentList",
	};

	/* //////////////////////////////////////////////////////////////////////////////// */
	// // Add tab panel //
	/* //////////////////////////////////////////////////////////////////////////////// */
	public final String ADD_TAB_NAME_FIELD_TEXT = "Please enter the name of the new Tab";
	public final String ADD_TAB_INDEX_LABEL = "Index:";
	public final String ADD_TAB_BUTTON_LABEL = "Add tab";

	/* //////////////////////////////////////////////////////////////////////////////// */
	// // change Data panel //
	/* //////////////////////////////////////////////////////////////////////////////// */
	public final String CHANGE_DATA_ADD_BUTTON_LABEL = "Add data";
	public final String CHANGE_DATA_SAVE_BUTTON_LABEL = "Save";
	public final String CHANGE_DATA_CANCEL_BUTTON_LABEL = "Cancel";
	public final String CHANGE_DATA_COPY_INDEX_SELECT_RADIO_TOOLTIP = "This decides which data will be copied to clipboard using the shortcut";
	public final String CHANGE_DATA_EDIT_BUTTON_TOOLTIP = "Press to edit this data";
	public final String CHANGE_DATA_COPY_BUTTON_TOOLTIP = "Copies the data to the clipboard";
	public final String CHANGE_DATA_REMOVE_BUTTON_TOOLTIP = "Press to remove this data";
	public final String CHANGE_DATA_UNDO_BUTTON_TOOLTIP = "Press to edit this data";

	/* //////////////////////////////////////////////////////////////////////////////// */
	// // Error/Warning/Info popup messages //
	/* //////////////////////////////////////////////////////////////////////////////// */
	public final String ERROR_STRING_NOT_SUPPORTED = "The entered string contains unsupported characters";
	public final String ERROR_DATA_0_ENTRIES = "This title has to have at least 1 string associated with it";
	public final String ERROR_PLEASE_SELECT_A_DATA_TAB = "Please select a data tab to add data to";
	public final String ERROR_SAVE_TO_FILE_FAILED = "The data couldn't be saved.";
	public final String ERROR_SAVE_BACKUP_FAILED = "The backup couldn't be saved.";
	public final String INFO_SAVE_BACKUP_OKAY = "The backup was saved!";
}
