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
	// // Menu //
	/* //////////////////////////////////////////////////////////////////////////////// */
	public final String FILE_MENU_NAME = "File";
	public final String EXTRAS_MENU_NAME = "Extras";

	/* //////////////////////////////////////////////////////////////////////////////// */
	// // Info panel //
	/* //////////////////////////////////////////////////////////////////////////////// */
	public final String NO_MODE_SELECTED = "No mode is selected";
	public final String[] MODE_BUTTON_LABELS = {"Retrieve mode", "Change mode", "Delete mode"};
	public final String[] MODE_INFO_LABEL_TEXT = {
			"Clicking on a label will show the stored contentList",
			"Clicking on a label will open a dialog to change the contentList",
			"Clicking on a label will delete the stored contentList",
	};

	/* //////////////////////////////////////////////////////////////////////////////// */
	// // Info panel //
	/* //////////////////////////////////////////////////////////////////////////////// */
	public final String ADD_TAB_NAME_FIELD_TEXT = "Please enter the name of the new Tab";
	public final String ADD_TAB_INDEX_LABEL = "Index:";
	public final String ADD_TAB_BUTTON_LABEL = "Add tab";

	/* //////////////////////////////////////////////////////////////////////////////// */
	// // Error/Warning messages //
	/* //////////////////////////////////////////////////////////////////////////////// */
	public final String ERROR_STRING_NOT_SUPPORTED = "The entered string contains unsupported characters";
}
