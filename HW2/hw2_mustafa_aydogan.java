import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class hw2_mustafa_aydogan {
    public static void main(String[] args) {
        
        // takes the original input and make some changes
        String originalInput = editInput(args[0]);
        int length = originalInput.length();

        // defining the default action 
        String action = "Start";
        int actionInt = 0;
        
        // creating stack and default push
        ArrayList<String> stack = new ArrayList<String>();
        push(stack, "0");

        // creating input stack
        ArrayList<String> input = new ArrayList<String>();
        toList(input, originalInput);

        // the first element of the stacks
        int stackPop = Integer.parseInt(peek(stack));
        String inputPop = peek(input);
        
        // assigning the first action
        action = parseTable(inputPop, stackPop);
        actionInt = parseTableInt(inputPop, stackPop);
        
        try{

            // creating the output file
            File output = new File(args[1]);
            output.createNewFile();

            // creating FileWriter object
            FileWriter outputWriter = new FileWriter(output);

            // write the first line into the output file
            outputWriter.write("Stack");
            outputWriter.write(spaces(35));
            outputWriter.write("Input");
            outputWriter.write(spaces(length));
            outputWriter.write("Action\n");
            
            // write the second line into the output file
            print(stack, input, action, actionInt, outputWriter, length);

            // while the action is not Accept or ERROR
            while(action.equals("Accept") == false && action.equals("ERROR") == false){

                // actions happen here
                process(stack, input, action, actionInt);

                // update stackPop, inputPop and action
                stackPop = Integer.parseInt(peek(stack));
                inputPop = peek(input);
                    
                action = parseTable(inputPop, stackPop);
                actionInt = parseTableInt(inputPop, stackPop);

                // writing the file
                print(stack, input, action, actionInt, outputWriter, length);
            }

            // if last action is ERROR
            if(action.equals("ERROR")){
                System.out.println("Error occured.");
            }

            else if(action.equals("Accept")){
                System.out.println("The input has been parsed successfully.");
            }

            // FileWriter closed.
            outputWriter.close();

        }catch(Exception e){
            System.out.println("Error occured.");
        }
    }

    //-------------------------------------  beginning of the methods  -----------------------------

    // actions occurs into this method
    public static void process(ArrayList<String> stack, ArrayList<String> input, String action, int actionInt) {

        // if the action is the Shift, this statement runs
        if(action.equals("Shift")){
            shift(stack, input, action, actionInt);
        }

        //if the action is the Reduce, this statement runs
        else if(action.equals("Reduce")){
            reduce(stack, input, action, actionInt);
        }
    }

    // the reduce method take stacks and action
    public static void reduce(ArrayList<String> stack, ArrayList<String> input, String action, int actionInt) {
        if(!action.equals("Reduce")){
            return;
        }

        // remove the first element of stack
        pop(stack);

        // state will use in GOTO
        int state = Integer.parseInt(stack.get(1));
        int index = 0;
        String first = "";
        String second = "";

        // elements, which is in the stack, are written into a string
        String mystring = stackToString(stack);

        if(actionInt == 1){
            index = mystring.indexOf("E")+1;
            first = mystring.substring(0, index);
            second = mystring.substring(index);
            first = first.replaceAll("T.+.E", "E");            // replace the string
            mystring = first + second;
            toList(stack, mystring);                            // the new stack string translate the stack
            state = Integer.parseInt(stack.get(1));             // the state is updated
            push(stack, parseTableInt("E", state)+"");          // after the reducing, GOTO write to stack
            return;                                             // quit the method
        }

        if(actionInt == 2){
            index = mystring.indexOf("T")+1;
            first = mystring.substring(0, index);
            second = mystring.substring(index);
            first = first.replaceAll("T", "E");
            mystring = first + second;
            toList(stack, mystring);
            state = Integer.parseInt(stack.get(1));
            push(stack, parseTableInt("E", state)+"");
            return;
        }

        if(actionInt == 3){
            index = mystring.indexOf("T")+1;
            first = mystring.substring(0, index);
            second = mystring.substring(index);
            first = first.replaceAll("F.*.T", "T");
            mystring = first + second;
            toList(stack, mystring);
            state = Integer.parseInt(stack.get(1));
            push(stack, parseTableInt("T", state)+"");
            return;
        }

        if(actionInt == 4){
            index = mystring.indexOf("F")+1;
            first = mystring.substring(0, index);
            second = mystring.substring(index);
            first = first.replaceAll("F", "T");
            mystring = first + second;
            toList(stack, mystring);
            state = Integer.parseInt(stack.get(1));
            push(stack, parseTableInt("T", state)+"");
            return;
        }

        if(actionInt == 5){
            mystring = mystring.replace(')', '>');
            mystring = mystring.replace('(', '<');
            index = mystring.indexOf("<")+1;
            first = mystring.substring(0, index);
            second = mystring.substring(index);
            first = first.replaceAll("> . E . <", "F"); 
            mystring = first + second;  
            mystring = mystring.replace("<", "(");
            mystring = mystring.replace(">", ")");
            toList(stack, mystring);
            state = Integer.parseInt(stack.get(1));
            push(stack, parseTableInt("F", state)+"");
            return;
        }

        if(actionInt == 6){
            index = mystring.indexOf("id")+2;
            first = mystring.substring(0, index);
            second = mystring.substring(index);
            first = first.replaceAll("id", "F");
            mystring = first + second;
            toList(stack, mystring);
            state = Integer.parseInt(stack.get(1));
            push(stack, parseTableInt("F", state)+"");
            return;
        }        
    }

    // the shift meethod take the stack and action
    // the method pushs the elements to stack
    private static void shift(ArrayList<String> stack, ArrayList<String> input, String action, int actionInt) {
        String inputPop = pop(input);
        
        push(stack, inputPop);
        push(stack, actionInt+"");
    }

    // this method takes a list and retuns the first elements, not remove read only
    public static String peek(ArrayList<String> list){
        String peek = list.get(0);

        return peek;
    }

    // this method takes a list and removes and return first element
    public static String pop(ArrayList<String> list){
        String peek = list.get(0);

        // if the first element of list is "0" or "$" not remove, return only
        if(peek.equals("$") == false && peek.equals("0") == false){
            list.remove(0);
        }

        return peek;
    }

    // the push methods take a list and an elements, adds the element into beginning of the list
    public static void push(ArrayList<String> list, String element) {
        list.add(0, element);
    }

    // this method takes a list and a string, then all elements of the string adds the list
    // if the list is not empty, delete all elements and continue
    public static void toList(ArrayList<String> list, String input) {
        String[] array = input.split(" ");

        // delete all elements
        int size = list.size();
        for (int i = 0; i < size; i++) {
            list.remove(0);
        }
        
        // add the elements into list
        for (int i = 0; i < array.length; i++) {
            if(array[i].equals("")) continue;
            list.add(array[i]);
        }        
    }

    // this method write the output into a external file
    public static void print(ArrayList<String> stack, ArrayList<String> input, String action, int actionInt, FileWriter outputWriter, int length ) throws IOException {

        String string = "";
        int size = stack.size();

        // create a string for the Stack column 
        int k = 40;
        for (int i = 0; i<k; i++) {
            if(i<size){
                string = string + stack.get(size-1-i);
                k = k - stack.get(size-1-i).length() + 1;
            }

            else{
                string = string + " ";
            }
        }
        
        size = input.size();

        // create a string for the Input column 
        k = length+1;
        
        for (int i = 0; i<k; i++) {
            if(i<size){
                string = string + input.get(i) + " ";
                k = k - input.get(size-1-i).length();
            }

            else{
                string = string + " ";
            }
        }

        // add the action to string
        string = string + "    " + action;

        // if the action is not Accept or ERROR, add the actionInt
        if(!action.equals("Accept") && !action.equals("ERROR")){
            string = string + " " + actionInt;
        }
        
        // writing to the file
        outputWriter.write(string + "\n");
    }

    // this method returns the actions in parse table 
    public static String parseTable(String action, int state) {
        String[] idArray     = new String[]{"Shift", "ERROR", "ERROR",  "ERROR",  "Shift", "ERROR",  "Shift", "Shift", "ERROR", "ERROR",  "ERROR",  "ERROR"};
        String[] plusArray   = new String[]{"ERROR", "Shift", "Reduce", "Reduce", "ERROR", "Reduce", "ERROR", "ERROR", "Shift", "Reduce", "Reduce", "Reduce"};
        String[] mulArray    = new String[]{"Shift", "ERROR", "Shift",  "Reduce", "ERROR", "Reduce", "ERROR", "ERROR", "ERROR", "Shift",  "Reduce", "Reduce"};
        String[] openArray   = new String[]{"Shift", "ERROR", "ERROR",  "ERROR",  "Shift", "ERROR", "Shift", "Shift", "ERROR", "ERROR", "ERROR", "ERROR"};
        String[] closeArray  = new String[]{"ERROR", "ERROR", "Reduce",  "Reduce",  "ERROR", "Reduce", "ERROR", "ERROR", "Shift", "Reduce", "Reduce", "Reduce"};
        String[] dollarArray = new String[]{"ERROR", "Accept", "Reduce", "Reduce",  "ERROR", "Reduce", "ERROR", "ERROR", "ERROR", "Reduce", "Reduce", "Reduce"};
        
        if(action.equals("id")){
            return idArray[state];
        }

        if(action.equals("+")){
            return plusArray[state];
        }

        if(action.equals("*")){
            return mulArray[state];
        }

        if(action.equals("(")){
            return openArray[state];
        }

        if(action.equals(")")){
            return closeArray[state];
        }

        if(action.equals("$")){
            return dollarArray[state];
        }
        
        return "ERROR";
    }

    //this method returns the integers in parse table 
    public static int parseTableInt(String action, int state) {
        int[] idArray     = new int[]{5, 0, 0, 0, 5, 0, 5, 5, 0, 0, 0, 0};
        int[] plusArray   = new int[]{0, 6, 2, 4, 0, 6, 0, 0, 6, 1, 3, 5};
        int[] mulArray    = new int[]{4, 0, 07, 4, 0, 6, 0, 0, 0, 7, 3, 5};
        int[] openArray   = new int[]{4, 0, 0, 0, 4, 0, 4, 4, 0, 0, 0, 0};
        int[] closeArray  = new int[]{0, 0, 2, 4, 0, 6, 0, 0, 11, 1, 3, 5};
        int[] dollarArray = new int[]{0, 0, 2, 4, 0, 6, 0, 0, 0, 1, 3, 5};
        int[] eArray      = new int[]{1, 0, 0, 0, 8, 0, 0, 0, 0, 0, 0, 0};
        int[] tArray      = new int[]{2, 0, 0, 0, 2, 0, 9, 0, 0, 0, 0, 0};
        int[] fArray      = new int[]{3, 0, 0, 0, 3, 0, 3, 10, 0, 0, 0, 0};

        if(action.equals("id")){
            return idArray[state];
        }

        if(action.equals("+")){
            return plusArray[state];
        }

        if(action.equals("*")){
            return mulArray[state];
        }

        if(action.equals("(")){
            return openArray[state];
        }

        if(action.equals(")")){
            return closeArray[state];
        }

        if(action.equals("$")){
            return dollarArray[state];
        }
        
        if(action.equals("E")){
            return eArray[state];
        }
        
        if(action.equals("T")){
            return tArray[state];
        }
        if(action.equals("F")){
            return fArray[state];
        }
        return 0;
    }

    // this method takes a stack and return a string that contains all stack elements
    public static String stackToString(ArrayList<String> stack) {
        String str = "";

        for (String string : stack) {
            str = str + " " + string;
        }
        
        return str;
    }

    // this method adds space between the lexemes
    public static String editInput(String originalInput) {
        String str = "";

        for (int i = 0; i < originalInput.length(); i++) {
            char ch = originalInput.charAt(i);
            if( ch == 'd' || ch == '+' || ch == '*' || ch == '(' || ch == ')'){
                str = str + ch + " ";
            }

            else{
                str = str + ch;
            }
        }

        return str;        
    }

    public static String spaces(int k) {
        String str = "";
        for (int i = 0; i < k; i++) {
            str = str + " ";
        }

        return str;        
    }
    //-------------------------------------  end of the methods  -----------------------------
}