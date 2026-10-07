import java.time.LocalDate;
import java.time.LocalTime;
import java.util.TreeSet;
import java.util.Comparator;

class Pet {
    Comparator<Appointment> chronoOrder = Comparator
                .comparing(Appointment::getDate)
                .thenComparing(Appointment::getTime);

    private String petId;
    private String name;
    private String species;
    private int age;
    private String ownerName;
    private String contactInfo;
    private LocalDate registrationDate = LocalDate.now();
    private TreeSet<Appointment> appointments = new TreeSet<>(chronoOrder);

    public Pet(String petId, String name, String species, int age, String ownerName, String contactInfo) {
        this.petId = petId;
        this.name = name;
        this.species = species;
        this.age = age;
        this.ownerName = ownerName;
        this.contactInfo = contactInfo;
    }

    public String getPetId() {
        return this.petId;
    }

    public String getName() {
        return this.name;
    }

    public String getSpecies() {
        return this.species;
    }

    public int getAge() {
        return this.age;
    }

    public String getOwnerName() {
        return this.ownerName;
    }

    public String getContactInfo() {
        return this.contactInfo;
    }

    public LocalDate getRegistrationDate() {
        return this.registrationDate;
    }

    public TreeSet<Appointment> getAppointments() {
        return this.appointments;
    } 

    public void setName(String name) {
        this.name = name;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public void setAppointment(Appointment appointment) {
        this.appointments.add(appointment);
    }

    @Override
    public String toString() {
        String appointmentsStr = "";

        for (Appointment a : this.appointments) {
            // appointmentsStr.concat((a.toString() + "; "));
            appointmentsStr += (a.toString() + "; ");

        }

        return "Pet named " + this.name + 
        ", a " + this.species + 
        ", aged " + Integer.toString(this.age) + 
        ", owned by " + this.ownerName +
        "(contact info: " + this.contactInfo + ") " +
        ", appointments: " + appointmentsStr;

    }

    public boolean equals(Pet pet) {
        if (pet.name.equalsIgnoreCase(this.name) 
                && pet.getRegistrationDate().equals(this.registrationDate)
                && pet.getOwnerName().equals(this.ownerName)
                && pet.getContactInfo().equals(this.contactInfo)
                && pet.getSpecies().equals(this.species)) {
            return true;
        } else {
            return false;
        }
    }

    public Appointment showTheUpcomingAppointment() {
        Appointment targetAnchor = new Appointment(Type.OTHERS, LocalDate.now(), LocalTime.now());
        return this.appointments.higher(targetAnchor);
    }

    public Appointment showThePreviousAppointment() {
        Appointment targetAnchor = new Appointment(Type.OTHERS, LocalDate.now(), LocalTime.now());
        return this.appointments.lower(targetAnchor);
    }

}