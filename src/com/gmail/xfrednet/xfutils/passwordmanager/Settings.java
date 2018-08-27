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

public class Settings {

	public int frameX;
	public int frameY;
	public int frameWidth;
	public int frameHeight;

	public static Settings InitDefault() {
		Settings settings = new Settings();

		// JFrame Position
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		settings.frameWidth = 800;
		settings.frameHeight = 600;
		settings.frameX = screenSize.width / 2  - settings.frameWidth / 2;
		settings.frameY = screenSize.height / 2 - settings.frameHeight / 2;

		return settings;
	}
	public static Settings LoadSettings(String fileName) {
		Settings settings = new Settings();

		if (!settings.loadSettings(fileName)) {
			return null;
		}

		return settings;
	}
	private boolean processLoadString(String name, String value) {
		try {
			switch (name) {
				case "frameX" :
					frameX = Integer.parseInt(value);
					return true;
				case "frameY" :
					frameY = Integer.parseInt(value);
					return true;
				case "frameWidth" :
					frameWidth = Integer.parseInt(value);
					return true;
				case "frameHeight":
					frameHeight = Integer.parseInt(value);
					return true;
			}
		} catch (NumberFormatException e) {
			System.err.println("Settings.processLoadString: NumberFormatException, value String: " + value);
		}

		return false;
	}
	private boolean loadSettings(String fileName) {
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
	public boolean saveOptions(String fileName) {
		try {
			Writer writer = new OutputStreamWriter(new FileOutputStream(fileName), StandardCharsets.UTF_8);

			writer.write("frameX="      + frameX      + "\n");
			writer.write("frameY="      + frameY      + "\n");
			writer.write("frameWidth="  + frameWidth  + "\n");
			writer.write("frameHeight=" + frameHeight + "\n");

			writer.close();
			return true;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return false;
	}

}
