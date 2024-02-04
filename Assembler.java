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
    private HashMap<String, String> rxLookup;
    private HashSet<String>  Miscellaneous;
    private HashSet<String>  LoadnStore;
    private HashSet<String>  Transfer;
    private HashSet<String>  ArithLogical;
    private HashSet<String>  RegisterOp;
    private HashSet<String>  ShiftnRotate;
    private HashSet<String>  IO;
    private HashSet<String>  FloatPVec;
    
    public Assembler()
    {
        //initialize the address and 2 arrays and the dictionaries
        address = 0;
        instructions = new ArrayList<String>();     
        parsedInstru = new ArrayList<String>(); 
        Ins2Bi = new HashMap<String, String>();
        Miscellaneous = new HashSet<String>();
        LoadnStore = new HashSet<String>();
        Transfer = new HashSet<String>();
        ArithLogical = new HashSet<String>();
        RegisterOp = new HashSet<String>();
        ShiftnRotate = new HashSet<String>();
        IO = new HashSet<String>();
        FloatPVec = new HashSet<String>();
        rxLookup = new HashMap<String, String>();
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
        //load the registers/Indexing lookup binary form
        rxLookup.put("0","00");
        rxLookup.put("1","01");
        rxLookup.put("2","10");
        rxLookup.put("3","11");
        //load different type of instructions
        Miscellaneous.add("HLT");
        Miscellaneous.add("TRAP");
        LoadnStore.add("LDR");
        LoadnStore.add("STR");
        LoadnStore.add("LDA");
        LoadnStore.add("LDX");
        LoadnStore.add("STX");
        Transfer.add("SETCCE");
        Transfer.add("JZ");
        Transfer.add("JNE");
        Transfer.add("JCC");
        Transfer.add("JMA");
        Transfer.add("JSR");
        Transfer.add("RFS");
        Transfer.add("SOB");
        Transfer.add("JGE");
        ArithLogical.add("AMR");
        ArithLogical.add("SMR");
        ArithLogical.add("AIR");
        ArithLogical.add("SIR");
        RegisterOp.add("MLT");
        RegisterOp.add("DVD");
        RegisterOp.add("TRR");
        RegisterOp.add("AND");
        RegisterOp.add("ORR");
        RegisterOp.add("NOT");
        ShiftnRotate.add("SRC");
        ShiftnRotate.add("RRC");
        IO.add("IN");
        IO.add("OUT");
        IO.add("CHK");
        FloatPVec.add("FADD");
        FloatPVec.add("FSUB");
        FloatPVec.add("VADD");
        FloatPVec.add("VSUB");
        FloatPVec.add("CNVRT");
        FloatPVec.add("LDFR");
        FloatPVec.add("STFR");
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
    
    public void formatAndConvertInstruction(String opcode, String r, String x, String indirectAdd, String Add){
        String I=indirectAdd;
        String opcodeBi=Ins2Bi.get(opcode);
        String add=String.format("%5s",Integer.toBinaryString(Integer.parseInt(Add))).replace(' ', '0');
        String res=BinaryToOctal(opcodeBi+r+x+I+add);
        parsedInstru.add(String.format("%06o",address)+" "+res);
    }
    
    public void handleMiscellaneousInstruction(String[] instruction){
        String opcode=instruction[0];
        if(opcode.equals("HLT")){
            //Halt
            parsedInstru.add(String.format("%06o",address)+" "+"000000");
        }
        else{
            //Trap
            String code=Integer.toString(Integer.parseInt(instruction[1],2));
            code = String.format("%4s", code).replace(' ', '0');
            String res=BinaryToOctal("100101000000"+code);
            parsedInstru.add(String.format("%06o",address)+" "+res);
        }
    }
    
    public void handleLoadStoreInstruction(String[] instruction){
        String opcode=instruction[0];
        String indirectAdd="0";
        String[] sep=instruction[1].split(",");
        if(opcode.equals("LDX")||opcode.equals("STX")){
            formatAndConvertInstruction(opcode,"00",rxLookup.get(sep[0]),sep.length>=3?sep[2]:"0",sep[1]);
        }
        else{
            formatAndConvertInstruction(opcode,rxLookup.get(sep[0]),rxLookup.get(sep[1]),sep.length>=4?sep[3]:"0",sep[2]);
        }
    }
    
    public void handleTransferInstruction(String[] instruction){
        String opcode=instruction[0];
        String indirectAdd="0";
        String[] sep=instruction[1].split(",");
        if(opcode.equals("JZ")||opcode.equals("JNE")||opcode.equals("JMA")||opcode.equals("JSR")){
            formatAndConvertInstruction(opcode,"00",rxLookup.get(sep[0]),sep.length>=3?sep[2]:"0",sep[1]);
        }
        else if(opcode.equals("SETCCE")){
            formatAndConvertInstruction("SETCCE", rxLookup.get(sep[0]),"00","0","00000");
        }
        else if(opcode.equals("RFS")){
            formatAndConvertInstruction("RFS","00","00","0",sep[0]);
        }
        else{
            formatAndConvertInstruction(opcode,rxLookup.get(sep[0]),rxLookup.get(sep[1]),sep.length>=4?sep[3]:"0",sep[2]);
        }
    }
    
    public void handleArithLogicalInstruction(String[] instruction){
        String opcode=instruction[0];
        String indirectAdd="0";
        String[] sep=instruction[1].split(",");
        if(opcode.equals("AMR")||opcode.equals("SMR")){
            formatAndConvertInstruction(opcode,rxLookup.get(sep[0]),rxLookup.get(sep[1]),sep.length>=4?sep[3]:"0",sep[2]);
        }
        else{
            formatAndConvertInstruction(opcode, rxLookup.get(sep[0]),"00","0",sep[1]);
        }
    }
    
    public void handleRegisterOpInstruction(String[] instruction){
        String opcode=instruction[0];
        String indirectAdd="0";
        String[] sep=instruction[1].split(",");
        if(opcode.equals("NOT")){
            formatAndConvertInstruction(opcode,rxLookup.get(sep[0]),"00","0","00000");
        }
        else{
            formatAndConvertInstruction(opcode, rxLookup.get(sep[0]),rxLookup.get(sep[1]),"0","00000");
        }
    }
    
    public void handleShiftnRotateInstruction(String[] instruction){
        String opcode=instruction[0];
        String indirectAdd="0";
        String[] sep=instruction[1].split(",");
        formatAndConvertInstruction(opcode, rxLookup.get(sep[0]),sep[3]+sep[2],"0",sep[1]);
    }
    
    public void handleIOInstruction(String[] instruction){
        String opcode=instruction[0];
        String indirectAdd="0";
        String[] sep=instruction[1].split(",");
        formatAndConvertInstruction(opcode, rxLookup.get(sep[0]),"00","0",sep[1]);
    }
    
    public void handleFloatPVecInstruction(String[] instruction){
        String opcode=instruction[0];
        String indirectAdd="0";
        String[] sep=instruction[1].split(",");
        formatAndConvertInstruction(opcode, rxLookup.get(sep[0]),rxLookup.get(sep[1]),sep.length>=4?sep[3]:"0",sep[2]);
    }
    
    public void parse(){
        StringBuffer sBuffer = new StringBuffer("LOC");
        if(!instructions.get(0).contentEquals(sBuffer)){
            address = 6;//first five address reserved for special use
        }
        for(String instruction:instructions){
            String[] tmpArr=instruction.split("\\s+");
            if(tmpArr[0].equals("LOC")){//set the address if specified
                address = Integer.parseInt(tmpArr[1]);
            }
            else if(tmpArr[0].equals("Data")){//if Data, straight to the parsed results
                if(tmpArr[1].equals("End")){
                    tmpArr[1]="01024";
                }
                parsedInstru.add(String.format("%06o",address) + " " + String.format("%06o",Integer.parseInt(tmpArr[1])));
                address++;
            }
            else {
            // determine which category this instruction belongs to
            if (Miscellaneous.contains(tmpArr[0])) {
                // Handle Miscellaneous instructions
                handleMiscellaneousInstruction(tmpArr);
            } else if (LoadnStore.contains(tmpArr[0])) {
                // Handle Load and Store instructions
                handleLoadStoreInstruction(tmpArr);
            } else if (Transfer.contains(tmpArr[0])) {
                // Handle Transfer instructions
                handleTransferInstruction(tmpArr);
            } else if (ArithLogical.contains(tmpArr[0])) {
                // Handle Arithmetic and Logical instructions
                handleArithLogicalInstruction(tmpArr);
            } else if (RegisterOp.contains(tmpArr[0])) {
                // Handle Register Operation instructions
                handleRegisterOpInstruction(tmpArr);
            } else if (ShiftnRotate.contains(tmpArr[0])) {
                // Handle Shift and Rotate instructions
                handleShiftnRotateInstruction(tmpArr);
            } else if (IO.contains(tmpArr[0])) {
                // Handle I/O instructions
                handleIOInstruction(tmpArr);
            } else if (FloatPVec.contains(tmpArr[0])) {
                // Handle Floating Point and Vector instructions
                handleFloatPVecInstruction(tmpArr);
            } else {
                System.out.println("Unknown instruction type: " + tmpArr[0]);
            }
                address++; // Assuming each instruction occupies one address space
            }
        }
    }
    
    public void writeListingFile(String fileName){
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
            for(int i=0;i<parsedInstru.size();i++){
                out.write(parsedInstru.get(i)+"   "+instructions.get(i)+"\n");
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
