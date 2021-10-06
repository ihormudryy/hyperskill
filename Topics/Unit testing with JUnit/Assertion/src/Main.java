class CalculatorTest {
    void testAddition() {
        Calculator calc = new Calculator();
        Assertions.assertEquals(3, calc.add(1, 2));
    }
}