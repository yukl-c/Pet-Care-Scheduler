
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeSet;

public class PetCareScheduler {
    public static Scanner scanner = new Scanner(System.in);
    public static String getPetId() {
        System.out.println("Input your pet Id.");
        String id = scanner.nextLine();
        return id;
    }

    public static String getPetName() {
        System.out.println("Input your pet name.");
        String name = scanner.nextLine();
        return name;
    }

    public static String getPetSpecies() {
        System.out.println("Input your pet species.");
        String species = scanner.nextLine();
        return species;
    }

    public static int getPetAge() {
        System.out.println("Input your pet age.");
        String ageStr = scanner.nextLine();
        return Integer.valueOf(ageStr);
    }

    public static String getPetOwnerName() {
        System.out.println("Input your pet owner's name.");
        String ownerName = scanner.nextLine();
        return ownerName;
    }

    public static String getPetContactInfo() {
        System.out.println("Input your pet contact info.");
        String contactInfo = scanner.nextLine();
        return contactInfo;
    }

    public static Type getAppointmenType() {
        System.out.println("Input the appointment Types (vet visit, vaccination, grooming, others).");
        String typeStr = scanner.nextLine();
        switch (typeStr) {
            case "vet visit": 
                return Type.VET_VISIT;
            case "vaccination":
                return Type.VACCINATION;
            case "grooming":
                return Type.GROOMING;
            default:
                return Type.OTHERS;
        }
    }

    public static String getAppointmentNotes() {
        System.out.println("Input the appointment notes.");
        String notes = scanner.nextLine();
        return notes;
    }

    public static LocalDate getAppointmentDate() {
        System.out.println("Input the date in MM/dd/yyyy format:");
        String appointmentDateStr = scanner.nextLine();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate appointmentDate = LocalDate.parse(appointmentDateStr, dateFormatter);
        return appointmentDate;
    }

    public static LocalTime getAppointmentTime() {
        System.out.println("Input the time in HH:mm format:");
        String appointmentTimeStr = scanner.nextLine();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime appointmentTime = LocalTime.parse(appointmentTimeStr, timeFormatter);
        return appointmentTime;
    }

    public static Appointment createAppointment() {
        Appointment newAppointment = null;
        Type appointmentType = getAppointmenType();
        String appointmentNotes = getAppointmentNotes();
        System.out.println("Are you tracking the mood for a current day? y/n");
        String isForCurrentDate = scanner.nextLine();
        if (isForCurrentDate.equalsIgnoreCase("n")) {
            LocalDate appointmentDate = getAppointmentDate();

            LocalTime appointmentTime = getAppointmentTime();

            if (appointmentNotes.strip().equals("")) {
                newAppointment = new Appointment(appointmentType, appointmentDate, appointmentTime);
            } else {
                newAppointment = new Appointment(appointmentType, appointmentDate, appointmentTime, appointmentNotes);
            }
        } else {
            if (appointmentNotes.strip().equals("")) {
                newAppointment = new Appointment(appointmentType);
            } else {
                newAppointment = new Appointment(appointmentType, appointmentNotes);
            }
        }
        return newAppointment;
    }

    public static boolean isPetValid(HashMap<String, Pet> petCareScheduler, Pet pet) throws InvalidPetException {
        for (Pet p : petCareScheduler.values()) {
            if (p.equals(pet)) {
                throw new InvalidPetException();
            }
        }

        return true;
    }

    public static boolean isAppointmentValid(TreeSet<Appointment> appointments, Appointment appointment) throws InvalidAppointmentException {
        for (Appointment a : appointments) {
            if (a.equals(appointment)) {
                throw new InvalidAppointmentException();
            }
        }

        return true;
    }

    public static Pet serachPetBaseOnId(HashMap<String, Pet> petCareScheduler, String petId) throws InvalidPetException {
        Pet serachedPet = petCareScheduler.get(petId);
        if (serachedPet == null) {
            throw new InvalidPetException();
        }
        return serachedPet;
    }

    public static Pet serachPetBaseOnPetInfo(HashMap<String, Pet> petCareScheduler, String name, String ownerName, String contactInfo) throws InvalidPetException {
        return petCareScheduler.values().stream()
                .filter(pet -> name.equals(pet.getName())
                                && ownerName.equals(pet.getOwnerName())
                                && contactInfo.equals(pet.getContactInfo()))
                .findFirst()
                .orElseThrow(InvalidPetException::new);
    }

    public static Appointment searchAppointment(TreeSet<Appointment> appointments, Type type, LocalDate date, LocalTime time) throws InvalidAppointmentException  {
        return appointments.stream()
                    .filter(a -> a.getType().equals(type)
                                    && a.getDate().equals(date)
                                    && a.getTime().equals(time))
                    .findFirst()
                    .orElseThrow(InvalidAppointmentException::new);
    }

    public static String findPetId(HashMap<String, Pet> petCareScheduler, Pet pet) throws InvalidPetException {
        return petCareScheduler.entrySet().stream()
                .filter(entry -> entry.getValue().equals(pet))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseThrow(InvalidPetException::new);
    }

    @FunctionalInterface
    public interface PetSearchOption {
        void select() throws InvalidPetException, InvalidAppointmentException;
    }

    public static void optionsForSerachingPet(PetSearchOption option1, PetSearchOption option2) throws InvalidPetException, InvalidAppointmentException {
        System.out.println("Enter '1' to serach by pet Id\n"
                            + "Enter '2' to search by pet infomation.");
        String serachVariant = scanner.nextLine();
        if (serachVariant.equals("1")) {
            option1.select();
        }
        else if (serachVariant.equals("2")) {
            option2.select();
        }
        else {
            System.out.println("Not a valid input!");
        }
    }

    public static Pet updatePet(Pet pet) {
        System.out.println("Enter '1' to update the pet name\n"
                + "Enter '2' to update the pet species\n"
                + "Enter '3' to update the pet age\n"
                + "Enter '4' to update the pet owner name\n"
                + "Enter '5' to update the pet contact information");

        String updatePetVariant = scanner.nextLine();

        switch (updatePetVariant) {
            case "1":
                String petName = getPetName();
                pet.setName(petName);
                break;
            case "2":
                int petAge = getPetAge();
                pet.setAge(petAge);
                break;
            case "3":
                String petSpecies = getPetSpecies();
                pet.setSpecies(petSpecies);
                break;
            case "4":
                String petOwnerName = getPetOwnerName();
                pet.setOwnerName(petOwnerName);
                break;
            case "5":
                String petContactInfo = getPetContactInfo();
                pet.setContactInfo(petContactInfo);
                break;
            default:
                System.out.println("Wrong Input");
        }

        return pet;
    }

    public static Appointment updateAppointment(Appointment appointment) {
        System.out.println("Enter '1' to update the appointment type\n"
                + "Enter '2' to update the appointment notes\n"
                + "Enter '3' to update the appointment date\n"
                + "Enter '4' to update the appointment time");

        String updatePetVariant = scanner.nextLine();

        switch (updatePetVariant) {
            case "1":
                Type appointmentType = getAppointmenType();
                appointment.setType(appointmentType);
                break;
            case "2":
                String appointmentNote = getAppointmentNotes();
                appointment.setNotes(appointmentNote);
                break;
            case "3":
                LocalDate appointmentDate = getAppointmentDate();
                appointment.setDate(appointmentDate);
                break;
            case "4":
                LocalTime appLocalTime = getAppointmentTime();
                appointment.setTime(appLocalTime);
                break;
            default:
                System.out.println("Wrong Input");
        }

        return appointment;
    }

    private static String jsonEscape(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private static String jsonUnescape(String value) {
        return value.replace("\\\"", "\"").replace("\\\\", "\\");
    }
    public static void savePets(HashMap<String, Pet> pets) throws IOException {
        DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("HH:mm");
        StringBuilder json = new StringBuilder();
        json.append("[\n");
        int petIndex = 0;
        for (Pet pet : pets.values()) {
            if (petIndex++ > 0) {
                json.append(",\n");
            }
            json.append("  {\n");
            json.append("    \"petId\": \"").append(jsonEscape(pet.getPetId())).append("\",\n");
            json.append("    \"name\": \"").append(jsonEscape(pet.getName())).append("\",\n");
            json.append("    \"species\": \"").append(jsonEscape(pet.getSpecies())).append("\",\n");
            json.append("    \"age\": ").append(pet.getAge()).append(",\n");
            json.append("    \"ownerName\": \"").append(jsonEscape(pet.getOwnerName())).append("\",\n");
            json.append("    \"contactInfo\": \"").append(jsonEscape(pet.getContactInfo())).append("\",\n");
            json.append("    \"registrationDate\": \"")
                .append(pet.getRegistrationDate().format(dateFmt)).append("\",\n");
            json.append("    \"appointments\": [\n");
            int apptIndex = 0;
            for (Appointment a : pet.getAppointments()) {
                if (apptIndex++ > 0) {
                    json.append(",\n");
                }
                json.append("      {");
                json.append("\"type\": \"").append(a.getType().name()).append("\", ");
                json.append("\"date\": \"").append(a.getDate().format(dateFmt)).append("\", ");
                json.append("\"time\": \"").append(a.getTime().format(timeFmt)).append("\", ");
                json.append("\"notes\": \"").append(jsonEscape(a.getNotes())).append("\"}");
            }
            json.append("\n    ]\n  }");
        }
        json.append("\n]\n");
        try (PrintWriter writer = new PrintWriter(new FileWriter("Pets.json"))) {
            writer.print(json);
        }
    }

    public static HashMap<String, Pet> loadPets(String path) throws IOException {
        HashMap<String, Pet> pets = new HashMap<>();
        File file = new File(path);
        if (!file.exists()) {
            return pets;
        }

        String content;
        try (Scanner fileScanner = new Scanner(file)) {
            fileScanner.useDelimiter("\\A");
            content = fileScanner.hasNext() ? fileScanner.next() : "";
        }
        if (content.isBlank()) {
            return pets;
        }

        DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("HH:mm");

        // savePets separates pets with "]\n  },\n  {", so this split stays outside appointment objects.
        String body = content.trim();
        if (body.startsWith("[")) {
            body = body.substring(1);
        }
        if (body.endsWith("]")) {
            body = body.substring(0, body.length() - 1);
        }
        String[] petChunks = body.split("\\]\\s*\\},\\s*\\{");

        for (String chunk : petChunks) {
            try {
                String petId = field(chunk, "petId");
                if (petId.isEmpty()) {
                    continue;
                }
                Pet pet = new Pet(
                        petId,
                        field(chunk, "name"),
                        field(chunk, "species"),
                        Integer.parseInt(field(chunk, "age")),
                        field(chunk, "ownerName"),
                        field(chunk, "contactInfo"));
                pet.setRegistrationDate(LocalDate.parse(field(chunk, "registrationDate"), dateFmt));

                int apptKey = chunk.indexOf("\"appointments\"");
                if (apptKey >= 0) {
                    int open = chunk.indexOf('[', apptKey);
                    if (open >= 0) {
                        int close = chunk.indexOf(']', open);
                        String apptBody = close >= 0
                                ? chunk.substring(open + 1, close)
                                : chunk.substring(open + 1);
                        if (!apptBody.isBlank()) {
                            String[] apptChunks = apptBody.split("\\},\\s*\\{");
                            for (String apptChunk : apptChunks) {
                                String typeName = field(apptChunk, "type");
                                if (typeName.isEmpty()) {
                                    continue;
                                }
                                Appointment appointment = new Appointment(
                                        Type.valueOf(typeName),
                                        LocalDate.parse(field(apptChunk, "date"), dateFmt),
                                        LocalTime.parse(field(apptChunk, "time"), timeFmt),
                                        field(apptChunk, "notes"));
                                pet.setAppointment(appointment);
                            }
                        }
                    }
                }
                pets.put(petId, pet);
            } catch (DateTimeParseException | IllegalArgumentException e) {
                System.err.println("Skipping unreadable pet record: " + e.getMessage());
            }
        }
        return pets;
    }

    private static String field(String chunk, String key) {
        String marker = "\"" + key + "\"";
        int keyAt = chunk.indexOf(marker);
        if (keyAt < 0) {
            return "";
        }
        int colon = chunk.indexOf(':', keyAt + marker.length());
        if (colon < 0) {
            return "";
        }
        int valueStart = colon + 1;
        while (valueStart < chunk.length() && Character.isWhitespace(chunk.charAt(valueStart))) {
            valueStart++;
        }
        if (valueStart >= chunk.length()) {
            return "";
        }
        if (chunk.charAt(valueStart) == '"') {
            int valueEnd = valueStart + 1;
            while (valueEnd < chunk.length()) {
                if (chunk.charAt(valueEnd) == '\\' && valueEnd + 1 < chunk.length()) {
                    valueEnd += 2;
                    continue;
                }
                if (chunk.charAt(valueEnd) == '"') {
                    break;
                }
                valueEnd++;
            }
            return jsonUnescape(chunk.substring(valueStart + 1, valueEnd));
        }
        int valueEnd = valueStart;
        while (valueEnd < chunk.length() && Character.isDigit(chunk.charAt(valueEnd))) {
            valueEnd++;
        }
        return chunk.substring(valueStart, valueEnd);
    }

    public static void main(String[] args)  {
        
        HashMap<String, Pet> petCareScheduler = new HashMap<>();
        try {
            petCareScheduler.putAll(loadPets("Pets.json"));
        } catch (IOException e) {
            System.err.println("Could not read Pets.json: " + e.getMessage());
        }

        boolean isStay = true;

        String petId;
        String petName;
        String petSpecies;
        int petAge;
        String petOwnerName;
        String petContactInfo;
        Appointment appointment;

        String serachVariant;

        while (isStay) {
            System.out.println("Press 'r' to register pet\n"
                    + "'sa' to schedule appointment(s)\n"
                    + "'up' to update pet information\n"
                    + "'ua' to update appointment(s)\n"
                    + "'sd' to store data\n"
                    + "'R' to get all records\n"
                    + "'g' to generate the report to a file\n"
                    + "Type 'Exit' to exit");
            String menuOption = scanner.nextLine();

            switch (menuOption) {
                case "r":
                    try {
                        petId = "PET_" + (Integer.toString(petCareScheduler.size() + 1));
                        petName = getPetName();
                        petSpecies = getPetSpecies();
                        petAge = getPetAge();
                        petOwnerName = getPetOwnerName();
                        petContactInfo = getPetContactInfo();
                        Pet newPet = new Pet(petId, petName, petSpecies, petAge, petOwnerName, petContactInfo);
                        boolean isPetValid = isPetValid(petCareScheduler, newPet);
                        if (isPetValid) {
                            petCareScheduler.put(petId, newPet);
                            System.err.println(petId + ", named " + petName + ", added successfully.");
                        }
                    } catch (InvalidPetException ipe) {
                        System.out.println("Invalid pet!");
                    }
                    continue;
                case "sa":
                    try {
                        optionsForSerachingPet(
                            () -> {
                                String searchedPetId = getPetId();
                                Pet petToBeSearched = serachPetBaseOnId(petCareScheduler, searchedPetId);
                                Appointment newAppointment = createAppointment();
                                petToBeSearched.setAppointment(newAppointment);
                                petCareScheduler.put(searchedPetId, petToBeSearched);
                                System.out.println("A new appointment is created for Pet named " + petToBeSearched.getName() + "(ID: " + searchedPetId + ")");
                            },
                            () -> {
                                String searchedPetName = getPetName();
                                String searchedOwnerName = getPetOwnerName();
                                String searchedContactInfo = getPetContactInfo();
                                Pet petToBeSearched = serachPetBaseOnPetInfo(petCareScheduler, searchedPetName, searchedOwnerName, searchedContactInfo);
                                String searchedPetId = findPetId(petCareScheduler, petToBeSearched);
                                Appointment newAppointment = createAppointment();
                                petToBeSearched.setAppointment(newAppointment);
                                petCareScheduler.put(searchedPetId, petToBeSearched);
                                System.out.println("A new appointment is created for Pet named " + searchedPetName + "(ID: " + searchedPetId + ")");
                            }
                        );
                    } catch (DateTimeParseException dpe) {
                        System.out.println("Incorrect format of date. Cannot serach pet.\n"+dpe);
                    } catch (InvalidPetException ipe) {
                        System.out.println("Incorrect date of pet. Cannot serach pet.\n"+ipe);
                    } catch (InvalidAppointmentException iae) {
                        System.out.println("Incorrect date of appointment. Cannot serach appointment.\n" + iae);
                    }
                    continue;
                case "up":
                    try {
                        optionsForSerachingPet(
                                () -> {
                                    String searchedPetId = getPetId();
                                    Pet petToBeSearched = serachPetBaseOnId(petCareScheduler, searchedPetId);
                                    Pet updatedPet = updatePet(petToBeSearched);
                                    petCareScheduler.put(searchedPetId, updatedPet);
                                    System.out.println("Successfully updated.");
                                },
                                () -> {
                                    String searchedPetName = getPetName();
                                    String searchedOwnerName = getPetOwnerName();
                                    String searchedContactInfo = getPetContactInfo();
                                    Pet petToBeSearched = serachPetBaseOnPetInfo(petCareScheduler, searchedPetName, searchedOwnerName, searchedContactInfo);
                                    String searchedPetId = findPetId(petCareScheduler, petToBeSearched);
                                    Pet updatedPet = updatePet(petToBeSearched);
                                    petCareScheduler.put(searchedPetId, updatedPet);
                                    System.out.println("Successfully updated.");
                                }
                        );
                    } catch (DateTimeParseException dpe) {
                        System.out.println("Incorrect format of date. Cannot serach pet.\n" + dpe);
                    } catch (InvalidPetException ipe) {
                        System.out.println("Incorrect date of pet. Cannot serach pet.\n" + ipe);
                    } catch (InvalidAppointmentException iae) {
                        System.out.println("Incorrect date of appointment. Cannot serach appointment.\n" + iae);
                    }
                    continue;
                case "ua":
                    try {
                        optionsForSerachingPet(
                                () -> {
                                    String searchedPetId = getPetId();
                                    Pet petToBeSearched = serachPetBaseOnId(petCareScheduler, searchedPetId);
                                    System.out.print("Find the appointment.");
                                    Type appointmentTypeToBeSearched = getAppointmenType();
                                    LocalDate appointmentDateToBeSearched = getAppointmentDate();
                                    LocalTime appointmentTimeToBeSearched = getAppointmentTime();
                                    TreeSet<Appointment> appointments = petToBeSearched.getAppointments();
                                    Appointment searchedAppointment = searchAppointment(appointments, appointmentTypeToBeSearched, appointmentDateToBeSearched, appointmentTimeToBeSearched);
                                    // int searchedAppointmentIdx = petToBeSearched.getAppointments().headSet(searchedAppointment).size();
                                    Appointment updatedAppointment = updateAppointment(searchedAppointment);
                                    appointments.remove(searchedAppointment);
                                    appointments.add(updatedAppointment);
                                    petToBeSearched.setAppointments(appointments);
                                    petCareScheduler.put(searchedPetId, petToBeSearched);
                                    System.out.println("Successfully updated.");
                                },
                                () -> {
                                    String searchedPetName = getPetName();
                                    String searchedOwnerName = getPetOwnerName();
                                    String searchedContactInfo = getPetContactInfo();
                                    Pet petToBeSearched = serachPetBaseOnPetInfo(petCareScheduler, searchedPetName, searchedOwnerName, searchedContactInfo);
                                    String searchedPetId = findPetId(petCareScheduler, petToBeSearched);
                                    Type appointmentTypeToBeSearched = getAppointmenType();
                                    LocalDate appointmentDateToBeSearched = getAppointmentDate();
                                    LocalTime appointmentTimeToBeSearched = getAppointmentTime();
                                    TreeSet<Appointment> appointments = petToBeSearched.getAppointments();
                                    Appointment searchedAppointment = searchAppointment(appointments, appointmentTypeToBeSearched, appointmentDateToBeSearched, appointmentTimeToBeSearched);
                                    // int searchedAppointmentIdx = petToBeSearched.getAppointments().headSet(searchedAppointment).size();
                                    Appointment updatedAppointment = updateAppointment(searchedAppointment);
                                    appointments.remove(searchedAppointment);
                                    appointments.add(updatedAppointment);
                                    petToBeSearched.setAppointments(appointments);
                                    petCareScheduler.put(searchedPetId, petToBeSearched);
                                    System.out.println("Successfully updated.");
                                }
                        );
                    } catch (DateTimeParseException dpe) {
                        System.out.println("Incorrect format of date. Cannot serach pet.\n" + dpe);
                    } catch (InvalidPetException ipe) {
                        System.out.println("Incorrect date of pet. Cannot serach pet.\n" + ipe);
                    } catch (InvalidAppointmentException iae) {
                        System.out.println("Incorrect date of appointment. Cannot serach appointment.\n" + iae);
                    }
                    continue;    
                case "sd":
                    try {
                        savePets(petCareScheduler);
                        System.out.println("The entries are written to a file");
                    } catch (IOException e) {
                        System.err.println("Error writing to file: " + e.getMessage());
                    }
                    continue;
                case "R":
                    System.out.println("Enter '1' to display all pets\n"
                                    + "Enter '2' to display all appointments for a pet\n"
                                    + "Enter '3' to display the upcoming appointments for all pets\n"
                                    + "Enter '4' to display the past appointments for all pets");

                        serachVariant = scanner.nextLine();

                        switch (serachVariant) {
                            case "1":
                                for (Pet pet : petCareScheduler.values()) {
                                    System.out.println(pet.toString());
                                }
                                break;
                            case "2":
                                try {
                                    optionsForSerachingPet(
                                        () -> {
                                            String searchedPetId = getPetId();
                                            Pet petToBeSearched = serachPetBaseOnId(petCareScheduler, searchedPetId);
                                            for (Appointment a : petToBeSearched.getAppointments()) {
                                                System.out.println(a.toString());
                                            }
                                        },
                                        () -> {
                                            String searchedPetName = getPetName();
                                            String searchedOwnerName = getPetOwnerName();
                                            String searchedContactInfo = getPetContactInfo();
                                            Pet petToBeSearched = serachPetBaseOnPetInfo(petCareScheduler, searchedPetName, searchedOwnerName, searchedContactInfo);
                                            findPetId(petCareScheduler, petToBeSearched);
                                            for (Appointment a : petToBeSearched.getAppointments()) {
                                                System.out.println(a.toString());
                                            }
                                        }
                                    );
                                } catch (DateTimeParseException dpe) {
                                    System.out.println("Incorrect format of date. Cannot serach pet.\n"+dpe);
                                } catch (InvalidPetException ipe) {
                                    System.out.println("Incorrect date of pet. Cannot serach pet.\n"+ipe);
                                } catch (InvalidAppointmentException iae) {
                                    System.out.println("Incorrect date of appointment. Cannot serach appointment.\n" + iae);
                                }
                                break;
                            case "3":
                                for (Pet pet : petCareScheduler.values()) {
                                    Appointment upcomingAppointment = pet.showTheUpcomingAppointment();
                                    if (upcomingAppointment != null) {
                                        System.out.println(pet.getName() + " 's upcoming appointment: "+ upcomingAppointment.toString());
                                    } else {
                                        System.out.println("No upcoming appointment found from " + pet.getName());
                                    }
                                }
                                break;
                            case "4":
                                for (Pet pet : petCareScheduler.values()) {
                                    Appointment previousAppointment = pet.showThePreviousAppointment();
                                    if (previousAppointment != null) {
                                        System.out.println(pet.getName() + " 's previous appointment: "+ previousAppointment.toString());
                                    } else {
                                        System.out.println("No previous appointment found from " + pet.getName());
                                    }
                                }
                                break;
                            default:
                                System.out.println("Invalid input!");
                                break;
                        }

                    continue;
                case "g":
                    System.out.println("Enter '1' to report the pets with upcoming appointments in the next week\n"
                            + "Enter '2' to report the pets overdue for a vet visit in the last 6 months.");
                    String reportVariant = scanner.nextLine();
                    LocalDate currenDate = LocalDate.now();
                    switch (reportVariant) {
                        case "1":
                            LocalDate dateNextWeek = currenDate.plusDays(7);
                            for (Pet p : petCareScheduler.values()) {
                                for (Appointment a : p.getAppointments()) {
                                    if (a.getDate().isAfter(currenDate) && a.getDate().isBefore(dateNextWeek)) {
                                        System.out.println(p.getName() + " 's upcoming appointment(s) in the next week: " + a.toString());
                                    }
                                }
                            }
                            break;
                        case "2":
                            LocalDate dateSixMonthBefore = currenDate.minusMonths(6);
                            for (Pet p : petCareScheduler.values()) {
                                for (Appointment a : p.getAppointments()) {
                                    if (a.getDate().isAfter(dateSixMonthBefore) 
                                            && a.getDate().isBefore(currenDate)
                                            && a.getType().equals(Type.VET_VISIT)
                                            && a.getNotes().contains("overdue")) {
                                        System.out.println(p.getName() + " 's overdue appointment(s) for a vet visit in the next week: " + a.toString());
                                    }
                                }
                            }
                            break;
                        default:
                            System.out.println("Invalid input!");
                            break;
                    }
                    
                    continue;
                case "Exit":
                    System.out.println("You exit.");
                    isStay = false;
                    break;
                default:
                    System.out.println("Invalid input, please try again.");
                    continue;
            }
        }

    }
}
