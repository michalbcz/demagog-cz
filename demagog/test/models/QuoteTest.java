package models;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Date;
import java.util.List;

import org.junit.Test;

public class QuoteTest {

	@Test
	public void saveTest() {
		Quote quote = new Quote();
		quote.url = "http://test.cz";
		quote.quoteText = "Kocka je pes";
		quote.creationDate = new Date();
		
		Quote.deleteAll();
		quote.save();
		
		List<Quote> quotes = Quote.findAll();
		assertThat(quotes.get(0)).isEqualTo(quote);
	}
	
}
