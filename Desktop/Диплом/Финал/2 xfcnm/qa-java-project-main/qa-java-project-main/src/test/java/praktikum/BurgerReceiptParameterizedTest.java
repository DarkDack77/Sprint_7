package praktikum;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(Parameterized.class)
public class BurgerReceiptParameterizedTest {

    private final IngredientType type;
    private final String expectedLine;

    public BurgerReceiptParameterizedTest(IngredientType type, String expectedLine) {
        this.type = type;
        this.expectedLine = expectedLine;
    }

    @Parameterized.Parameters(name = "type={0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {IngredientType.SAUCE, "= sauce test ingredient ="},
                {IngredientType.FILLING, "= filling test ingredient ="}
        });
    }

    @Test
    public void getReceiptShouldContainCorrectIngredientTypeLine() {
        Burger burger = new Burger();

        Bun bun = mock(Bun.class);
        when(bun.getName()).thenReturn("bun");
        when(bun.getPrice()).thenReturn(1.0f);

        Ingredient ingredient = mock(Ingredient.class);
        when(ingredient.getType()).thenReturn(type);
        when(ingredient.getName()).thenReturn("test ingredient");
        when(ingredient.getPrice()).thenReturn(2.0f);

        burger.setBuns(bun);
        burger.addIngredient(ingredient);

        String receipt = burger.getReceipt();

        assertTrue("Чек должен содержать строку с типом ингредиента", receipt.contains(expectedLine));
    }
}