
import Exceptions.InvalidNumberOfArguments;
import Exceptions.NotValidDirectory;
import java.io.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.nio.file.*;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Scanner;
import Exceptions.*;

class Terminal {
    // change current path
    static void cd(ArrayList<String> path) throws Exception{
        if(path.size()>1){
            throw new InvalidNumberOfArguments("requires at most 1 parameter");
        }
        if(path.size()==0){
            path.add(System.getProperty("user.home"));
        }
        path.set(0,getPath(path.get(0)));
        File dir = new File(path.get(0));
        if(dir.isDirectory()) {
            System.setProperty("user.dir", dir.getAbsolutePath());
        } else {
            throw new NotValidDirectory("directory not found");
        }
    }
    // list all files and directories in current path
    static void ls(BufferedWriter bufferedWriter) throws IOException {
        File dir = new File(System.getProperty("user.dir"));
        String[] files = dir.list();
        for(String file: files){
            if(bufferedWriter==null){
                System.out.println(file);
            }else {
                bufferedWriter.write(file + "\n");
            }
        }
        if(bufferedWriter!=null){
            bufferedWriter.close();
        }
    }

    // taken n parameter files and directory then it copies this files
    // into that directory
    static void cp(ArrayList<String> args) throws Exception{
        if(args.size()<2){
            throw new InvalidNumberOfArguments("too few parameters");
        }
        String dirName=args.get(args.size()-1);
        dirName=getPath(dirName);
        File dir = new File(dirName);
        if(!dir.isDirectory()) {
            throw new NotValidDirectory(dirName+" not found");
        }
        // we don't iterate over the last element because it is the directory
        for(int i=0 ; i<args.size()-1 ; i++){
            String fileName=args.get(i);
            fileName=getPath(fileName);
            File file = new File(fileName);
            if(file.isFile() || file.isDirectory()) {
                File f2=new File(dir+"/"+file.getName());
                try {
                    Files.copy(file.toPath(), f2.toPath());
                }catch (FileAlreadyExistsException e){
                    System.err.println(fileName+" already exists");
                }
            } else {
                System.err.println(fileName+" not found");
            }
        }
    }

    static void more(ArrayList<String> args) throws InvalidNumberOfArguments {
        if(args.size()!=1){
            throw new InvalidNumberOfArguments("more requires exactly one parameter");
        }
        String filePath=getPath(args.get(0));
        File file = new File(filePath);

        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            System.err.println(filePath+" not found");
            return;
        }
        String st;
        try {
            Scanner scanner=new Scanner(System.in);
            while ((st = br.readLine()) != null) {
                System.out.println(st);
                System.err.println("press enter to continue");
                scanner.nextLine();
            }
            System.out.println("end of file");
        }catch (IOException e){
            System.err.println("error with reading from file");
        }
    }
    // this function handles long and short paths
    static String getPath(String path){
        if(path.charAt(0)!='/'){
            path=System.getProperty("user.dir")+"/"+path;
        }
        return path;
    }





    static void mkdir(ArrayList<String> arr) throws Exception {
        if(arr.size()!=1){
            throw new InvalidNumberOfArguments("requires exactly one parameter");
        }
        String argument = arr.get(0);
        File dir = new File(getPath(argument));
        boolean status = dir.mkdirs();
        if (status) System.out.println("Directory created successfully");
        else {
            throw new DirectoryExists("Failed to create the directory!" +
                    "Make sure the directory doesn't already exist");
        }
    }

    static void rmdir(ArrayList<String> arr) throws Exception {
        if (arr.size() != 1) {
            throw new InvalidNumberOfArguments("requires exactly one parameter");
        }
        String argument = arr.get(0);
        File dir = new File(getPath(argument));
        boolean status = dir.delete();
        if (status) System.out.println("Directory deleted successfully");
        else {
            throw new DirectoryNotEmpty("Failed to delete the directory!\n " +
                    "Make sure the directory are empty");
        }
    }

    static void mv(ArrayList<String> arr) throws IOException, DirectoryWithTheSameName, NoSuchFileOrDirectory {
        String source = arr.get(0);
        String Destination = arr.get(1);
        File sourceDir = new File(getPath(source));
        File destenationDir = new File(getPath(Destination));
        if (!sourceDir.exists()) {
            throw new NoSuchFileOrDirectory("No such file with the same name to move");
        }
        if (destenationDir.exists()) {
            throw new DirectoryWithTheSameName("There is a file with the same name");
        } else {
            Path temp = Files.move(Paths.get(getPath(source)), Paths.get(getPath(Destination)));
            System.out.println("File moved successfully");
        }
    }

    static void rm(ArrayList<String> arr) throws IOException {
        String argument = arr.get(0);
        //SimpleFileVisitor is the default implementation of the FileVisitor interface
        Path dir = Paths.get(getPath(argument));
        Traverse traverse = new Traverse();
        Files.walkFileTree(dir, traverse);
    }


    static void date(BufferedWriter out ) throws IOException
    {
        java.util.Date d = new java.util.Date();
        if(out==null){
            System.out.println(d.toString());
        }else {
            out.write(d.toString());
            out.close();
        }
    }
    static void help(BufferedWriter out) throws IOException
    {
        if(out!=null) {
            out.write("args  : List all command arguments.\n");
            out.write("cat   : Return content of file or multiple of files.\n");
            out.write("cd    : Changes the current directory to another one.\n");
            out.write("clear : Clear the screen.\n");

            out.write("cp    : Copy file to another file or group of files into other directory.\n");
            out.write("date  : Current date/time.\n");
            out.write("help  : List all user commands and the syntax of their arguments.\n");
            out.write("ls    : List each given file or directory name.\n");

            out.write("mkdir : Creates a directory with each given name.\n");
            out.write("more  : Display and scroll down the output in one direction only.\n");
            out.write("mv    : Moves file content to another file or group of files to another directory.\n");
            out.write("pwd   : Display current user directory.\n");

            out.write("rm    : Removes each specified file.\n");
            out.write("rmdir : Removes each given empty directory.\n");
            out.close();
        }else{
            System.out.println("args  : List all command arguments.\n");
            System.out.println("cat   : Return content of file or multiple of files.\n");
            System.out.println("cd    : Changes the current directory to another one.\n");
            System.out.println("clear : Clear the screen.\n");

            System.out.println("cp    : Copy file to another file or group of files into other directory.\n");
            System.out.println("date  : Current date/time.\n");
            System.out.println("help  : List all user commands and the syntax of their arguments.\n");
            System.out.println("ls    : List each given file or directory name.\n");

            System.out.println("mkdir : Creates a directory with each given name.\n");
            System.out.println("more  : Display and scroll down the output in one direction only.\n");
            System.out.println("mv    : Moves file content to another file or group of files to another directory.\n");
            System.out.println("pwd   : Display current user directory.\n");

            System.out.println("rm    : Removes each specified file.\n");
            System.out.println("rmdir : Removes each given empty directory.\n");
        }
    }
    static void pwd(BufferedWriter out) throws IOException
    {
        if(out!=null) {
            out.write(System.getProperty("user.dir") + "\n");
            out.close();
        }else{
            System.out.println(System.getProperty("user.dir"));
        }
    }
    static void cat(ArrayList<String> paths, BufferedWriter out) throws IOException
    {
        File file;
        InputStream reader = null;
        for (String path :  paths)
        {
            try
            {
                file =  new File(getPath(path));
                if(file.exists())
                {
                    if(!file.isDirectory())
                    {
                        if (file.canRead())
                        {
                            reader = new FileInputStream(file);
                            if(out!=null) {
                                out.write(new String(reader.readAllBytes()) + "\n");
                            } else {
                                System.out.print(new String(reader.readAllBytes()) + "\n");
                            }
                        }
                        else
                            System.out.println(path + " Access Denied");
                    }
                    else
                        System.out.println(path + " Is directory");
                }
                else
                    System.out.println(path + " Doesn't name file or directory");
            }
            finally
            {
                if(reader != null) reader.close();
            }
        }
        if(out!=null){
            out.close();
        }
    }
    public static void args(ArrayList<String> arr) throws NoSuchCommand {
        String argument = arr.get(0);
        switch(argument)
        {
            case "cp":
            case "mv":
            {
                System.out.println("arg1 SourcePath, arg2 DestinationPath");
                break;
            }
            case "cd":
            {
                System.out.println("arg1 DesirablePath or no arguments : DefaultPath");
                break;
            }
            case "ls":
            {
                System.out.println("arg1 option or no argument");
                break;
            }
            case "clear": case "pwd": case "|": case ">": case "<": case ">>":
        {
            System.out.println("no arguments");
            break;
        }
            case "rm":
            {
                System.out.println("arg1 RemovedFileOrDirectory");
                break;
            }
            case "mkdir": case "rmdir" :
        {
            System.out.println("arg1 DirectoryPath");
            break;
        }
            case "date":
            {
                System.out.println("arg1 settedDateor no argumen : show data");
                break;
            }
            case "echo":
            {
                System.out.println("arg1 printedArgument");
                break;
            }
            case "cat":
            {
                System.out.println("arg1 firstFile, arg2 secondFile or arg1 only : show content of the file");
                break;
            }
            case "more": case "less":
        {
            System.out.println("arg1 showedFile");
            break;
        }
            default:
            {
                throw new NoSuchCommand("No such commnad!");
            }
        }
    }
    static void clear()
    {
        for(int i  = 0;i < 100;i++)
            System.out.print("\n");
    }
}


class Traverse extends SimpleFileVisitor<Path>
{
    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) throws IOException {
        Files.delete(file);
        return FileVisitResult.CONTINUE;
    }
    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException e) throws IOException {
        Files.delete(dir);
        return FileVisitResult.CONTINUE;
    }
}
