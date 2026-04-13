package praktikum;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class BurgerTest {

    private Burger burger;
    private Bun bun;
    private Ingredient ingredient1;
    private Ingredient ingredient2;

    @Before
    public void setUp() {
        burger = new Burger();

        bun = mock(Bun.class);
        when(bun.getName()).thenReturn("black bun");
        when(bun.getPrice()).thenReturn(100.0f);

        ingredient1 = mock(Ingredient.class);
        when(ingredient1.getPrice()).thenReturn(50.0f);
        when(ingredient1.getType()).thenReturn(IngredientType.SAUCE);
        when(ingredient1.getName()).thenReturn("hot sauce");

        ingredient2 = mock(Ingredient.class);
        when(ingredient2.getPrice()).thenReturn(70.0f);
        when(ingredient2.getType()).thenReturn(IngredientType.FILLING);
        when(ingredient2.getName()).thenReturn("beef");
    }

    @Test
    public void setBunsShouldSetBun() {
        burger.setBuns(bun);

        assertSame("Булка должна сохраниться в burger.bun", bun, burger.bun);
    }

    @Test
    public void addIngredientShouldAddIngredientToList() {
        burger.addIngredient(ingredient1);

        assertEquals("После добавления должен быть 1 ингредиент", 1, burger.ingredients.size());
        assertSame("Добавленный ингредиент должен лежать в списке", ingredient1, burger.ingredients.get(0));
    }

    @Test
    public void removeIngredientShouldRemoveIngredientByIndex() {
        burger.addIngredient(ingredient1);
        burger.addIngredient(ingredient2);

        burger.removeIngredient(0);

        assertEquals("После удаления должен остаться 1 ингредиент", 1, burger.ingredients.size());
        assertSame("Должен остаться второй ингредиент", ingredient2, burger.ingredients.get(0));
    }

    @Test
    public void moveIngredientShouldMoveIngredientToNewIndex() {
        burger.addIngredient(ingredient1);
        burger.addIngredient(ingredient2);

        burger.moveIngredient(0, 1);

        List<Ingredient> ingredients = burger.ingredients;
        assertEquals("Размер списка не должен меняться", 2, ingredients.size());
        assertSame("Первым должен стать второй ингредиент", ingredient2, ingredients.get(0));
        assertSame("Вторым должен стать первый ингредиент", ingredient1, ingredients.get(1));
    }

    @Test
    public void getPriceShouldReturnCorrectPrice() {
        burger.setBuns(bun);
        burger.addIngredient(ingredient1);
        burger.addIngredient(ingredient2);

        float actualPrice = burger.getPrice();

        assertEquals(
                "Цена должна считаться как булка*2 + сумма ингредиентов",
                320.0f,
                actualPrice,
                0.0f
        );

        verify(bun, atLeastOnce()).getPrice();
        verify(ingredient1, atLeastOnce()).getPrice();
        verify(ingredient2, atLeastOnce()).getPrice();
    }

    @Test
    public void getReceiptShouldReturnCorrectReceipt() {
        burger.setBuns(bun);
        burger.addIngredient(ingredient1);
        burger.addIngredient(ingredient2);

        String expected = String.format(
                "(==== black bun ====)%n" +
                        "= sauce hot sauce =%n" +
                        "= filling beef =%n" +
                        "(==== black bun ====)%n" +
                        "%n" +
                        "Price: 320,000000%n"
        );

        String actual = burger.getReceipt();

        assertEquals("Чек должен быть сформирован корректно", expected, actual);

        verify(bun, atLeastOnce()).getName();
        verify(ingredient1, atLeastOnce()).getType();
        verify(ingredient1, atLeastOnce()).getName();
        verify(ingredient2, atLeastOnce()).getType();
        verify(ingredient2, atLeastOnce()).getName();
    }
}
