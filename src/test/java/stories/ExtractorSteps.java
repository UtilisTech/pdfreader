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
package stories;

import com.som_service.pdfreader.textobject.TextObjectExtractor;
import com.som_service.pdfreader.textobject.TextObjectPage;
import com.som_service.pdfreader.textobject.TextObjectWord;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.jbehave.core.annotations.BeforeStories;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.junit.Assert;

/**
 * This is a test class to map story steps to actions
 * @author Eugene Dementiev
 */
public class ExtractorSteps {
	private PDDocument document;
	private double spacingMultiplier = -1;
	private TextObjectPage to_page;
	private List<TextObjectWord> line;
	private TextObjectWord word;
	
	//@BeforeStories
	
	@Given("pdf \"$pdf\"")
    public void initPDFFile(String pdfName) {
		URL pdfURL = resources.Resources.class.getResource(pdfName);
		File file = new File(pdfURL.getPath());
		try {
			document = PDDocument.load(file);
		} catch (IOException ex) {
			Assert.fail("Error accessing file "+pdfName+ ": "+ex.getMessage());
		}
		Assert.assertTrue(document != null);
    }
	
	@Given("spacing multiplier $mult")
	public void setSpacingMultiplier(double spacingMultiplier){
		this.spacingMultiplier = spacingMultiplier;
	}
	
	@When("I look at page $page")
	public void extractTokensFromPage(int page){
		try {
			to_page = TextObjectExtractor.extractPageObjects(document, page);
		} catch (IOException ex) {
			Assert.fail("Error extracting page data from page "+page+ ": "+ex.getMessage());
		}
		
		if (spacingMultiplier > 0){
			to_page.rows.setSpaceScaleMultiplier(spacingMultiplier);
		}
	}
	
	@Then("I get $lines lines")
	public void countLinesOnPage(int lines){
		Assert.assertEquals(lines, to_page.rows.asWords().size());
	}
	
	@When("I look at line $lineIndex")
	public void getLineOnPage(int lineIndex){
		line = to_page.rows.asWords().get(lineIndex-1);
	}
	
	@Then("I get $words words")
	public void countWordsOnLine(int words){
		//System.out.println(line);
		Assert.assertEquals(words, line.size());
	}
	
	@When("I look at the word $wordIndex")
	public void findWord(int wordIndex){
		word = line.get(wordIndex-1);
	}
	
	@Then("the word is \"$wordString\"")
	public void matchWord(String wordString){
		Assert.assertEquals(wordString, word.toString());
	}
	
}
