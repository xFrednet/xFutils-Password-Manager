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

import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

class Settings {

	 int frameX;
	int frameY;
	int frameWidth;
	int frameHeight;
	long lastBackup;

	static Settings InitDefault() {
		Settings settings = new Settings();

		// JFrame Position
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		settings.frameWidth = 800;
		settings.frameHeight = 600;
		settings.frameX = screenSize.width / 2  - settings.frameWidth / 2;
		settings.frameY = screenSize.height / 2 - settings.frameHeight / 2;
		settings.lastBackup = 0;

		return settings;
	}
	static Settings LoadSettings(String fileName) {
		Settings settings = new Settings();

		if (!settings.load(fileName)) {
			return null;
		}

		return settings;
	}
	private boolean processLoadString(String name, String value) {
		try {
			switch (name) {
				case "frameX" :
					this.frameX = Integer.parseInt(value);
					return true;
				case "frameY" :
					this.frameY = Integer.parseInt(value);
					return true;
				case "frameWidth" :
					this.frameWidth = Integer.parseInt(value);
					return true;
				case "frameHeight":
					this.frameHeight = Integer.parseInt(value);
					return true;
				case "lastBackup":
					this.lastBackup = Long.parseLong(value);
					return true;
				default:
					return false;
			}
		} catch (NumberFormatException e) {
			System.err.println("Settings.processLoadString: NumberFormatException, value String: " + value);
		}

		return false;
	}
	private boolean load(String fileName) {
		File file = new File(fileName);

		if (!file.exists())
			return false;

		try {
			Scanner reader = new Scanner(file);

			boolean hasLoadingFailed = false;
			while (reader.hasNextLine()) {
				String line = reader.nextLine();
				String[] parts = line.split("=");

				// Validation
				if (parts.length != 2) {
					hasLoadingFailed = true;
					break;
				}

				// Process string
				if (!processLoadString(parts[0], parts[1])) {
					hasLoadingFailed = true;
					break;
				}

			}

			reader.close();
			return !hasLoadingFailed;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return false;
	}
	boolean save(String fileName) {
		try {
			Writer writer = new OutputStreamWriter(new FileOutputStream(fileName), StandardCharsets.UTF_8);

			writer.write("frameX="      + this.frameX      + "\n");
			writer.write("frameY="      + this.frameY      + "\n");
			writer.write("frameWidth="  + this.frameWidth  + "\n");
			writer.write("frameHeight=" + this.frameHeight + "\n");
			writer.write("lastBackup="  + this.lastBackup + "\n");

			writer.close();
			return true;

		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;
	}

}
