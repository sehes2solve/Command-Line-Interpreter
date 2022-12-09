import Exceptions.DirectoryWithTheSameName;
import Exceptions.NoSuchFileOrDirectory;
import Exceptions.NotValidCommand;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scan = new Scanner(System.in);
        // change the default path
        System.setProperty("user.dir", System.getProperty("user.home"));

        while (true) {
            String line = scan.nextLine();
            String[] cmds = line.split("\\|");
            boolean exit = false;
            ArrayList<String> This_is_for_mv_only = new ArrayList<>();
            for (String command : cmds) {
                try {
                    Parser.parse(command);
                    String cmd = Parser.getCmd();
                    ArrayList<String> pars = Parser.getArguments();
                    switch (cmd) {
                        case "cd":
                            Terminal.cd(pars);
                            break;
                        case "ls":
                            Terminal.ls(Parser.file);
                            break;
                        case "cp":
                            Terminal.cp(pars);
                            break;
                        case "more":
                            Terminal.more(pars);
                            break;
                        case "exit":
                            exit = true;
                            break;
                        case "mkdir":
                            Terminal.mkdir(pars);
                            break;
                        case "rmdir":
                            Terminal.rmdir(pars);
                            break;
                        case "rm":
                            Terminal.rm(pars);
                            break;
                        case "mv":
                            This_is_for_mv_only.add(pars.get(0));
                            This_is_for_mv_only.add(pars.get(1));
                            Terminal.mv(pars);
                            break;
                        case "args":
                            Terminal.args(pars);
                            break;
                        case "date":
                            Terminal.date(Parser.file);
                            break;
                        case "help":
                            Terminal.help(Parser.file);
                             break;
                        case "pwd":
                            Terminal.pwd(Parser.file);
                            break;
                        case "cat":
                            Terminal.cat(pars,Parser.file);
                            break;
                        case "clear":
                            Terminal.clear();
                            break;
                        default:
                            throw new NotValidCommand();
                    }
                }
                catch(DirectoryWithTheSameName e)
                {
                    System.err.println(e.getMessage());
                    System.err.println("Do you want to replace it ? [Y or N]");
                    Scanner cin = new Scanner(System.in);
                    String res = cin.nextLine();
                    if(res.equals("Y") || res.equals("y"))
                    {
                        ArrayList<String> arr = new ArrayList<>();
                        arr.add(This_is_for_mv_only.get(1));
                        Terminal.rm(arr);
                        try {
                            Terminal.mv(This_is_for_mv_only);
                        } catch (DirectoryWithTheSameName exp) {
                            System.out.println(exp.getMessage());
                        } catch (NoSuchFileOrDirectory noSuchFileOrDirectory) {
                            noSuchFileOrDirectory.printStackTrace();
                        }
                    }
                }catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
            if (exit) break;
        }
        if (Parser.file != null) {
            Parser.file.close();
        }
    }
}
