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

public class PartitioningTests
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
  @Test public void testRegisterNamePass()
  {
    final List<String> lines = new ArrayList<String>();
    final int expected = 0;
    lines.add("RET R0");
    assertEquals("fail", expected, this.machine.execute(lines));
  }
  //EC_V1
  @Test(expected = InvalidInstructionException.class) public void testRegisterNameFail()
  {
    final List<String> lines = new ArrayList<String>();
    final int expected = 0;
    lines.add("RET S0");
    assertEquals("fail", expected, this.machine.execute(lines));
  }
  
  //EC_V2
  @Test public void testRegisterValuePass()
  {
    final List<String> lines = new ArrayList<String>();
    final int expected = 0;
    lines.add("RET R31");
    assertEquals("fail", expected, this.machine.execute(lines));
  }
  //EC_V2
  @Test(expected = InvalidInstructionException.class) public void testRegisterValueFail()
  {
    final List<String> lines = new ArrayList<String>();
    final int expected = 0;
    lines.add("RET R32");
    assertEquals("fail", expected, this.machine.execute(lines));
  }
  
  //EC_V3
  @Test public void testValuePass()
  {
    final List<String> lines = new ArrayList<String>();
    final int expected = 65535;
    lines.add("MOV R0 65535");
    lines.add("RET R0");
    assertEquals("fail", expected, this.machine.execute(lines));
  }
  //EC_V3
  @Test(expected = InvalidInstructionException.class) public void testValueFail()
  {
    final List<String> lines = new ArrayList<String>();
    final int expected = 0;
    lines.add("MOV R0 -65536");
    lines.add("RET R0");
    assertEquals("fail", expected, this.machine.execute(lines));
  }

  //EC_V4
  @Test public void testAdd()
  {
    final List<String> lines = new ArrayList<String>();
    final int expected = 3;
    lines.add("MOV R1 1");
    lines.add("MOV R2 2");
    lines.add("ADD R3 R1 R2");
    lines.add("RET R3");
    assertEquals("fail", expected, this.machine.execute(lines));
  }

  //EC_V5
  @Test public void testSub()
  {
    final List<String> lines = new ArrayList<String>();
    final int expected = 1;
    lines.add("MOV R1 3");
    lines.add("MOV R2 2");
    lines.add("SUB R3 R1 R2");
    lines.add("RET R3");
    assertEquals("fail", expected, this.machine.execute(lines));
  }
  
  //EC_V6
  @Test public void testMul()
  {
    final List<String> lines = new ArrayList<String>();
    final int expected = 15;
    lines.add("MOV R1 3");
    lines.add("MOV R2 5");
    lines.add("MUL R3 R1 R2");
    lines.add("RET R3");
    assertEquals("fail", expected, this.machine.execute(lines));
  }
  
  //EC_V7
  @Test public void testDiv()
  {
    final List<String> lines = new ArrayList<String>();
    final int expected = 2;
    lines.add("MOV R0 100");
    lines.add("MOV R1 10");
    lines.add("MOV R2 5");
    lines.add("DIV R0 R1 R2");
    lines.add("RET R0");
    assertEquals("fail", expected, this.machine.execute(lines));
  }
  //EC_N3
  @Test public void testDivZero()
  {
    final List<String> lines = new ArrayList<String>();
    final int expected = 100;
    lines.add("MOV R0 100");
    lines.add("MOV R1 10");
    lines.add("MOV R2 0");
    lines.add("DIV R0 R1 R2");
    lines.add("RET R0");
    assertEquals("fail", expected, this.machine.execute(lines));
  }
  
  //EC_V8
  @Test public void testRetMov()
  {
    final List<String> lines = new ArrayList<String>();
    final int expected = 65535;
    lines.add("MOV R1 65535");
    lines.add("RET R1");
    assertEquals("fail", expected, this.machine.execute(lines));
  }
  
  //EC_V9
  @Test public void testLdrStr()
  {
    final List<String> lines = new ArrayList<String>();
    final int expected = 100;
    lines.add("MOV R0 1");
    lines.add("MOV R1 100");
    lines.add("MOV R2 0");
    lines.add("STR R0  1 R1");
    lines.add("LDR R2 R0  1");
    lines.add("RET R2");
    assertEquals("fail", expected, this.machine.execute(lines));
  }
  
  //EC_V10
  @Test public void testJmpJz()
  {
    final List<String> lines = new ArrayList<String>();
    final int expected = 100;
    lines.add("MOV R0 100");
    lines.add("JMP 2");
    lines.add("MOV R0 200");
    lines.add("RET R0");
    assertEquals("fail", expected, this.machine.execute(lines));
  }
  
  //EC_V11
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
  
  //EC_N6
  @Test public void testJmpJzOne()
  {
    final List<String> lines = new ArrayList<String>();
    final int expected = 100;
    lines.add("MOV R0 0");
    lines.add("JZ  R0 1");
    lines.add("MOV R0 100");
    lines.add("RET R0");
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
