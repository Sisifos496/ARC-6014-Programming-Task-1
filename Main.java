import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

//Defining Classes
class RobotBattery {
    String batteryHealthStatus;
    double batteryVoltage;

    RobotBattery(String batteryHealthStatus, double batteryVoltage) {
        this.batteryHealthStatus = batteryHealthStatus;
        this.batteryVoltage = batteryVoltage;
    }
}

class ControlItemTask {
    String itemDescription;
    boolean itemStatus;

    ControlItemTask(String description) {
        this.itemDescription = description;
        this.itemStatus = false;
    }

    void ChangeStatus() {
        this.itemStatus = !this.itemStatus;
    }
}

public class Main {
    //Defining Arrays to Store Class Instances
    private static ArrayList<RobotBattery> batteriesList = new ArrayList<>();
    private static ArrayList<ControlItemTask> itemControlList = new ArrayList<>();

    public static void main(String[] args) {
        boolean allConditionsChecked = false;
        Scanner scanner = new Scanner(System.in);

        //Main While Loop That Program Repeats For The User To Do The Functions
        while (!allConditionsChecked) {
            System.out.println("1 => Madde Ekle");
            System.out.println("2 => Madde Çıkar");
            System.out.println("3 => Madde Kontrolü Yap");
            System.out.println("4 => Akü Ekle veya En İyi Aküyü Seç");
            System.out.println("5 => Genel Kontrol Yap");
            System.out.println("6 => Programı Kapat \n");

            int choice = scanner.nextInt();
            scanner.nextLine();

            //Switch Case Used Here To Do The Correct Action According To The User
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
                    AddingSelectingBatteries(scanner);
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

    //AddItem Functionality
    private static void addItem(Scanner scanner) {
        System.out.print("Eklemek istediğiniz maddeyi giriniz: ");

        String itemDescriptionInput = scanner.nextLine();
        itemControlList.add(new ControlItemTask(itemDescriptionInput)); //Adding ItemControlList
        System.out.println("Madde eklendi.\n");
    }

    //RemoveItem Functionality
    private static void removeItem(Scanner scanner) {
        for (int i = 0; i < itemControlList.size(); i++) {
            ControlItemTask itemRemoved = itemControlList.get(i);
            System.out.println((i + 1) + "- " + itemRemoved.itemDescription + " [" + (itemRemoved.itemStatus ? "Doğru" : "Yanlış") + "]");
        }

        System.out.print("Çıkarmak istediğiniz madde numarasını giriniz: ");

        int itemIndex = scanner.nextInt() - 1;
        scanner.nextLine();

        //Checking If The User Input For Index Matches
        if (itemIndex >= 0 && itemIndex < itemControlList.size()) {
            itemControlList.remove(itemIndex); //Removing The Item According To The Index Input From The User
            System.out.println("Madde Çıkarıldı.\n");
        } else {
            System.out.println("Geçersiz madde numarası.\n");
        }
    }

    //Changing The Status of Items Functionality
    private static void controlItems(Scanner scanner) {
        if (itemControlList.isEmpty()) {
            System.out.println("Kontrol listesinde madde yok.\n");
            return;
        }

        for (int i = 0; i < itemControlList.size(); i++) {
            ControlItemTask controlListItem = itemControlList.get(i);
            System.out.println((i + 1) + "- " + controlListItem.itemDescription + " [" + (controlListItem.itemStatus ? "Doğru" : "Yanlış") + "]");
        }

        System.out.print("Kontrol etmek istediğiniz madde numarasını giriniz: ");
        int itemIndex = scanner.nextInt() - 1;
        scanner.nextLine();

        //Checking If The User Input For Index Matches
        if (itemIndex >= 0 && itemIndex < itemControlList.size()) {
            itemControlList.get(itemIndex).ChangeStatus(); //Change The Status of The Item
            System.out.println("Maddenin Statüsü Değiştirildi.\n");
        } else {
            System.out.println("Geçersiz madde numarası.\n");
        }
    }

    //Adding Batteries and Selecting The Best Battery
    private static void AddingSelectingBatteries(Scanner scanner) {
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

    //Adding Battery Functionality
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

        batteriesList.add(new RobotBattery(healthStatus, voltage)); //Adding The Battery To The List
        System.out.println("Akü eklendi. \n");
    }

    //Selecting Best Battery Functionality
    private static void selectBestBattery() {
        if (batteriesList.isEmpty()) {
            System.out.println("Kayıtlı akü yok.\n");
            return;
        }

        RobotBattery bestRobotBattery = null;

        //Conditions To Select The Best Battery: If the Voltage Is Better Automatically Select The One With Better Voltage Else Choose The One With Better Health Status
        for (RobotBattery robotBattery : batteriesList) {
            if (bestRobotBattery == null ||
                    (robotBattery.batteryVoltage > bestRobotBattery.batteryVoltage) ||
                    (robotBattery.batteryVoltage == bestRobotBattery.batteryVoltage && robotBattery.batteryHealthStatus.equals("good") && bestRobotBattery.batteryHealthStatus.equals("fair"))) {
                bestRobotBattery = robotBattery;
            }
        }

        if (bestRobotBattery != null) {
            System.out.println("Seçilen en iyi akü: " + bestRobotBattery.batteryVoltage + "V, Sağlık Durumu: " + bestRobotBattery.batteryHealthStatus);
        }
    }

    //Functionality To Check All The Tasks Are Done and Battery Is Added
    private static boolean checkReadiness() {
        boolean allChecked = true;

        for (ControlItemTask controlListItem : itemControlList) {
            if (!controlListItem.itemStatus) {
                allChecked = false;
                System.out.println("Kontrol edilmemiş madde: " + controlListItem.itemDescription);
            }
        }

        if (batteriesList.isEmpty()) {
            System.out.println("Hiç akü eklenmemiş.");
            return false;
        }

        RobotBattery bestRobotBattery = null;

        for (RobotBattery robotBattery : batteriesList) {
            if (bestRobotBattery == null ||
                    (robotBattery.batteryVoltage > bestRobotBattery.batteryVoltage) ||
                    (robotBattery.batteryVoltage == bestRobotBattery.batteryVoltage && robotBattery.batteryHealthStatus.equals("good") && bestRobotBattery.batteryHealthStatus.equals("fair"))) {
                bestRobotBattery = robotBattery;
            }
        }

        if (bestRobotBattery == null || !allChecked) {
            return false;
        }

        return true;
    }
}
