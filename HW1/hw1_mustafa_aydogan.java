import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class hw1_mustafa_aydogan {

    public static void main(String[] args) {
        
        String line;

        try{
            // Creating file, Writer and Scanner
            File input  = new File(args[0]);
            File output = new File(args[1]);
            output.createNewFile();

            Scanner scan = new Scanner(input);
            FileWriter outputWriter = new FileWriter(output);

            int lineNumber = 0;

            // this while loop controls that there is an invalid identifier or operator
            // if there is an invlid operator or identifier, prints an error message and exits
            while(scan.hasNextLine()){
                line = scan.nextLine();
                lineNumber++;
                ArrayList<String> list = splitLexeme(line);

                for(int i=0; i<list.size(); i++){

                    if(list.get(i).equals("")) continue;
    
                    if(token(list.get(i)).equals("Unknown operator!")){
                        System.out.println("Error: Unknown operator! \"" + list.get(i) + "\" at line: " + lineNumber);
                        outputWriter.close();
                        scan.close();
                        System.exit(0);
                    }
                    
                    if(token(list.get(i)).equals("Unknown identifier!")){
                        System.out.println("Error: Unknown identifier! \"" + list.get(i) + "\" at line: " + lineNumber);
                        outputWriter.close();
                        scan.close();
                        System.exit(0);
                    }
                }
            }

            scan.close();
            scan = new Scanner(input);

            // reads the file and write to output file line by line
            while(scan.hasNextLine()){
                line = scan.nextLine();
                writeOutput(outputWriter, line);
            }

            outputWriter.close();
            scan.close();
        }catch(Exception e){
            System.out.println("File i/o error!");
        }
    }

    // ---------------- beginning of the methods -----------------------------------

    // this method takes a string and split the lexemes, then write the output file
    public static void writeOutput(FileWriter outputWriter, String line){

        try{
            ArrayList<String> list = splitLexeme(line);

            for(int i=0; i<list.size(); i++){

                if(list.get(i).equals("")) continue;

                outputWriter.write("Next token is " + token(list.get(i)) + " Next lexeme is " + list.get(i)+"\n");
            }    

        }catch(Exception e){
            System.out.println("File i/o error2!");
        }
    }

    // this method takes a string and split the lexemes and adds the an arraylist
    public static ArrayList<String> splitLexeme(String line){
        String[] lexemes = line.split(" ");
        ArrayList<String> list = new ArrayList<>();

        for(int i=0; i<lexemes.length; i++){
            list.add(lexemes[i]);
        }  

        editTheList(list, "<=");
        editTheList(list, ">=");
        editTheList(list, "(");
        editTheList(list, ")");
        editTheList(list, ";");
        editTheList(list, "{");
        editTheList(list, "}");
        editTheList(list, "-");
        editTheList(list, "+");
        editTheList(list, "*");
        editTheList(list, "/");
        editTheList(list, "<", "<=");
        editTheList(list, ">", ">=");
        editTheListAssign(list, "=");

        return list;
    }

    // this method takes an arraylist and delimiter
    // update the arraylist according to delimiter
    public static void editTheList(ArrayList<String> list, String delimiter){

        try {
            int i = 0;
            while(i < list.size()) {
                String str = list.get(i);
                int k = str.indexOf(delimiter);
                int length = delimiter.length();
                
                if(k != -1){
                    list.remove(i);
                    list.add(i, str.substring(k+length));
                    list.add(i, str.substring(k,k+length));
                    list.add(i, str.substring(0,k));
                    i++;
                    i++;
                }
                
                i++;
            }
        } catch (Exception e) {
            System.out.println("Error!");
        }
    }

    // overloading for "<=" and ">="
    public static void editTheList(ArrayList<String> list, String delimiter, String different){

        try {
            int i = 0;
            while(i < list.size()) {
                String str = list.get(i);
                int k = str.indexOf(delimiter);
                int l = str.indexOf(different);
                int length = delimiter.length();
                
                if(k != -1 && k-l != 0){
                    list.remove(i);
                    list.add(i, str.substring(k+length));
                    list.add(i, str.substring(k,k+length));
                    list.add(i, str.substring(0,k));
                    i = i+2;
                }
                
                i++;
            }
        } catch (Exception e) {
            System.out.println("Error!");
        }
    }

    // this method and editTheList method are similar
    // this method wrote for "="
    public static void editTheListAssign(ArrayList<String> list, String delimiter){

        try {
            int i = 0;
            while(i < list.size()) {
                String str = list.get(i);
                int k = str.indexOf(delimiter);
                int l = str.indexOf("<=");
                int m = str.indexOf(">=");
                int length = delimiter.length();

                int max = (l>m) ? l : m;
                
                if(max == -1 && k != -1){
                    list.remove(i);
                    list.add(i, str.substring(k+length));
                    list.add(i, str.substring(k,k+length));
                    list.add(i, str.substring(0,k));
                    i = i+2;
                }

                else if(k != -1 && k-l != 1 && k-m != 1){
                    list.remove(i);
                    list.add(i, str.substring(k+length));
                    list.add(i, str.substring(k,k+length));
                    list.add(i, str.substring(0,k));
                    i = i+2;
                }
                
                i++;
            }
        } catch (Exception e) {
            System.out.println("Error!");
        }
    }

    // this method takes a lexeme and return the token
    public static String token(String lexeme) {

        String[] arrayTok = new String[22];
        String[] arrayLex = new String[18];
        
        int index = 21;

        arrayTok[0] = "FOR_STATEMENT";
        arrayTok[1] = "LPARANT";
        arrayTok[2] = "RPARANT";
        arrayTok[3] = "INT_TYPE";
        arrayTok[4] = "CHAR_TYPE";
        arrayTok[5] = "ASSIGNM";
        arrayTok[6] = "SEMICOLON";
        arrayTok[7] = "GREATER";
        arrayTok[8] = "LESS";
        arrayTok[9] = "GRE_EQ";
        arrayTok[10] = "LESS_EQ";
        arrayTok[11] = "LCURLYB";
        arrayTok[12] = "RCURLYB";
        arrayTok[13] = "RETURN_STMT";
        arrayTok[14] = "SUBT";
        arrayTok[15] = "DIV";
        arrayTok[16] = "MULT";
        arrayTok[17] = "ADD";
        arrayTok[18] = "identifier";
        arrayTok[19] = "INT_LIT";
        arrayTok[20] = "Unknown operator!";
        arrayTok[21] = "Unknown identifier!";

        arrayLex[0] = "for";
        arrayLex[1] = "(";
        arrayLex[2] = ")";
        arrayLex[3] = "int";
        arrayLex[4] = "char";
        arrayLex[5] = "=";
        arrayLex[6] = ";";
        arrayLex[7] = ">";
        arrayLex[8] = "<";
        arrayLex[9] = ">=";
        arrayLex[10] = "<=";
        arrayLex[11] = "{";
        arrayLex[12] = "}";
        arrayLex[13] = "return";
        arrayLex[14] = "-";
        arrayLex[15] = "/";
        arrayLex[16] = "*";
        arrayLex[17] = "+";

        if(isInteger(lexeme)){
            return arrayTok[19];
        }

        for (int i = 0; i < arrayLex.length; i++) {
            if(arrayLex[i].equals(lexeme)){
                return arrayTok[i];
            }
        }

        if(isIdentifier(lexeme)){
            return arrayTok[18];
        }

        if(lexeme.length() == 1 && isIdentifier(lexeme) == false){
            return arrayTok[20];
        }
        
        return arrayTok[index];
    }

    // this method controls if input is an integer
    public static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;

        } catch (Exception e) {
            return false;
        }
    }

    // this method controls if input is an identifier
    public static boolean isIdentifier(String str) {

        String validCharacters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ()=;<>{}-/*+";

        if(str.length() == 1 && (validCharacters.indexOf(str) > -1)){
            return true;
        }
        
        return false;
    }
}

//----------------------- end of the file -----------------------------