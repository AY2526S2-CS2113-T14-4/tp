package seedu.duke.category;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.lang.reflect.Field;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class CategoryManagerTest {

    @BeforeEach
    void resetSingleton() throws Exception {
        Field instance = CategoryManager.class.getDeclaredField("instance");
        instance.setAccessible(true);
        instance.set(null, null);
    }

    @Test
    void addCustomCategory_newName_returnsTrue() {
        assertTrue(CategoryManager.getInstance().addCustomCategory("groceries"));
    }

    @Test
    void addCustomCategory_duplicate_returnsFalse() {
        CategoryManager cm = CategoryManager.getInstance();
        cm.addCustomCategory("groceries");
        assertFalse(cm.addCustomCategory("groceries"));
    }

    @Test
    void addCustomCategory_builtInName_returnsFalse() {
        //reject because food is one of the pre-built categories
        assertFalse(CategoryManager.getInstance().addCustomCategory("food")); 
    }

    @Test
    void addCustomCategory_caseInsensitive_treatedAsDuplicate() {
        CategoryManager cm = CategoryManager.getInstance();
        cm.addCustomCategory("Groceries");
        assertFalse(cm.addCustomCategory("GROCERIES"));
    }


    @Test
    void removeCustomCategory_existingCustom_returnsTrue() {
        CategoryManager cm = CategoryManager.getInstance();
        cm.addCustomCategory("hobbies");
        assertTrue(cm.removeCustomCategory("hobbies"));
    }

    @Test
    void removeCustomCategory_notFound_returnsFalse() {
        assertFalse(CategoryManager.getInstance().removeCustomCategory("hobbies"));
    }

    @Test
    void removeCustomCategory_builtIn_returnsFalse() {
        // Built-ins must be protected
        assertFalse(CategoryManager.getInstance().removeCustomCategory("food"));
    }

    @Test
    void isValidExpenseCategory_builtIn_returnsTrue() {
        assertTrue(CategoryManager.getInstance().isValidExpenseCategory("food"));
    }

    @Test
    void isValidExpenseCategory_addedCustom_returnsTrue() {
        CategoryManager cm = CategoryManager.getInstance();
        cm.addCustomCategory("petcare");
        assertTrue(cm.isValidExpenseCategory("petcare"));
    }

    @Test
    void isValidExpenseCategory_unknown_returnsFalse() {
        assertFalse(CategoryManager.getInstance().isValidExpenseCategory("xyz"));
    }

    @Test
    void isValidExpenseCategory_afterRemoval_returnsFalse() {
        CategoryManager cm = CategoryManager.getInstance();
        cm.addCustomCategory("petcare");
        cm.removeCustomCategory("petcare");
        assertFalse(cm.isValidExpenseCategory("petcare"));
    }

    @Test
    void getAllExpenseCategories_containsBuiltInsAndCustom() {
        CategoryManager cm = CategoryManager.getInstance();
        cm.addCustomCategory("groceries");
        var all = cm.getAllExpenseCategories();
        assertTrue(all.contains("food"));      // built-in
        assertTrue(all.contains("groceries")); // custom
    }
}