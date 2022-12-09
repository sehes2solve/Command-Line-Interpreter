import Exceptions.MissingFileToOutput;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

class Parser {
    private static ArrayList<String> args; // Will be filled by arguments extracted by parse method
    private static String cmd; // Will be filled by the command extracted by parse method
    static BufferedWriter file;
    static void parse(String input) throws IOException, MissingFileToOutput {
        input=input.replaceAll("\\\\ ","#");
        // for every command reset the bufferedwriter
        file=null;
        String[] list=input.split(" ");
        cmd=list[0];
        args=new ArrayList<>();
        /*
         * if the user wants to enter a space in the parameter then
         * he should add '\' before it
         */
        for(int i=1 ; i<list.length ;i++){
            list[i]=list[i].replaceAll("#"," ");
        }
        for(int i=1 ; i<list.length ;i++){
            String cur = list[i];
            if(cur.equals(">")){
                if(i<list.length-1) {
                    file = new BufferedWriter(new FileWriter(Terminal.getPath(list[i + 1]), false));
                    i++;
                }else{
                    throw new MissingFileToOutput("missing the name of the file to output ");
                }
            }else if(cur.equals(">>")){
                if(i<list.length-1) {
                    file = new BufferedWriter(new FileWriter(Terminal.getPath(list[i + 1]), true));
                    i++;
                }else {
                    throw new MissingFileToOutput("missing the name of the file to output ");
                }
            }else{
                args.add(cur);
            }
        }
    }
    static String getCmd(){
        return cmd;
    }
    static ArrayList<String> getArguments(){
        return args;
    }
}
