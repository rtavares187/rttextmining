package br.cefetrj.ppcic.mt.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;

/**
 * 
 * @author rtavares
 *
 */
public class TextUtil {
	
	private static String STOP_WORDS = "a, about, above, across, after, again, against, all, almost, "  
								+ "alone, along, already, also, although, always, am, among, an, "  
								+ "and, another, any, anybody, anyone, anything, anywhere, are, " 
								+ "area, areas, aren't, around, as, ask, asked, asking, asks, at, " 
								+ "away, b, back, backed, backing, backs, be, became, because, "  
								+ "become, becomes, been, before, began, behind, being, beings, " 
								+ "below, best, better, between, big, both, but, by, c, came, can, " 
								+ "cannot, can't, case, cases, certain, certainly, clear, clearly, "  
								+ "come, could, couldn't, d, did, didn't, differ, different, differently, "  
								+ "do, does, doesn't, doing, done, don't, down, downed, downing, downs, "  
								+ "during, e, each, early, either, end, ended, ending, ends, enough, even, "  
								+ "evenly, ever, every, everybody, everyone, everything, everywhere, f, "  
								+ "face, faces, fact, facts, far, felt, few, find, finds, first, for, four, "  
								+ "from, full, fully, further, furthered, furthering, furthers, g, gave, "  
								+ "general, generally, get, gets, give, given, gives, go, going, good, "  
								+ "goods, got, great, greater, greatest, group, grouped, grouping, groups, "  
								+ "h, had, hadn't, has, hasn't, have, haven't, having, he, he'd, he'll, her, "
								+ "here, here's, hers, herself, he's, high, higher, highest, him, himself, his, "
								+ "how, however, how's, i, i'd, if, i'll, i'm, important, in, interest, interested, "
								+ "interesting, interests, into, is, isn't, it, its, it's, itself, i've, j, just, "
								+ "k, keep, keeps, kind, knew, know, known, knows, l, large, largely, last, "
								+ "later, latest, least, less, let, lets, let's, like, likely, long, longer, "
								+ "longest, m, made, make, making, man, many, may, me, member, members, men, "
								+ "might, more, most, mostly, mr, mrs, much, must, mustn't, my, myself, n, "
								+ "necessary, need, needed, needing, needs, never, new, newer, newest, next, no, "
								+ "nobody, non, noone, nor, not, nothing, now, nowhere, number, numbers, o, of, "
								+ "off, often, old, older, oldest, on, once, one, only, open, opened, opening, "
								+ "opens, or, order, ordered, ordering, orders, other, others, ought, our, ours, "
								+ "ourselves, out, over, own, p, part, parted, parting, parts, per, perhaps, place, "
								+ "places, point, pointed, pointing, points, possible, present, presented, "
								+ "presenting, presents, problem, problems, put, puts, q, quite, r, rather, "
								+ "really, right, room, rooms, s, said, same, saw, say, says, second, "
								+ "seconds, see, seem, seemed, seeming, seems, sees, several, shall, shan't,"
								+ " she, she'd, she'll, she's, should, shouldn't, show, showed, showing, shows, "
								+ "side, sides, since, small, smaller, smallest, so, some, somebody, someone, "
								+ "something, somewhere, state, states, still, such, sure, t, take, taken, than, "
								+ "that, that's, the, their, theirs, them, themselves, then, there, therefore, "
								+ "there's, these, they, they'd, they'll, they're, they've, thing, things, think, "
								+ "thinks, this, those, though, thought, thoughts, three, through, thus, to, today, "
								+ "together, too, took, toward, turn, turned, turning, turns, two, u, under, until, "
								+ "up, upon, us, use, used, uses, v, very, w, want, wanted, wanting, wants, was, "
								+ "wasn't, way, ways, we, we'd, well, we'll, wells, went, were, we're, weren't, "
								+ "we've, what, what's, when, when's, where, where's, whether, which, while, who, "
								+ "whole, whom, who's, whose, why, why's, will, with, within, without, won't, work, "
								+ "worked, working, works, would, wouldn't, x, y, year, years, yes, yet, you, you'd, "
								+ "you'll, young, younger, youngest, your, you're, yours, yourself, yourselves, "
								+ "you've, z";
	
	public static List<String> getStopWordList(){
		
		return Arrays.asList((STOP_WORDS + ", ").split(","));
		
	}
	
	public static List<String> removeTokens(List<String> lstOriginal, List<String> lstRemove){
		
		for(int i = 0; i < lstOriginal.size(); i++)
			lstOriginal.set(i, lstOriginal.get(i).trim().toLowerCase());
		
		for(int i = 0; i < lstRemove.size(); i++)
			lstRemove.set(i, lstRemove.get(i).trim().toLowerCase());
		
		List<String> lstRet = new ArrayList<String>();
		lstRet.addAll(lstOriginal);
		
		lstRet.removeAll(lstRemove);
		
		return lstRet;
		
	}
	
	public static String clean(String s){
		
		return s.replaceAll("[\\p{P}&&[^\u0027]]", "");
		
	}
	
	public static List<String> getContentList(String s){
		
		return Arrays.asList(s.split(" "));
		
	}
	
	public static String formatTextToLength(String s, int fixedLength){
		
		int sL = s.length();
		int cL = fixedLength - sL;
		
		for(int i = 0; i < cL; i++)
			s += " ";
		
		return s;
		
	}
	
	public static void writePdf(String filePath, String header, String text){
		
		try{
			
			File file = new File(filePath);
			if(file.exists())
				file.delete();
				
			file.createNewFile();
			
			PdfWriter writer = new PdfWriter(filePath);
			PdfDocument pdf = new PdfDocument(writer);
			
			PdfFont font = PdfFontFactory.createFont(FontConstants.TIMES_ROMAN);
			
			Text iHeader = new Text(header).setFont(font);
			iHeader.setFontSize(10);
			
			Text iText = new Text(text).setFont(font);
			iText.setFontSize(8);
			
			Document document = new Document(pdf, PageSize.A4.rotate());
			document.add(new Paragraph().add(iHeader).add(iText));
			document.close();
			
		}catch(Exception e){
			
			e.printStackTrace();
			
		}
		
	}
	
}
