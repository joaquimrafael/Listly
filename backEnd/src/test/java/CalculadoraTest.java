import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CalculadoraTest {

    @Test
    public void testSoma() {
        Calculadora calc = new Calculadora();
        assertEquals(5, calc.somar(2, 3));
    }
    @Test
    public void testSomaErro() {
        Calculadora calc = new Calculadora();
        assertEquals(10, calc.somar(2, 3));
    }
}
