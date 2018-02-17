/**
 * 
 */
package hcalfmXMLvalidator;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;

import rcms.fm.app.level1.HCALFunctionManager;
import rcms.fm.app.level1.HCALxmlHandler;
import rcms.fm.fw.user.UserActionException;

/**
 * @author John Hakala
 *
 */
public class xmlValidator {

  /**
   * class to use the HCALxmlHandler for validating xml files
   */
  public xmlValidator() {
    // TODO Auto-generated constructor stub

  }

  private static String getXMLtype(Path path) throws IOException {
    final Scanner scanner = new Scanner(path);
    String answer = "";
    while (scanner.hasNextLine()) {
      final String lineFromFile = scanner.nextLine();
      if(lineFromFile.contains("<RunConfigs>")) { 
        answer =  "grandmaster";    
        break;
      }
      else if(lineFromFile.contains("<mastersnippet>")) { 
        answer =  "master";    
        break;
      }
    }
    scanner.close();
    return answer;
  }

  /**
   * @param inputFile is the user's file they want to validate
   * @param whereToCopy is the directory where we want to create a directory named inputFile, and the file inside will be named pro
   * @return the path to the "pro" copy
   * @throws Exception if it couldn't make the copy
   */
  private static Path makeProCopy(Path inputFile, Path whereToCopy ) throws Exception {
    
    Path theDir = Paths.get(whereToCopy.toString(), inputFile.getFileName().toString());
    if (Files.notExists(theDir)) {
      Files.createDirectory(theDir);
    }
    else if (!Files.isDirectory(theDir)){
      System.out.println("requested to use " + whereToCopy + " as the temporary directory, but a file with the name '" + inputFile + "' already exists there [it should be a directory]" );
      System.exit(1);
    }
    Files.copy(inputFile, Paths.get(theDir.toString(), "pro"), StandardCopyOption.REPLACE_EXISTING);
    return theDir;
  }

  /**
   * @param inputFile is the file we want to validate by feeding it to the HCALFM's HCALxmlHandler
   * @throws Exception if it fails validation
   */
  private static void feedMasterSnippet(String inputFile) throws Exception {
    System.out.println("Validating master: " + inputFile);
    Path path = Paths.get(inputFile);
    String fileName = path.getFileName().toString();
    String enclosingDir = path.getParent().toString() + "/";
    HCALxmlHandler xmlHandler = new HCALxmlHandler(new HCALFunctionManager());

    try {
      xmlHandler.parseMasterSnippet(fileName, enclosingDir);
    }
    catch (UserActionException e) {
      // TODO Auto-generated catch block
      System.out.println("failure:");
      e.printStackTrace();
      throw new Exception("HCALxmlHandler failed at parsing the mastersnippet.");
    }
  }

  /**
   * @param inputFile the grandmaster to validate
   * @throws Exception if it fails to parse the grandmaster
   */
  private static void feedGrandmasterSnippet(String inputFile) throws Exception {
    System.out.println("Validating grandmaster: " + inputFile);
    Path path = Paths.get(inputFile);
    HCALxmlHandler xmlHandler = new HCALxmlHandler(new HCALFunctionManager());
    try {
      xmlHandler.parseHCALuserXML(HCALxmlHandler.readFile(path.toString(), Charset.defaultCharset()));
    }
    catch (UserActionException e) {
      // TODO Auto-generated catch block
      System.out.println("failure:");
      e.printStackTrace();
      throw new Exception("HCALxmlHandler failed at parsing the mastersnippet.");
    }
  }

  /**
   * @param args: two arguments, the xml to validate and the temporary directory to put it in
   */
  public static void main(String[] args) {
    // TODO Auto-generated method stub
    String tempDir = "/tmp";
    if (args.length > 2 || args.length < 1) {
      System.out.println("Usage:");
      System.out.println("first argument: the xml to be validated.");
      System.out.println("second argument (optional): where to make a temporary copy [default=/tmp].");
      System.exit(1);
    }
    else {
      if (args.length != 1) {
        tempDir = args[1];
      }
      try {
        if      (getXMLtype(Paths.get(args[0])).equals(     "master" )) {
          Path pathToPro = Paths.get("");
          try {
            pathToPro = makeProCopy(Paths.get(args[0]), Paths.get(tempDir));
          }
          catch (Exception e1) {
            System.out.println("error while trying to copy the file to a temporary directory:");
            e1.printStackTrace();
            System.exit(1);
          }
          feedMasterSnippet(pathToPro.toString());
        }
        else if (getXMLtype(Paths.get(args[0])).equals("grandmaster" )) {
          feedGrandmasterSnippet(args[0]);
        }
        else {
          System.out.println("Error: this xml does not appear to be either a master or a grandmaster.");
          System.exit(1);
        }
        System.out.println(args[0] + " passes validation.");
      }
      catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        System.exit(1);
      }
    }
  }
}
