package jenkins.model;

import static org.junit.Assert.assertTrue;
import org.junit.Test;
import jenkins.model.IdStrategy.CaseSensitive;

public class Q4IdStrategyTest {
	@Test
	public void testReturnsNormalFilename() {
		CaseSensitive caseSens = new CaseSensitive();
		String testFilename = caseSens.idFromFilename("hello.txt");
		assertTrue("Test returns filename", testFilename.equals("hello.txt"));
	}
	
	@Test
	public void testFilenameToUpper() {
		CaseSensitive caseSens = new CaseSensitive();
		String testFilename = caseSens.idFromFilename("he~llo.txt");
		assertTrue("Test returns uppercase", testFilename.equals("heLlo.txt"));
	}
}
