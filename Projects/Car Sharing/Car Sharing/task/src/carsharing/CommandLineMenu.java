package carsharing;

import carsharing.database.*;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class CommandLineMenu {

    private static final String LOG_IN_AS_A_MANAGER = "1. Log in as a manager";
    private static final String LOG_IN_AS_A_CUSTOMER = "2. Log in as a customer";
    private static final String CREATE_A_CUSTOMER = "3. Create a customer";
    private static final String EXIT_MENU = "0. Exit\n";
    private static final String COMPANY_LIST_MENU = "1. Company list";
    private static final String COMPANY_CREATE_MENU = "2. Create a company";
    private static final String CAR_LIST_MENU = "1. Car list";
    private static final String CAR_CREATE_MENU = "2. Create a car";
    private static final String BACK_MENU = "0. Back\n";
    private static final String CUSTOMER_RENT_MENU = "1. Rent a car";
    private static final String CUSTOMER_RETURN_MENU = "2. Return a rented car";
    private static final String CUSTOMER_LIST_MENU = "3. My rented car";
    private static final String CAR_LIST_EMPTY = "The car list is empty!\n";

    private static final Scanner scanner = new Scanner(System.in);
    private static CompanyDao companyDao;
    private static CarDao carDao;
    private static CustomerDao customerDao;

    private static void printCarCompanyMenu(Company company) throws InterruptedException {
        boolean back = false;
        while (!back) {
            System.out.println("'" + company.getName() + "' company");
            System.out.println(CAR_LIST_MENU);
            System.out.println(CAR_CREATE_MENU);
            System.out.println(BACK_MENU);
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    List<Car> cars = carDao.getByCompanyId(company.getId());
                    if (cars.size() > 0) {
                        System.out.println("Car list:");
                        for (int i = 0; i < cars.size(); i++) {
                            System.out.println((i + 1) + ". " + cars.get(i).getName() + " rented: " + customerDao.isCarLocked(cars.get(i).getId()));
                        }
                    } else {
                        System.out.println(CAR_LIST_EMPTY);
                    }
                    break;
                case "2":
                    System.out.println("Enter the car name:");
                    String car_name = scanner.nextLine();
                    carDao.save(new Car(car_name, company.getId()));
                    System.out.println("The car was created!\n");
                    break;
                case "0":
                    throw new InterruptedException("Exit to main menu");
            }
        }
    }

    private static void printCustomerChoiceMenu(Customer customer) throws InterruptedException {
        boolean back = false;
        while (!back) {
            System.out.println(CUSTOMER_RENT_MENU);
            System.out.println(CUSTOMER_RETURN_MENU);
            System.out.println(CUSTOMER_LIST_MENU);
            System.out.println(BACK_MENU);
            String choice = scanner.nextLine();
            switch (choice) {
                case "1": // Rent a car
                    if (customerDao.getRentedCar(customer.getId()).isPresent()) {
                        System.out.println("You've already rented a car!\n");
                        break;
                    }
                    List<Company> companies = companyDao.getAll();
                    if (companies.size() > 0) {
                        System.out.println("Choose a company:");
                        for (int i = 0; i < companies.size(); i++) {
                            System.out.println((i + 1) + ". " + companies.get(i).getName());
                        }
                        System.out.println(BACK_MENU);
                        String choiceCompany = scanner.nextLine();
                        if (Integer.valueOf(choiceCompany) == 0) {
                            back = true;
                        } else {
                            Integer companyId = companies.get(Integer.valueOf(choiceCompany) - 1).getId();
                            List<Car> cars = carDao.getByCompanyId(companyId)
                                                    .stream()
                                                    .filter(c -> !customerDao.isCarLocked(c.getId()))
                                                    .collect(Collectors.toList());
                            if (cars.size() > 0) {
                                System.out.println("Choose a car:");
                                for (int i = 0; i < cars.size(); i++) {
                                    System.out.println((i + 1) + ". " + cars.get(i).getName());
                                }
                                int index = scanner.nextInt() ;
                                index = index <= 0 ? 1 : index;
                                Integer carId = cars.get(index - 1).getId();
                                String[] params = new String[]{Integer.toString(carId)};
                                customerDao.update(customer, params);
                                System.out.println("You rented '" + cars.get(index - 1).getName() + "'");
                            } else {
                                System.out.println(CAR_LIST_EMPTY);
                            }
                        }
                    } else {
                        System.out.println("The company list is empty!\n");
                        back = true;
                    }
                    break;

                case "2": // Return a rented car
                    Optional<Car> rentedCar = customerDao.getRentedCar(customer.getId());
                    if (rentedCar.isPresent()) {
                        customerDao.releaseCar(customer.getId());
                        System.out.println("You've returned a rented car! " + rentedCar.get().getName() + "\n");
                    } else {
                        System.out.println("You didn't rent a car!\n");
                    }
                    break;

                case "3": // My rented car
                    Optional<Car> myCar = customerDao.getRentedCar(customer.getId());
                    if (myCar.isPresent()) {
                        System.out.println("Your rented car:");
                        System.out.println(myCar.get().getName());
                        System.out.println("Company:");
                        System.out.println(companyDao.get(myCar.get().getCompanyId()).get().getName());
                    } else {
                        System.out.println("You didn't rent a car!\n");
                    }
                    break;
                case "0":
                    throw new InterruptedException("Exit to main menu\n");
            }
        }
    }

    private static void printCompanyChooseMenu() {
        boolean back = false;
        while (!back) {
            List<Company> companies = companyDao.getAll();
            if (companies.size() > 0) {
                System.out.println("Choose the company:");
                for (int i = 0; i < companies.size(); i++) {
                    System.out.println((i + 1) + ". " + companies.get(i).getName());
                }
                System.out.println(BACK_MENU);
                String choice = scanner.nextLine();
                if (Integer.valueOf(choice) == 0) {
                    back = true;
                } else {
                    try {
                        printCarCompanyMenu(companies.get(Integer.valueOf(choice) - 1));
                    } catch (InterruptedException e) {
                        back = true;
                    }
                }
            } else {
                System.out.println("The company list is empty!\n");
                back = true;
            }
        }
    }

    private static void printCustomerSubMenu() {
        boolean back = false;
        while (!back) {
            List<Customer> customers = customerDao.getAll();
            if (customers.size() > 0) {
                System.out.println("Choose a customer:");
                for (int i = 0; i < customers.size(); i++) {
                    System.out.println((i + 1) + ". " + customers.get(i).getName());
                }
                System.out.println(BACK_MENU);
                String choice = scanner.nextLine();
                if (Integer.valueOf(choice) == 0) {
                    back = true;
                } else {
                    try {
                        printCustomerChoiceMenu(customers.get(Integer.valueOf(choice) - 1));
                    } catch (InterruptedException e) {
                        back = true;
                    }
                }
            } else {
                System.out.println("The customer list is empty!\n");
                back = true;
            }
        }
    }

    private static void printManagerSubMenu() {
        boolean back = false;
        while (!back) {
            System.out.println(COMPANY_LIST_MENU);
            System.out.println(COMPANY_CREATE_MENU);
            System.out.println(BACK_MENU);
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    printCompanyChooseMenu();
                    break;
                case "2":
                    System.out.println("Enter the company name:");
                    String company_name = scanner.nextLine();
                    companyDao.save(new Company(company_name));
                    System.out.println("The company was created!\n");
                    break;
                case "0":
                    back = true;
                    break;
                default:
                    System.out.println("Such value is not supported");
            }
        }
    }

    public static void printMainMenu(Connection connection) {
        companyDao = new CompanyDao(connection, "COMPANY");
        carDao = new CarDao(connection, "CAR");
        customerDao = new CustomerDao(connection, "CUSTOMER");

        boolean exit = false;
        while (!exit) {
            System.out.println(LOG_IN_AS_A_MANAGER);
            System.out.println(LOG_IN_AS_A_CUSTOMER);
            System.out.println(CREATE_A_CUSTOMER);
            System.out.println(EXIT_MENU);
            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    printManagerSubMenu();
                    break;
                case "2":
                    printCustomerSubMenu();
                    break;
                case "3":
                    System.out.println("Enter the customer name:");
                    customerDao.save(new Customer(scanner.nextLine()));
                    System.out.println("The customer was created!\n");
                    break;
                case "0":
                    exit = true;
                    break;
                default:
                    System.out.println("Such value is not supported");
            }
        }
    }
}
