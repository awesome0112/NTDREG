package data.testDriverData;
import java.io.FileWriter;
public class TestDriver {
private static boolean mark(String statement, boolean isTrueCondition, boolean isFalseCondition) {
StringBuilder markResult = new StringBuilder();
markResult.append(statement).append("===");
markResult.append(isTrueCondition).append("===");
markResult.append(isFalseCondition).append("---end---");
writeDataToFile(markResult.toString(), "src/main/java/data/testDriverData/runTestDriverData.txt", true);
if (!isTrueCondition && !isFalseCondition) return true;
return !isFalseCondition;
}
private static void writeDataToFile(String data, String path, boolean append) {
try {
FileWriter writer = new FileWriter(path, append);
writer.write(data);
writer.close();
} catch(Exception e) {
e.printStackTrace();
}
}
public static int accountBalanceAfterPurchase(int purchaseAmount)
{
if (((purchaseAmount % 10 >= 5) && mark("purchaseAmount % 10 >= 5", true, false)) || mark("purchaseAmount % 10 >= 5", false, true))
{
{
mark("purchaseAmount+=10 - purchaseAmount % 10;\n", false, false);
purchaseAmount+=10 - purchaseAmount % 10;
}
}
else {
{
mark("purchaseAmount-=purchaseAmount % 10;\n", false, false);
purchaseAmount-=purchaseAmount % 10;
}
}
mark("return 100 - purchaseAmount;\n", false, false);
return 100 - purchaseAmount;
}

public static void main(String[] args) {
writeDataToFile("", "src/main/java/data/testDriverData/runTestDriverData.txt", false);
long startRunTestTime = System.nanoTime();
Object output = accountBalanceAfterPurchase(0);
long endRunTestTime = System.nanoTime();
double runTestDuration = (endRunTestTime - startRunTestTime) / 1000000.0;
writeDataToFile(runTestDuration + "===" + output, "src/main/java/data/testDriverData/runTestDriverData.txt", true);
}
}