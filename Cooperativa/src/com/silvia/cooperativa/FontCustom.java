package com.silvia.cooperativa;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.FontFactory;

public class FontCustom {
	public static final Font myCustomFont = new Font(FontFamily.TIMES_ROMAN, 12, Font.BOLD|Font.ITALIC, BaseColor.ORANGE);
	public static final Font normalBoldFont = new Font(FontFamily.HELVETICA, 15, Font.NORMAL|Font.BOLD, BaseColor.BLACK);
	public static final Font boldItalicFont= FontFactory.getFont("arial", 14, Font.BOLD|Font.ITALIC, BaseColor.BLACK);
	public static final Font boldFont = new Font(FontFamily.HELVETICA, 12, Font.NORMAL|Font.BOLD, BaseColor.BLACK);
	public static final Font normalFont = new Font(FontFamily.HELVETICA, 12, Font.NORMAL, BaseColor.BLACK);
	public static final Font italicFont = new Font(FontFamily.COURIER, 12, Font.ITALIC, BaseColor.BLACK);
}
