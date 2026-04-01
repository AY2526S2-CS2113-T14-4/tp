package seedu.duke.category;

import seedu.duke.transaction.Expense;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Manages custom categories and store them to data/categories.txt.
 * The 7 built-in categories defined in Expense cannot be removed and are stored separately.
 */
public class CategoryManager {

    private static final String CATEGORIES_FILE = "data/categories.txt";
    private static final String DATA_DIR = "data";

    private static CategoryManager instance;

    private final Set<String> customCategories = new LinkedHashSet<>();

    private CategoryManager() {}

    /**
     * Returns an app-wide instance.
     * @return the single app-wide instance.
     */ 
    public static CategoryManager getInstance() {
        if (instance == null) {
            instance = new CategoryManager();
        }
        return instance;
    }

    /**
     * Load custom categories from the defined path.
     * Called in main when app starts, before transaction list loads
     */
    public void load() {
        try {
            Files.createDirectories(Paths.get(DATA_DIR));
            Path p = Paths.get(CATEGORIES_FILE);
            if (!Files.exists(p)) {
                return;
            }
            for (String line : Files.readAllLines(p)) {
                String cat = line.trim().toLowerCase();
                if (!cat.isEmpty()) {
                    customCategories.add(cat);
                }
            }
        } catch (IOException e) {
            System.out.println("[WARN] Could not load custom categories: " + e.getMessage());
        }
    }

    /**
     * Automatically writes the categories to the txt file.
     */
    private void save() {
        try {
            Files.createDirectories(Paths.get(DATA_DIR));
            Path target = Paths.get(CATEGORIES_FILE);
            Path tmp = Paths.get(CATEGORIES_FILE + ".tmp");
            Files.write(tmp, new ArrayList<>(customCategories));
            Files.move(tmp, target, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.out.println("[WARN] Could not save custom categories: " + e.getMessage());
        }
    }

    /**
     * Adds the new custom category defined by the user
     * @param name defined by user
     * @return true when added, false if exists
     */
    public boolean addCustomCategory(String name) {
        String lower = name.toLowerCase();
        if (Expense.VALID_CATEGORIES.contains(lower) || customCategories.contains(lower)) {
            return false;
        }
        customCategories.add(lower);
        save();
        return true;
    }

    /**
     * Deletes a custom category.
     * @param name designated by user for deletion.
     * @return true if removed, false if name does not exist 
     */
    public boolean removeCustomCategory(String name) {
        String lower = name.toLowerCase();
        if (!customCategories.contains(lower)) {
            return false;
        }
        customCategories.remove(lower);
        save();
        return true;
    }

    /**
     * Get the current custom categories defined by user.
     * @return the custom user-defined categories.
     */
    public Set<String> getCustomCategories() {
        return new LinkedHashSet<>(customCategories);
    }

    /**
     * Checks the validity of an expense category
     * @param category being checked 
     * @return true if category is valid, be it given or user-defined, false otherwise. 
     */
    public boolean isValidExpenseCategory(String category) {
        String lower = category.toLowerCase();
        return Expense.VALID_CATEGORIES.contains(lower) || customCategories.contains(lower);
    }

    /**
     * Get all available expense categories.
     * @return list of all available categories, including both built-in and user-defined
     */
    public List<String> getAllExpenseCategories() {
        List<String> all = new ArrayList<>(Expense.VALID_CATEGORIES);
        all.addAll(customCategories);
        return all;
    }
}
