import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

class ControlItem {
    String itemDescription;
    boolean itemStatus;

    ControlItem(String description) {
        this.itemDescription = description;
        this.itemStatus = false;
    }

    void ChangeStatus() {
        this.itemStatus = !this.itemStatus;
    }
}

class Battery {
    String healthStatus;
    double voltage;

    Battery(String healthStatus, double voltage) {
        this.healthStatus = healthStatus;
        this.voltage = voltage;
    }
}

public class Main {
    private static ArrayList<ControlItem> todoList = new ArrayList<>();
    private static ArrayList<Battery> batteriesList = new ArrayList<>();

    public static void main(String[] args) {
        boolean allConditionsChecked = false;
        Scanner scanner = new Scanner(System.in);

        while (!allConditionsChecked) {
            System.out.println("1 => Madde Ekleme");
            System.out.println("2 => Madde Çıkarma");
            System.out.println("3 => Madde Kontrolü Yapma");
            System.out.println("4 => Akü Ekleme ve Seçme");
            System.out.println("5 => Genel Kontrol");
            System.out.println("6 => Programı Kapat \n");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    addItem(scanner);
                    break;
                case 2:
                    removeItem(scanner);
                    break;
                case 3:
                    controlItems(scanner);
                    break;
                case 4:
                    manageBatteries(scanner);
                    break;
                case 5:
                    if (checkReadiness()) {
                        selectBestBattery();
                        System.out.println("Maça Hazır!");
                        allConditionsChecked = true;
                    } else {
                        System.out.println("Tüm maddeler kontrol edilmemiş veya uygun akü seçilmemiş.\n");
                    }
                    break;
                case 6:
                    allConditionsChecked = true;
                    break;
                default:
                    System.out.println("Geçersiz seçim. Lütfen tekrar deneyin.\n");
            }
        }
        scanner.close();
    }

    private static void addItem(Scanner scanner) {
        System.out.print("Eklemek istediğiniz maddeyi giriniz: ");
        String description = scanner.nextLine();
        todoList.add(new ControlItem(description));
        System.out.println("Madde eklendi.\n");
    }

    private static void removeItem(Scanner scanner) {
        for (int i = 0; i < todoList.size(); i++) {
            ControlItem item = todoList.get(i);
            System.out.println((i + 1) + "- " + item.itemDescription + " [" + (item.itemStatus ? "Doğru" : "Yanlış") + "]");
        }

        System.out.print("Çıkarmak istediğiniz madde numarasını giriniz: ");
        int itemIndex = scanner.nextInt() - 1;
        scanner.nextLine();

        if (itemIndex >= 0 && itemIndex < todoList.size()) {
            todoList.remove(itemIndex);
            System.out.println("Madde Çıkarıldı.\n");
        } else {
            System.out.println("Geçersiz madde numarası.\n");
        }
    }

    private static void controlItems(Scanner scanner) {
        if (todoList.isEmpty()) {
            System.out.println("Kontrol listesinde madde yok.\n");
            return;
        }

        for (int i = 0; i < todoList.size(); i++) {
            ControlItem item = todoList.get(i);
            System.out.println((i + 1) + "- " + item.itemDescription + " [" + (item.itemStatus ? "Doğru" : "Yanlış") + "]");
        }

        System.out.print("Kontrol etmek istediğiniz madde numarasını giriniz: ");
        int itemIndex = scanner.nextInt() - 1;
        scanner.nextLine();

        if (itemIndex >= 0 && itemIndex < todoList.size()) {
            todoList.get(itemIndex).ChangeStatus();
            System.out.println("Madde Doğru olarak işaretlendi.\n");
        } else {
            System.out.println("Geçersiz madde numarası.\n");
        }
    }

    private static void manageBatteries(Scanner scanner) {
        System.out.println("1 => Akü ekleme");
        System.out.println("2 => En uygun aküyü seç\n");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                addBattery(scanner);
                break;
            case 2:
                selectBestBattery();
                break;
            default:
                System.out.println("Geçersiz seçim. Lütfen tekrar deneyin.");
        }
    }

    private static void addBattery(Scanner scanner) {
        System.out.print("Akü sağlık durumu (iyi/ortalama): ");
        String healthStatus = scanner.nextLine();
        healthStatus = healthStatus.toLowerCase(new Locale("tr", "TR"));

        if (!healthStatus.equals("iyi") && !healthStatus.equals("ortalama")) {
            System.out.println(healthStatus);
            System.out.println("Akünün sağlık durumunu bu ikisinden biri olarak seçin (iyi/ortalama)\n");
            return;
        }

        System.out.print("Akü voltajı: (12.2 - 13)\n");
        double voltage = scanner.nextDouble();
        scanner.nextLine();

        if (voltage <= 12.2 || voltage >= 13) {
            System.out.println("Lütfen 12.2 ve 13 Voltaj Arasında Bir Aküyü Giriniz.\n");
            return;
        }

        batteriesList.add(new Battery(healthStatus, voltage));
        System.out.println("Akü eklendi. \n");
    }

    private static void selectBestBattery() {
        if (batteriesList.isEmpty()) {
            System.out.println("Kayıtlı akü yok.\n");
            return;
        }

        Battery bestBattery = null;

        for (Battery battery : batteriesList) {
            if (bestBattery == null ||
                    (battery.voltage > bestBattery.voltage) ||
                    (battery.voltage == bestBattery.voltage && battery.healthStatus.equals("good") && bestBattery.healthStatus.equals("fair"))) {
                bestBattery = battery;
            }
        }

        if (bestBattery != null) {
            System.out.println("Seçilen en iyi akü: " + bestBattery.voltage + "V, Sağlık Durumu: " + bestBattery.healthStatus);
        }
    }

    private static boolean checkReadiness() {
        boolean allChecked = true;

        for (ControlItem item : todoList) {
            if (!item.itemStatus) {
                allChecked = false;
                System.out.println("Kontrol edilmemiş madde: " + item.itemDescription);
            }
        }

        if (batteriesList.isEmpty()) {
            System.out.println("Hiç akü eklenmemiş.");
            return false;
        }

        Battery bestBattery = null;

        for (Battery battery : batteriesList) {
            if (bestBattery == null ||
                    (battery.voltage > bestBattery.voltage) ||
                    (battery.voltage == bestBattery.voltage && battery.healthStatus.equals("good") && bestBattery.healthStatus.equals("fair"))) {
                bestBattery = battery;
            }
        }

        if (bestBattery == null || !allChecked) {
            return false;
        }

        return true;
    }
}
