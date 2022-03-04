
import ontology.tool.console.CLIOptionsParser;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;



@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CLIOptionsParserTest {

    @Test
    @Order(1)
    @DisplayName("Empty arguments test")
    void emptyArgsTest(){
        String[] args = {};
        CLIOptionsParser cli = new CLIOptionsParser();
        assertFalse(cli.parseCLI(args),"Return value is not false.");
        assertEquals(cli.getInputFiles(),null,"Input files array is not empty.");
        assertTrue(cli.getDestination().isEmpty(),"Destination is not empty.");
        assertTrue(cli.getFormat().isEmpty(),"Format is not empty.");
        assertTrue(cli.getLanguage().isEmpty(),"Language is not empty.");
        assertFalse(cli.getHelp(),"Help is not false.");
    }

    @Test
    @Order(2)
    @DisplayName("Arguments test input file.")
    void argsTestFile() {
        String[] args = {"file.owl"};
        CLIOptionsParser cli = new CLIOptionsParser();
        assertTrue(cli.parseCLI(args),"Return value is not true.");
        assertEquals(cli.getInputFiles().length,1,"Input files length is not 1.");
        assertEquals(cli.getInputFiles()[0],"file.owl","Input files length is not file.owl.");
        assertTrue(cli.getDestination().isEmpty(),"Destination is not empty.");
        assertTrue(cli.getFormat().isEmpty(),"Format is not empty.");
        assertTrue(cli.getLanguage().isEmpty(),"Language is not empty.");
        assertFalse(cli.getHelp(),"Help is not false.");
    }

    @Test
    @Order(3)
    @DisplayName("Arguments test 2 files.")
    void argsTestFile2() {
        String[] args = {"file.owl","file2.owl"};
        CLIOptionsParser cli = new CLIOptionsParser();
        assertTrue(cli.parseCLI(args),"Return value is not true.");
        assertEquals(cli.getInputFiles().length,2,"Input files length is not 2.");
        assertTrue(cli.getDestination().isEmpty(),"Destination is not empty.");
        assertTrue(cli.getFormat().isEmpty(),"Format is not empty.");
        assertTrue(cli.getLanguage().isEmpty(),"Language is not empty.");
        assertFalse(cli.getHelp(),"Help is not false.");
    }

    @Test
    @Order(4)
    @DisplayName("Arguments test empty file.")
    void argsTestEmptyFile() {
        String[] args = {"-d","/dest"};
        CLIOptionsParser cli = new CLIOptionsParser();
        assertFalse(cli.parseCLI(args),"Return value is not false.");
        assertEquals(cli.getInputFiles().length,0,"Input files length is not empty.");
        assertTrue(cli.getDestination().isEmpty(),"Destination is not empty.");
        assertTrue(cli.getFormat().isEmpty(),"Format is not empty.");
        assertTrue(cli.getLanguage().isEmpty(),"Language is not empty.");
        assertFalse(cli.getHelp(),"Help is not false.");
    }

    @Test
    @Order(5)
    @Disabled
    @DisplayName("Arguments test help.")
    void argsTestHelp() {
        String[] args = {"-h"};
        CLIOptionsParser cli = new CLIOptionsParser();
        assertTrue(cli.parseCLI(args),"Return value is not true.");
        assertEquals(cli.getInputFiles(),null,"Input files length is not null.");
        assertTrue(cli.getDestination().isEmpty(),"Destination is not empty.");
        assertTrue(cli.getFormat().isEmpty(),"Format is not empty.");
        assertTrue(cli.getLanguage().isEmpty(),"Language is not empty.");
        assertTrue(cli.getHelp(),"Help is not true.");
    }

    @Test
    @Order(6)
    @Disabled
    @DisplayName("Arguments test help 2.")
    void argsTestHelp2() {
        String[] args = {"--help"};
        CLIOptionsParser cli = new CLIOptionsParser();
        assertTrue(cli.parseCLI(args),"Return value is not true.");
        assertEquals(cli.getInputFiles(),null,"Input files length is not null.");
        assertTrue(cli.getDestination().isEmpty(),"Destination is not empty.");
        assertTrue(cli.getFormat().isEmpty(),"Format is not empty.");
        assertTrue(cli.getLanguage().isEmpty(),"Language is not empty.");
        assertTrue(cli.getHelp(),"Help is not true.");
    }

    @Test
    @Order(7)
    @DisplayName("Arguments test format.")
    void argsTestFormat() {
        String[] args = {"file.owl","-f","turtle"};
        CLIOptionsParser cli = new CLIOptionsParser();
        assertTrue(cli.parseCLI(args),"Return value is not true.");
        assertEquals(cli.getInputFiles().length,1,"Input files length is not 1.");
        assertTrue(cli.getDestination().isEmpty(),"Destination is not empty.");
        assertEquals(cli.getFormat(),"turtle","Format is not turtle.");
        assertTrue(cli.getLanguage().isEmpty(),"Language is not empty.");
        assertFalse(cli.getHelp(),"Help is not false.");
    }

    @Test
    @Order(8)
    @DisplayName("Arguments test destination.")
    void argsTestDestination() {
        String[] args = {"file.owl", "-f", "turtle","-d","destination"};
        CLIOptionsParser cli = new CLIOptionsParser();
        assertTrue(cli.parseCLI(args), "Return value is not true.");
        assertEquals(cli.getInputFiles().length,1,"Input files length is not 1.");
        assertEquals(cli.getDestination(),"destination","Destination is not empty.");
        assertEquals(cli.getFormat(),"turtle","Format is not turtle.");
        assertTrue(cli.getLanguage().isEmpty(),"Language is not empty.");
        assertFalse(cli.getHelp(),"Help is not false.");

    }

    @Test
    @Order(9)
    @DisplayName("Arguments test language.")
    void argsTestLanguage() {
        String[] args = {"file.owl", "-f", "turtle","-d","/destination","-l","java"};
        CLIOptionsParser cli = new CLIOptionsParser();
        assertTrue(cli.parseCLI(args), "Return value is not true.");
        assertEquals(cli.getInputFiles().length,1,"Input files length is not 1.");
        assertEquals(cli.getDestination(),"/destination","Destination is not empty.");
        assertEquals(cli.getFormat(),"turtle","Format is not turtle.");
        assertEquals(cli.getLanguage(),"java","Language is not empty.");
        assertFalse(cli.getHelp(),"Help is not false.");

    }

}
