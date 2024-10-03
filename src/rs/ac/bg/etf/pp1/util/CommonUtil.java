package rs.ac.bg.etf.pp1.util;



import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;


import rs.ac.bg.etf.pp1.ast.SyntaxNode;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

public class CommonUtil {
	
	private static Logger log = Logger.getLogger(CommonUtil.class);
	
	private static Map<String, MethodDescriptor> methods = new HashMap<>();
    private static final Set<Integer> validIOOperands;
    private static final Set<Integer> validReadObjTypes;
    
    static {
    	validIOOperands = new TreeSet<>();
    	validReadObjTypes = new TreeSet<>();
    	validIOOperands.add(Struct.Int);
		validIOOperands.add(Struct.Char);
		validIOOperands.add(Struct.Bool);
		validReadObjTypes.add(Obj.Var);
		validReadObjTypes.add(Obj.Elem);
		validReadObjTypes.add(Obj.Fld);	
    }
	
	private CommonUtil() {
	}
	
	public static Obj getObjByName(String name, int line) {
		Obj r = Tab.find(name);
		
		if(r == Tab.noObj) {
			log.warn("Obj with name: " + name + " not found! Line: " + line);
		}
		
		return r;
	}
	
	public static Obj getSymbolInScope(String name) {
		Obj obj = Tab.currentScope.findSymbol(name);
		if(Objects.nonNull(obj)) {
			return obj;
		}
		log.warn("Obj with name: " + name + " not found in current scope!");
		return null;
	}
	
	public static boolean objExist(String name) {
		Obj r = Tab.find(name);
		
		if(r == Tab.noObj) {
			return false;
		}
		
		return true;
	}

	public static MethodDescriptor getDeclaredMethod(String name) {
		return methods.get(name);
	}
	
	public static MethodDescriptor declareMethod(String name, List<Obj> params) {
		MethodDescriptor m = methods.get(name);
		if(Objects.isNull(m)) {
			m = new MethodDescriptor(name, params);
			methods.put(name, m);
		} else {
			m.setParams(params);;
			methods.put(name, m);
		}
		return m;
	}
	
	public static MethodDescriptor declareMethod(String name, MethodDescriptor m) {
		methods.put(name, m);
		return m;
	}
	
	public static boolean isValidIOOperand(int type) {
		return validIOOperands.contains(type);
	}
	
	public static boolean isValidReadObjType(int type) {
		return validReadObjTypes.contains(type);
	}
	
	public static void validateMethod(String name, List<Struct> actualParams) {
		MethodDescriptor m = methods.get(name);
		if(name.equals(CommonKeys.MAIN)) {
			return;
		}
		if(Objects.isNull(m)) {
			log.error("Nema metode sa tim imenom!");
		}
		if(m.getDeclaredParams().size()!=actualParams.size()) {
    		log.error("Broj argumenata se ne slaze sa brojem parametara");
    	}
		switch(name) {
			case Constants.ORD: 
			case Constants.CHR:
			case Constants.LEN:
				Struct parametar = actualParams.get(0);
				if((parametar.getKind()==m.getDeclaredParams().get(0).getKind() && !name.equals(Constants.LEN)) 
						|| (name.equals(Constants.LEN) && parametar.getKind()==m.getDeclaredParams().get(0).getKind() && (parametar.getElemType().getKind()==Struct.Int || parametar.getElemType().getKind()==Struct.Char))) {
				} else {
					log.error("Error in CommonUtil while validating method params, check with debug...");
				}
				break;
			default:
				for(int i=0;i<m.getDeclaredParams().size();i++) {
		    		Struct f = m.getDeclaredParams().get(i).getType();
		    		Struct a =actualParams.get(i);
		    		if(Objects.isNull(a) || !a.assignableTo(f)) {
		    			log.error("formalni parametar i actual nisu istog tipa");
		    			//TODO Create switch parser
		    			log.error("Passed type " + a.getKind() + " but expected type " + f.getKind());
		    		}
		    	}				
		}
	}
	
	public static void checkFunctionParamTypes(String functionName, SyntaxNode syntaxNode, List<Struct> actualParams) {
	  if (objExist(functionName)) {
		  Obj funcObj = getObjByName(functionName, 0);
		  
		  if (funcObj.getKind() != Obj.Meth || funcObj.getLevel() != actualParams.size()) {
		      return;
		  }
		  
        funcObj.getLocalSymbols().stream()
                .filter(localSymbol -> localSymbol.getFpPos() > 0)
                .filter(funcParam -> !funcParam.getType()
                        .equals(actualParams.get(funcParam.getFpPos() - 1)))
                .forEach(funcParam -> {
                    log.error("Function " + functionName + " Param missmatch!");
                });

	  }
	}
	
    public static boolean isDoubleDeclaration(String identifier, int level, boolean inMethod) {
        // not in symbol table or not in the same scope level
        if (!objExist(identifier)) {
            return false;
        }
        		
        if(getObjByName(identifier, -1).getLevel() != level) {
        	return false;
        }

        // check if method of the same name exists in the same class
        int objKind = getObjByName(identifier, -1).getKind();
        Struct currentType = getObjByName(identifier, -1).getType();
        Obj foundObj = getSymbolInScope(identifier);
        if (Objects.isNull(foundObj) || objKind == Obj.Meth && !currentType.equals(foundObj.getType()))
            return false;
        
        // same scope level
        // obj found in symbol table is not a class field or
        // current declaration is not method parameter
        return objKind != Obj.Fld || !inMethod;
    }

    public static void encodeFileToBase64(String inputFilePath, String outputFilePath) throws IOException {
        // Read the content of the input file
        FileInputStream fis = new FileInputStream(inputFilePath);
        byte[] inputBytes = new byte[fis.available()];
        fis.read(inputBytes);
        fis.close();

        // Encode the content to Base64
        String encodedString = Base64.getEncoder().encodeToString(inputBytes);

        // Write the Base64 encoded content to the output file
        FileWriter fw = new FileWriter(outputFilePath);
        fw.write(encodedString);
        fw.close();

        //System.out.println("File encoded to Base64 successfully!");
    }

    public static void decodeBase64ToFile(String inputFilePath, String outputFilePath) throws IOException {
        // Read the Base64 encoded content from the input file
        FileReader fr = new FileReader(inputFilePath);
        BufferedReader br = new BufferedReader(fr);
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();

        // Decode the Base64 encoded content
        byte[] decodedBytes = Base64.getDecoder().decode(sb.toString());

        // Write the decoded content to the output file
        FileOutputStream fos = new FileOutputStream(outputFilePath);
        fos.write(decodedBytes);
        fos.close();

        //System.out.println("File decoded from Base64 successfully!");
    }

}
