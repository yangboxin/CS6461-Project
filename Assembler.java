import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
/**
 * Assembler translates the instruction to its octal representation with the
 * address of this instruction
 * 
 * 
 * @Boxin Yang
 * @Feb 1
 */
public class Assembler
{
    private int address;    
    private ArrayList<String> instructions;
    private ArrayList<String> parsedInstru;
    private HashMap<String, String> Ins2Bi;
    private HashSet<String>  Miscellaneous;
    private HashSet<String>  LoadnStore;
    private HashSet<String>  Transfer;
    private HashSet<String>  ArithLogical;
    private HashSet<String>  RegisterOp;
    private HashSet<String>  IO;
    private HashSet<String>  FloatPVec;
    
    public Assembler()
    {
        //initialize the address and 2 arrays and the dictionary
        address = 0;
        instructions = new ArrayList<String>();     
        parsedInstru = new ArrayList<String>(); 
        Ins2Bi = new HashMap<String, String>();
    }

    public void loadDict(){
        //load the hashtable of instructions with its opcode
        Ins2Bi.put("HLT","000000");
        Ins2Bi.put("TRAP","100101");
        Ins2Bi.put("LDR","000001");
        Ins2Bi.put("STR","000010");
        Ins2Bi.put("LDA","000011");
        Ins2Bi.put("LDX","000100");
        Ins2Bi.put("STX","000101");
        Ins2Bi.put("SETCCE","100100");
        Ins2Bi.put("JZ","000110");
        Ins2Bi.put("JNE","000111");
        Ins2Bi.put("JCC","001000");
        Ins2Bi.put("JMA","001001");
        Ins2Bi.put("JSR","001010");
        Ins2Bi.put("RFS","001011");
        Ins2Bi.put("SOB","001100");
        Ins2Bi.put("JGE","001101");
        Ins2Bi.put("AMR","001110");
        Ins2Bi.put("SMR","001111");
        Ins2Bi.put("AIR","010000");
        Ins2Bi.put("SIR","010001");
        Ins2Bi.put("MLT","010010");
        Ins2Bi.put("DVD","010011");
        Ins2Bi.put("TRR","010100");
        Ins2Bi.put("AND","010101");
        Ins2Bi.put("ORR","010110");
        Ins2Bi.put("NOT","010111");
        Ins2Bi.put("SRC","011000");
        Ins2Bi.put("RRC","011001");
        Ins2Bi.put("IN","011010");
        Ins2Bi.put("OUT","011011");
        Ins2Bi.put("CHK","011100");
        Ins2Bi.put("FADD","011101");
        Ins2Bi.put("FSUB","011110");
        Ins2Bi.put("VADD","011111");
        Ins2Bi.put("VSUB","100000");
        Ins2Bi.put("CNVRT","100001");
        Ins2Bi.put("LDFR","100010");
        Ins2Bi.put("STFR","100011");
    }
    
    public void readFile(String fileName){
        //read from the input file and put each line of instruction into the array
        try {
            BufferedReader in = new BufferedReader(new FileReader(fileName));
            String str;
            while ((str = in.readLine()) != null) {
                instructions.add(str);
            }
        } catch (IOException e) {
        }
    }
    
    public String BinaryToOctal(String binary){
        //convert the binary string to octal string from the lowest digit
        StringBuilder res=new StringBuilder();
        for(int i=binary.length();i>1;i-=3){
            String binarySub3=binary.substring(Math.max(i-3,1),i);
            res.insert(0,Integer.toString(Integer.parseInt(binarySub3,2),8));
        }
        res.insert(0,binary.charAt(0));
        return res.toString();
    }
    
    public void parse(){
        StringBuffer sBuffer = new StringBuffer("LOC");
        if(!instructions.get(0).contentEquals(sBuffer)){
            address = 6;//first five address reserved for special use
        }
        for(String instruction:instructions){
            String[] tmpArr=instruction.split("\\s+");
            if(tmpArr[0]=="LOC"){//set the address if specified
                address = Integer.parseInt(tmpArr[1]);
            }
            else if(tmpArr[0]=="Data"){//if Data, straight to the parsed results
                parsedInstru.add(String.format("%06o",address) + " " + String.format("%06o",tmpArr[1]));
            }
            else{
                String indirectFlag="0";//default indirect addressing set to 0
                String[] params=tmpArr[1].split(",");
                if(params.length>3){//check if there is an indirect addressing
                    indirectFlag=params[3];
                }
                String tmp="";
                for(String p:params){//convert the parameters to its binary form
                    int decimal=Integer.parseInt(p);
                    if(p.length()==2){
                        tmp+=indirectFlag;
                    }
                    tmp+=Integer.toBinaryString(decimal);
                }
                String opcode=Ins2Bi.get(tmpArr[0]);
                System.out.println(tmp);
                tmp=String.format("%010b",tmp);
                String converted=BinaryToOctal(opcode+tmp);//convert the binary form to the final octal form
                parsedInstru.add(String.format("%06o",address)+" "+converted);
                address++;
            }
        }
    }
    
    public void writeListingFile(String fileName){
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
            for(String p:parsedInstru){
                out.write(p+instructions+"\n");
            }
            out.close();
        } catch (IOException e) {
        }
    }
    
    public void writeLoadFile(String fileName){
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
            for(String p:parsedInstru){
                out.write(p+"\n");
            }
            out.close();
        } catch (IOException e) {
        }
    }
    
    public void main(){
        readFile("./test case.txt");
        loadDict();
        parse();
        writeListingFile("./ListingFile.txt");
        writeLoadFile("./LoadFile.txt");
    }
}
