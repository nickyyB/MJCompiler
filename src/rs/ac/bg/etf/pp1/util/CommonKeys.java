package rs.ac.bg.etf.pp1.util;

import rs.etf.pp1.symboltable.concepts.Struct;

/*
 * @nbrkovic
 * Util class for constants
 * 
 */
public class CommonKeys {
	
	private CommonKeys() {}

	public static String MAIN = "main";
	public static String NAMESPACE = "namespace";
	public static String BOOLEAN = "bool";
	public static String ORD = "ord";
	public static String CHR = "chr";
	public static String LEN  = "len";
	
	public static int NAMESPACE_KIND = 7;
	
	
	public static Struct boolType = new Struct(Struct.Bool);
}
