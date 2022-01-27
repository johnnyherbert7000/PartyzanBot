package by.protest.helper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import by.protest.bot.helper.Utils;

public class UtilsTest {
	
	@Test
	public void convertToPrivateAutoNumberWithRegionTest() {
		assertEquals("1234 HH-7",Utils.convertToPrivateAutoNumberWithRegion("1234HH7"));
	}
	
	@Test
	public void convertToAutoNumberWithRegionTest() {
		assertEquals("AA 1234-7",Utils.convertToAutoNumberWithRegion("AA12347"));
		assertEquals("CB 7711-2",Utils.convertToAutoNumberWithRegion("CB77112"));
	}
	
	@Test
	public void convertOldAutoRegistration3LettersTest() {
		assertEquals("1111 AAA",Utils.convertOldAutoRegistration3Letters("1111AAA"));
	}
	
	@Test
	public void convertOldAutoRegistration2LettersTest() {
		assertEquals("2222 MA",Utils.convertOldAutoRegistration2Letters("2222MA"));
	}
	
	@Test
	public void convertMunitipalAutoRegistration2LettersTest() {
		assertEquals("AE 1234",Utils.convertMunitipalAutoRegistration2Letters("AE1234"));
	}
	
	@Test
	public void converterTest() {
		
		assertEquals("AE 1234",Utils.convertFromDbRepresentation("AE1234"));
		assertEquals("AX 9999",Utils.convertFromDbRepresentation("AX9999"));
		assertEquals("MC 5005",Utils.convertFromDbRepresentation("MC5005"));
		assertEquals("IT 1717",Utils.convertFromDbRepresentation("IT1717"));
		
		assertEquals("1234 AE",Utils.convertFromDbRepresentation("1234AE"));
		assertEquals("9999 AX",Utils.convertFromDbRepresentation("9999AX"));
		assertEquals("5005 MC",Utils.convertFromDbRepresentation("5005MC"));
		assertEquals("1717 XI",Utils.convertFromDbRepresentation("1717XI"));
		
		assertEquals("1234 AET",Utils.convertFromDbRepresentation("1234AET"));
		assertEquals("9999 TAX",Utils.convertFromDbRepresentation("9999TAX"));
		assertEquals("5005 MKC",Utils.convertFromDbRepresentation("5005MKC"));
		assertEquals("1717 XAI",Utils.convertFromDbRepresentation("1717XAI"));
		
		assertEquals("1888 AE-1",Utils.convertFromDbRepresentation("1888AE1"));
		assertEquals("7777 EE-2",Utils.convertFromDbRepresentation("7777EE2"));
		assertEquals("5995 MO-3",Utils.convertFromDbRepresentation("5995MO3"));
		assertEquals("1717 IX-4",Utils.convertFromDbRepresentation("1717IX4"));
		
		assertEquals("AA 8888-0",Utils.convertFromDbRepresentation("AA88880"));
		assertEquals("CB 7711-2",Utils.convertFromDbRepresentation("CB77112"));
		assertEquals("MM 0001-3",Utils.convertFromDbRepresentation("MM00013"));
		assertEquals("OO 7000-1",Utils.convertFromDbRepresentation("OO70001"));
		
		assertEquals("1234567",Utils.convertFromDbRepresentation("1234567"));
		assertEquals("OO70008",Utils.convertFromDbRepresentation("OO70008"));
		assertEquals("7000AA9",Utils.convertFromDbRepresentation("7000AA9"));
		assertEquals("OOO7777",Utils.convertFromDbRepresentation("OOO7777"));
	}
	
	@Test
	public void matchPossiblePaternTest() {
		
		
		assertTrue(Utils.matchesPossiblePattern("000AA"));
		assertTrue(Utils.matchesPossiblePattern("AA000"));
		assertTrue(Utils.matchesPossiblePattern("AA0007"));
		assertTrue(Utils.matchesPossiblePattern("000AAT"));
		assertTrue(Utils.matchesPossiblePattern("99XX5"));
		assertTrue(Utils.matchesPossiblePattern("0AA6"));
		assertTrue(Utils.matchesPossiblePattern("0001AA1"));
		assertTrue(Utils.matchesPossiblePattern("07AH0"));
		assertTrue(Utils.matchesPossiblePattern("AA1456"));
		assertTrue(Utils.matchesPossiblePattern("KA123"));
		assertTrue(Utils.matchesPossiblePattern("TT12345"));
		assertTrue(Utils.matchesPossiblePattern("8787AAB"));
		assertTrue(Utils.matchesPossiblePattern("0XX7"));
		assertTrue(Utils.matchesPossiblePattern("12345"));
		assertTrue(Utils.matchesPossiblePattern("BB1234"));
		assertTrue(Utils.matchesPossiblePattern("0AAT"));
		
		assertFalse(Utils.matchesPossiblePattern("0AATA"));
		assertFalse(Utils.matchesPossiblePattern("ATA1456")); // FIXME correct it !!!
		assertFalse(Utils.matchesPossiblePattern("123456"));
		assertFalse(Utils.matchesPossiblePattern("12AA12"));
		assertFalse(Utils.matchesPossiblePattern("A1A1"));
		assertFalse(Utils.matchesPossiblePattern("1456A4"));
		assertFalse(Utils.matchesPossiblePattern("12A12"));
		assertFalse(Utils.matchesPossiblePattern("C35C"));
		assertFalse(Utils.matchesPossiblePattern("CC1457C"));
		assertFalse(Utils.matchesPossiblePattern("ATTC"));
		assertFalse(Utils.matchesPossiblePattern("1234SS7"));
		
	}

}
