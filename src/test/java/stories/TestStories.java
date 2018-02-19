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

import java.util.Arrays;
import java.util.List;
import org.jbehave.core.Embeddable;
import org.jbehave.core.configuration.*;
import org.jbehave.core.io.*;
import org.jbehave.core.junit.*;
import org.jbehave.core.reporters.*;
import org.jbehave.core.steps.*;

/**
 *
 * @author Eugene Dementiev
 */
public class TestStories extends JUnitStories {
 
    // Here we specify the configuration, starting from default MostUsefulConfiguration, and changing only what is needed
    @Override
    public Configuration configuration() {
        return new MostUsefulConfiguration()
            // where to find the stories
			.useStoryLoader(new LoadFromClasspath(this.getClass()))
            // CONSOLE and TXT reporting
            .useStoryReporterBuilder(
				new StoryReporterBuilder()
				.withDefaultFormats()
				.withFormats(Format.CONSOLE, Format.TXT)
				.withFailureTrace(true)
			);
    }
 
    // Here we specify the steps classes
    @Override
		public InjectableStepsFactory stepsFactory() {        
        // varargs, can have more that one steps classes
        return new InstanceStepsFactory(configuration(), new ExtractorSteps());
    }
		
	@Override
	protected List<String> storyPaths() {
		return new StoryFinder().findPaths(CodeLocations.codeLocationFromClass(this.getClass()), "**/*.story", "**/excluded*.story");

	}

}
