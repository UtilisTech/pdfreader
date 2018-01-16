/*
 * MIT License
 * 
 * Copyright (c) 2017 Eugene Dementiev
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.som_service.pdfreader.textobject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;

/**
 * Creates a page representation of the PDF page with tokens grouped by line<br/>
 * Grouping is performed by the corresponding TextPosition's getYDirAdj()<br/>
 * Loosely inspired by org.apache.pdfbox.examples.util.PrintTextLocations by Ben Litchfield
 * 
 * @author Eugene Dementiev
 */
public class TextObjectExtractor extends PDFTextStripper {
	
	public final TextObjectPage page = new TextObjectPage();
	
    /**
     * Constructor that can throw an exception
     *
     * @throws IOException If there is an error loading the properties.
     */
    public TextObjectExtractor() throws IOException {}

	/**
	 * Create TextObjects from TextPosition add add them to the page representation
	 * 
	 * @throws IOException 
	 */
    @Override
    protected void writeString(String string, List<TextPosition> textPositions) throws IOException {
        for (TextPosition text : textPositions) {
			page.add(new TextObject(text));
        }
    }
	
	private TextObjectPage extract(PDDocument document, int page_index) throws IOException {

		PDFTextStripper stripper = new TextObjectExtractor();
		stripper.setSortByPosition(true);
		stripper.setStartPage(page_index);
		stripper.setEndPage(page_index);

		Writer dummy = new OutputStreamWriter(new ByteArrayOutputStream());
		stripper.writeText(document, dummy);

		return ((TextObjectExtractor)stripper).page;
	}

	/**
	 * Extracts tokens from a single page of a PDDocument
	 * @param document where to extract from
	 * @param page_index index of the page to extract from (starting from 1)
	 * @return corresponding TextObjectPage
	 * @throws IOException
	 */
	public static TextObjectPage extractPageObjects(PDDocument document, int page_index) throws IOException {
		return (new TextObjectExtractor()).extract(document, page_index);
	}
}
