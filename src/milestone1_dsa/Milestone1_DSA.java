/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package milestone1_dsa;

/**
 *
 * @author User Alvin kent bigbig
 */
import java.util.*;

class Stock {
    private String dateEntered;
    private String stockLabel;
    private String brand;
    private String engineNumber;
    private String status;

    public Stock(String dateEntered, String stockLabel, String brand, String engineNumber, String status) {
        this.dateEntered = dateEntered;
        this.stockLabel = stockLabel;
        this.brand = brand;
        this.engineNumber = engineNumber;
        this.status = status;
    }

    public String getDateEntered() {
        return dateEntered;
    }

    public String getStockLabel() {
        return stockLabel;
    }

    public String getBrand() {
        return brand;
    }

    public String getEngineNumber() {
        return engineNumber;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return String.format("%-12s %-10s %-10s %-15s %-10s", dateEntered, stockLabel, brand, engineNumber, status);
    }
}

class InventoryManager {
    private LinkedList<Stock> inventory;
    private Stack<Stock> undoStack;
    private Queue<Stock> searchResultsQueue;
    private Scanner scanner;

    public InventoryManager() {
        inventory = new LinkedList<>();
        undoStack = new Stack<>();
        searchResultsQueue = new LinkedList<>();
        scanner = new Scanner(System.in);
    }

    // Add new stock
    public void addNewStock(String dateEntered, String stockLabel, String brand, String engineNumber, String status) {
        if (!isValidDate(dateEntered)) {
            System.out.println("Invalid date format. Try again.");
            return;
        }
        Stock newStock = new Stock(dateEntered, stockLabel, brand, engineNumber, status);
        inventory.add(newStock);
        undoStack.push(newStock);
        System.out.println("Stock added successfully.");
    }

    // Delete incorrect stock
    public void deleteStock() {
        System.out.print("Enter the engine number of the stock you want to delete: ");
        String engineNumber = scanner.nextLine().trim();
        for (Stock stock : inventory) {
            if (stock.getEngineNumber().equals(engineNumber)) {
                System.out.println("Stock found: " + stock);
                System.out.print("Are you sure you want to delete this stock? (yes/no): ");
                String confirmation = scanner.nextLine().trim().toLowerCase();
                if (confirmation.equals("yes")) {
                    inventory.remove(stock);
                    System.out.println("Stock deleted successfully.");
                }
                return;
            }
        }
        System.out.println("No stock found with the given engine number.");
    }

    // Sort stocks by brand using Merge Sort
    public void sortStocksByBrand() {
        System.out.print("Enter the brand to sort by (or 'All' to sort all): ");
        String brand = scanner.nextLine().trim();
        List<Stock> filteredList = new ArrayList<>();
        for (Stock stock : inventory) {
            if (brand.equalsIgnoreCase("All") || stock.getBrand().equalsIgnoreCase(brand)) {
                filteredList.add(stock);
            }
        }
        mergeSort(filteredList, 0, filteredList.size() - 1);
        System.out.println("\nSorted Stocks by Brand:");
        for (Stock stock : filteredList) {
            System.out.println(stock);
        }
    }

    // Merge Sort implementation
    private void mergeSort(List<Stock> list, int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;
            mergeSort(list, left, mid);
            mergeSort(list, mid + 1, right);
            merge(list, left, mid, right);
        }
    }

    private void merge(List<Stock> list, int left, int mid, int right) {
        List<Stock> temp = new ArrayList<>();
        int i = left, j = mid + 1;
        while (i <= mid && j <= right) {
            if (list.get(i).getBrand().compareTo(list.get(j).getBrand()) <= 0) {
                temp.add(list.get(i++));
            } else {
                temp.add(list.get(j++));
            }
        }
        while (i <= mid) {
            temp.add(list.get(i++));
        }
        while (j <= right) {
            temp.add(list.get(j++));
        }
        for (i = left; i <= right; i++) {
            list.set(i, temp.get(i - left));
        }
    }

    // Search stock by engine number
    public void searchStockByEngineNumber() {
        System.out.print("Enter engine number to search for: ");
        String engineNumber = scanner.nextLine().trim();
        for (Stock stock : inventory) {
            if (stock.getEngineNumber().equals(engineNumber)) {
                searchResultsQueue.add(stock);
                System.out.println("Found: " + stock);
                return;
            }
        }
        System.out.println("Stock not found.");
    }

    // Display all stocks
    public void displayAllStocks() {
        System.out.println(String.format("%-12s %-10s %-10s %-15s %-10s", "Date", "Label", "Brand", "Engine No.", "Status"));
        for (Stock stock : inventory) {
            System.out.println(stock);
        }
    }

    // Undo last addition
    public void undoLastAddition() {
        if (!undoStack.isEmpty()) {
            Stock lastAdded = undoStack.pop();
            inventory.remove(lastAdded);
            System.out.println("Undo successful. Last added stock removed: " + lastAdded);
        } else {
            System.out.println("No operations to undo.");
        }
    }

    // Validate date format (basic validation)
    private boolean isValidDate(String date) {
        return date.matches("\\d{1,2}/\\d{1,2}/\\d{4}");
    }

    public void manageInventory() {
        while (true) {
            System.out.println("\nChoose an operation: ");
            System.out.println("1. Add Stock");
            System.out.println("2. Delete Stock");
            System.out.println("3. Sort Stocks by Brand");
            System.out.println("4. Search Stock by Engine Number");
            System.out.println("5. Display All Stocks");
            System.out.println("6. Undo Last Addition");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter date (MM/DD/YYYY): ");
                    String date = scanner.nextLine();
                    System.out.print("Enter stock label: ");
                    String label = scanner.nextLine();
                    System.out.print("Enter brand: ");
                    String brand = scanner.nextLine();
                    System.out.print("Enter engine number: ");
                    String engineNumber = scanner.nextLine();
                    System.out.print("Enter status (On-hand/Sold): ");
                    String status = scanner.nextLine();
                    addNewStock(date, label, brand, engineNumber, status);
                }
                case 2 -> deleteStock();
                case 3 -> sortStocksByBrand();
                case 4 -> searchStockByEngineNumber();
                case 5 -> displayAllStocks();
                case 6 -> undoLastAddition();
                case 7 -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}

public class Milestone1_DSA {
    public static void main(String[] args) {
        InventoryManager manager = new InventoryManager();

        // Add all provided data
        String[][] stockData = {
            {"2/1/2023", "Old", "Honda", "142QVTSIUR", "On-hand"},
            {"2/1/2023", "Old", "Honda", "PZCT1S00XE", "Sold"},
            {"2/1/2023", "Old", "Honda", "4VBTV8YNM7", "Sold"},
            {"2/1/2023", "Old", "Honda", "95AN3AWVF4", "On-hand"},
            {"2/3/2023", "Old", "Kawasaki", "483QHIM661", "On-hand"},
            {"2/3/2023", "Old", "Kymco", "SPHA17SSEE", "On-hand"},
            {"2/3/2023", "Old", "Kymco", "0AV7SWGX93", "Sold"},
            {"2/4/2023", "Old", "Kymco", "QMUB6UYLKL", "Sold"},
            {"2/4/2023", "Old", "Honda", "V96GMTFFEI", "Sold"},
            {"2/5/2023", "Old", "Kawasaki", "4J8UA0FMVY", "Sold"},
            {"2/5/2023", "Old", "Kawasaki", "A8BDL926FA", "Sold"},
            {"2/5/2023", "Old", "Kawasaki", "X8G5ZZ7A69", "Sold"},
            {"2/6/2023", "Old", "Honda", "TY5SU0WPDX", "On-hand"},
            {"2/6/2023", "Old", "Honda", "5Q0EZG7WKB", "On-hand"},
            {"2/6/2023", "Old", "Suzuki", "9XUOUOJ2XZ", "On-hand"},
            {"2/6/2023", "Old", "Kymco", "YUL4UTC4FU", "On-hand"},
            {"2/6/2023", "Old", "Kymco", "2ESQRHAXWG", "On-hand"},
            {"2/7/2023", "Old", "Kymco", "J8JA99VWZE", "Sold"},
            {"2/7/2023", "Old", "Kymco", "NS530HOT9H", "Sold"},
            {"2/7/2023", "Old", "Suzuki", "URIA0XXM05", "Sold"},
            {"2/7/2023", "Old", "Yamaha", "IDN93SI4KW", "Sold"},
            {"2/7/2023", "Old", "Honda", "PVAWKD51CE", "Sold"},
            {"2/7/2023", "Old", "Honda", "K4KHCQAU41", "Sold"},
            {"2/8/2023", "Old", "Honda", "Z4NY5JGZZT", "Sold"},
            {"2/8/2023", "Old", "Honda", "IRQACSKUNZ", "Sold"},
            {"2/8/2023", "Old", "Yamaha", "TMZCTALNDL", "Sold"},
            {"2/8/2023", "Old", "Yamaha", "DVFUIA0YVB", "Sold"},
            {"2/8/2023", "Old", "Kymco", "4M793VVAHI", "On-hand"},
            {"2/8/2023", "Old", "Suzuki", "5N7IQVJ2BA", "On-hand"},
            {"3/1/2023", "New", "Suzuki", "NO8VW05PU9", "On-hand"},
            {"3/1/2023", "New", "Yamaha", "NWIP2MQEIN", "Sold"},
            {"3/1/2023", "New", "Kawasaki", "1HCWCVZSX8", "Sold"},
            {"3/3/2023", "New", "Kawasaki", "Z46VKPIJBY", "Sold"},
            {"3/3/2023", "New", "Kawasaki", "LYQVEHJ6IU", "Sold"},
            {"3/3/2023", "New", "Yamaha", "BVGQQNMATL", "Sold"},
            {"3/4/2023", "New", "Kymco", "URWMSQZCBU", "Sold"},
            {"3/4/2023", "New", "Yamaha", "5NGI5UZ8T2", "On-hand"},
            {"3/5/2023", "New", "Honda", "W2UYM0EIRS", "On-hand"},
            {"3/5/2023", "New", "Honda", "AITLTSJUK2", "On-hand"},
            {"3/5/2023", "New", "Yamaha", "45CNYV7IFF", "On-hand"},
            {"3/6/2023", "New", "Kymco", "MXS36NKV96", "Sold"},
            {"3/6/2023", "New", "Kymco", "PWM3MJWPYE", "Sold"},
            {"3/6/2023", "New", "Kymco", "5I80N9HB7W", "Sold"},
            {"3/6/2023", "New", "Yamaha", "D01JMJL9PG", "On-hand"},
            {"3/6/2023", "New", "Suzuki", "1R88BOJW8W", "On-hand"},
            {"3/7/2023", "New", "Suzuki", "LAMH9Y1YD6", "On-hand"},
            {"3/7/2023", "New", "Yamaha", "02G7NJCRGS", "On-hand"},
            {"3/7/2023", "New", "Kawasaki", "392XSUBMUW", "On-hand"}
            // Add all other data here...
        };

        // Add all data to the inventory
        for (String[] data : stockData) {
            manager.addNewStock(data[0], data[1], data[2], data[3], data[4]);
        }

        // Manage inventory
        manager.manageInventory();
    }
}