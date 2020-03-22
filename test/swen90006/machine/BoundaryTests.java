package swen90006.machine;

import java.util.List;
import java.util.ArrayList;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.FileSystems;

import org.junit.*;
import org.junit.rules.Timeout;

import static org.junit.Assert.*;

public class BoundaryTests
{
  private Machine machine;
  
  // Each test times out after 4.5s
  @Rule
  public Timeout timeout = new Timeout(4500);
  
  //Any method annotated with "@Before" will be executed before each test,
  //allowing the tester to set up some shared resources.
  @Before public void setUp()
  {
    this.machine = new Machine();
  }

  //Any method annotated with "@After" will be executed after each test,
  //allowing the tester to release any shared resources used in the setup.
  @After public void tearDown()
  {
  }
  
  //EC_V1
  @Test public void testRegisterSymbolOnPoint()
  {
    final List<String> lines = new ArrayList<String>();
    final int expected = 0;
    lines.add("MOV R0 0");
    lines.add("RET R0");
    assertEquals("fail", expected, this.machine.execute(lines));
  }
  
  //EC_V1
  @Test(expected = InvalidInstructionException.class) public void testRegisterSymbolOffPoint()
  {
    final List<String> lines = new ArrayList<String>();
    final int expected = 0;
    lines.add("MOV Q1 1");
    lines.add("RET Q1");
    assertEquals("fail", expected, this.machine.execute(lines));
  }
  //EC_V1
  @Test(expected = InvalidInstructionException.class) public void testRegisterSymbolOffPoint2()
  {
    final List<String> lines = new ArrayList<String>();
    final int expected = 0;
    lines.add("MOV S2 1");
    lines.add("RET S2");
    assertEquals("fail", expected, this.machine.execute(lines));
  }

  //EC_V2
  @Test public void testRegisterNumberOnPoint()
  {
    final List<String> lines = new ArrayList<String>();
    final int expected = 1;
    lines.add("MOV R0 1");
    lines.add("RET R0");
    final int actual = this.machine.execute(lines);
    assertEquals("fail", expected, actual);
  }
  //EC_V2
  @Test public void testRegisterNumberOnPoint2()
  {
    final List<String> lines = new ArrayList<String>();
    final int expected = 1;
    lines.add("MOV R31 1");
    lines.add("RET R31");
    final int actual = this.machine.execute(lines);
    assertEquals("fail", expected, actual);
  }
  
  //EC_V2
  @Test(expected = InvalidInstructionException.class) public void testRegisterNumberOffPoint()
  {
    final List<String> lines = new ArrayList<String>();
    final int expected = 0;
    lines.add("ADD R1 R0 R-1");
    lines.add("RET R1");
    assertEquals("fail", expected, this.machine.execute(lines));
  }
  //EC_V2
  @Test(expected = InvalidInstructionException.class) public void testRegisterNumberOffPoint2()
  {
    final List<String> lines = new ArrayList<String>();
    final int expected = 0;
    lines.add("ADD R1 R0 R32");
    lines.add("RET R1");
    assertEquals("fail", expected, this.machine.execute(lines));
  }
  
  //EC_V3
  @Test public void testValueOnPoint()
  {
    final List<String> lines = new ArrayList<String>();
    final int expected = -65535;
    lines.add("MOV R0 -65535");
    lines.add("RET R0");
    assertEquals("fail", expected, this.machine.execute(lines));
  }
  //EC_V3
  @Test public void testValueOnPoint2()
  {
    final List<String> lines = new ArrayList<String>();
    final int expected = 65535;
    lines.add("MOV R0 65535");
    lines.add("RET R0");
    assertEquals("fail", expected, this.machine.execute(lines));
  }
  
  //EC_V3
  @Test(expected = InvalidInstructionException.class) public void testValueOffPoint()
  {
    final List<String> lines = new ArrayList<String>();
    final int expected = 0;
    lines.add("MOV R0 -65536");
    lines.add("RET R0");
    assertEquals("fail", expected, this.machine.execute(lines));
  }
  //EC_V3
  @Test(expected = InvalidInstructionException.class) public void testValueOffPoint2()
  {
    final List<String> lines = new ArrayList<String>();
    final int expected = 0;
    lines.add("MOV R0 65536");
    lines.add("RET R0");
    assertEquals("fail", expected, this.machine.execute(lines));
  }
  
  //EC_V4
  @Test public void testAddOnPoint()
  {
    final List<String> lines = new ArrayList<String>();
    final int rb = -65535, rc = -65535, expected = -131070;
    lines.add(String.format("MOV R1 %d", rb));
    lines.add(String.format("MOV R2 %d", rc));
    lines.add("ADD R0 R1 R2");
    lines.add("RET R0");
    final int actual = this.machine.execute(lines);
    assertEquals("fail", expected, actual);
  }
  //EC_V4
  @Test public void testAddOnPoint2()
  {
    final List<String> lines = new ArrayList<String>();
    final int rb = 65535, rc = 65535, expected = 131070;
    lines.add(String.format("MOV R1 %d", rb));
    lines.add(String.format("MOV R2 %d", rc));
    lines.add("ADD R0 R1 R2");
    lines.add("RET R0");
    final int actual = this.machine.execute(lines);
    assertEquals("fail", expected, actual);
  }
  
  //EC_V4
  @Test(expected = InvalidInstructionException.class) public void testAddOffPoint()
  {
    final List<String> lines = new ArrayList<String>();
    final int rb = -65536, rc = -65536, expected = -131072;
    lines.add(String.format("MOV R1 %d", rb));
    lines.add(String.format("MOV R2 %d", rc));
    lines.add("ADD R0 R1 R2");
    lines.add("RET R0");
    final int actual = this.machine.execute(lines);
    assertEquals("fail", expected, actual);
  }
  //EC_V4
  @Test(expected = InvalidInstructionException.class) public void testAddOffPoint2()
  {
    final List<String> lines = new ArrayList<String>();
    final int rb = 65536, rc = 65536, expected = 131072;
    lines.add(String.format("MOV R1 %d", rb));
    lines.add(String.format("MOV R2 %d", rc));
    lines.add("ADD R0 R1 R2");
    lines.add("RET R0");
    final int actual = this.machine.execute(lines);
    assertEquals("fail", expected, actual);
  }
  
  //EC_V5
  @Test public void testSubOnPoint()
  {
    final List<String> lines = new ArrayList<String>();
    final int rb = -65535, rc = 65535, expected = -131070;
    lines.add(String.format("MOV R1 %d", rb));
    lines.add(String.format("MOV R2 %d", rc));
    lines.add("SUB R0 R1 R2");
    lines.add("RET R0");
    final int actual = this.machine.execute(lines);
    assertEquals("fail", expected, actual);
  }
  //EC_V5
  @Test public void testSubOnPoint2()
  {
    final List<String> lines = new ArrayList<String>();
    final int rb = 65535, rc = -65535, expected = 131070;
    lines.add(String.format("MOV R1 %d", rb));
    lines.add(String.format("MOV R2 %d", rc));
    lines.add("SUB R0 R1 R2");
    lines.add("RET R0");
    final int actual = this.machine.execute(lines);
    assertEquals("fail", expected, actual);
  }
  
  //EC_V5
  @Test(expected = InvalidInstructionException.class) public void testSubOffPoint()
  {
    final List<String> lines = new ArrayList<String>();
    final int rb = -65536, rc = 65536, expected = -131072;
    lines.add(String.format("MOV R1 %d", rb));
    lines.add(String.format("MOV R2 %d", rc));
    lines.add("SUB R0 R1 R2");
    lines.add("RET R0");
    final int actual = this.machine.execute(lines);
    assertEquals("fail", expected, actual);
  }
  //EC_V5
  @Test(expected = InvalidInstructionException.class) public void testSubOffPoint2()
  {
    final List<String> lines = new ArrayList<String>();
    final int rb = 65536, rc = -65536, expected = 131072;
    lines.add(String.format("MOV R1 %d", rb));
    lines.add(String.format("MOV R2 %d", rc));
    lines.add("SUB R0 R1 R2");
    lines.add("RET R0");
    final int actual = this.machine.execute(lines);
    assertEquals("fail", expected, actual);
  }
  
  //EC_V6
  @Test public void testMulOnPoint()
  {
    final List<String> lines = new ArrayList<String>();
    final int rb = 65535, rc = 65535;
    final long expected = 4294836225L;
    lines.add(String.format("MOV R1 %d", rb));
    lines.add(String.format("MOV R2 %d", rc));
    lines.add("MUL R0 R1 R2");
    lines.add("RET R0");
    final int actual = this.machine.execute(lines);
    assertNotEquals("fail", expected, actual);
  }
  //EC_V6
  @Test public void testMulOnPoint2()
  {
    final List<String> lines = new ArrayList<String>();
    final int rb = -65535, rc = 65535;
    final long expected = -4294836225L;
    lines.add(String.format("MOV R1 %d", rb));
    lines.add(String.format("MOV R2 %d", rc));
    lines.add("MUL R0 R1 R2");
    lines.add("RET R0");
    final int actual = this.machine.execute(lines);
    assertNotEquals("fail", expected, actual);
  }
  
  //EC_V6
  @Test(expected = InvalidInstructionException.class) public void testMulOffPoint()
  {
    final List<String> lines = new ArrayList<String>();
    final int rb = 65536, rc = 65536;
    final long expected = 4294967296L;
    lines.add(String.format("MOV R1 %d", rb));
    lines.add(String.format("MOV R2 %d", rc));
    lines.add("MUL R0 R1 R2");
    lines.add("RET R0");
    final int actual = this.machine.execute(lines);
    assertNotEquals("fail", expected, actual);
  }
  //EC_V6
  @Test(expected = InvalidInstructionException.class) public void testMulOffPoint2()
  {
    final List<String> lines = new ArrayList<String>();
    final int rb = -65536, rc = 65536;
    final long expected = -4294967296L;
    lines.add(String.format("MOV R1 %d", rb));
    lines.add(String.format("MOV R2 %d", rc));
    lines.add("MUL R0 R1 R2");
    lines.add("RET R0");
    final int actual = this.machine.execute(lines);
    assertNotEquals("fail", expected, actual);
  }
  
  //EC_V7
  @Test public void testDivOnPoint()
  {
    final List<String> lines = new ArrayList<String>();
    final int rb = -65535, rc = 1, expected = -65535;
    lines.add(String.format("MOV R1 %d", rb));
    lines.add(String.format("MOV R2 %d", rc));
    lines.add("DIV R0 R1 R2");
    lines.add("RET R0");
    final int actual = this.machine.execute(lines);
    assertEquals("fail", expected, actual);
  }
  //EC_V7
  @Test public void testDivOnPoint2()
  {
    final List<String> lines = new ArrayList<String>();
    final int rb = 65535, rc = 1, expected = 65535;
    lines.add(String.format("MOV R1 %d", rb));
    lines.add(String.format("MOV R2 %d", rc));
    lines.add("DIV R0 R1 R2");
    lines.add("RET R0");
    final int actual = this.machine.execute(lines);
    assertEquals("fail", expected, actual);
  }
  
  //EC_V7
  @Test public void testDivOffPoint()
  {
    final List<String> lines = new ArrayList<String>();
    final int rb = 65535, rc = -1, expected = -65535;
    lines.add(String.format("MOV R1 %d", rb));
    lines.add(String.format("MOV R2 %d", rc));
    lines.add("DIV R0 R1 R2");
    lines.add("RET R0");
    final int actual = this.machine.execute(lines);
    assertEquals("fail", expected, actual);
  }
  //EC_V7
  @Test public void testDivOffPoint2()
  {
    final List<String> lines = new ArrayList<String>();
    final int rb = 65535, rc = 1, expected = 65535;
    lines.add(String.format("MOV R1 %d", rb));
    lines.add(String.format("MOV R2 %d", rc));
    lines.add("DIV R0 R1 R2");
    lines.add("RET R0");
    final int actual = this.machine.execute(lines);
    assertEquals("fail", expected, actual);
  }
  
  //EC_V8
  @Test public void testMovRetOnPoint()
  {
    final List<String> lines = new ArrayList<String>();
    final int v = 0, expected = 0;
    lines.add(String.format("MOV R0 %d", v));
    lines.add("RET R0");
    final int actual = this.machine.execute(lines);
    assertEquals("fail", expected, actual);
  }
  //EC_V8
  @Test public void testMovRetOffPoint()
  {
    final List<String> lines = new ArrayList<String>();
    final int v = -1, expected = 0;
    lines.add(String.format("MOV R0 %d", v));
    lines.add("RET R0");
    final int actual = this.machine.execute(lines);
    assertNotEquals("fail", expected, actual);
  }
  //EC_V8
  @Test public void testMovRetOffPoint2()
  {
    final List<String> lines = new ArrayList<String>();
    final int v = 1, expected = 0;
    lines.add(String.format("MOV R0 %d", v));
    lines.add("RET R0");
    final int actual = this.machine.execute(lines);
    assertNotEquals("fail", expected, actual);
  }
  
  //EC_V9
  @Test public void testLdrStrOnPoint()
  {
    final List<String> lines = new ArrayList<String>();
    final int b = 0, v = 0, expected = 1;
    lines.add(String.format("MOV R0 %d", b));
    lines.add("MOV R1 1");
    lines.add(String.format("STR R0 %d R1", v));
    lines.add(String.format("LDR R2 R0 %d", v));
    lines.add("RET R2");
    final int actual = this.machine.execute(lines);
    assertEquals("fail", expected, actual);
  }
  //EC_V9
  @Test public void testLdrStrOnPoint2()
  {
    final List<String> lines = new ArrayList<String>();
    final int b = 0, v = 65535, expected = 1;
    lines.add(String.format("MOV R0 %d", b));
    lines.add("MOV R1 1");
    lines.add(String.format("STR R0 %d R1", v));
    lines.add(String.format("LDR R2 R0 %d", v));
    lines.add("RET R2");
    final int actual = this.machine.execute(lines);
    assertEquals("fail", expected, actual);
  }
  
  //EC_V9
  @Test public void testLdrStrOffPoint()
  {
    final List<String> lines = new ArrayList<String>();
    final int b = 0, v = -1, expected = 1;
    lines.add(String.format("MOV R0 %d", b));
    lines.add("MOV R1 1");
    lines.add(String.format("STR R0 %d R1", v));
    lines.add(String.format("LDR R2 R0 %d", v));
    lines.add("RET R2");
    final int actual = this.machine.execute(lines);
    assertNotEquals("fail", expected, actual);
  }
  //EC_V9
  @Test public void testLdrStrOffPoint2()
  {
    final List<String> lines = new ArrayList<String>();
    final int b = 1, v = 65535, expected = 1;
    lines.add(String.format("MOV R0 %d", b));
    lines.add("MOV R1 1");
    lines.add(String.format("STR R0 %d R1", v));
    lines.add(String.format("LDR R2 R0 %d", v));
    lines.add("RET R2");
    final int actual = this.machine.execute(lines);
    assertNotEquals("fail", expected, actual);
  }
  
  //EC_V10
  @Test public void testJmpJzOnPoint()
  {
    final List<String> lines = new ArrayList<String>();
    final int N = 2;
    lines.add(String.format("JMP %d", N - 1));
    lines.add("RET R0");
    assertEquals("fail", 0, this.machine.execute(lines));
  }
  //EC_V10
  @Test(expected = NoReturnValueException.class) public void testJmpJzOffPoint()
  {
    final List<String> lines = new ArrayList<String>();
    lines.add(String.format("JMP %d", -1));
    lines.add("RET R0");
    assertEquals("fail", 0, this.machine.execute(lines));
  }
  //EC_V10
  @Test(expected = NoReturnValueException.class) public void testJmpJzOffPoint2()
  {
    final List<String> lines = new ArrayList<String>();
    final int N = 2;
    lines.add(String.format("JMP %d", N));
    lines.add("RET R0");
    assertEquals("fail", 0, this.machine.execute(lines));
  }
  
  boolean infiniteLoop = true;
  int result = Integer.MIN_VALUE;
  
  //EC_V11
  @Test public void testJmpJzZero()
  {
    final List<String> lines = new ArrayList<String>();
    lines.add("JMP 0");
    lines.add("RET R0");
    
    new Thread(new Runnable() {
      public void run() {
        result = machine.execute(lines);
        infiniteLoop = false;
      }
    }).start();
    
    long start = System.currentTimeMillis();
    long end = start + 2000;
    while (System.currentTimeMillis() < end) {
      if (result != Integer.MIN_VALUE) {
        // Program has returned
        assertTrue(false);
      }
    }
  }
  
  //EC_N1
  @Test public void testCommentOnPoint()
  {
    final List<String> lines = new ArrayList<String>();
    final int expected = 0;
    lines.add("; this is a comment");
    lines.add("RET R0");
    final int actual = this.machine.execute(lines);
    assertEquals("fail", expected, actual);
  }
  //EC_N1
  @Test(expected = InvalidInstructionException.class) public void testCommentOffPoint()
  {
    final List<String> lines = new ArrayList<String>();
    final int expected = 0;
    lines.add(": this is a comment");
    lines.add("RET R0");
    final int actual = this.machine.execute(lines);
    assertEquals("fail", expected, actual);
  }
  //EC_N1
  @Test(expected = InvalidInstructionException.class) public void testCommentOffPoint2()
  {
    final List<String> lines = new ArrayList<String>();
    final int expected = 0;
    lines.add("< this is a comment");
    lines.add("RET R0");
    final int actual = this.machine.execute(lines);
    assertEquals("fail", expected, actual);
  }
  
  //EC_N2
  @Test public void testBlankLineOnPoint()
  {
    final List<String> lines = new ArrayList<String>();
    final int expected = 0;
    lines.add("");
    lines.add("RET R0");
    final int actual = this.machine.execute(lines);
    assertEquals("fail", expected, actual);
  }
  //EC_N2
  @Test public void testBlankLineOffPoint()
  {
    final List<String> lines = new ArrayList<String>();
    final int expected = 0;
    lines.add(" ");
    lines.add("RET R0");
    final int actual = this.machine.execute(lines);
    assertEquals("fail", expected, actual);
  }
  
  //EC_N3
  @Test public void testDivZeroOnPoint()
  {
    final List<String> lines = new ArrayList<String>();
    final int rb = 65535, rc = 0, expected = 0;
    lines.add(String.format("MOV R1 %d", rb));
    lines.add(String.format("MOV R2 %d", rc));
    lines.add("DIV R0 R1 R2");
    lines.add("RET R0");
    final int actual = this.machine.execute(lines);
    assertEquals("fail", expected, actual);
  }
  //EC_N3
  @Test public void testDivZeroOffPoint()
  {
    final List<String> lines = new ArrayList<String>();
    final int rb = 65535, rc = 1, expected = 65535;
    lines.add(String.format("MOV R1 %d", rb));
    lines.add(String.format("MOV R2 %d", rc));
    lines.add("DIV R0 R1 R2");
    lines.add("RET R0");
    final int actual = this.machine.execute(lines);
    assertEquals("fail", expected, actual);
  }
  //EC_N3
  @Test public void testDivZeroOffPoint2()
  {
    final List<String> lines = new ArrayList<String>();
    final int rb = 65535, rc = -1, expected = -65535;
    lines.add(String.format("MOV R1 %d", rb));
    lines.add(String.format("MOV R2 %d", rc));
    lines.add("DIV R0 R1 R2");
    lines.add("RET R0");
    final int actual = this.machine.execute(lines);
    assertEquals("fail", expected, actual);
  }
  
  //EC_N4
  @Test public void testLdrStrNoOpOnPoint()
  {
    final List<String> lines = new ArrayList<String>();
    final int b = 0, v = 0, expected = -1;
    lines.add(String.format("MOV R0 %d", b));
    lines.add("MOV R1 -1");
    lines.add(String.format("STR R0 %d R1", v));
    lines.add(String.format("LDR R2 R0 %d", v));
    lines.add("RET R2");
    final int actual = this.machine.execute(lines);
    assertEquals("fail", expected, actual);
  }
  //EC_N4
  @Test public void testLdrStrNoOpOffPoint()
  {
    final List<String> lines = new ArrayList<String>();
    final int b = -1, v = 0, expected = 1;
    lines.add(String.format("MOV R0 %d", b));
    lines.add("MOV R1 1");
    lines.add(String.format("STR R0 %d R1", v));
    lines.add(String.format("LDR R2 R0 %d", v));
    lines.add("RET R2");
    final int actual = this.machine.execute(lines);
    assertNotEquals("fail", expected, actual);
  }
  
  //EC_N5
  @Test public void testLdrStrNoOpOnPoint2()
  {
    final List<String> lines = new ArrayList<String>();
    final int b = 65535, v = 0, expected = 1;
    lines.add(String.format("MOV R0 %d", b));
    lines.add("MOV R1 1");
    lines.add(String.format("STR R0 %d R1", v));
    lines.add(String.format("LDR R2 R0 %d", v));
    lines.add("RET R2");
    final int actual = this.machine.execute(lines);
    assertEquals("fail", expected, actual);
  }
  //EC_N5
  @Test public void testLdrStrNoOpOffPoint2()
  {
    final List<String> lines = new ArrayList<String>();
    final int b = 65535, v = 1, expected = 1;
    lines.add(String.format("MOV R0 %d", b));
    lines.add("MOV R1 1");
    lines.add(String.format("STR R0 %d R1", v));
    lines.add(String.format("LDR R2 R0 %d", v));
    lines.add("RET R2");
    final int actual = this.machine.execute(lines);
    assertNotEquals("fail", expected, actual);
  }
  
  //EC_N6
  @Test public void testJmpJzOneOnPoint()
  {
    final List<String> lines = new ArrayList<String>();
    lines.add("JMP 1");
    lines.add("RET R0");
    assertEquals("fail", 0, this.machine.execute(lines));
  }
  //EC_N6
  @Test public void testJmpJzOneOffPoint()
  {
    final List<String> lines = new ArrayList<String>();
    lines.add("JMP 2");
    lines.add("MOV R0 0");
    lines.add("RET R0");
    assertEquals("fail", 0, this.machine.execute(lines));
  }
  
  //JZ_0
  @Test public void testJzZeroOnPoint()
  {
    final List<String> lines = new ArrayList<String>();
    final int expected = 65535;
    lines.add("MOV R0 0");
    lines.add("MOV R1 65535");
    lines.add("JZ  R0 2");
    lines.add("RET R0");
    lines.add("RET R1");
    assertEquals("fail", expected, this.machine.execute(lines));
  }
  //JZ_0
  @Test public void testJzZeroOffPoint()
  {
    final List<String> lines = new ArrayList<String>();
    final int expected = 1;
    lines.add("MOV R0 1");
    lines.add("MOV R1 65535");
    lines.add("JZ  R0 2");
    lines.add("RET R0");
    lines.add("RET R1");
    assertEquals("fail", expected, this.machine.execute(lines));
  }
  //JZ_0
  @Test public void testJzZeroOffPoint2()
  {
    final List<String> lines = new ArrayList<String>();
    final int expected = -1;
    lines.add("MOV R0 -1");
    lines.add("MOV R1 65535");
    lines.add("JZ  R0 2");
    lines.add("RET R0");
    lines.add("RET R1");
    assertEquals("fail", expected, this.machine.execute(lines));
  }

  //Read in a file containing a program and convert into a list of
  //string instructions
  private List<String> readInstructions(String file)
  {
    Charset charset = Charset.forName("UTF-8");
    List<String> lines = null;
    try {
      lines = Files.readAllLines(FileSystems.getDefault().getPath(file), charset);
    }
    catch (Exception e){
      System.err.println("Invalid input file! (stacktrace follows)");
      e.printStackTrace(System.err);
      System.exit(1);
    }
    return lines;
  }
}
